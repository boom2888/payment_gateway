package com.games.thunes;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.games.common.constant.ThunesConstants;
import com.games.common.enums.ThunesTransferStatus;
import com.games.common.exception.CustomException;
import com.games.common.utils.SignUtil;
import com.games.framework.config.ThunesConfig;
import com.games.payment.domain.Shop;
import com.games.payment.domain.ThunesTransferRecord;
import com.games.payment.service.IShopService;
import com.games.payment.service.IThunesTransferRecordService;
import com.games.thunes.dto.*;
import com.games.thunes.vo.TransferRequestVo;
import com.games.thunes.vo.TransferRequestVo.BeneficiaryInfoVo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Thunes 转账业务服务
 *
 * @author System
 * @date 2025-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ThunesTransferService {

    private final ThunesApiService thunesApiService;
    private final IThunesTransferRecordService transferRecordService;
    private final ThunesConfigService thunesConfigService;
    private final ThunesPayerSyncService payerSyncService;
    private final ThunesConfig thunesConfig;
    private final IShopService shopService;

    /**
     * 处理转账请求
     *
     * @param shop    商户信息
     * @param request 转账请求
     * @return 转账结果
     */
    public TransferResponseDto processTransfer(Shop shop, TransferRequestVo request) {
        log.info("开始处理转账请求 - 商户ID: {}, 外部订单号: {}", shop.getId(), request.getExternalId());

        try {
            // 1. 验证商户
            validateMerchant(shop);

            // 2. 验证 Thunes 业务参数
            validateThunesBusinessRules(request);

            // 3. 检查重复订单
            checkDuplicateOrder(shop.getId(), request.getExternalId());

            // 4. 创建转账记录
            ThunesTransferRecord record = createTransferRecord(shop, request);

            // 5. 创建报价（支持支付方重试）
            QuotationResult quotationResult = createQuotationWithRetry(
                    request.getTargetCountry(), request.getTargetCurrency(), request);
            QuotationDto quotation = quotationResult.getQuotation();
            PayerDto payer = quotationResult.getPayer();
            record.setQuotationId(quotation.getId().toString());
            record.setPayerId(payer.getId());
            record.setExchangeRate(quotation.getWholesaleFxRate());
            if (quotation.getFee() != null) {
                record.setFeeAmount(quotation.getFee().getAmount());
                record.setFeeCurrency(quotation.getFee().getCurrency());
            }
            if (quotation.getDestination() != null) {
                record.setTargetAmount(quotation.getDestination().getAmount());
            }
            record.setStatus(ThunesTransferStatus.QUOTATION_CREATED.getCode());
            record.setUpdatedAt(new Date());
            transferRecordService.updateById(record);

            // 6. 创建交易
            TransactionDto transaction = createTransaction(quotation.getId().toString(), request, payer);
            record.setTransactionId(transaction.getId().toString());
            record.setThunesStatus(transaction.getStatus());
            record.setStatus(ThunesTransferStatus.TRANSACTION_CREATED.getCode());
            record.setUpdatedAt(new Date());
            transferRecordService.updateById(record);

            // 7. 确认交易
            TransactionDto confirmedTransaction = confirmTransaction(transaction.getId().toString());
            record.setThunesStatus(confirmedTransaction.getStatus());
            record.setStatus(ThunesTransferStatus.CONFIRMED.getCode());
            record.setUpdatedAt(new Date());
            transferRecordService.updateById(record);

            // 8. 构建响应
            TransferResponseDto response = buildTransferResponse(record, quotation);

            log.info("转账处理完成 - 转账ID: {}, 状态: {}", record.getId(), record.getStatus());
            return response;

        } catch (Exception e) {
            log.error("转账处理失败 - 商户ID: {}, 外部订单号: {}", shop.getId(), request.getExternalId(), e);

            // 如果转账记录已创建，更新状态为失败
            try {
                ThunesTransferRecord existingRecord = transferRecordService.getByMerchantIdAndExternalId(
                        shop.getId(), request.getExternalId());
                if (existingRecord != null) {
                    existingRecord.setStatus(ThunesTransferStatus.FAILED.getCode());
                    existingRecord.setFailReason(e.getMessage());
                    existingRecord.setUpdatedAt(new Date());
                    transferRecordService.updateById(existingRecord);
                }
            } catch (Exception updateException) {
                log.error("更新失败状态异常", updateException);
            }

            throw new CustomException("转账处理失败: " + e.getMessage());
        }
    }

    /**
     * 查询转账状态（带权限验证）
     *
     * @param transferId 转账ID
     * @param merchantId 商户ID
     * @return 转账状态
     */
    public TransferStatusDto getTransferStatus(Long transferId, Long merchantId) {
        ThunesTransferRecord record = transferRecordService.getById(transferId);
        if (record == null) {
            throw new CustomException("转账记录不存在");
        }

        // 验证转账记录属于该商户
        if (!merchantId.equals(record.getMerchantId())) {
            throw new CustomException("无权访问该转账记录");
        }

        // 如果有 Thunes 交易ID，查询最新状态
        if (StrUtil.isNotBlank(record.getTransactionId())) {
            try {
                TransactionDto transaction = thunesApiService.getTransactionStatus(record.getTransactionId());
                if (!StrUtil.equals(record.getThunesStatus(), transaction.getStatus())) {
                    // 更新状态
                    record.setThunesStatus(transaction.getStatus());
                    record.setUpdatedAt(new Date());
                    transferRecordService.updateById(record);
                }
            } catch (Exception e) {
                log.warn("查询 Thunes 交易状态失败 - 交易ID: {}", record.getTransactionId(), e);
            }
        }

        return convertToStatusDto(record);
    }

    /**
     * 构建转账响应对象
     *
     * @param record    转账记录
     * @param quotation 报价信息
     * @return 转账响应DTO
     */
    private TransferResponseDto buildTransferResponse(ThunesTransferRecord record, QuotationDto quotation) {
        TransferResponseDto response = new TransferResponseDto();
        response.setTransferId(record.getId());
        response.setExternalId(record.getExternalId());
        response.setStatus(record.getStatus());
        response.setThunesTransactionId(record.getTransactionId());
        response.setQuotationId(record.getQuotationId());
        response.setExchangeRate(record.getExchangeRate());
        response.setFee(record.getFeeAmount());
        if (quotation.getFee() != null) {
            response.setFeeCurrency(quotation.getFee().getCurrency());
        }
        if (quotation.getDestination() != null) {
            response.setTargetAmount(quotation.getDestination().getAmount());
            response.setTargetCurrency(quotation.getDestination().getCurrency());
        }
        return response;
    }

    /**
     * 验证 Thunes 业务规则
     */
    private void validateThunesBusinessRules(TransferRequestVo request) {
        log.info("开始验证 Thunes 业务规则 - 外部订单号: {}", request.getExternalId());

        // 1. 验证 Thunes API 必需的基础参数
        validateThunesRequiredFields(request);

        // 2. 验证交易类型
        if (!Arrays.asList("C2C", "B2C", "C2B", "B2B").contains(request.getTransactionType())) {
            throw new CustomException("不支持的交易类型: " + request.getTransactionType());
        }

        // 3. 根据交易类型验证账户信息
        String transactionType = request.getTransactionType();
        if ("C2C".equals(transactionType) || "B2C".equals(transactionType)) {
            // 个人收款方交易：验证收款方账户信息（至少需要一种账户标识）
            BeneficiaryInfoVo beneficiary = request.getBeneficiary();
            boolean hasAccountInfo = StrUtil.isNotBlank(beneficiary.getBankAccountNumber()) ||
                    StrUtil.isNotBlank(beneficiary.getAbaRoutingNumber()) ||
                    StrUtil.isNotBlank(beneficiary.getIban()) ||
                    StrUtil.isNotBlank(beneficiary.getClabe()) ||
                    (StrUtil.isNotBlank(beneficiary.getSortCode())
                            && StrUtil.isNotBlank(beneficiary.getAccountNumber()))
                    ||
                    StrUtil.isNotBlank(beneficiary.getMsisdn());

            if (!hasAccountInfo) {
                throw new CustomException("收款方必须提供至少一种账户标识（银行账号、IBAN、CLABE、排序代码+账号或手机号）");
            }
        } else if ("C2B".equals(transactionType) || "B2B".equals(transactionType)) {
            // 企业收款方交易：验证企业收款方账户信息
            TransferRequestVo.ReceivingBusinessInfoVo receivingBusiness = request.getReceivingBusiness();
            if (receivingBusiness == null) {
                throw new CustomException("企业收款方交易必须提供 receivingBusiness 信息");
            }

            // 验证账户信息（至少需要一种账户标识）
            boolean hasAccountInfo = StrUtil.isNotBlank(receivingBusiness.getBankAccountNumber()) ||
                    StrUtil.isNotBlank(receivingBusiness.getAbaRoutingNumber()) ||
                    StrUtil.isNotBlank(receivingBusiness.getIban()) ||
                    StrUtil.isNotBlank(receivingBusiness.getClabe()) ||
                    (StrUtil.isNotBlank(receivingBusiness.getSortCode())
                            && StrUtil.isNotBlank(receivingBusiness.getAccountNumber()))
                    ||
                    StrUtil.isNotBlank(receivingBusiness.getMsisdn());

            if (!hasAccountInfo) {
                throw new CustomException("企业收款方必须提供至少一种账户标识（银行账号、IBAN、CLABE、排序代码+账号或手机号）");
            }
        }

        // 注意：具体的字段验证将在选择 Payer 后根据 Payer 配置进行动态验证

        log.info("Thunes 基础业务规则验证通过 - 交易类型: {}, 目标国家: {}",
                request.getTransactionType(), request.getTargetCountry());
    }

    private void validateFieldsByPayerConfig(TransferRequestVo request, PayerDto payer) {
        String transactionType = request.getTransactionType();
        PayerDto.TransactionTypeDto transactionConfig = payer.getTransactionTypeConfig(transactionType);

        if (transactionConfig == null) {
            throw new CustomException("选择的支付方不支持 " + transactionType + " 交易类型");
        }

        List<List<String>> requiredSendingFields = transactionConfig.getRequiredSendingEntityFields();
        List<List<String>> requiredReceivingFields = transactionConfig.getRequiredReceivingEntityFields();

        // 验证发送方实体字段（B2C/B2B 交易）
        if ("B2C".equals(transactionType) || "B2B".equals(transactionType)) {
            validateSendingEntityFields(request, requiredSendingFields);
        }

        // 验证收款方实体字段（根据交易类型选择 beneficiary 或 receivingBusiness）
        validateReceivingEntityFields(request, requiredReceivingFields);

        // 验证个人信息字段
        if ("C2C".equals(transactionType) || "C2B".equals(transactionType)) {
            validateSenderIndividualFields(request.getSender());
        }

        if ("C2C".equals(transactionType) || "B2C".equals(transactionType)) {
            validateBeneficiaryIndividualFields(request.getBeneficiary());
        }

        log.info("基于 Payer 配置的字段验证通过 - PayerId: {}, 交易类型: {}", payer.getId(), transactionType);
    }

    /**
     * 验证发送方实体字段
     */
    private void validateSendingEntityFields(TransferRequestVo request, List<List<String>> requiredFieldGroups) {
        if (requiredFieldGroups == null || requiredFieldGroups.isEmpty()) {
            return;
        }

        String transactionType = request.getTransactionType();
        Object sendingEntity = null;
        String entityName = "";

        if ("B2C".equals(transactionType) || "B2B".equals(transactionType)) {
            // B2C/B2B 交易：使用 sendingBusiness
            sendingEntity = request.getSendingBusiness();
            entityName = "发送方企业信息";
        } else {
            // C2C/C2B 交易：使用 sender（但通常不需要验证实体字段）
            sendingEntity = request.getSender();
            entityName = "发送方信息";
        }

        if (sendingEntity == null) {
            throw new CustomException(entityName + "不能为空");
        }

        validateRequiredFieldGroups(sendingEntity, requiredFieldGroups, entityName);
    }

    /**
     * 验证收款方实体字段
     */
    private void validateReceivingEntityFields(TransferRequestVo request, List<List<String>> requiredFieldGroups) {
        if (requiredFieldGroups == null || requiredFieldGroups.isEmpty()) {
            return;
        }

        String transactionType = request.getTransactionType();
        if ("C2C".equals(transactionType) || "B2C".equals(transactionType)) {
            TransferRequestVo.BeneficiaryInfoVo beneficiary = request.getBeneficiary();
            if (beneficiary == null) {
                throw new CustomException("收款方信息不能为空");
            }
            validateRequiredFieldGroups(beneficiary, requiredFieldGroups, "收款方信息");
            return;
        }

        TransferRequestVo.ReceivingBusinessInfoVo receivingBusiness = request.getReceivingBusiness();
        if (receivingBusiness == null) {
            throw new CustomException("收款方企业信息不能为空");
        }

        validateRequiredFieldGroups(receivingBusiness, requiredFieldGroups, "收款方企业信息");
    }

    /**
     * 验证必需字段组合（通用方法）
     */
    private void validateRequiredFieldGroups(Object obj, List<List<String>> requiredFieldGroups, String objectName) {
        // 检查是否满足任一字段组合要求
        boolean satisfied = false;
        StringBuilder errorMsg = new StringBuilder(objectName + "缺少必需字段，需要满足以下任一组合：");

        for (List<String> fieldGroup : requiredFieldGroups) {
            boolean groupSatisfied = true;
            StringBuilder groupDesc = new StringBuilder("[");

            for (String field : fieldGroup) {
                if (groupDesc.length() > 1)
                    groupDesc.append(", ");
                groupDesc.append(field);

                if (isFieldMissing(obj, field)) {
                    groupSatisfied = false;
                }
            }
            groupDesc.append("]");
            errorMsg.append(" ").append(groupDesc);

            if (groupSatisfied) {
                satisfied = true;
                break;
            }
        }

        if (!satisfied) {
            throw new CustomException(errorMsg.toString());
        }
    }

    /**
     * 验证发送方个人信息字段
     */
    private void validateSenderIndividualFields(TransferRequestVo.SenderInfoVo sender) {
        if (sender == null) {
            throw new CustomException("发送方个人信息不能为空");
        }

        if (StrUtil.isBlank(sender.getFirstName())) {
            throw new CustomException("发送方名字不能为空");
        }
        if (StrUtil.isBlank(sender.getLastName())) {
            throw new CustomException("发送方姓氏不能为空");
        }
        if (StrUtil.isBlank(sender.getAddress())) {
            throw new CustomException("发送方地址不能为空");
        }
        if (StrUtil.isBlank(sender.getCity())) {
            throw new CustomException("发送方城市不能为空");
        }
    }

    /**
     * 验证收款方个人信息字段
     */
    private void validateBeneficiaryIndividualFields(TransferRequestVo.BeneficiaryInfoVo beneficiary) {
        if (beneficiary == null) {
            throw new CustomException("收款方个人信息不能为空");
        }

        if (StrUtil.isBlank(beneficiary.getFirstName())) {
            throw new CustomException("收款方名字不能为空");
        }
        if (StrUtil.isBlank(beneficiary.getLastName())) {
            throw new CustomException("收款方姓氏不能为空");
        }
    }

    private String normalizeFieldName(String fieldName) {
        if (StrUtil.isBlank(fieldName)) {
            return fieldName;
        }
        return fieldName.replace("_", "").toLowerCase(Locale.ROOT);
    }

    /**
     * 检查对象中指定字段是否缺失或为空
     *
     * @param obj       目标对象
     * @param fieldName 字段名（支持蛇形或驼峰）
     * @return 字段是否缺失或为空
     */
    private boolean isFieldMissing(Object obj, String fieldName) {
        if (obj == null || StrUtil.isBlank(fieldName)) {
            return true;
        }

        String normalizedFieldName = normalizeFieldName(fieldName);
        if (StrUtil.isBlank(normalizedFieldName)) {
            return true;
        }
        try {
            Field field = findFieldByNormalizedName(obj.getClass(), normalizedFieldName);
            if (field == null) {
                log.warn("未找到字段 - 类: {}, 字段: {}", obj.getClass().getSimpleName(), fieldName);
                return true;
            }
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value == null) {
                return true;
            }
            if (value instanceof String) {
                return StrUtil.isBlank((String) value);
            }
            return false;
        } catch (Exception e) {
            log.warn("检查字段失败 - 类: {}, 字段: {}, 错误: {}",
                    obj.getClass().getSimpleName(), fieldName, e.getMessage());
            return true;
        }
    }

    private Field findFieldByNormalizedName(Class<?> clazz, String normalizedFieldName) {
        if (clazz == null || StrUtil.isBlank(normalizedFieldName)) {
            return null;
        }
        for (Field field : clazz.getDeclaredFields()) {
            if (normalizeFieldName(field.getName()).equals(normalizedFieldName)) {
                return field;
            }
        }
        return null;
    }

    /**
     * 验证 Thunes API 必需的字段
     */
    private void validateThunesRequiredFields(TransferRequestVo request) {
        // 验证基础字段
        if (StrUtil.isBlank(request.getExternalId())) {
            throw new CustomException("externalId 外部订单号不能为空");
        }

        if (StrUtil.isBlank(request.getTransactionType())) {
            throw new CustomException("transactionType 交易类型不能为空");
        }

        if (StrUtil.isBlank(request.getCurrency())) {
            throw new CustomException("currency 转账货币不能为空");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException("amount 转账金额必须大于0");
        }

        if (StrUtil.isBlank(request.getTargetCountry())) {
            throw new CustomException("targetCountry 目标国家代码不能为空");
        }
        if (StrUtil.isBlank(request.getTargetCurrency())) {
            throw new CustomException("targetCurrency 目标货币不能为空");
        }

        // 验证 Transaction API 必需字段
        if (StrUtil.isBlank(request.getPurposeOfRemittance())) {
            throw new CustomException("purposeOfRemittance 汇款目的不能为空");
        }

        // 根据交易类型验证必需的对象
        String transactionType = request.getTransactionType();
        switch (transactionType) {
            case "C2C":
                // C2C: 需要 sender 和 beneficiary
                if (request.getSender() == null) {
                    throw new CustomException("sender 发送方信息不能为空");
                }
                if (request.getBeneficiary() == null) {
                    throw new CustomException("beneficiary 收款方信息不能为空");
                }
                break;

            case "C2B":
                // C2B: 需要 sender 和 receiving_business
                if (request.getSender() == null) {
                    throw new CustomException("sender 发送方信息不能为空");
                }
                if (request.getReceivingBusiness() == null) {
                    throw new CustomException("receivingBusiness 收款方企业信息不能为空");
                }
                break;

            case "B2C":
                // B2C: 需要 sending_business 和 beneficiary
                if (request.getSendingBusiness() == null) {
                    throw new CustomException("sendingBusiness 发送方企业信息不能为空");
                }
                if (request.getBeneficiary() == null) {
                    throw new CustomException("beneficiary 收款方信息不能为空");
                }
                break;

            case "B2B":
                // B2B: 只需要 sending_business 和 receiving_business
                if (request.getSendingBusiness() == null) {
                    throw new CustomException("sendingBusiness 发送方企业信息不能为空");
                }
                if (request.getReceivingBusiness() == null) {
                    throw new CustomException("receivingBusiness 收款方企业信息不能为空");
                }
                // B2B 交易不需要 beneficiary
                break;

            default:
                throw new CustomException("不支持的交易类型: " + transactionType);
        }

        log.info("Thunes API 基础字段验证通过 - 交易类型: {}", request.getTransactionType());
    }

    /**
     * 验证商户
     */
    private void validateMerchant(Shop shop) {
        if (shop == null) {
            throw new CustomException("商户不存在");
        }
        // 可以添加更多商户状态验证逻辑，如商户状态、权限等
        log.debug("商户验证通过 - 商户ID: {}", shop.getId());
    }

    /**
     * 检查重复订单
     */
    private void checkDuplicateOrder(Long merchantId, String externalId) {
        ThunesTransferRecord existing = transferRecordService.getByMerchantIdAndExternalId(merchantId, externalId);
        if (existing != null) {
            throw new CustomException("订单号已存在: " + externalId);
        }
    }

    /**
     * 创建转账记录
     */
    private ThunesTransferRecord createTransferRecord(Shop shop, TransferRequestVo request) {
        ThunesTransferRecord record = new ThunesTransferRecord();
        record.setMerchantId(shop.getId());
        record.setExternalId(request.getExternalId());
        record.setTransactionType(request.getTransactionType());
        record.setAmount(request.getAmount());
        record.setCurrency(request.getCurrency());
        record.setTargetCountry(request.getTargetCountry());
        record.setTargetCurrency(request.getTargetCurrency());
        record.setCallbackUrl(request.getCallbackUrl());
        record.setStatus(ThunesTransferStatus.CREATED.getCode());
        record.setCreatedAt(new Date());
        record.setUpdatedAt(new Date());

        // 构建 JSON 动态数据
        TransactionData transactionData = buildTransactionData(request);
        record.setTransactionData(transactionData);

        // 设置平台手续费 (从商户配置获取)
        if (shop.getThunesTransferFee() != null) {
            record.setHandlingFee(shop.getThunesTransferFee());
        } else {
            record.setHandlingFee(BigDecimal.ZERO);
        }

        transferRecordService.createTransferRecord(record);
        return record;
    }

    /**
     * 构建交易动态数据
     */
    private TransactionData buildTransactionData(TransferRequestVo request) {
        TransactionData data = new TransactionData();

        // 构建发送方信息
        data.setSender(buildSenderInfo(request));

        // 构建收款方信息
        data.setBeneficiary(buildBeneficiaryInfo(request));

        // 构建支付方式信息
        data.setPaymentMethod(buildPaymentMethodInfo(request));

        // 构建合规信息
        data.setCompliance(buildComplianceInfo(request));

        return data;
    }

    /**
     * 构建发送方信息
     */
    private TransactionData.PartyInfo buildSenderInfo(TransferRequestVo request) {
        String transactionType = request.getTransactionType();
        TransactionData.PartyInfo senderInfo = new TransactionData.PartyInfo();

        if ("C2C".equals(transactionType) || "C2B".equals(transactionType)) {
            // 个人发送方
            senderInfo.setType("INDIVIDUAL");
            if (request.getSender() != null) {
                TransactionData.IndividualInfo individual = new TransactionData.IndividualInfo();
                TransferRequestVo.SenderInfoVo sender = request.getSender();

                individual.setFirstName(sender.getFirstName());
                individual.setLastName(sender.getLastName());
                individual.setLastName2(sender.getLastName2());
                individual.setMiddleName(sender.getMiddleName());
                individual.setNativeName(sender.getNativeName());
                individual.setCode(sender.getCode());
                individual.setIdType(sender.getIdType());
                individual.setIdCountryIsoCode(sender.getIdCountryIsoCode());
                individual.setIdNumber(sender.getIdNumber());
                individual.setIdDeliveryDate(sender.getIdDeliveryDate());
                individual.setIdExpirationDate(sender.getIdExpirationDate());
                individual.setAddress(sender.getAddress());
                individual.setPostalCode(sender.getPostalCode());
                individual.setCity(sender.getCity());
                individual.setProvinceState(sender.getProvinceState());
                individual.setCountryIsoCode(ThunesConstants.FIXED_SOURCE_COUNTRY); // 固定马耳他
                individual.setMsisdn(sender.getMsisdn());
                individual.setEmail(sender.getEmail());
                individual.setDateOfBirth(sender.getDateOfBirth());
                individual.setGender(sender.getGender());
                individual.setOccupation(sender.getOccupation());
                individual.setBankAccountNumber(sender.getBankAccountNumber());
                individual.setBeneficiaryRelationship(sender.getBeneficiaryRelationship());
                individual.setSourceOfFunds(sender.getSourceOfFunds());
                individual.setNationalityCountryIsoCode(sender.getNationalityCountryIsoCode());
                individual.setCountryOfBirthIsoCode(sender.getCountryOfBirthIsoCode());

                senderInfo.setIndividual(individual);
            }
        } else {
            // 企业发送方
            senderInfo.setType("BUSINESS");
            if (request.getSendingBusiness() != null) {
                TransactionData.BusinessInfo business = new TransactionData.BusinessInfo();
                TransferRequestVo.SendingBusinessInfoVo sendingBusiness = request.getSendingBusiness();

                business.setRegisteredName(sendingBusiness.getRegisteredName());
                business.setTradingName(sendingBusiness.getTradingName());
                business.setCode(sendingBusiness.getCode());
                business.setRegistrationNumber(sendingBusiness.getRegistrationNumber());
                business.setTaxId(sendingBusiness.getTaxId());
                business.setDateOfIncorporation(sendingBusiness.getDateOfIncorporation());
                business.setAddress(sendingBusiness.getAddress());
                business.setPostalCode(sendingBusiness.getPostalCode());
                business.setCity(sendingBusiness.getCity());
                business.setProvinceState(sendingBusiness.getProvinceState());
                business.setCountryIsoCode(ThunesConstants.FIXED_SOURCE_COUNTRY); // 固定马耳他
                business.setMsisdn(sendingBusiness.getMsisdn());
                business.setEmail(sendingBusiness.getEmail());
                business.setBusinessRelationship(sendingBusiness.getBusinessRelationship());

                // 代表人信息
                if (StrUtil.isNotBlank(sendingBusiness.getRepresentativeFirstname()) ||
                        StrUtil.isNotBlank(sendingBusiness.getRepresentativeLastname())) {
                    TransactionData.RepresentativeInfo representative = new TransactionData.RepresentativeInfo();
                    representative.setFirstName(sendingBusiness.getRepresentativeFirstname());
                    representative.setLastName(sendingBusiness.getRepresentativeLastname());
                    representative.setLastname2(sendingBusiness.getRepresentativeLastname2());
                    representative.setMiddleName(sendingBusiness.getRepresentativeMiddlename());
                    representative.setNativeName(sendingBusiness.getRepresentativeNativename());
                    representative.setIdType(sendingBusiness.getRepresentativeIdType());
                    representative.setIdCountryIsoCode(sendingBusiness.getRepresentativeIdCountryIsoCode());
                    representative.setIdNumber(sendingBusiness.getRepresentativeIdNumber());
                    representative.setIdDeliveryDate(sendingBusiness.getRepresentativeIdDeliveryDate());
                    representative.setIdExpirationDate(sendingBusiness.getRepresentativeIdExpirationDate());
                    business.setRepresentative(representative);
                }

                senderInfo.setBusiness(business);
            }
        }

        return senderInfo;
    }

    /**
     * 构建收款方信息
     */
    private TransactionData.PartyInfo buildBeneficiaryInfo(TransferRequestVo request) {
        String transactionType = request.getTransactionType();
        TransactionData.PartyInfo beneficiaryInfo = new TransactionData.PartyInfo();

        if ("C2C".equals(transactionType) || "B2C".equals(transactionType)) {
            // 个人收款方
            beneficiaryInfo.setType("INDIVIDUAL");
            if (request.getBeneficiary() != null) {
                TransactionData.IndividualInfo individual = new TransactionData.IndividualInfo();
                TransferRequestVo.BeneficiaryInfoVo beneficiary = request.getBeneficiary();

                individual.setFirstName(beneficiary.getFirstName());
                individual.setLastName(beneficiary.getLastName());
                individual.setLastName2(beneficiary.getLastName2());
                individual.setMiddleName(beneficiary.getMiddleName());
                individual.setNativeName(beneficiary.getNativeName());
                individual.setNationalityCountryIsoCode(beneficiary.getNationalityCountryIsoCode());
                individual.setCode(beneficiary.getCode());
                individual.setDateOfBirth(beneficiary.getDateOfBirth());
                individual.setCountryOfBirthIsoCode(beneficiary.getCountryOfBirthIsoCode());
                individual.setGender(beneficiary.getGender());
                individual.setIdType(beneficiary.getIdType());
                individual.setIdCountryIsoCode(beneficiary.getIdCountryIsoCode());
                individual.setIdNumber(beneficiary.getIdNumber());
                individual.setIdDeliveryDate(beneficiary.getIdDeliveryDate());
                individual.setIdExpirationDate(beneficiary.getIdExpirationDate());
                individual.setAddress(beneficiary.getAddress());
                individual.setPostalCode(beneficiary.getPostalCode());
                individual.setCity(beneficiary.getCity());
                individual.setProvinceState(beneficiary.getProvinceState());
                individual.setCountryIsoCode(beneficiary.getCountryIsoCode());
                individual.setMsisdn(beneficiary.getMsisdn());
                individual.setEmail(beneficiary.getEmail());
                individual.setOccupation(beneficiary.getOccupation());
                individual.setBankAccountHolderName(beneficiary.getBankAccountHolderName());

                beneficiaryInfo.setIndividual(individual);
            }
        } else {
            // 企业收款方
            beneficiaryInfo.setType("BUSINESS");
            if (request.getReceivingBusiness() != null) {
                TransactionData.BusinessInfo business = new TransactionData.BusinessInfo();
                TransferRequestVo.ReceivingBusinessInfoVo receivingBusiness = request.getReceivingBusiness();

                business.setRegisteredName(receivingBusiness.getRegisteredName());
                business.setTradingName(receivingBusiness.getTradingName());
                business.setRegistrationNumber(receivingBusiness.getRegistrationNumber());
                business.setTaxId(receivingBusiness.getTaxId());
                business.setDateOfIncorporation(receivingBusiness.getDateOfIncorporation());
                business.setAddress(receivingBusiness.getAddress());
                business.setPostalCode(receivingBusiness.getPostalCode());
                business.setCity(receivingBusiness.getCity());
                business.setProvinceState(receivingBusiness.getProvinceState());
                business.setCountryIsoCode(receivingBusiness.getCountryIsoCode());
                business.setMsisdn(receivingBusiness.getMsisdn());
                business.setEmail(receivingBusiness.getEmail());

                // 代表人信息
                if (StrUtil.isNotBlank(receivingBusiness.getRepresentativeFirstname()) ||
                        StrUtil.isNotBlank(receivingBusiness.getRepresentativeLastname())) {
                    TransactionData.RepresentativeInfo representative = new TransactionData.RepresentativeInfo();
                    representative.setFirstName(receivingBusiness.getRepresentativeFirstname());
                    representative.setLastName(receivingBusiness.getRepresentativeLastname());
                    representative.setLastname2(receivingBusiness.getRepresentativeLastname2());
                    representative.setMiddleName(receivingBusiness.getRepresentativeMiddlename());
                    representative.setNativeName(receivingBusiness.getRepresentativeNativename());
                    representative.setIdType(receivingBusiness.getRepresentativeIdType());
                    representative.setIdCountryIsoCode(receivingBusiness.getRepresentativeIdCountryIsoCode());
                    representative.setIdNumber(receivingBusiness.getRepresentativeIdNumber());
                    representative.setIdDeliveryDate(receivingBusiness.getRepresentativeIdDeliveryDate());
                    representative.setIdExpirationDate(receivingBusiness.getRepresentativeIdExpirationDate());
                    business.setRepresentative(representative);
                }

                beneficiaryInfo.setBusiness(business);
            }
        }

        return beneficiaryInfo;
    }

    /**
     * 构建支付方式信息
     */
    private TransactionData.PaymentMethodInfo buildPaymentMethodInfo(TransferRequestVo request) {
        TransactionData.PaymentMethodInfo paymentMethod = new TransactionData.PaymentMethodInfo();

        // 根据交易类型确定支付方式来源
        String transactionType = request.getTransactionType();

        if ("C2C".equals(transactionType) || "B2C".equals(transactionType)) {
            // 个人收款方的支付方式
            if (request.getBeneficiary() != null) {
                TransferRequestVo.BeneficiaryInfoVo beneficiary = request.getBeneficiary();
                if (StrUtil.isNotBlank(beneficiary.getBankAccountNumber())) {
                    paymentMethod.setType("BANK_ACCOUNT");
                    TransactionData.BankAccountInfo bankAccount = new TransactionData.BankAccountInfo();
                    bankAccount.setBankAccountNumber(beneficiary.getBankAccountNumber());
                    bankAccount.setAccountNumber(beneficiary.getAccountNumber());
                    bankAccount.setBranchNumber(beneficiary.getBranchNumber());
                    bankAccount.setAccountType(beneficiary.getAccountType());
                    bankAccount.setSwiftBicCode(beneficiary.getSwiftBicCode());
                    bankAccount.setSortCode(beneficiary.getSortCode());
                    bankAccount.setIban(beneficiary.getIban());
                    bankAccount.setClabe(beneficiary.getClabe());
                    paymentMethod.setBankAccount(bankAccount);
                } else if (StrUtil.isNotBlank(beneficiary.getMsisdn())) {
                    paymentMethod.setType("MOBILE_WALLET");
                    TransactionData.MobileWalletInfo mobileWallet = new TransactionData.MobileWalletInfo();
                    mobileWallet.setPhoneNumber(beneficiary.getMsisdn());
                    paymentMethod.setMobileWallet(mobileWallet);
                }
            }
        } else {
            // 企业收款方的支付方式
            if (request.getReceivingBusiness() != null) {
                TransferRequestVo.ReceivingBusinessInfoVo receivingBusiness = request.getReceivingBusiness();
                if (StrUtil.isNotBlank(receivingBusiness.getBankAccountNumber())) {
                    paymentMethod.setType("BANK_ACCOUNT");
                    TransactionData.BankAccountInfo bankAccount = new TransactionData.BankAccountInfo();
                    bankAccount.setBankAccountNumber(receivingBusiness.getBankAccountNumber());
                    bankAccount.setAccountNumber(receivingBusiness.getAccountNumber());
                    bankAccount.setBranchNumber(receivingBusiness.getBranchNumber());
                    bankAccount.setAccountType(receivingBusiness.getAccountType());
                    bankAccount.setSwiftBicCode(receivingBusiness.getSwiftBicCode());
                    bankAccount.setSortCode(receivingBusiness.getSortCode());
                    bankAccount.setIban(receivingBusiness.getIban());
                    bankAccount.setClabe(receivingBusiness.getClabe());
                    paymentMethod.setBankAccount(bankAccount);
                } else if (StrUtil.isNotBlank(receivingBusiness.getMsisdn())) {
                    paymentMethod.setType("MOBILE_WALLET");
                    TransactionData.MobileWalletInfo mobileWallet = new TransactionData.MobileWalletInfo();
                    mobileWallet.setPhoneNumber(receivingBusiness.getMsisdn());
                    paymentMethod.setMobileWallet(mobileWallet);
                }
            }
        }

        return paymentMethod;
    }

    /**
     * 构建合规信息
     */
    private TransactionData.ComplianceInfo buildComplianceInfo(TransferRequestVo request) {
        TransactionData.ComplianceInfo compliance = new TransactionData.ComplianceInfo();

        compliance.setPurposeOfRemittance(request.getPurposeOfRemittance());
        compliance.setExternalCode(request.getExternalCode());
        compliance.setDocumentReferenceNumber(request.getDocumentReferenceNumber());
        // 记录商户回调地址
        compliance.setCallbackUrl(request.getCallbackUrl());

        return compliance;
    }

    /**
     * 选择首选的支付方（优先选择知名银行，并根据交易类型筛选）
     */
    private PayerDto selectPreferredPayer(List<PayerDto> payers, String transactionType) {
        // 首先筛选支持指定交易类型的支付方
        List<PayerDto> supportedPayers = payers.stream()
                .filter(payer -> payer.supportsTransactionType(transactionType))
                .collect(Collectors.toList());

        if (supportedPayers.isEmpty()) {
            log.warn("没有找到支持交易类型 {} 的支付方，可用支付方数量: {}", transactionType, payers.size());
            // 如果没有明确支持的，使用原始列表（可能是API返回的数据不完整）
            supportedPayers = payers;
        } else {
            log.info("找到 {} 个支持交易类型 {} 的支付方", supportedPayers.size(), transactionType);
        }

        String targetCountry = supportedPayers.get(0).getCountryIsoCode();

        // 根据目标国家获取首选银行列表（动态获取）
        List<String> preferredBanks = thunesConfigService.getPreferredBanks(targetCountry);

        if (preferredBanks.isEmpty()) {
            // 如果没有配置首选银行，使用第一个可用的支付方
            PayerDto firstPayer = supportedPayers.get(0);
            log.debug("使用第一个支持 {} 交易类型的支付方（{}）: {}", transactionType, targetCountry, firstPayer.getName());
            return firstPayer;
        }

        // 首先尝试找到首选银行
        for (String preferredBank : preferredBanks) {
            for (PayerDto payer : supportedPayers) {
                if (payer.getName().contains(preferredBank)) {
                    log.debug("选择支持 {} 交易类型的首选银行（{}）: {}", transactionType, targetCountry, payer.getName());
                    return payer;
                }
            }
        }

        // 如果没有找到首选银行，返回第一个支持该交易类型的
        PayerDto firstPayer = supportedPayers.get(0);
        log.debug("使用第一个支持 {} 交易类型的支付方（{}）: {}", transactionType, targetCountry, firstPayer.getName());
        return firstPayer;
    }

    @Getter
    private static class QuotationResult {
        private final QuotationDto quotation;
        private final PayerDto payer;

        private QuotationResult(QuotationDto quotation, PayerDto payer) {
            this.quotation = quotation;
            this.payer = payer;
        }

    }

    /**
     * 创建报价（支持支付方重试）
     */
    private QuotationResult createQuotationWithRetry(String countryCode, String currency, TransferRequestVo request) {
        // 使用新的 Payer 查询方式（优先数据库，API 作为备用）
        List<PayerDto> payers = payerSyncService.getPayersWithFallback(countryCode, currency);
        if (payers == null || payers.isEmpty()) {
            throw new CustomException("未找到合适的支付方 - 国家: " + countryCode + ", 货币: " + currency);
        }

        // 按优先级排序支付方，并根据交易类型筛选
        PayerDto preferredPayer = selectPreferredPayer(payers, request.getTransactionType());

        // 验证金额是否在支付方限制范围内
        validateAmountLimits(request.getAmount(), preferredPayer, request.getTransactionType());

        // 根据选择的 Payer 进行动态字段验证
        validateFieldsByPayerConfig(request, preferredPayer);

        // 首先尝试首选支付方
        try {
            QuotationDto quotation = createQuotationForPayer(preferredPayer, request);
            return new QuotationResult(quotation, preferredPayer);
        } catch (CustomException e) {
            if (e.getMessage().contains("Payer is inactive")) {
                log.warn("首选支付方不活跃，尝试其他支持 {} 交易类型的支付方 - 支付方ID: {}",
                        request.getTransactionType(), preferredPayer.getId());

                // 尝试其他支持该交易类型的支付方
                for (PayerDto payer : payers) {
                    if (!payer.getId().equals(preferredPayer.getId()) &&
                            payer.supportsTransactionType(request.getTransactionType())) {
                        try {
                            // 验证金额是否在当前支付方限制范围内
                            validateAmountLimits(request.getAmount(), payer, request.getTransactionType());

                            // 根据当前 Payer 进行动态字段验证
                            validateFieldsByPayerConfig(request, payer);

                            log.info("尝试支持 {} 交易类型的支付方 - ID: {}, 名称: {}",
                                    request.getTransactionType(), payer.getId(), payer.getName());
                            QuotationDto quotation = createQuotationForPayer(payer, request);
                            return new QuotationResult(quotation, payer);
                        } catch (CustomException retryException) {
                            handleRetryablePayerException(retryException, payer.getId());
                        }
                    }
                }

                // 所有支持该交易类型的支付方都不活跃，尝试其他支付方
                log.warn("所有支持 {} 交易类型的支付方都不活跃，尝试其他支付方", request.getTransactionType());
                for (PayerDto payer : payers) {
                    if (!payer.getId().equals(preferredPayer.getId()) &&
                            !payer.supportsTransactionType(request.getTransactionType())) {
                        try {
                            // 验证金额是否在当前支付方限制范围内
                            validateAmountLimits(request.getAmount(), payer, request.getTransactionType());

                            log.info("尝试其他支付方（可能不完全支持 {} 交易类型）- ID: {}, 名称: {}",
                                    request.getTransactionType(), payer.getId(), payer.getName());
                            QuotationDto quotation = createQuotationForPayer(payer, request);
                            return new QuotationResult(quotation, payer);
                        } catch (CustomException retryException) {
                            handleRetryablePayerException(retryException, payer.getId());
                        }
                    }
                }

                // 所有支付方都不活跃
                throw new CustomException(
                        "所有支付方在您的 Thunes 账户中都不活跃或不支持当前转账金额。" +
                                "请联系 Thunes 支持团队激活 " + countryCode + " 的支付方，特别是支持 " + request.getTransactionType()
                                + " 交易类型的支付方。" +
                                "可用支付方数量: " + payers.size());
            } else {
                // 如果是其他错误，直接抛出
                throw e;
            }
        }
    }

    /**
     * 验证转账金额是否在支付方限制范围内
     */
    private void validateAmountLimits(BigDecimal amount, PayerDto payer, String transactionType) {
        // 检查支付方的全局限制
        if (payer.getMinAmount() != null && amount.compareTo(payer.getMinAmount()) < 0) {
            throw new CustomException(String.format("转账金额 %s 低于支付方最小限制 %s %s",
                    amount, payer.getMinAmount(), payer.getCurrency()));
        }

        if (payer.getMaxAmount() != null && amount.compareTo(payer.getMaxAmount()) > 0) {
            throw new CustomException(String.format("转账金额 %s 超过支付方最大限制 %s %s",
                    amount, payer.getMaxAmount(), payer.getCurrency()));
        }

        // 检查特定交易类型的限制
        if (payer.getTransactionTypes() != null) {
            PayerDto.TransactionTypeDto transactionTypeConfig = payer.getTransactionTypes().get(transactionType);
            if (transactionTypeConfig != null) {
                if (transactionTypeConfig.getMinimumTransactionAmount() != null &&
                        amount.compareTo(transactionTypeConfig.getMinimumTransactionAmount()) < 0) {
                    throw new CustomException(String.format("转账金额 %s 低于 %s 交易类型最小限制 %s %s",
                            amount, transactionType, transactionTypeConfig.getMinimumTransactionAmount(),
                            payer.getCurrency()));
                }

                if (transactionTypeConfig.getMaximumTransactionAmount() != null &&
                        amount.compareTo(transactionTypeConfig.getMaximumTransactionAmount()) > 0) {
                    throw new CustomException(String.format("转账金额 %s 超过 %s 交易类型最大限制 %s %s",
                            amount, transactionType, transactionTypeConfig.getMaximumTransactionAmount(),
                            payer.getCurrency()));
                }
            }
        }

        log.debug("金额验证通过 - 支付方ID: {}, 交易类型: {}, 金额: {} {}",
                payer.getId(), transactionType, amount, payer.getCurrency());
    }

    /**
     * 为特定支付方创建报价
     */
    private QuotationDto createQuotationForPayer(PayerDto payer, TransferRequestVo request) {
        log.info("为支付方创建报价 - 支付方ID: {}, 名称: {}", payer.getId(), payer.getName());

        QuotationRequestDto quotationRequest = new QuotationRequestDto();

        // 设置必需参数
        quotationRequest.setExternalId(request.getExternalId() + "_quote");
        quotationRequest.setPayerId(payer.getId());
        quotationRequest.setMode(ThunesConstants.MODE_SOURCE_AMOUNT);
        quotationRequest.setTransactionType(request.getTransactionType());

        // 设置源信息 - 固定使用马耳他作为源国家
        QuotationRequestDto.SourceDto source = new QuotationRequestDto.SourceDto();
        source.setCountryIsoCode(ThunesConstants.FIXED_SOURCE_COUNTRY);
        source.setCurrency(request.getCurrency());
        source.setAmount(request.getAmount());
        quotationRequest.setSource(source);

        // 设置目标信息
        QuotationRequestDto.DestinationDto destination = new QuotationRequestDto.DestinationDto();
        destination.setCountryIsoCode(request.getTargetCountry());
        destination.setCurrency(request.getTargetCurrency());
        quotationRequest.setDestination(destination);

        // 验证请求
        validateQuotationRequest(quotationRequest);

        log.info("创建报价请求 - 外部ID: {}, 支付方ID: {}, 交易类型: {}, 源国家: {}, 源货币: {}, 目标国家: {}, 目标货币: {}",
                quotationRequest.getExternalId(), quotationRequest.getPayerId(),
                quotationRequest.getTransactionType(), source.getCountryIsoCode(), source.getCurrency(),
                destination.getCountryIsoCode(), destination.getCurrency());

        return thunesApiService.createQuotation(quotationRequest);
    }

    /**
     * 验证报价请求的必需字段
     */
    private void validateQuotationRequest(QuotationRequestDto request) {
        if (StrUtil.isBlank(request.getExternalId())) {
            throw new CustomException("报价请求缺少外部ID");
        }
        if (request.getPayerId() == null) {
            throw new CustomException("报价请求缺少支付方ID");
        }
        if (StrUtil.isBlank(request.getMode())) {
            throw new CustomException("报价请求缺少模式");
        }
        if (StrUtil.isBlank(request.getTransactionType())) {
            throw new CustomException("报价请求缺少交易类型");
        }
        if (request.getSource() == null) {
            throw new CustomException("报价请求缺少源信息");
        }
        if (request.getDestination() == null) {
            throw new CustomException("报价请求缺少目标信息");
        }

        // 验证源信息
        QuotationRequestDto.SourceDto source = request.getSource();
        if (StrUtil.isBlank(source.getCountryIsoCode())) {
            throw new CustomException("报价请求源信息缺少国家代码");
        }
        if (StrUtil.isBlank(source.getCurrency())) {
            throw new CustomException("报价请求源信息缺少货币");
        }
        if (source.getAmount() == null || source.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException("报价请求源信息缺少有效金额");
        }

        // 验证目标信息
        QuotationRequestDto.DestinationDto destination = request.getDestination();
        if (StrUtil.isBlank(destination.getCountryIsoCode())) {
            throw new CustomException("报价请求目标信息缺少国家代码");
        }
        if (StrUtil.isBlank(destination.getCurrency())) {
            throw new CustomException("报价请求目标信息缺少货币");
        }

        log.debug("报价请求验证通过");
    }

    /**
     * 创建交易
     */
    private TransactionDto createTransaction(String quotationId, TransferRequestVo request, PayerDto payer) {
        TransactionRequestDto transactionRequest = new TransactionRequestDto();

        // 设置外部订单号
        transactionRequest.setExternalId(request.getExternalId());

        // 根据支付方配置过滤收款方标识信息
        String transactionType = request.getTransactionType();
        transactionRequest.setCreditPartyIdentifier(buildCreditPartyIdentifier(request, payer));

        // 根据交易类型设置不同的字段组合
        switch (transactionType) {
            case "C2C":
                // C2C: 需要 sender 和 beneficiary
                transactionRequest.setSender(buildSenderForIndividual(request.getSender()));
                transactionRequest.setBeneficiary(buildBeneficiaryForIndividual(request.getBeneficiary()));
                break;

            case "C2B":
                // C2B: 需要 sender 和 receiving_business
                transactionRequest.setSender(buildSenderForIndividual(request.getSender()));
                if (request.getReceivingBusiness() != null) {
                    transactionRequest.setReceivingBusiness(buildReceivingBusiness(request.getReceivingBusiness()));
                }
                break;

            case "B2C":
                // B2C: 需要 sending_business 和 beneficiary
                transactionRequest.setSendingBusiness(buildSendingBusiness(request));
                transactionRequest.setBeneficiary(buildBeneficiaryForIndividual(request.getBeneficiary()));
                break;

            case "B2B":
                // B2B: 需要 sending_business 和 receiving_business
                transactionRequest.setSendingBusiness(buildSendingBusiness(request));
                if (request.getReceivingBusiness() != null) {
                    transactionRequest.setReceivingBusiness(buildReceivingBusiness(request.getReceivingBusiness()));
                }
                break;

            default:
                throw new CustomException("不支持的交易类型: " + transactionType);
        }

        // 设置其他信息
        transactionRequest.setCallbackUrl(resolveThunesCallbackUrl());
        transactionRequest.setPurposeOfRemittance(request.getPurposeOfRemittance());
        transactionRequest.setDocumentReferenceNumber(request.getDocumentReferenceNumber());

        return thunesApiService.createTransaction(quotationId, transactionRequest);
    }

    /**
     * 通知商户转账状态回调
     */
    public void notifyMerchantCallback(ThunesTransferRecord record) {
        if (record == null) {
            log.warn("Thunes转账回调商户失败 - 转账记录为空");
            return;
        }
        String callbackUrl = resolveMerchantCallbackUrl(record);
        if (StrUtil.isBlank(callbackUrl)) {
            log.warn("Thunes转账回调商户跳过 - 回调地址为空, 转账ID: {}, 外部订单号: {}",
                    record.getId(), record.getExternalId());
            return;
        }
        Shop shop = shopService.getById(record.getMerchantId());
        if (shop == null) {
            log.warn("Thunes转账回调商户失败 - 商户不存在, merchantId: {}, 转账ID: {}",
                    record.getMerchantId(), record.getId());
            return;
        }

        Map<String, Object> payload = buildMerchantCallbackPayload(record, shop);
        try (HttpResponse response = HttpRequest.post(callbackUrl)
                .body(JSON.toJSONString(payload))
                .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                .charset(Charset.defaultCharset())
                .execute()) {
            String body = response.body();
            log.info("Thunes转账回调商户完成 - 转账ID: {}, 外部订单号: {}, 回调地址: {}",
                    record.getId(), record.getExternalId(), callbackUrl);
            log.info("Thunes转账回调商户参数: {}", JSON.toJSONString(payload));
            log.info("Thunes转账回调商户响应: {}", body);
        } catch (Exception e) {
            log.error("Thunes转账回调商户异常 - 转账ID: {}, 外部订单号: {}",
                    record.getId(), record.getExternalId(), e);
        }
    }

    private Map<String, Object> buildMerchantCallbackPayload(ThunesTransferRecord record, Shop shop) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("transferId", record.getId());
        payload.put("merchantId", record.getMerchantId());
        payload.put("externalId", record.getExternalId());
        payload.put("transactionId", record.getTransactionId());
        payload.put("status", record.getStatus());
        payload.put("thunesStatus", record.getThunesStatus());
        payload.put("transactionType", record.getTransactionType());
        payload.put("amount", record.getAmount());
        payload.put("currency", record.getCurrency());
        payload.put("targetCountry", record.getTargetCountry());
        payload.put("targetAmount", record.getTargetAmount());
        payload.put("targetCurrency", record.getTargetCurrency());
        payload.put("failReason", record.getFailReason());
        payload.put("callbackTime", new Date());
        try {
            payload.put("sign", SignUtil.sign(shop.getMerchantHashSecretKey(), record.getExternalId()));
        } catch (Exception e) {
            log.warn("Thunes转账回调签名生成失败 - merchantId: {}, 转账ID: {}",
                    shop.getId(), record.getId(), e);
        }
        return payload;
    }

    private String resolveMerchantCallbackUrl(ThunesTransferRecord record) {
        if (record == null) {
            return null;
        }
        if (StrUtil.isNotBlank(record.getCallbackUrl())) {
            return record.getCallbackUrl();
        }
        if (record.getTransactionData() == null || record.getTransactionData().getCompliance() == null) {
            return null;
        }
        return record.getTransactionData().getCompliance().getCallbackUrl();
    }

    private String resolveThunesCallbackUrl() {
        String callbackUrl = thunesConfig.getCallbackFullUrl();
        if (StrUtil.isBlank(callbackUrl)) {
            throw new CustomException("Thunes回调地址未配置");
        }
        return callbackUrl;
    }

    private TransactionRequestDto.CreditPartyIdentifierDto buildCreditPartyIdentifier(
            TransferRequestVo request, PayerDto payer) {
        String transactionType = request.getTransactionType();
        Object creditPartySource = resolveCreditPartySource(transactionType, request);
        if (creditPartySource == null) {
            throw new CustomException("收款方账户信息不能为空");
        }

        PayerDto.TransactionTypeDto transactionConfig = payer != null ? payer.getTransactionTypeConfig(transactionType)
                : null;
        List<List<String>> accepted = transactionConfig != null ? transactionConfig.getCreditPartyIdentifiersAccepted()
                : null;

        if (accepted == null || accepted.isEmpty()) {
            return buildCreditPartyIdentifierFallback(transactionType, request);
        }

        List<String> matchedGroup = null;
        for (List<String> group : accepted) {
            if (group == null || group.isEmpty()) {
                continue;
            }
            boolean groupSatisfied = true;
            for (String field : group) {
                if (isFieldMissing(creditPartySource, field)) {
                    groupSatisfied = false;
                    break;
                }
            }
            if (groupSatisfied) {
                matchedGroup = group;
                break;
            }
        }

        if (matchedGroup == null) {
            throw new CustomException("收款方账户信息缺少必需字段，需要满足以下任一组合：" +
                    formatFieldGroups(accepted));
        }

        TransactionRequestDto.CreditPartyIdentifierDto creditParty = new TransactionRequestDto.CreditPartyIdentifierDto();
        for (String field : matchedGroup) {
            applyCreditPartyField(creditParty, creditPartySource, field);
        }

        return creditParty;
    }

    private Object resolveCreditPartySource(String transactionType, TransferRequestVo request) {
        if ("C2C".equals(transactionType) || "B2C".equals(transactionType)) {
            return request.getBeneficiary();
        }
        if ("C2B".equals(transactionType) || "B2B".equals(transactionType)) {
            return request.getReceivingBusiness();
        }
        return null;
    }

    private TransactionRequestDto.CreditPartyIdentifierDto buildCreditPartyIdentifierFallback(
            String transactionType, TransferRequestVo request) {
        Object creditPartySource = resolveCreditPartySource(transactionType, request);
        if (creditPartySource == null) {
            throw new CustomException("收款方账户信息不能为空");
        }

        TransactionRequestDto.CreditPartyIdentifierDto creditParty = new TransactionRequestDto.CreditPartyIdentifierDto();
        copyMatchingCreditPartyFields(creditParty, creditPartySource);

        return creditParty;
    }

    private void applyCreditPartyField(TransactionRequestDto.CreditPartyIdentifierDto target,
            Object source, String fieldName) {
        String value = getStringFieldValue(source, fieldName);
        if (StrUtil.isBlank(value)) {
            return;
        }

        String normalizedFieldName = normalizeFieldName(fieldName);
        if (StrUtil.isBlank(normalizedFieldName)) {
            return;
        }
        try {
            Field field = findFieldByNormalizedName(target.getClass(), normalizedFieldName);
            if (field == null) {
                log.warn("未识别的收款方标识字段: {}", fieldName);
                return;
            }
            if (!String.class.equals(field.getType())) {
                return;
            }
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            log.warn("设置字段失败 - 字段: {}, 错误: {}", fieldName, e.getMessage());
        }
    }

    private String getStringFieldValue(Object source, String fieldName) {
        if (source == null || StrUtil.isBlank(fieldName)) {
            return null;
        }
        String normalizedFieldName = normalizeFieldName(fieldName);
        if (StrUtil.isBlank(normalizedFieldName)) {
            return null;
        }
        try {
            Field field = findFieldByNormalizedName(source.getClass(), normalizedFieldName);
            if (field == null) {
                return null;
            }
            field.setAccessible(true);
            Object value = field.get(source);
            return value == null ? null : String.valueOf(value);
        } catch (Exception e) {
            log.warn("读取字段失败 - 类: {}, 字段: {}, 错误: {}",
                    source.getClass().getSimpleName(), fieldName, e.getMessage());
            return null;
        }
    }

    private void copyMatchingCreditPartyFields(TransactionRequestDto.CreditPartyIdentifierDto target,
            Object source) {
        for (Field field : target.getClass().getDeclaredFields()) {
            if (!String.class.equals(field.getType())) {
                continue;
            }
            applyCreditPartyField(target, source, field.getName());
        }
    }

    private String formatFieldGroups(List<List<String>> fieldGroups) {
        if (fieldGroups == null || fieldGroups.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (List<String> group : fieldGroups) {
            if (group == null || group.isEmpty()) {
                continue;
            }
            builder.append(" [").append(String.join(", ", group)).append("]");
        }
        return builder.toString();
    }

    /**
     * 确认交易
     */
    private TransactionDto confirmTransaction(String transactionId) {
        return thunesApiService.confirmTransaction(transactionId);
    }

    /**
     * 验证转账请求签名
     *
     * @param shop    商户信息
     * @param request 转账请求
     * @return 验证结果
     */
    public boolean verifyTransferSignature(Shop shop, TransferRequestVo request) {
        try {
            String expectedSignature = SignUtil.sign(shop.getMerchantHashSecretKey(), request.getExternalId());
            boolean isValid = expectedSignature.equals(request.getSignature());

            if (!isValid) {
                log.warn("转账签名验证失败 - 商户ID: {}, 外部订单号: {}, 期望签名: {}, 实际签名: {}",
                        shop.getId(), request.getExternalId(), expectedSignature, request.getSignature());
            }

            return isValid;
        } catch (Exception e) {
            log.error("转账签名验证异常 - 商户ID: {}, 外部订单号: {}",
                    shop.getId(), request.getExternalId(), e);
            return false;
        }
    }

    /**
     * 检查 Thunes 是否支持指定的国家和货币
     *
     * @param countryCode 国家代码
     * @param currency    货币代码
     * @return 是否支持
     */
    public boolean isSupportedCountryAndCurrency(String countryCode, String currency) {
        return thunesConfigService.isSupportedCountryAndCurrency(countryCode, currency);
    }

    /**
     * 验证查询状态请求签名
     *
     * @param shop       商户信息
     * @param transferId 转账ID
     * @param signature  签名
     * @return 验证结果
     */
    public boolean verifyStatusQuerySignature(Shop shop, Long transferId, String signature) {
        try {
            // 使用转账ID作为签名内容
            String expectedSignature = SignUtil.sign(shop.getMerchantHashSecretKey(), transferId.toString());
            boolean isValid = expectedSignature.equals(signature);

            if (!isValid) {
                log.warn("状态查询签名验证失败 - 商户ID: {}, 转账ID: {}, 期望签名: {}, 实际签名: {}",
                        shop.getId(), transferId, expectedSignature, signature);
            }

            return isValid;
        } catch (Exception e) {
            log.error("状态查询签名验证异常 - 商户ID: {}, 转账ID: {}",
                    shop.getId(), transferId, e);
            return false;
        }
    }

    /**
     * 处理可重试的支付方异常
     * 如果异常是可重试的（支付方不活跃或金额限制不符合），则记录日志并返回
     * 如果是其他类型的异常，则直接抛出
     *
     * @param exception 捕获的异常
     * @param payerId   支付方ID
     */
    private void handleRetryablePayerException(CustomException exception, Long payerId) {
        String message = exception.getMessage();
        if (message.contains("Payer is inactive")) {
            log.warn("支付方不活跃，继续尝试下一个 - 支付方ID: {}", payerId);
        } else if (message.contains("金额超出限制")) {
            log.warn("支付方金额限制不符合，继续尝试下一个 - 支付方ID: {}, 错误: {}", payerId, message);
        } else {
            // 如果是其他错误，直接抛出
            throw exception;
        }
    }

    /**
     * 构建个人发送方信息
     */
    private TransactionRequestDto.SenderDto buildSenderForIndividual(TransferRequestVo.SenderInfoVo senderInfo) {
        if (senderInfo == null) {
            return null;
        }

        TransactionRequestDto.SenderDto sender = new TransactionRequestDto.SenderDto();
        sender.setLastname(senderInfo.getLastName());
        sender.setFirstname(senderInfo.getFirstName());
        sender.setLastname2(senderInfo.getLastName2());
        sender.setMiddlename(senderInfo.getMiddleName());
        sender.setNativename(senderInfo.getNativeName());
        sender.setNationalityCountryIsoCode(senderInfo.getNationalityCountryIsoCode());
        sender.setDateOfBirth(senderInfo.getDateOfBirth());
        sender.setCountryOfBirthIsoCode(senderInfo.getCountryOfBirthIsoCode());
        sender.setGender(senderInfo.getGender());
        sender.setAddress(senderInfo.getAddress());
        sender.setPostalCode(senderInfo.getPostalCode());
        sender.setCity(senderInfo.getCity());
        sender.setProvinceState(senderInfo.getProvinceState());
        sender.setCountryIsoCode(ThunesConstants.FIXED_SOURCE_COUNTRY); // 固定为马耳他
        sender.setMsisdn(senderInfo.getMsisdn());
        sender.setEmail(senderInfo.getEmail());
        sender.setIdType(senderInfo.getIdType());
        sender.setIdCountryIsoCode(senderInfo.getIdCountryIsoCode());
        sender.setIdNumber(senderInfo.getIdNumber());
        sender.setIdDeliveryDate(senderInfo.getIdDeliveryDate());
        sender.setIdExpirationDate(senderInfo.getIdExpirationDate());
        sender.setOccupation(senderInfo.getOccupation());
        sender.setBankAccountNumber(senderInfo.getBankAccountNumber());
        sender.setBeneficiaryRelationship(senderInfo.getBeneficiaryRelationship());
        sender.setSourceOfFunds(senderInfo.getSourceOfFunds());
        sender.setCode(senderInfo.getCode());

        return sender;
    }

    /**
     * 构建个人收款方信息
     */
    private TransactionRequestDto.BeneficiaryDto buildBeneficiaryForIndividual(
            TransferRequestVo.BeneficiaryInfoVo beneficiaryInfo) {
        if (beneficiaryInfo == null) {
            return null;
        }

        TransactionRequestDto.BeneficiaryDto beneficiary = new TransactionRequestDto.BeneficiaryDto();
        beneficiary.setLastname(beneficiaryInfo.getLastName());
        beneficiary.setFirstname(beneficiaryInfo.getFirstName());
        beneficiary.setLastname2(beneficiaryInfo.getLastName2());
        beneficiary.setMiddlename(beneficiaryInfo.getMiddleName());
        beneficiary.setNativename(beneficiaryInfo.getNativeName());
        beneficiary.setNationalityCountryIsoCode(beneficiaryInfo.getNationalityCountryIsoCode());
        beneficiary.setCode(beneficiaryInfo.getCode());
        beneficiary.setDateOfBirth(beneficiaryInfo.getDateOfBirth());
        beneficiary.setCountryOfBirthIsoCode(beneficiaryInfo.getCountryOfBirthIsoCode());
        beneficiary.setGender(beneficiaryInfo.getGender());
        beneficiary.setAddress(beneficiaryInfo.getAddress());
        beneficiary.setPostalCode(beneficiaryInfo.getPostalCode());
        beneficiary.setCity(beneficiaryInfo.getCity());
        beneficiary.setProvinceState(beneficiaryInfo.getProvinceState());
        beneficiary.setCountryIsoCode(beneficiaryInfo.getCountryIsoCode());
        beneficiary.setMsisdn(beneficiaryInfo.getMsisdn());
        beneficiary.setEmail(beneficiaryInfo.getEmail());
        beneficiary.setIdType(beneficiaryInfo.getIdType());
        beneficiary.setIdCountryIsoCode(beneficiaryInfo.getIdCountryIsoCode());
        beneficiary.setIdNumber(beneficiaryInfo.getIdNumber());
        beneficiary.setIdDeliveryDate(beneficiaryInfo.getIdDeliveryDate());
        beneficiary.setIdExpirationDate(beneficiaryInfo.getIdExpirationDate());
        beneficiary.setOccupation(beneficiaryInfo.getOccupation());
        beneficiary.setBankAccountHolderName(beneficiaryInfo.getBankAccountHolderName());

        return beneficiary;
    }

    /**
     * 构建发送方企业信息
     */
    private TransactionRequestDto.SendingBusinessDto buildSendingBusiness(TransferRequestVo request) {
        String transactionType = request.getTransactionType();

        if ("B2C".equals(transactionType) || "B2B".equals(transactionType)) {
            // B2C/B2B 交易：使用 sendingBusiness
            TransferRequestVo.SendingBusinessInfoVo sendingBusiness = request.getSendingBusiness();
            if (sendingBusiness == null) {
                return null;
            }

            TransactionRequestDto.SendingBusinessDto dto = new TransactionRequestDto.SendingBusinessDto();
            dto.setRegisteredName(sendingBusiness.getRegisteredName());
            dto.setTradingName(sendingBusiness.getTradingName());
            dto.setAddress(sendingBusiness.getAddress());
            dto.setPostalCode(sendingBusiness.getPostalCode());
            dto.setCity(sendingBusiness.getCity());
            dto.setProvinceState(sendingBusiness.getProvinceState());
            dto.setCountryIsoCode(ThunesConstants.FIXED_SOURCE_COUNTRY); // 固定为马耳他
            dto.setMsisdn(sendingBusiness.getMsisdn());
            dto.setEmail(sendingBusiness.getEmail());
            dto.setRegistrationNumber(sendingBusiness.getRegistrationNumber());
            dto.setCode(sendingBusiness.getCode());
            dto.setTaxId(sendingBusiness.getTaxId());
            dto.setDateOfIncorporation(sendingBusiness.getDateOfIncorporation());
            dto.setRepresentativeLastname(sendingBusiness.getRepresentativeLastname());
            dto.setRepresentativeLastname2(sendingBusiness.getRepresentativeLastname2());
            dto.setRepresentativeFirstname(sendingBusiness.getRepresentativeFirstname());
            dto.setRepresentativeMiddlename(sendingBusiness.getRepresentativeMiddlename());
            dto.setRepresentativeNativename(sendingBusiness.getRepresentativeNativename());
            dto.setRepresentativeIdType(sendingBusiness.getRepresentativeIdType());
            dto.setRepresentativeIdCountryIsoCode(sendingBusiness.getRepresentativeIdCountryIsoCode());
            dto.setRepresentativeIdNumber(sendingBusiness.getRepresentativeIdNumber());
            dto.setRepresentativeIdDeliveryDate(sendingBusiness.getRepresentativeIdDeliveryDate());
            dto.setRepresentativeIdExpirationDate(sendingBusiness.getRepresentativeIdExpirationDate());
            dto.setBusinessRelationship(sendingBusiness.getBusinessRelationship());

            return dto;
        } else {
            // C2C/C2B 交易：从 sender 构建（向后兼容）
            TransferRequestVo.SenderInfoVo senderInfo = request.getSender();
            if (senderInfo == null) {
                return null;
            }

            TransactionRequestDto.SendingBusinessDto dto = new TransactionRequestDto.SendingBusinessDto();
            dto.setRegisteredName(senderInfo.getRegisteredName());
            dto.setCountryIsoCode(ThunesConstants.FIXED_SOURCE_COUNTRY); // 固定为马耳他
            dto.setCode(senderInfo.getCode());
            dto.setRegistrationNumber(senderInfo.getRegistrationNumber());
            dto.setTaxId(senderInfo.getTaxId());

            return dto;
        }
    }

    private TransactionRequestDto.ReceivingBusinessDto buildReceivingBusiness(
            TransferRequestVo.ReceivingBusinessInfoVo receivingBusinessVo) {
        TransactionRequestDto.ReceivingBusinessDto receivingBusiness = new TransactionRequestDto.ReceivingBusinessDto();

        receivingBusiness.setRegisteredName(receivingBusinessVo.getRegisteredName());
        receivingBusiness.setTradingName(receivingBusinessVo.getTradingName());
        receivingBusiness.setAddress(receivingBusinessVo.getAddress());
        receivingBusiness.setPostalCode(receivingBusinessVo.getPostalCode());
        receivingBusiness.setCity(receivingBusinessVo.getCity());
        receivingBusiness.setProvinceState(receivingBusinessVo.getProvinceState());
        receivingBusiness.setCountryIsoCode(receivingBusinessVo.getCountryIsoCode());
        receivingBusiness.setMsisdn(receivingBusinessVo.getMsisdn());
        receivingBusiness.setEmail(receivingBusinessVo.getEmail());
        receivingBusiness.setRegistrationNumber(receivingBusinessVo.getRegistrationNumber());
        receivingBusiness.setTaxId(receivingBusinessVo.getTaxId());
        receivingBusiness.setDateOfIncorporation(receivingBusinessVo.getDateOfIncorporation());
        receivingBusiness.setRepresentativeLastname(receivingBusinessVo.getRepresentativeLastname());
        receivingBusiness.setRepresentativeLastname2(receivingBusinessVo.getRepresentativeLastname2());
        receivingBusiness.setRepresentativeFirstname(receivingBusinessVo.getRepresentativeFirstname());
        receivingBusiness.setRepresentativeMiddlename(receivingBusinessVo.getRepresentativeMiddlename());
        receivingBusiness.setRepresentativeNativename(receivingBusinessVo.getRepresentativeNativename());
        receivingBusiness.setRepresentativeIdType(receivingBusinessVo.getRepresentativeIdType());
        receivingBusiness.setRepresentativeIdCountryIsoCode(receivingBusinessVo.getRepresentativeIdCountryIsoCode());
        receivingBusiness.setRepresentativeIdNumber(receivingBusinessVo.getRepresentativeIdNumber());
        receivingBusiness.setRepresentativeIdDeliveryDate(receivingBusinessVo.getRepresentativeIdDeliveryDate());
        receivingBusiness.setRepresentativeIdExpirationDate(receivingBusinessVo.getRepresentativeIdExpirationDate());

        return receivingBusiness;
    }

    /**
     * 转换为状态 DTO
     */
    private TransferStatusDto convertToStatusDto(ThunesTransferRecord record) {
        TransferStatusDto dto = new TransferStatusDto();
        BeanUtil.copyProperties(record, dto);
        dto.setTransferId(record.getId());
        dto.setThunesTransactionId(record.getTransactionId());
        return dto;
    }
}
