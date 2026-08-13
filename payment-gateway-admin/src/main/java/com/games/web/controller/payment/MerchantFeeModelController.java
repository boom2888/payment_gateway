package com.games.web.controller.payment;

import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.games.payment.domain.Shop;
import com.games.payment.service.IShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.games.common.annotation.Log;
import com.games.common.core.controller.BaseController;
import com.games.common.core.domain.AjaxResult;
import com.games.common.enums.BusinessType;
import com.games.payment.domain.MerchantFeeModel;
import com.games.payment.service.IMerchantFeeModelService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 商户收费模型Controller
 *
 * @author Ticker
 * @date 2025-07-16
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/merchantFeeModel" )
public class MerchantFeeModelController extends BaseController {

    private final IMerchantFeeModelService iMerchantFeeModelService;
    private final IShopService shopService;

    /**
     * 查询商户收费模型列表
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantFeeModel:list')")
    @GetMapping("/list")
    public TableDataInfo list(MerchantFeeModel merchantFeeModel) {
        startPage();
        List<MerchantFeeModel> list = iMerchantFeeModelService.selectAllList(merchantFeeModel);
        return getDataTable(list);
    }

    /**
     * 导出商户收费模型列表
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantFeeModel:export')" )
    @Log(title = "商户收费模型" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(MerchantFeeModel merchantFeeModel) {
        List<MerchantFeeModel> list = iMerchantFeeModelService.queryList(merchantFeeModel);
        ExcelUtil<MerchantFeeModel> util = new ExcelUtil<MerchantFeeModel>(MerchantFeeModel.class);
        return util.exportExcel(list, "merchantFeeModel" );
    }

    /**
     * 获取商户收费模型详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantFeeModel:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) String id) {
        MerchantFeeModel feeModel = iMerchantFeeModelService.getById(id);
        return AjaxResult.success(feeModel);
    }

    /**
     * 新增商户收费模型
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantFeeModel:add')" )
    @Log(title = "商户收费模型" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MerchantFeeModel merchantFeeModel) {
        return toAjax(iMerchantFeeModelService.save(merchantFeeModel) ? 1 : 0);
    }

    /**
     * 修改商户收费模型
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantFeeModel:edit')" )
    @Log(title = "商户收费模型" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MerchantFeeModel merchantFeeModel) {
        return toAjax(iMerchantFeeModelService.updateById(merchantFeeModel) ? 1 : 0);
    }

    /**
     * 删除商户收费模型
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantFeeModel:remove')" )
    @Log(title = "商户收费模型" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable String[] ids) {
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Shop::getMerchantFeeModelId, Arrays.asList(ids));
        queryWrapper.eq(Shop::getDeleted, 0);
        List<Shop> shops = shopService.list(queryWrapper);
        if (!shops.isEmpty()) {
            // 构建错误信息
            StringBuilder errorMsg = new StringBuilder("以下收费模型正在被商户使用，无法删除: ");
            Set<String> usedModelIds = shops.stream()
                    .map(shop -> String.valueOf(shop.getMerchantFeeModelId()))
                    .collect(Collectors.toSet());

            for (String modelId : usedModelIds) {
                long count = shops.stream()
                        .filter(shop -> String.valueOf(shop.getMerchantFeeModelId()).equals(modelId))
                        .count();
                errorMsg.append("[").append(modelId).append("](").append(count).append("个商户) ");
            }
            return AjaxResult.error(errorMsg.toString());
        }
        return toAjax(iMerchantFeeModelService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
