package com.games.web.controller.payment;

import java.util.List;
import java.util.Arrays;

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
import com.games.payment.domain.BusinessCustomerCorporationMember;
import com.games.payment.service.IBusinessCustomerCorporationMemberService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 企业客户公司成员Controller
 * 
 * @author Ticker
 * @date 2025-07-10
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/businessCustomerCorporationMem" )
public class BusinessCustomerCorporationMemberController extends BaseController {

    private final IBusinessCustomerCorporationMemberService iBusinessCustomerCorporationMemberService;

    /**
     * 查询企业客户公司成员列表
     */
    @PreAuthorize("@ss.hasPermi('payment:businessCustomerCorporationMem:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusinessCustomerCorporationMember businessCustomerCorporationMember) {
        startPage();
        List<BusinessCustomerCorporationMember> list = iBusinessCustomerCorporationMemberService.selectAllList(businessCustomerCorporationMember);
        return getDataTable(list);
    }

    /**
     * 导出企业客户公司成员列表
     */
    @PreAuthorize("@ss.hasPermi('payment:businessCustomerCorporationMem:export')" )
    @Log(title = "企业客户公司成员" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(BusinessCustomerCorporationMember businessCustomerCorporationMember) {
        List<BusinessCustomerCorporationMember> list = iBusinessCustomerCorporationMemberService.queryList(businessCustomerCorporationMember);
        ExcelUtil<BusinessCustomerCorporationMember> util = new ExcelUtil<BusinessCustomerCorporationMember>(BusinessCustomerCorporationMember.class);
        return util.exportExcel(list, "businessCustomerCorporationMem" );
    }

    /**
     * 获取企业客户公司成员详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:businessCustomerCorporationMem:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iBusinessCustomerCorporationMemberService.getById(id));
    }

    /**
     * 新增企业客户公司成员
     */
    @PreAuthorize("@ss.hasPermi('payment:businessCustomerCorporationMem:add')" )
    @Log(title = "企业客户公司成员" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusinessCustomerCorporationMember businessCustomerCorporationMember) {
        return toAjax(iBusinessCustomerCorporationMemberService.save(businessCustomerCorporationMember) ? 1 : 0);
    }

    /**
     * 修改企业客户公司成员
     */
    @PreAuthorize("@ss.hasPermi('payment:businessCustomerCorporationMem:edit')" )
    @Log(title = "企业客户公司成员" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusinessCustomerCorporationMember businessCustomerCorporationMember) {
        return toAjax(iBusinessCustomerCorporationMemberService.updateById(businessCustomerCorporationMember) ? 1 : 0);
    }

    /**
     * 删除企业客户公司成员
     */
    @PreAuthorize("@ss.hasPermi('payment:businessCustomerCorporationMem:remove')" )
    @Log(title = "企业客户公司成员" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iBusinessCustomerCorporationMemberService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
