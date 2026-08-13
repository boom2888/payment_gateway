package com.games.web.controller.payment;

import com.games.common.annotation.Log;
import com.games.common.core.controller.BaseController;
import com.games.common.core.domain.R;
import com.games.common.enums.BusinessType;
import com.games.common.utils.CountryUtils;
import com.games.payment.domain.Shop;
import com.games.payment.service.IShopService;
import com.games.thunes.ThunesApiService;
import com.games.thunes.ThunesTransferService;
import com.games.thunes.dto.PayerDto;
import com.games.thunes.dto.TransferResponseDto;
import com.games.thunes.vo.TransferRequestVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Thunes 转账管理控制器
 *
 * @author System
 * @date 2026-02-05
 */
@Api(tags = "OTC管理-转账")
@Slf4j
@RestController
@RequestMapping("/payment/thunes/transfer")
@RequiredArgsConstructor
public class ThunesTransferController extends BaseController {

    private final ThunesApiService thunesApiService;
    private final ThunesTransferService thunesTransferService;
    private final IShopService shopService;

    @ApiOperation("获取国家列表")
    @GetMapping("/countries")
    @PreAuthorize("@ss.hasPermi('payment:thunes:transfer:query')")
    public R<List<CountryUtils.CountryInfo>> getCountries() {
        log.info("获取国家列表");
        try {
            List<CountryUtils.CountryInfo> countries = CountryUtils.getAllCountries();
            return R.ok(countries);
        } catch (Exception e) {
            log.error("获取国家列表失败", e);
            return R.fail("获取国家列表失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取已审核通过的商户列表")
    @GetMapping("/merchants")
    @PreAuthorize("@ss.hasPermi('payment:thunes:transfer:query')")
    public R<List<Map<String, Object>>> getApprovedMerchants() {
        log.info("获取已审核通过的商户列表");
        try {
            // 查询审核通过的商户（businessProfileStatus = 1 表示已批准）
            List<Shop> shops = shopService.lambdaQuery()
                    .eq(Shop::getBusinessProfileStatus, 1)
                    .select(Shop::getId, Shop::getBusinessName, Shop::getUserId)
                    .list();
            
            List<Map<String, Object>> merchants = shops.stream()
                    .map(shop -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", shop.getId());
                        map.put("businessName", shop.getBusinessName());
                        // userName 需要通过 userId 关联查询，这里暂时使用商户名称
                        map.put("userName", shop.getBusinessName());
                        return map;
                    })
                    .collect(Collectors.toList());
            
            log.info("获取到 {} 个已审核商户", merchants.size());
            return R.ok(merchants);
        } catch (Exception e) {
            log.error("获取商户列表失败", e);
            return R.fail("获取商户列表失败: " + e.getMessage());
        }
    }

    @ApiOperation("搜索国家")
    @GetMapping("/countries/search")
    @PreAuthorize("@ss.hasPermi('payment:thunes:transfer:query')")
    public R<List<CountryUtils.CountryInfo>> searchCountries(
            @ApiParam("搜索关键词") @RequestParam(required = false) String keyword) {
        log.info("搜索国家 - 关键词: {}", keyword);
        try {
            List<CountryUtils.CountryInfo> countries = CountryUtils.searchCountries(keyword);
            return R.ok(countries);
        } catch (Exception e) {
            log.error("搜索国家失败", e);
            return R.fail("搜索国家失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取收款方信息字段")
    @GetMapping("/payers")
    @PreAuthorize("@ss.hasPermi('payment:thunes:transfer:query')")
    public R<List<PayerDto>> getPayers(
            @ApiParam("国家代码") @RequestParam(required = false) String countryCode,
            @ApiParam("货币代码") @RequestParam(required = false) String currency) {
        log.info("获取Thunes收款方信息 - 国家: {}, 货币: {}", countryCode, currency);
        try {
            List<PayerDto> payers = thunesApiService.getPayers(countryCode, currency);
            return R.ok(payers);
        } catch (Exception e) {
            log.error("获取收款方信息失败", e);
            return R.fail("获取收款方信息失败: " + e.getMessage());
        }
    }

    @ApiOperation("执行转账")
    @Log(title = "Thunes转账", businessType = BusinessType.INSERT)
    @PostMapping
    @PreAuthorize("@ss.hasPermi('payment:thunes:transfer:add')")
    public R<TransferResponseDto> transfer(@Validated @RequestBody TransferRequestVo request) {
        log.info("执行Thunes转账 - 商户ID: {}, 外部订单号: {}", request.getMerchantId(), request.getExternalId());
        try {
            // 获取商户信息
            Shop shop = shopService.getById(request.getMerchantId());
            if (shop == null) {
                return R.fail("商户不存在");
            }

            // 处理转账
            TransferResponseDto response = thunesTransferService.processTransfer(shop, request);
            return R.ok(response);
        } catch (Exception e) {
            log.error("转账失败", e);
            return R.fail("转账失败: " + e.getMessage());
        }
    }
}
