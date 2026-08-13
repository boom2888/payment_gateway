package com.games.web.controller.payment;

import com.games.common.core.controller.BaseController;
import com.games.common.core.domain.R;
import com.games.payment.service.ILocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 地理位置控制层
 * 
 * @author System
 * @date 2026-02-05
 */
@Api(tags = "公共服务-地理位置")
@Slf4j
@RestController
@RequestMapping("/payment/location")
@RequiredArgsConstructor
public class LocationController extends BaseController {

    private final ILocationService locationService;

    @ApiOperation("获取省/州列表")
    @GetMapping("/states")
    public R<List<Map<String, String>>> getStates(@RequestParam String countryCode) {
        return R.ok(locationService.getStates(countryCode));
    }

    @ApiOperation("获取城市列表")
    @GetMapping("/cities")
    public R<List<String>> getCities(@RequestParam String countryCode, @RequestParam String stateName) {
        return R.ok(locationService.getCities(countryCode, stateName));
    }
}
