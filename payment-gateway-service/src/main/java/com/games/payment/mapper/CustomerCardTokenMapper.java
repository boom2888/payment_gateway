package com.games.payment.mapper;

import com.games.payment.domain.Card;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 客户卡令牌Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface CustomerCardTokenMapper extends BaseMapper<Card> {
        /**
         * 查询客户卡令牌列表
         *
         * @param customerCardToken 客户卡令牌
         * @return 客户卡令牌集合
         */
        List<Card> selectAllList(Card customerCardToken);

        Long selectCustomerId(@Param("cardFinger") String cardFinger, @Param("userID") Long userID,
                        @Param("acquirerId") Long acquirerId, @Param("merchantId") Long merchantId);

        Card selectByCardFingerAndUserID(@Param("cardFinger") String cardFinger, @Param("userID") Long userID,
                        @Param("acquirerId") Long acquirerId);

}
