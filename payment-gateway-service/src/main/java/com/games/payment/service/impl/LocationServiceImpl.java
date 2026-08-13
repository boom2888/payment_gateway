package com.games.payment.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.games.common.utils.StringUtils;
import com.games.payment.service.ILocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 地理位置服务实现类
 * 使用 CountriesNow API (https://countriesnow.space/)
 * 
 * @author System
 * @date 2026-02-05
 */
@Slf4j
@Service
public class LocationServiceImpl implements ILocationService {

    private static final String API_BASE = "https://countriesnow.space/api/v0.1/countries";

    // 简单的本地缓存，生产环境建议使用 Redis
    private final Map<String, List<Map<String, String>>> statesCache = new ConcurrentHashMap<>();
    private final Map<String, List<String>> citiesCache = new ConcurrentHashMap<>();

    @Override
    public List<Map<String, String>> getStates(String countryCode) {
        if (StringUtils.isEmpty(countryCode)) {
            return Collections.emptyList();
        }

        if (statesCache.containsKey(countryCode)) {
            return statesCache.get(countryCode);
        }

        try {
            String url = API_BASE + "/states/q?iso3=" + countryCode;
            log.info("Requesting states from: {}", url);

            String response = HttpUtil.get(url);
            JSONObject json = JSONUtil.parseObj(response);

            if (json.getBool("error", true)) {
                log.error("获取省州失败 (iso3={}): {}", countryCode, json.getStr("msg"));

                com.games.common.utils.CountryUtils.CountryInfo countryInfo = com.games.common.utils.CountryUtils
                        .getCountryByCode(countryCode);
                if (countryInfo != null) {
                    // 第一层兜底：iso2
                    if (StringUtils.isNotEmpty(countryInfo.getCode2())) {
                        String iso2 = countryInfo.getCode2();
                        log.info("Trying fallback with iso2: {}", iso2);
                        response = HttpUtil.get(API_BASE + "/states/q?iso2=" + iso2);
                        json = JSONUtil.parseObj(response);
                    }

                    // 第二层兜底：国家英文名称
                    if (json.getBool("error", true) && StringUtils.isNotEmpty(countryInfo.getNameEn())) {
                        String countryName = countryInfo.getNameEn();
                        log.info("Trying fallback with country name: {}", countryName);
                        response = HttpUtil.get(API_BASE + "/states/q?country="
                                + HttpUtil.encodeParams(countryName, java.nio.charset.StandardCharsets.UTF_8));
                        json = JSONUtil.parseObj(response);
                    }
                }

                if (json.getBool("error", true)) {
                    log.error("所有兜底尝试后依然无法获取省州数据");
                    return Collections.emptyList();
                }
            }

            JSONArray statesArr = json.getJSONObject("data").getJSONArray("states");
            List<Map<String, String>> result = new ArrayList<>();
            for (int i = 0; i < statesArr.size(); i++) {
                JSONObject state = statesArr.getJSONObject(i);
                Map<String, String> map = new HashMap<>();
                map.put("name", state.getStr("name"));
                map.put("code", state.getStr("state_code"));
                result.add(map);
            }

            statesCache.put(countryCode, result);
            return result;
        } catch (Exception e) {
            log.error("调用 CountriesNow API 获取省州异常", e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> getCities(String countryCode, String stateName) {
        if (StringUtils.isEmpty(countryCode) || StringUtils.isEmpty(stateName)) {
            return Collections.emptyList();
        }

        String cacheKey = countryCode + "_" + stateName;
        if (citiesCache.containsKey(cacheKey)) {
            return citiesCache.get(cacheKey);
        }

        try {
            // Cities 接口 GET /q 对 iso3 支持可能不稳，优先尝试用 iso3
            // 如果报错 "country not found"，建议前端传递完整的国家名称
            String url = String.format("%s/state/cities/q?iso3=%s&state=%s",
                    API_BASE, countryCode, HttpUtil.encodeParams(stateName, java.nio.charset.StandardCharsets.UTF_8));

            log.info("Requesting cities from: {}", url);
            String response = HttpUtil.get(url);
            JSONObject json = JSONUtil.parseObj(response);

            if (json.getBool("error", true)) {
                log.warn("获取城市失败 (GET-iso3): {}", json.getStr("msg"));

                com.games.common.utils.CountryUtils.CountryInfo countryInfo = com.games.common.utils.CountryUtils
                        .getCountryByCode(countryCode);
                if (countryInfo != null) {
                    // 第一层兜底：GET-iso2
                    if (StringUtils.isNotEmpty(countryInfo.getCode2())) {
                        String iso2 = countryInfo.getCode2();
                        String url2 = String.format("%s/state/cities/q?iso2=%s&state=%s",
                                API_BASE, iso2,
                                HttpUtil.encodeParams(stateName, java.nio.charset.StandardCharsets.UTF_8));
                        log.info("Trying fallback-iso2 for cities: {}", url2);
                        response = HttpUtil.get(url2);
                        json = JSONUtil.parseObj(response);
                    }

                    // 第二层兜底：GET-countryName
                    if (json.getBool("error", true) && StringUtils.isNotEmpty(countryInfo.getNameEn())) {
                        String nameEn = countryInfo.getNameEn();
                        String url3 = String.format("%s/state/cities/q?country=%s&state=%s",
                                API_BASE, HttpUtil.encodeParams(nameEn, java.nio.charset.StandardCharsets.UTF_8),
                                HttpUtil.encodeParams(stateName, java.nio.charset.StandardCharsets.UTF_8));
                        log.info("Trying fallback-nameEn for cities: {}", url3);
                        response = HttpUtil.get(url3);
                        json = JSONUtil.parseObj(response);
                    }
                }
            }

            if (json.getBool("error", true)) {
                log.warn("GET 方式全部失败，尝试原始 POST 方式");
                // 第三层/最终尝试：POST
                Map<String, Object> param = new HashMap<>();
                param.put("iso3", countryCode);
                param.put("state", stateName);
                response = HttpUtil.post(API_BASE + "/state/cities", JSONUtil.toJsonStr(param));
                json = JSONUtil.parseObj(response);
            }

            if (json.getBool("error", true)) {
                log.error("获取城市最终失败: {}", json.getStr("msg"));
                return Collections.emptyList();
            }

            List<String> result = json.getJSONArray("data").toList(String.class);
            citiesCache.put(cacheKey, result);
            return result;
        } catch (Exception e) {
            log.error("调用 CountriesNow API 获取城市异常", e);
            return Collections.emptyList();
        }
    }
}
