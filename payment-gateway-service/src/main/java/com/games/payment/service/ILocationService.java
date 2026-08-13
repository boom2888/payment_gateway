package com.games.payment.service;

import java.util.List;
import java.util.Map;

/**
 * 地理位置服务接口
 * 
 * @author System
 * @date 2026-02-05
 */
public interface ILocationService {

    /**
     * 获取指定国家的所有省/州
     * 
     * @param countryCode 国家代码 (ISO-3 or Name)
     * @return 省/州列表
     */
    List<Map<String, String>> getStates(String countryCode);

    /**
     * 获取指定国家、省/州的城市列表
     * 
     * @param countryCode 国家代码
     * @param stateName   省/州名称
     * @return 城市列表
     */
    List<String> getCities(String countryCode, String stateName);
}
