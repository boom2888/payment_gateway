package com.games.payment.mapper;

import com.games.pay.vo.ShopEntity;
import com.games.payment.domain.Shop;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 企业客户公司Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ShopMapper extends BaseMapper<Shop> {
    /**
     * 查询企业客户公司列表
     *
     * @param businessCustomerCorporation 企业客户公司
     * @return 企业客户公司集合
     */
    List<ShopEntity> selectAllList(Shop businessCustomerCorporation);
    List<ShopEntity> selectListWithUser(Shop businessCustomerCorporation);

    List<ShopEntity> listWhitelistSecondaryMerchantDetails(@Param("parentId") Long parentId,
                                                           @Param("whiteListOn") Integer whiteListOn);

    ShopEntity getInfoById(Long id);
}
