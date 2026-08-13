package com.games.payment.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.games.pay.vo.ShopEntity;
import com.games.payment.domain.Order;
import com.games.payment.domain.Shop;

import java.util.List;

/**
 * 企业客户公司Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IShopService extends IService<Shop> {


    List<Shop> getRuningShops();

    /**
     * 查询企业客户公司列表
     *
     * @param businessCustomerCorporation 企业客户公司
     * @return 企业客户公司集合
     */
    List<ShopEntity> selectAllList(Shop businessCustomerCorporation);

    /**
     * 查询列表
     */
    List<Shop> queryList(Shop businessCustomerCorporation);

    List<ShopEntity> selectAuthList(Shop shop);

    boolean authEdit(Shop dbShop);

    List<Shop> getApiBackOpenShops();

    ShopEntity getInfoById(Long id);

    /**
     * 根据 Binderr 实体ID更新企业资料状态
     *
     * @param entityId Binderr 实体ID
     * @param businessProfileStatus 要设置的企业资料状态
     * @return 是否更新成功（找到记录并更新）
     */
    boolean updateBusinessProfileStatusByEntityId(Long entityId, Integer businessProfileStatus);

    /**
     * 根据 Binderr 实体ID获取商户信息
     * @param entityId Binderr 实体ID
     * @return Shop
     */
    Shop getByEntityId(Long entityId);

    /**
     * 查询 businessProfileStatus 为 3 的商户列表
     * @return 商户列表
     */
    List<Shop> listByBusinessProfileStatusThree();

    Shop replaceShopToSecondaryMerchant(Shop shop, String cardNumber, Order dbOrder);
}
