package com.games.payment.mapper;

import com.games.payment.domain.WhitelistedCreditCards;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
/**
 * 白名单信用卡Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface WhitelistedCreditCardsMapper extends BaseMapper<WhitelistedCreditCards> {
    /**
     * 查询白名单信用卡列表
     *
     * @param whitelistedCreditCards 白名单信用卡
     * @return 白名单信用卡集合
     */
    List<WhitelistedCreditCards> selectAllList(WhitelistedCreditCards whitelistedCreditCards);

    int batchInsertOrUpdate(@Param("list") List<WhitelistedCreditCards> entityList);

    /**
     * 批量删除（字段顺序匹配索引 comb_idx_partial_card_number）
     * @param entityList 待删除的实体列表
     * @return 删除的记录数
     */
    int deleteByCardQuadruples(@Param("list") List<WhitelistedCreditCards> entityList);

    /**
     * 按 (shop_id, card_finger) 批量删除
     * @param entityList 待删除的实体列表（只需填 shopId、cardFinger）
     * @return 删除的记录数
     */
    int deleteByCardFingers(@Param("list") List<WhitelistedCreditCards> entityList);

    /**
     * 按 (bin_number, last_four_number, shop_id) 批量删除
     * @param entityList 待删除的实体列表（只需填 binNumber、lastFourNumber、shopId）
     * @return 删除的记录数
     */
    int deleteByCardTriples(@Param("list") List<WhitelistedCreditCards> entityList);

    /**
     * 根据卡号和商户组合批量查询现有记录
     * @param entityList 卡号组合
     * @return 已存在的白名单信用卡
     */
    List<WhitelistedCreditCards> selectByCardTriples(@Param("list") List<WhitelistedCreditCards> entityList);

}
