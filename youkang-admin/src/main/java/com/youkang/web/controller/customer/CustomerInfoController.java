package com.youkang.web.controller.customer;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youkang.system.domain.CustomerSubjectGroup;
import com.youkang.system.domain.resp.customer.CustomerSelectorResp;
import com.youkang.system.domain.resp.customer.SubjectGroupSelectorResp;
import com.youkang.system.service.customer.ISubjectGroupInfoService;
import jakarta.servlet.http.HttpServletResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.youkang.common.annotation.Log;
import com.youkang.common.core.domain.PageResp;
import com.youkang.common.core.domain.R;
import com.youkang.common.enums.BusinessType;
import com.youkang.system.domain.CustomerInfo;
import com.youkang.system.service.customer.ICustomerInfoService;
import com.youkang.common.utils.poi.ExcelUtil;
import com.youkang.common.utils.StringUtils;

/**
 * 客户信息Controller
 *
 * @author youkang
 * @date 2025-11-20
 */
@RestController
@RequestMapping("/customer/info")
public class CustomerInfoController
{
    @Autowired
    private ICustomerInfoService customerInfoService;

    @Autowired
    private ISubjectGroupInfoService subjectGroupInfoService;

    /**
     * 查询客户信息列表（MyBatis Plus 分页查询）
     */
    @PreAuthorize("@ss.hasPermi('customer:info:list')")
    @GetMapping("/list")
    public R<PageResp> list(CustomerInfo customerInfo)
    {
        // 业务逻辑在 Service 层处理
        IPage<CustomerInfo> page = customerInfoService.queryPage(customerInfo);
        return R.ok(PageResp.of(page.getRecords(), page.getTotal()));
    }

    @GetMapping("/customerSelector")
    public R<Page<CustomerSelectorResp>> getCustomerSelector(@RequestParam(required = false) String queryString){
        return R.ok(customerInfoService.customerSelector(queryString));
    }

    @GetMapping("subjectGroupSelector")
    public R<Page<SubjectGroupSelectorResp>> getSubjectGroupSelector(@RequestParam(required = false) String queryString){
        return R.ok(subjectGroupInfoService.getSubjectGroupSelector(queryString));
    }

    /**
     * 导出客户信息列表
     */
    @PreAuthorize("@ss.hasPermi('customer:info:export')")
    @Log(title = "客户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerInfo customerInfo)
    {
        // 构建查询条件
        LambdaQueryWrapper<CustomerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(customerInfo.getCustomerName()), CustomerInfo::getCustomerName, customerInfo.getCustomerName())
               .eq(StringUtils.isNotEmpty(customerInfo.getRegion()), CustomerInfo::getRegion, customerInfo.getRegion());
        List<CustomerInfo> list = customerInfoService.list(wrapper);
        ExcelUtil<CustomerInfo> util = new ExcelUtil<>(CustomerInfo.class);
        util.exportExcel(response, list, "客户信息数据");
    }

    /**
     * 获取客户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('customer:info:query')")
    @GetMapping(value = "/{id}")
    public R<CustomerInfo> getInfo(@PathVariable("id") Integer id)
    {
        return R.ok(customerInfoService.getById(id));
    }

    /**
     * 新增客户信息
     */
    @PreAuthorize("@ss.hasPermi('customer:info:add')")
    @Log(title = "客户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@RequestBody CustomerInfo customerInfo)
    {
        boolean result = customerInfoService.save(customerInfo);
        return result ? R.ok() : R.fail("新增客户信息失败");
    }

    /**
     * 修改客户信息
     */
    @PreAuthorize("@ss.hasPermi('customer:info:edit')")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@RequestBody CustomerInfo customerInfo)
    {
        boolean result = customerInfoService.updateById(customerInfo);
        return result ? R.ok() : R.fail("修改客户信息失败");
    }

    /**
     * 删除客户信息
     */
    @PreAuthorize("@ss.hasPermi('customer:info:remove')")
    @Log(title = "客户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Integer[] ids) {
        boolean result = customerInfoService.removeByIds(Arrays.asList(ids));
        return result ? R.ok() : R.fail("删除客户信息失败");
    }
}
