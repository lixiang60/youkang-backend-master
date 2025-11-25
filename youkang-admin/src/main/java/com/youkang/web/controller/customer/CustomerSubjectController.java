package com.youkang.web.controller.customer;

import com.youkang.common.core.domain.R;
import com.youkang.system.domain.CustomerSubjectGroup;
import com.youkang.system.service.customer.ICustomerSubjectGroupService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customerSubject")
public class CustomerSubjectController {
    @Resource
    private ICustomerSubjectGroupService service;

    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('customer:customerSubject:add')")
    public R<?> add(@Valid @RequestBody CustomerSubjectGroup req){
        service.add(req);
        return R.ok();
    }

    @PostMapping("/query")
    @PreAuthorize("@ss.hasPermi('customer:customerSubject:query')")
    public R<?> query(@RequestBody CustomerSubjectGroup req){
        return R.ok();
    }

    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermi('customer:customerSubject:update')")
    public R<?> update(@RequestBody CustomerSubjectGroup req){
        return R.ok();
    }

    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('customer:customerSubject:delete')")
    public R<?> delete(@Valid @RequestBody CustomerSubjectGroup req){
        return R.ok();
    }

}
