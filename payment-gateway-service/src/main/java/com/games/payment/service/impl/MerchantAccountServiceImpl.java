package com.games.payment.service.impl;

import com.games.payment.domain.MerchantAccountChangeLog;
import com.games.payment.domain.MovementData;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.MerchantAccountMapper;
import com.games.payment.domain.MerchantAccount;
import com.games.payment.service.IMerchantAccountService;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.games.payment.service.IMerchantAccountChangeLogService;
import com.games.payment.enums.MerchantAccountChangeType;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * 商户账户Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MerchantAccountServiceImpl extends ServiceImpl<MerchantAccountMapper, MerchantAccount>
        implements IMerchantAccountService {

    private static final Object BALANCE_UPDATE_LOCK = new Object();

    @Resource
    private MerchantAccountMapper merchantAccountMapper;

    @Resource
    private IMerchantAccountChangeLogService merchantAccountChangeLogService;

    /**
     * 查询商户账户列表
     *
     * @param merchantAccount 商户账户
     * @return 商户账户
     */
    @Override
    public List<MerchantAccount> selectAllList(MerchantAccount merchantAccount) {
        return getBaseMapper().selectAllList(merchantAccount);
    }

    @Override
    public List<MerchantAccount> queryList(MerchantAccount merchantAccount) {
        LambdaQueryWrapper<MerchantAccount> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(merchantAccount.getMerchantId())) {
            lqw.eq(MerchantAccount::getMerchantId, merchantAccount.getMerchantId());
        }
        if (StringUtils.isNotBlank(merchantAccount.getAccountId())) {
            lqw.eq(MerchantAccount::getAccountId, merchantAccount.getAccountId());
        }
        if (StringUtils.isNotBlank(merchantAccount.getCurrency())) {
            lqw.eq(MerchantAccount::getCurrency, merchantAccount.getCurrency());
        }
        if (merchantAccount.getMinBalance() != null) {
            lqw.eq(MerchantAccount::getMinBalance, merchantAccount.getMinBalance());
        }
        if (merchantAccount.getCreatedAt() != null) {
            lqw.eq(MerchantAccount::getCreatedAt, merchantAccount.getCreatedAt());
        }
        if (merchantAccount.getUpdatedAt() != null) {
            lqw.eq(MerchantAccount::getUpdatedAt, merchantAccount.getUpdatedAt());
        }
        return this.list(lqw);
    }

    @Override
    public void updateBalance(List<MovementData> merchantSettlementList) {
        synchronized (BALANCE_UPDATE_LOCK) {
            int originalSize = merchantSettlementList == null ? 0 : merchantSettlementList.size();
            log.info("开始根据结算数据更新商户资产，原始结算记录数: {}", originalSize);

            Map<String, BigDecimal> merchantSumMap = aggregateMerchantSettlements(merchantSettlementList);
            if (merchantSumMap.isEmpty()) {
                log.warn("聚合后的商户结算数据为空，跳过更新商户资产和记录变更日志");
                return;
            }

            log.info("聚合后的商户结算数据条数(按商户维度): {}", merchantSumMap.size());

            // 记录资产变更日志
            Long movementImportId = getMovementImportId(merchantSettlementList);
            recordChangeLogs(merchantSumMap, movementImportId, MerchantAccountChangeType.UPDATE_BALANCE);

            merchantAccountMapper.updateBalance(merchantSumMap);
            log.info("商户资产余额更新完成，参与更新的商户数: {}", merchantSumMap.size());
        }
    }

    @Override
    public void rollbackBalance(List<MovementData> merchantSettlementList) {
        synchronized (BALANCE_UPDATE_LOCK) {
            int originalSize = merchantSettlementList == null ? 0 : merchantSettlementList.size();
            log.info("开始根据结算数据回滚商户资产，原始结算记录数: {}", originalSize);

            Map<String, BigDecimal> merchantSumMap = aggregateMerchantSettlements(merchantSettlementList);
            if (merchantSumMap.isEmpty()) {
                log.warn("聚合后的商户结算数据为空，跳过回滚商户资产和记录变更日志");
                return;
            }

            log.info("聚合后的商户结算数据条数(按商户维度): {}", merchantSumMap.size());

            // 记录资产变更日志
            Long movementImportId = getMovementImportId(merchantSettlementList);
            recordChangeLogs(merchantSumMap, movementImportId, MerchantAccountChangeType.ROLLBACK_BALANCE);

            for (Map.Entry<String, BigDecimal> entry : merchantSumMap.entrySet()) {
                merchantAccountMapper.rollbackBalance(entry.getKey(), entry.getValue());
            }
            log.info("商户资产余额回滚完成，参与回滚的商户数: {}", merchantSumMap.size());
        }
    }

    /**
     * 聚合商户结算费用
     * 
     * @param merchantSettlementList 商户结算列表
     * @return 商户ID到总金额的映射（若列表为空或无有效数据则返回空Map）
     */
    private Map<String, BigDecimal> aggregateMerchantSettlements(List<MovementData> merchantSettlementList) {
        Map<String, BigDecimal> merchantSumMap = new HashMap<>();
        if (merchantSettlementList == null || merchantSettlementList.isEmpty()) {
            log.warn("聚合商户结算数据时，传入的结算列表为空");
            return merchantSumMap;
        }
        int skipped = 0;
        for (MovementData movementData : merchantSettlementList) {
            if (movementData == null
                    || movementData.getMerchantId() == null
                    || movementData.getMerchantSettlementFee() == null) {
                skipped++;
                continue;
            }
            merchantSumMap.merge(movementData.getMerchantId(),
                    movementData.getMerchantSettlementFee(),
                    BigDecimal::add);
        }
        log.info("聚合商户结算数据完成，原始记录数: {}，有效记录数: {}，被跳过记录数: {}",
                merchantSettlementList.size(), merchantSumMap.size(), skipped);
        return merchantSumMap;
    }

    private Long getMovementImportId(List<MovementData> list) {
        if (list != null && !list.isEmpty() && list.get(0) != null) {
            return list.get(0).getMovementImportId();
        }
        return null;
    }

    private void recordChangeLogs(Map<String, BigDecimal> merchantSumMap, Long movementImportId,
            MerchantAccountChangeType changeType) {
        log.info("开始生成商户资产变更日志，movementImportId: {}，变更类型: {}，商户数: {}",
                movementImportId, changeType.getCode(), merchantSumMap.size());

        // 查询现有账户信息
        LambdaQueryWrapper<MerchantAccount> query = Wrappers.lambdaQuery();
        query.in(MerchantAccount::getMerchantId, merchantSumMap.keySet())
                .eq(MerchantAccount::getCurrency, "USD");
        List<MerchantAccount> existingAccounts = this.list(query);
        Map<String, MerchantAccount> accountMap = existingAccounts.stream()
                .collect(Collectors.toMap(MerchantAccount::getMerchantId, a -> a));

        List<MerchantAccountChangeLog> logs = new ArrayList<>();

        for (Map.Entry<String, BigDecimal> entry : merchantSumMap.entrySet()) {
            String merchantId = entry.getKey();
            BigDecimal amount = entry.getValue();
            MerchantAccount account = accountMap.get(merchantId);

            BigDecimal balanceBefore = (account != null && account.getMinBalance() != null)
                    ? account.getMinBalance()
                    : BigDecimal.ZERO;

            BigDecimal changeAmount;
            BigDecimal balanceAfter;

            if (MerchantAccountChangeType.ROLLBACK_BALANCE.equals(changeType)) {
                // 回滚时，amount是正数（费用），所以是减去
                changeAmount = amount.negate();
                balanceAfter = balanceBefore.subtract(amount);
            } else {
                // 更新时，amount是正数（结算金额），所以是加上
                // 注意：这里假设updateBalance是增加余额。
                // 查看MerchantAccountMapper.xml: min_balance = min_balance +
                // IFNULL(VALUES(min_balance), 0)
                // 传入的amount是MovementData.getMerchantSettlementFee()
                // 如果SettlementFee是负数（例如Declined订单），那么amount是负数，加上负数就是减少。
                // 所以直接加amount即可。
                changeAmount = amount;
                balanceAfter = balanceBefore.add(amount);
            }

            MerchantAccountChangeLog log = new MerchantAccountChangeLog()
                    .setMerchantId(merchantId)
                    .setCurrency("USD")
                    .setBalanceBefore(balanceBefore)
                    .setBalanceAfter(balanceAfter)
                    .setChangeAmount(changeAmount)
                    .setChangeType(changeType.getCode())
                    .setMovementImportId(movementImportId)
                    .setRemark(changeType.getDesc());
            logs.add(log);
        }

        log.info("生成商户资产变更日志完成，即将批量保存，记录条数: {}", logs.size());
        merchantAccountChangeLogService.batchSave(logs);
    }

}
