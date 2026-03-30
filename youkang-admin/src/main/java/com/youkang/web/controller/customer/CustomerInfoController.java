package com.youkang.web.controller.customer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youkang.common.utils.SecurityUtils;
import com.youkang.system.domain.CustomerSubjectGroup;
import com.youkang.system.domain.SubjectGroupInfo;
import com.youkang.system.domain.req.customer.CustomerAddReq;
import com.youkang.system.domain.req.customer.CustomerEditReq;
import com.youkang.system.domain.req.customer.CustomerQueryReq;
import com.youkang.system.domain.resp.customer.CustomerResp;
import com.youkang.system.domain.resp.customer.CustomerSelectorResp;
import com.youkang.system.domain.resp.customer.SubjectGroupSelectorResp;
import com.youkang.system.service.customer.ICustomerSubjectGroupService;
import com.youkang.system.service.customer.ISubjectGroupInfoService;
import jakarta.servlet.http.HttpServletResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.youkang.common.annotation.Log;
import com.youkang.common.core.domain.PageResp;
import com.youkang.common.core.domain.YKResponse;
import com.youkang.common.enums.BusinessType;
import com.youkang.system.domain.CustomerInfo;
import com.youkang.system.service.customer.ICustomerInfoService;
import com.youkang.common.utils.poi.ExcelUtil;
import com.youkang.common.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 客户信息Controller
 *
 * @author youkang
 * @date 2025-11-20
 */
@Tag(name = "客户信息管理", description = "客户信息的增删改查操作")
@RestController
@RequestMapping("/customer/info")
public class CustomerInfoController {

    @Autowired
    private ICustomerInfoService customerInfoService;

    @Autowired
    private ISubjectGroupInfoService subjectGroupInfoService;

    @Autowired
    private ICustomerSubjectGroupService customerSubjectGroupService;
    /**
     * 查询客户信息列表
     */
    @Operation(summary = "查询客户信息列表", description = "分页查询客户信息列表")
    @PreAuthorize("@ss.hasPermi('customer:info:list')")
    @GetMapping("/list")
    public YKResponse<PageResp> list(@Parameter(description = "客户查询条件") CustomerQueryReq queryReq) {
        IPage<CustomerResp> page = customerInfoService.queryPage(queryReq);
        return YKResponse.ok(PageResp.of(page.getRecords(), page.getTotal()));
    }

    /**
     * 客户选择器
     */
    @Operation(summary = "客户选择器", description = "获取客户选择器数据")
    @GetMapping("/customerSelector")
    public YKResponse<Page<CustomerSelectorResp>> getCustomerSelector(@Parameter(description = "查询字符串") @RequestParam(required = false) String queryString) {
        return YKResponse.ok(customerInfoService.customerSelector(queryString));
    }

    /**
     * 课题组选择器
     */
    @Operation(summary = "课题组选择器", description = "获取课题组选择器数据")
    @GetMapping("/subjectGroupSelector")
    public YKResponse<Page<SubjectGroupSelectorResp>> getSubjectGroupSelector(@Parameter(description = "查询字符串") @RequestParam(required = false) String queryString) {
        return YKResponse.ok(subjectGroupInfoService.getSubjectGroupSelector(queryString));
    }

    /**
     * 导出客户信息列表
     */
    @Operation(summary = "导出客户信息", description = "导出客户信息列表为Excel文件")
    @PreAuthorize("@ss.hasPermi('customer:info:export')")
    @Log(title = "客户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @Parameter(description = "客户查询条件") CustomerQueryReq queryReq) {
        LambdaQueryWrapper<CustomerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(queryReq.getCustomerName()), CustomerInfo::getCustomerName, queryReq.getCustomerName())
                .eq(StringUtils.isNotEmpty(queryReq.getRegion()), CustomerInfo::getRegion, queryReq.getRegion());
        List<CustomerInfo> list = customerInfoService.list(wrapper);
        ExcelUtil<CustomerInfo> util = new ExcelUtil<>(CustomerInfo.class);
        util.exportExcel(response, list, "客户信息数据");
    }

    /**
     * 获取客户信息详细信息
     */
    @Operation(summary = "获取客户详情", description = "根据ID获取客户详细信息")
    @PreAuthorize("@ss.hasPermi('customer:info:query')")
    @GetMapping(value = "/{id}")
    public YKResponse<CustomerResp> getInfo(@Parameter(description = "客户ID") @PathVariable("id") Integer id) {
        CustomerResp resp = customerInfoService.getDetail(id);
        return YKResponse.ok(resp);
    }

    /**
     * 新增客户信息
     */
    @Operation(summary = "新增客户", description = "新增客户信息")
    @PreAuthorize("@ss.hasPermi('customer:info:add')")
    @Log(title = "客户信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public YKResponse<Void> add(@Parameter(description = "客户信息") @Validated @RequestBody CustomerAddReq addReq) {
        CustomerInfo entity = new CustomerInfo();
        BeanUtils.copyProperties(addReq, entity);
        String username = SecurityUtils.getUsername();
        entity.setCreateBy(username);
        entity.setCreateTime(LocalDateTime.now());
        boolean result = customerInfoService.save(entity);
        if (result){
            if (entity.getSubjectGroupId()!= null){
                SubjectGroupInfo subjectGroupInfo = subjectGroupInfoService.getById(entity.getSubjectGroupId());
                CustomerSubjectGroup customerSubjectGroup = new CustomerSubjectGroup();
                customerSubjectGroup.setCustomerId(entity.getId());
                customerSubjectGroup.setSubjectGroupId(subjectGroupInfo.getId());
                customerSubjectGroup.setCreateUser(username);
                customerSubjectGroup.setCreateTime(LocalDateTime.now());
                customerSubjectGroupService.add(customerSubjectGroup);
            }
        }
        return result ? YKResponse.ok() : YKResponse.fail("新增客户信息失败");
    }

    /**
     * 修改客户信息
     */
    @Operation(summary = "修改客户", description = "修改客户信息")
    @PreAuthorize("@ss.hasPermi('customer:info:edit')")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public YKResponse<Void> edit(@Parameter(description = "客户信息") @Validated @RequestBody CustomerEditReq editReq) {
        CustomerInfo entity = new CustomerInfo();
        BeanUtils.copyProperties(editReq, entity);
        entity.setUpdateBy(SecurityUtils.getUsername());
        entity.setUpdateTime(LocalDateTime.now());
        boolean result = customerInfoService.updateById(entity);
        return result ? YKResponse.ok() : YKResponse.fail("修改客户信息失败");
    }

    /**
     * 删除客户信息
     */
    @Operation(summary = "删除客户", description = "批量删除客户信息")
    @PreAuthorize("@ss.hasPermi('customer:info:remove')")
    @Log(title = "客户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{ids}")
    public YKResponse<Void> remove(@Parameter(description = "客户ID数组") @PathVariable Integer[] ids) {
        boolean result = customerInfoService.removeByIds(Arrays.asList(ids));
        return result ? YKResponse.ok() : YKResponse.fail("删除客户信息失败");
    }
}
