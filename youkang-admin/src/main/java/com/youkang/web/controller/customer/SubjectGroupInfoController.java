package com.youkang.web.controller.customer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.youkang.common.utils.SecurityUtils;
import com.youkang.system.domain.req.subjectgroup.BatchUpdateRep;
import com.youkang.system.domain.req.subjectgroup.SubjectGroupAddReq;
import com.youkang.system.domain.req.subjectgroup.SubjectGroupQueryReq;
import com.youkang.system.domain.req.subjectgroup.SubjectGroupUpdateReq;
import com.youkang.system.domain.resp.subjectgroup.SubjectGroupResp;
import jakarta.servlet.http.HttpServletResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.youkang.common.annotation.Log;
import com.youkang.common.core.domain.PageResp;
import com.youkang.common.core.domain.R;
import com.youkang.common.enums.BusinessType;
import com.youkang.system.domain.SubjectGroupInfo;
import com.youkang.system.service.customer.ISubjectGroupInfoService;
import com.youkang.common.utils.poi.ExcelUtil;
import com.youkang.common.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 课题组信息Controller
 *
 * @author youkang
 * @date 2025-01-20
 */
@Tag(name = "课题组信息管理", description = "课题组信息的增删改查操作")
@RestController
@RequestMapping("/customer/subjectGroup")
public class SubjectGroupInfoController {

    @Autowired
    private ISubjectGroupInfoService subjectGroupInfoService;

    /**
     * 查询课题组信息列表
     */
    @Operation(summary = "查询课题组信息列表", description = "分页查询课题组信息列表")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:list')")
    @GetMapping("/list")
    public R<PageResp> list(@Parameter(description = "课题组查询条件")  SubjectGroupQueryReq queryReq) {
        IPage<SubjectGroupResp> page = subjectGroupInfoService.queryPage(queryReq);
        return R.ok(PageResp.of(page.getRecords(), page.getTotal()));
    }

    /**
     * 导出课题组信息列表
     */
    @Operation(summary = "导出课题组信息", description = "导出课题组信息列表为Excel文件")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:export')")
    @Log(title = "课题组信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @Parameter(description = "课题组查询条件") SubjectGroupQueryReq queryReq) {
        LambdaQueryWrapper<SubjectGroupInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(queryReq.getName()),
                        SubjectGroupInfo::getName, queryReq.getName())
                .eq(StringUtils.isNotEmpty(queryReq.getRegion()),
                        SubjectGroupInfo::getRegion, queryReq.getRegion());

        List<SubjectGroupInfo> list = subjectGroupInfoService.list(wrapper);
        ExcelUtil<SubjectGroupInfo> util = new ExcelUtil<>(SubjectGroupInfo.class);
        util.exportExcel(response, list, "课题组信息数据");
    }

    /**
     * 获取课题组信息详细信息
     */
    @Operation(summary = "获取课题组详情", description = "根据ID获取课题组详细信息")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:query')")
    @GetMapping(value = "/{id}")
    public R<SubjectGroupResp> getInfo(@Parameter(description = "课题组ID") @PathVariable("id") Integer id) {
        SubjectGroupResp resp = subjectGroupInfoService.getDetail(id);
        return R.ok(resp);
    }

    /**
     * 新增课题组信息
     */
    @Operation(summary = "新增课题组", description = "新增课题组信息")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:add')")
    @Log(title = "课题组信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<Void> add(@Parameter(description = "课题组信息") @Validated @RequestBody SubjectGroupAddReq addReq) {
        SubjectGroupInfo entity = new SubjectGroupInfo();
        BeanUtils.copyProperties(addReq, entity);
        boolean result = subjectGroupInfoService.saveSubjectGroup(entity);
        return result ? R.ok() : R.fail("新增课题组信息失败");
    }

    /**
     * 修改课题组信息
     */
    @Operation(summary = "修改课题组", description = "修改课题组信息")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:edit')")
    @Log(title = "课题组信息", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public R<Void> edit(@Parameter(description = "课题组信息") @Validated @RequestBody SubjectGroupUpdateReq updateReq) {
        SubjectGroupInfo entity = new SubjectGroupInfo();
        BeanUtils.copyProperties(updateReq, entity);
        boolean result = subjectGroupInfoService.updateById(entity);
        return result ? R.ok() : R.fail("修改课题组信息失败");
    }

    /**
     * 批量修改课题组信息
     */
    @Operation(summary = "修改课题组", description = "批量修改课题组信息")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:edit')")
    @Log(title = "批量修改课题组信息", businessType = BusinessType.UPDATE)
    @PostMapping("/editBatch")
    public R<Void> editBatch(@Parameter(description = "课题组信息") @Validated @RequestBody BatchUpdateRep updateReq) {
        subjectGroupInfoService.editBatch(updateReq);
        return R.ok();
    }

    /**
     * 删除课题组信息
     */
    @Operation(summary = "删除课题组", description = "批量删除课题组信息")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:remove')")
    @Log(title = "课题组信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{ids}")
    public R<Void> remove(@Parameter(description = "课题组ID数组") @PathVariable Integer[] ids) {
        boolean result = subjectGroupInfoService.removeByIds(Arrays.asList(ids));
        return result ? R.ok() : R.fail("删除课题组信息失败");
    }

    /**
     * 批量新增课题组信息
     */
    @Operation(summary = "批量新增课题组", description = "批量新增多个课题组信息")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:add')")
    @Log(title = "批量新增课题组", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public R<Void> batchAdd(@Parameter(description = "课题组列表") @RequestBody List<SubjectGroupAddReq> addReqs) {
        List<SubjectGroupInfo> entities = addReqs.stream().map(req -> {
            SubjectGroupInfo entity = new SubjectGroupInfo();
            BeanUtils.copyProperties(req, entity);
            entity.setCreateTime(LocalDateTime.now());
            entity.setCreateBy(SecurityUtils.getUsername());
            return entity;
        }).toList();
        boolean result = subjectGroupInfoService.saveBatch(entities);
        return result ? R.ok() : R.fail("批量新增课题组失败");
    }
}
