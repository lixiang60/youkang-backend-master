package com.youkang.web.controller.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.youkang.common.annotation.Log;
import com.youkang.common.core.domain.PageResp;
import com.youkang.common.core.domain.R;
import com.youkang.common.enums.BusinessType;
import com.youkang.common.utils.SecurityUtils;
import com.youkang.common.utils.poi.ExcelUtil;
import com.youkang.system.domain.SampleInfo;
import com.youkang.system.domain.req.order.*;
import com.youkang.system.domain.resp.order.SampleResp;
import com.youkang.system.domain.resp.order.SampleTemplateResp;
import com.youkang.system.service.order.ISampleInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * 样品信息Controller
 *
 * @author youkang
 */
@Tag(name = "样品信息管理", description = "样品信息的增删改查操作")
@RestController
@RequestMapping("/order/sample")
public class SampleInfoController {

    @Autowired
    private ISampleInfoService sampleInfoService;

    /**
     * 查询样品信息列表
     */
    @Operation(summary = "查询样品信息列表", description = "分页查询样品信息列表")
    @PreAuthorize("@ss.hasPermi('order:sample:list')")
    @PostMapping("/list")
    public R<PageResp> list(@Parameter(description = "样品查询条件") @RequestBody SampleQueryReq req) {
        IPage<SampleResp> page = sampleInfoService.queryPage(req);
        return R.ok(PageResp.of(page.getRecords(), page.getTotal()));
    }

    /**
     * 导出样品信息列表
     */
    @Operation(summary = "导出样品信息", description = "导出样品信息列表为Excel文件")
    @PreAuthorize("@ss.hasPermi('order:sample:export')")
    @Log(title = "样品信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @Parameter(description = "样品查询条件") SampleQueryReq req) {
        List<SampleResp> list = sampleInfoService.queryList(req);
        ExcelUtil<SampleResp> util = new ExcelUtil<>(SampleResp.class);
        util.exportExcel(response, list, "样品信息数据");
    }

    /**
     * 导入样品信息
     */
    @Operation(summary = "导入样品信息", description = "从Excel文件导入样品信息")
    @PreAuthorize("@ss.hasPermi('order:sample:import')")
    @Log(title = "样品信息", businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    public R<String> importData(
            @Parameter(description = "Excel文件") MultipartFile file,
            @Parameter(description = "是否更新已存在的数据") @RequestParam(defaultValue = "false") boolean updateSupport
    ) throws Exception {
        ExcelUtil<SampleInfo> util = new ExcelUtil<>(SampleInfo.class);
        List<SampleInfo> sampleList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = sampleInfoService.importSample(sampleList, updateSupport, operName);
        return R.ok(message);
    }

    /**
     * 下载导入模板
     */
    @Operation(summary = "下载导入模板", description = "下载样品信息导入Excel模板")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<SampleInfo> util = new ExcelUtil<>(SampleInfo.class);
        util.importTemplateExcel(response, "样品信息数据");
    }

    /**
     * 获取样品详细信息
     */
    @Operation(summary = "获取样品详情", description = "根据样品ID获取样品详细信息")
    @PreAuthorize("@ss.hasPermi('order:sample:query')")
    @GetMapping(value = "/{sampleId}")
    public R<SampleResp> getInfo(@PathVariable String sampleId) {
        return R.ok(sampleInfoService.queryById(sampleId));
    }

    /**
     * 新增样品信息
     */
    @Operation(summary = "新增样品", description = "新增样品信息")
    @PreAuthorize("@ss.hasPermi('order:sample:add')")
    @Log(title = "样品信息", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Parameter(description = "样品信息") @RequestBody SampleItemReq req) {
        boolean result = sampleInfoService.addSample(req);
        return result ? R.ok() : R.fail("新增样品信息失败");
    }

    /**
     * 修改样品信息
     */
    @Operation(summary = "修改样品", description = "修改样品信息")
    @PreAuthorize("@ss.hasPermi('order:sample:edit')")
    @Log(title = "样品信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Parameter(description = "样品更新请求") @RequestBody SampleUpdateReq req) {
        boolean result = sampleInfoService.updateSample(req);
        return result ? R.ok() : R.fail("修改样品信息失败");
    }

    /**
     * 删除样品信息
     */
    @Operation(summary = "删除样品", description = "批量删除样品信息")
    @PreAuthorize("@ss.hasPermi('order:sample:remove')")
    @Log(title = "样品信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sampleIds}")
    public R<Void> remove(@Parameter(description = "样品ID数组") @PathVariable String[] sampleIds) {
        boolean result = sampleInfoService.removeByIds(Arrays.asList(sampleIds));
        return result ? R.ok() : R.fail("删除样品信息失败");
    }

    //============================================模板相关=================================================
    @Operation(summary = "获取模板列表", description = "分页获取模板列表")
    @PreAuthorize("@ss.hasPermi('order:sample:template:list')")
    @PostMapping("/template/list")
    public R<PageResp> templateList(@Parameter(description = "模板查询条件") @RequestBody SampleTemplateQueryReq req) {
        IPage<SampleTemplateResp> page = sampleInfoService.queryTemplatePage(req);
        return R.ok(PageResp.of(page.getRecords(), page.getTotal()));
    }
    @Operation(summary = "添加模板板号", description = "添加模板板号")
    @PreAuthorize("@ss.hasPermi('order:sample:template:update')")
    @Log(title = "添加模板板号信息", businessType = BusinessType.UPDATE)
    @PostMapping("/template/updateTemplateNo")
    public R<Void> updateTemplateNo(@Parameter(description = "板号添加") @RequestBody SampleTemplateUpdateReq req) {
        sampleInfoService.updateTemplate(req);
        return R.ok();
    }

    @Operation(summary = "添加模板孔号", description = "添加模板孔号")
    @PreAuthorize("@ss.hasPermi('order:sample:template:update')")
    @Log(title = "添加模板孔号", businessType = BusinessType.UPDATE)
    @PostMapping("/template/updateTemplateHoleNo")
    public R<Void> updateTemplateHoleNo(@Parameter(description = "添加模板孔号") @RequestBody HoleNoUpdateReq req) {
        sampleInfoService.updateTemplateHoleNo(req);
        return R.ok();
    }

    @Operation(summary = "模板bdt", description = "模板bdt")
    @PreAuthorize("@ss.hasPermi('order:sample:template:update')")
    @Log(title = "模板bdt", businessType = BusinessType.UPDATE)
    @PostMapping("/template/templateBDT")
    public R<List<SampleTemplateResp>> templateBDT(@Parameter(description = "模板bdt") @RequestBody TemplateQueryReq req) {
        sampleInfoService.templateBDT(req);
        return R.ok();
    }




}
