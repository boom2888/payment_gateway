package com.games.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.jdbc.Null;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.CountryCategoryMapper;
import com.games.payment.domain.CountryCategory;
import com.games.payment.service.ICountryCategoryService;

import java.util.List;
import java.util.Map;

/**
 * 欧洲国家的地区Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-22
 */
@Service
public class CountryCategoryServiceImpl extends ServiceImpl<CountryCategoryMapper, CountryCategory> implements ICountryCategoryService {

    /**
     * 查询欧洲国家的地区列表
     *
     * @param countryCategory 欧洲国家的地区
     * @return 欧洲国家的地区
     */
    @Override
    public List<CountryCategory> selectAllList(CountryCategory countryCategory)
    {
        return getBaseMapper().selectAllList(countryCategory);
    }


    @Override
    public List<CountryCategory> queryList(CountryCategory countryCategory) {
        LambdaQueryWrapper<CountryCategory> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(countryCategory.getCountry())){
            lqw.eq(CountryCategory::getCountry ,countryCategory.getCountry());
        }
        if (StringUtils.isNotBlank(countryCategory.getCategory())){
            lqw.eq(CountryCategory::getCategory ,countryCategory.getCategory());
        }
        return this.list(lqw);
    }

    @Override
    public boolean isEu(String country) {
        LambdaQueryWrapper<CountryCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CountryCategory::getCountry, country);
        queryWrapper.eq(CountryCategory::getCategory, "EU");
        return (getBaseMapper().selectOne(queryWrapper) != null);
    }


}
