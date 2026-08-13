package com.games.api;

import cn.hutool.json.JSONUtil;
import com.games.api.dto.RiskControlEntity;
import com.games.api.enums.PaymentMethod;
import com.games.api.enums.RiskCacheName;
import com.games.api.vo.PayOrderVo;
import com.games.common.core.redis.RedisCache;
import com.games.common.exception.BusinessException;
import com.games.common.utils.SignUtil;
import com.games.common.utils.StringUtils;
import com.games.payment.domain.Order;
import com.games.payment.domain.Shop;
import com.games.payment.service.ICustomerService;
import com.games.wynpay.WynPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 安全支付拦截服务
 */
@Slf4j
@Service
public class SecurityInterceptionService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    ICustomerService customerService;

    @Autowired
    PaymentCheckService paymentCheckService;

    @Autowired
    WynPayConfig wynPayConfig;

    public void payInterception(Order dbOrder, Shop shop,PayOrderVo  vo){
        if (!wynPayConfig.getCacheEnabled()) {
            System.out.println("写入缓存开关处于关闭状态");
            return;
        }
        RiskControlEntity riskControlEntity = buildRiskControlEntityForPayOrder(dbOrder, shop, vo);
        this.payInterception(riskControlEntity);
    }

    /**
     * 拦截支付
     * @param riskControlEntity
     * @throws BusinessException
     */
    public void payInterception(RiskControlEntity riskControlEntity) throws BusinessException{
        Boolean isNewUser = riskControlEntity.getIsNewUser();
        riskControlEntity.setPaymentMethod(PaymentMethod.NUVEI.getName());
        payInterceptionIsNewUser(riskControlEntity, isNewUser);
    }

    private RiskControlEntity buildRiskControlEntityForPayOrder(Order dbOrder, Shop shop, PayOrderVo vo){
        String ip = vo.getDeviceDetail().getIpAddress();
        String json = redisCache.getCacheObject(RiskCacheName.RISK_ENTITY.getName() + dbOrder.getSsoOrderId());
        if (json == null) {
            log.error(StringUtils.format("orderId:{} not found", dbOrder.getSsoOrderId()));
        }
        RiskControlEntity riskControlEntity;
        riskControlEntity = JSONUtil.toBean(json, RiskControlEntity.class);
        riskControlEntity = riskControlEntity == null ? new RiskControlEntity() : riskControlEntity;
        riskControlEntity.setIpAddress(ip);
        String rawPan = vo.getCardInfo() != null ? vo.getCardInfo().getCardNumber() : null;
	        String maskedPan = SignUtil.maskPan(rawPan);
	        riskControlEntity.setCardNumber(maskedPan);
	        riskControlEntity.setCardFinger(SignUtil.cardFingerprint(rawPan));
	        riskControlEntity.setCardOrganization(maskedPan);
	        riskControlEntity.setPayAmount(dbOrder.getFiatAmount());
	        return riskControlEntity;
	    }

	    /**
	     * 判断是否是新用户
     * @param riskControlEntity
     * @throws BusinessException
     */
    private void payInterceptionIsNewUser(RiskControlEntity riskControlEntity, Boolean isNewUser) throws BusinessException{
        String email = riskControlEntity.getEmail();
        String payMethod = riskControlEntity.getPaymentMethod();
        if (isNewUser) {
            // 新用户 ＜＝1小时 同一邮箱 大于2500欧	等于3笔	3D
            paymentCheckService.check(1, email, payMethod, "2500", 2, 1, riskControlEntity);

            // 新用户 ＜＝1小时 同一邮箱 大于2500欧	等于3笔	非3D
            paymentCheckService.check(1, email, payMethod, "2500", 2, 0, riskControlEntity);

            // 新用户 ＜＝1小时 同一邮箱 大于500美元	等于3笔
            paymentCheckService.check(1, email, payMethod, "500", 2, 2, riskControlEntity);

            // 新用户 <=24小时 同一邮箱 大于12笔 3D
            paymentCheckService.check(24, email, payMethod, null, 12, 1, riskControlEntity);

            // 新用户 <=24小时 同一邮箱 大于3000欧
            paymentCheckService.check(24, email, payMethod, "3000", null, 2, riskControlEntity);

            // 新用户 <=24小时 同一邮箱 大于8笔 非3D
            paymentCheckService.check(24, email, payMethod, null, 8, 0, riskControlEntity);

            //
        } else {
            // 用户 ＜＝1小时 同一邮箱 大于15笔	非3D
            paymentCheckService.check(1, email, payMethod, null, 15, 0, riskControlEntity);

            // 用户 ＜＝1小时 同一邮箱 大于25笔	3D
            paymentCheckService.check(1, email, payMethod, null, 25, 1, riskControlEntity);

            // 用户 ＜＝24小时 同一邮箱 大于30笔
            paymentCheckService.check(24, email, payMethod, null, 30, 2, riskControlEntity);

            // 用户 ＜＝24小时 同一邮箱 大于20000欧 3d
            paymentCheckService.check(24, email, payMethod, "20000", null, 1, riskControlEntity);

            // 用户 ＜＝24小时 同一邮箱 大于12笔 非3D
            paymentCheckService.check(24, email, payMethod, null, 12, 0, riskControlEntity);

            // 用户 <=24小时 同一邮箱 大于20000欧 非3D
            paymentCheckService.check(24, email, payMethod, "20000", null, 0, riskControlEntity);

        }

    }

}
