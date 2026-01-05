package com.youkang.web.controller.customer;

import com.youkang.common.core.domain.R;
import com.youkang.system.domain.CustomerSubjectGroup;
import com.youkang.system.domain.req.CustomerUpdateReq;
import com.youkang.system.domain.req.DeleteReq;
import com.youkang.system.domain.req.customer.CustomerSubjectGroupReq;
import com.youkang.system.service.customer.ICustomerSubjectGroupService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "客户课题组关联管理", description = "客户与课题组关联关系的增删改查操作")
@RestController
@RequestMapping("/customerSubject")
public class CustomerSubjectController {
    @Resource
    private ICustomerSubjectGroupService service;

    @Operation(summary = "新增客户课题组关联", description = "新增客户与课题组的关联关系")
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('customer:customerSubject:add')")
    public R<?> add(@Parameter(description = "客户课题组关联信息") @Valid @RequestBody CustomerSubjectGroup req){
        service.add(req);
        return R.ok();
    }

    @Operation(summary = "查询客户课题组关联", description = "查询客户与课题组的关联关系")
    @PostMapping("/query")
    @PreAuthorize("@ss.hasPermi('customer:customerSubject:query')")
    public R<?> query(@Parameter(description = "查询条件") @RequestBody CustomerSubjectGroupReq req){
        return R.ok(service.query(req));
    }

    @Operation(summary = "更新客户课题组关联", description = "更新客户与课题组的关联关系")
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermi('customer:customerSubject:update')")
    public R<?> update(@Parameter(description = "更新信息") @RequestBody CustomerUpdateReq req){
        service.update(req);
        return R.ok();
    }

    @Operation(summary = "删除客户课题组关联", description = "删除客户与课题组的关联关系")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('customer:customerSubject:delete')")
    public R<?> delete(@Parameter(description = "删除信息") @Valid @RequestBody DeleteReq req){
        service.delete(req);
        return R.ok();
    }

}
