package com.youkang.web.controller.customer;

import java.util.Arrays;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class SubjectGroupInfoController
{
    @Autowired
    private ISubjectGroupInfoService subjectGroupInfoService;

    /**
     * 查询课题组信息列表（MyBatis Plus 分页查询）
     */
    @Operation(summary = "查询课题组信息列表", description = "分页查询课题组信息列表")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:list')")
    @GetMapping("/list")
    public R<PageResp> list(@Parameter(description = "课题组查询条件") @RequestBody SubjectGroupInfo subjectGroupInfo) {
        // 业务逻辑在 Service 层处理
        IPage<SubjectGroupInfo> page = subjectGroupInfoService.queryPage(subjectGroupInfo);
        return R.ok(PageResp.of(page.getRecords(), page.getTotal()));
    }

    /**
     * 导出课题组信息列表
     */
    @Operation(summary = "导出课题组信息", description = "导出课题组信息列表为Excel文件")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:export')")
    @Log(title = "课题组信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @Parameter(description = "课题组查询条件") SubjectGroupInfo subjectGroupInfo) {
        // 构建查询条件
        LambdaQueryWrapper<SubjectGroupInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(subjectGroupInfo.getName()),
                     SubjectGroupInfo::getName, subjectGroupInfo.getName())
               .eq(StringUtils.isNotEmpty(subjectGroupInfo.getRegion()),
                   SubjectGroupInfo::getRegion, subjectGroupInfo.getRegion());

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
    public R<SubjectGroupInfo> getInfo(@Parameter(description = "课题组ID") @PathVariable("id") Integer id)
    {
        return R.ok(subjectGroupInfoService.getById(id));
    }

    /**
     * 新增课题组信息
     */
    @Operation(summary = "新增课题组", description = "新增课题组信息")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:add')")
    @Log(title = "课题组信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<Void> add(@Parameter(description = "课题组信息") @RequestBody SubjectGroupInfo subjectGroupInfo)
    {
        boolean result = subjectGroupInfoService.save(subjectGroupInfo);
        return result ? R.ok() : R.fail("新增课题组信息失败");
    }

    /**
     * 修改课题组信息
     */
    @Operation(summary = "修改课题组", description = "修改课题组信息")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:edit')")
    @Log(title = "课题组信息", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public R<Void> edit(@Parameter(description = "课题组信息") @RequestBody SubjectGroupInfo subjectGroupInfo)
    {
        boolean result = subjectGroupInfoService.updateById(subjectGroupInfo);
        return result ? R.ok() : R.fail("修改课题组信息失败");
    }

    /**
     * 删除课题组信息
     */
    @Operation(summary = "删除课题组", description = "批量删除课题组信息")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:remove')")
    @Log(title = "课题组信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@Parameter(description = "课题组ID数组") @PathVariable Integer[] ids)
    {
        boolean result = subjectGroupInfoService.removeByIds(Arrays.asList(ids));
        return result ? R.ok() : R.fail("删除课题组信息失败");
    }

    /**
     * 批量新增课题组信息（MyBatis Plus 特有功能）
     */
    @Operation(summary = "批量新增课题组", description = "批量新增多个课题组信息")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:add')")
    @Log(title = "批量新增课题组", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public R<Void> batchAdd(@Parameter(description = "课题组列表") @RequestBody List<SubjectGroupInfo> subjectGroups)
    {
        // saveBatch 会自动分批插入，默认每批1000条
        boolean result = subjectGroupInfoService.saveBatch(subjectGroups);
        return result ? R.ok() : R.fail("批量新增课题组失败");
    }

    /**
     * 根据名称查询课题组（示例：自定义查询方法）
     */
    @Operation(summary = "根据名称查询课题组", description = "根据课题组名称查询课题组信息")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:query')")
    @GetMapping("/getByName")
    public R<SubjectGroupInfo> getByName(@Parameter(description = "课题组名称") @RequestParam String name)
    {
        // 使用链式查询（最简洁的方式）
        SubjectGroupInfo subjectGroup = subjectGroupInfoService.lambdaQuery()
                .eq(SubjectGroupInfo::getName, name)
                .one();  // 查询一条记录

        return R.ok(subjectGroup);
    }

    /**
     * 统计各地区的课题组数量（示例：分组统计）
     */
    @Operation(summary = "按地区统计课题组", description = "统计各地区的课题组数量")
    @PreAuthorize("@ss.hasPermi('customer:subjectGroup:query')")
    @GetMapping("/countByRegion")
    public R<List<SubjectGroupInfo>> countByRegion()
    {
        // MyBatis Plus 支持分组查询
        LambdaQueryWrapper<SubjectGroupInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SubjectGroupInfo::getRegion)
               .groupBy(SubjectGroupInfo::getRegion);

        List<SubjectGroupInfo> list = subjectGroupInfoService.list(wrapper);
        return R.ok(list);
    }
}
