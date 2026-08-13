package com.games.web.controller.payment;

import com.games.common.core.domain.R;
import com.games.thunes.ThunesConfigService;
import com.games.thunes.ThunesConfigTransactionService;
import com.games.thunes.ThunesPayerSyncService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Thunes 配置管理控制器
 *
 * @author System
 * @date 2025-01-09
 */
@Api(tags = "Thunes 配置管理")
@Slf4j
@RestController
@RequestMapping("/thunes/config")
@RequiredArgsConstructor
public class ThunesConfigController {

	private final ThunesConfigService thunesConfigService;
	private final ThunesConfigTransactionService thunesConfigTransactionService;
	private final ThunesPayerSyncService payerSyncService;

	@ApiOperation("获取支持的国家列表")
	@GetMapping("/countries")
	@PreAuthorize("@ss.hasPermi('thunes:config:query')")
	public R<Set<String>> getSupportedCountries() {
		Set<String> countries = thunesConfigService.getSupportedCountries();
		return R.ok(countries);
	}

	@ApiOperation("获取指定国家支持的货币列表")
	@GetMapping("/countries/{countryCode}/currencies")
	@PreAuthorize("@ss.hasPermi('thunes:config:query')")
	public R<Set<String>> getSupportedCurrencies(@PathVariable String countryCode) {
		Set<String> currencies = thunesConfigService.getSupportedCurrencies(countryCode);
		return R.ok(currencies);
	}

	@ApiOperation("获取指定国家的首选银行列表")
	@GetMapping("/countries/{countryCode}/preferred-banks")
	@PreAuthorize("@ss.hasPermi('thunes:config:query')")
	public R<List<String>> getPreferredBanks(@PathVariable String countryCode) {
		List<String> banks = thunesConfigService.getPreferredBanks(countryCode);
		return R.ok(banks);
	}

	@ApiOperation("检查是否支持指定的国家和货币")
	@GetMapping("/support-check")
	@PreAuthorize("@ss.hasPermi('thunes:config:query')")
	public R<Boolean> checkSupport(@RequestParam String countryCode, @RequestParam String currency) {
		boolean isSupported = thunesConfigService.isSupportedCountryAndCurrency(countryCode, currency);
		return R.ok(isSupported);
	}

	@ApiOperation("手动同步所有配置")
	@PostMapping("/sync")
	@PreAuthorize("@ss.hasPermi('thunes:config:sync')")
	public R<String> manualSync() {
		try {
			log.info("管理员手动触发 Thunes 配置同步");
			thunesConfigService.manualSyncAllConfig();
			return R.ok("配置同步已完成");
		} catch (Exception e) {
			log.error("手动同步配置失败", e);
			return R.fail("配置同步失败: " + e.getMessage());
		}
	}

	@ApiOperation("同步国家货币配置")
	@PostMapping("/sync/countries")
	@PreAuthorize("@ss.hasPermi('thunes:config:sync')")
	public R<String> syncCountryCurrency() {
		try {
			log.info("管理员手动触发国家货币配置同步");
			thunesConfigTransactionService.syncCountryCurrencyConfig();
			return R.ok("国家货币配置同步已完成");
		} catch (Exception e) {
			log.error("同步国家货币配置失败", e);
			return R.fail("国家货币配置同步失败: " + e.getMessage());
		}
	}

	@ApiOperation("同步首选银行配置")
	@PostMapping("/sync/banks")
	@PreAuthorize("@ss.hasPermi('thunes:config:sync')")
	public R<String> syncPreferredBanks() {
		try {
			log.info("管理员手动触发首选银行配置同步");
			thunesConfigTransactionService.syncPreferredBankConfig();
			return R.ok("首选银行配置同步已完成");
		} catch (Exception e) {
			log.error("同步首选银行配置失败", e);
			return R.fail("首选银行配置同步失败: " + e.getMessage());
		}
	}

	@ApiOperation("获取同步统计信息")
	@GetMapping("/sync/stats")
	@PreAuthorize("@ss.hasPermi('thunes:config:query')")
	public R<Map<String, Object>> getSyncStats() {
		Map<String, Object> stats = thunesConfigService.getSyncStats();
		return R.ok(stats);
	}

	// ==================== Payer 管理相关接口 ====================

	@ApiOperation("手动同步 Payer 数据")
	@PostMapping("/sync/payers")
	@PreAuthorize("@ss.hasPermi('thunes:config:sync')")
	public R<String> syncPayers() {
		try {
			log.info("管理员手动触发 Payer 数据同步");
			payerSyncService.fullSync();
			return R.ok("Payer 数据同步已完成");
		} catch (Exception e) {
			log.error("手动同步 Payer 数据失败", e);
			return R.fail("Payer 数据同步失败: " + e.getMessage());
		}
	}

	@ApiOperation("获取 Payer 同步统计信息")
	@GetMapping("/payers/stats")
	@PreAuthorize("@ss.hasPermi('thunes:config:query')")
	public R<ThunesPayerSyncService.PayerSyncStats> getPayerSyncStats() {
		ThunesPayerSyncService.PayerSyncStats stats = payerSyncService.getSyncStats();
		return R.ok(stats);
	}

	@ApiOperation("检查 Payer 数据可用性")
	@GetMapping("/payers/availability")
	@PreAuthorize("@ss.hasPermi('thunes:config:query')")
	public R<Map<String, Object>> checkPayerDataAvailability() {
		Map<String, Object> result = new HashMap<>();
		result.put("hasData", payerSyncService.hasPayerData());
		result.put("lastSyncTime", payerSyncService.getLastSyncTime());
		return R.ok(result);
	}
}