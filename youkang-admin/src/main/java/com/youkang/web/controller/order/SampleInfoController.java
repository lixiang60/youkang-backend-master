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
import com.youkang.system.domain.resp.order.*;
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
    @Operation(summary = "获取样品详情", description = "根据生产编号获取样品详细信息")
    @PreAuthorize("@ss.hasPermi('order:sample:query')")
    @GetMapping(value = "/{produceId}")
    public R<SampleResp> getInfo(@PathVariable Long produceId) {
        return R.ok(sampleInfoService.queryById(produceId));
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
    @DeleteMapping("/{produceIds}")
    public R<Void> remove(@Parameter(description = "生产编号数组") @PathVariable Long[] produceIds) {
        boolean result = sampleInfoService.removeByIds(Arrays.asList(produceIds));
        return result ? R.ok() : R.fail("删除样品信息失败");
    }

    //============================================模板排版=================================================
    @Operation(summary = "获取模板列表", description = "分页获取模板列表")
    @PreAuthorize("@ss.hasPermi('order:sample:template:list')")
    @PostMapping("/template/list")
    public R<PageResp> templateList(@Parameter(description = "模板查询条件") @RequestBody SampleTemplateQueryReq req) {
        IPage<SampleTemplateResp> page = sampleInfoService.queryTemplatePage(req);
        return R.ok(PageResp.of(page.getRecords(), page.getTotal()));
    }
    @Operation(summary = "添加模板板号和孔号", description = "添加模板板号")
    @PreAuthorize("@ss.hasPermi('order:sample:template:update')")
    @Log(title = "添加模板板号信息", businessType = BusinessType.UPDATE)
    @PostMapping("/template/updateTemplateNo")
    public R<Void> updateTemplateNo(@Parameter(description = "板号添加") @RequestBody SampleTemplateUpdateReq req) {
        sampleInfoService.updateTemplate(req);
        return R.ok();
    }

    @Operation(summary = "单个添加模板孔号", description = "添加模板孔号")
    @PreAuthorize("@ss.hasPermi('order:sample:template:update')")
    @Log(title = "添加模板孔号", businessType = BusinessType.UPDATE)
    @PostMapping("/template/updateTemplateHoleNo")
    public R<Void> updateTemplateHoleNo(@Parameter(description = "添加模板孔号") @RequestBody HoleNoUpdateReq req) {
        sampleInfoService.updateTemplateHoleNo(req);
        return R.ok();
    }

    @Operation(summary = "排版忽略", description = "排版忽略")
    @PreAuthorize("@ss.hasPermi('order:sample:template:ignoreTemp')")
    @Log(title = "排版忽略", businessType = BusinessType.UPDATE)
    @GetMapping("/template/ignoreTemp")
    public R<Void> ignoreTemp(@RequestBody SampleTemplateUpdateReq req) {
        sampleInfoService.ignoreTemp(req);
        return R.ok();
    }

    @Operation(summary = "模板bdt", description = "模板bdt")
    @PreAuthorize("@ss.hasPermi('order:sample:template:update')")
    @Log(title = "模板bdt", businessType = BusinessType.UPDATE)
    @PostMapping("/template/templateBDT")
    public R<List<SampleTemplateResp>> templateBDT(@Parameter(description = "模板bdt") @RequestBody TemplateQueryReq req) {
        return R.ok(sampleInfoService.templateBDT(req));
    }

    @Operation(summary = "获取特定模板板号剩余孔数量", description = "获取特定板号剩余孔数量")
    @PreAuthorize("@ss.hasPermi('order:sample:template:getHoleNum')")
    @GetMapping("/template/getHoleNum")
    public R<Integer> getHoleNum(@RequestParam String templateNo) {
        return R.ok(sampleInfoService.getHoleNum(templateNo));
    }

    //=============================================模板生产============================================
    @Operation(summary = "获取模板生产列表", description = "分页获取模板列表")
    @PreAuthorize("@ss.hasPermi('order:sample:template')")
    @PostMapping("/template/produce/list")
    public R<PageResp> templateProduceList(@RequestBody TemplateProduceQueryReq req) {
        IPage<TemplateProduceResp> page = sampleInfoService.queryTemplateProudcePage(req);
        return R.ok(PageResp.of(page.getRecords(), page.getTotal()));
    }

    @Operation(summary = "设置模板状态", description = "设置模板状态")
    @PreAuthorize("@ss.hasPermi('order:sample:template')")
    @PostMapping("/template/produce/tempStatus")
    public R<?> updateTempStatus(@RequestBody TemplateProduceUpdateReq req) {
        sampleInfoService.updateTempStatus(req);
        return R.ok();
    }

    @Operation(summary = "设置原浓度以及添加版号孔号", description = "设置原浓度以及添加版号孔号")
    @PreAuthorize("@ss.hasPermi('order:sample:template')")
    @PostMapping("/template/produce/originConcentration")
    public R<?> updateOriginConcentration(@RequestBody OriginConcentrationUpdateReq req) {
        sampleInfoService.updateOriginConcentration(req);
        return R.ok();
    }

    @Operation(summary = "退回", description = "模板生产退回")
    @PreAuthorize("@ss.hasPermi('order:sample:template')")
    @PostMapping("/template/produce/sendBack")
    public R<?> sendBack(@RequestBody TemplateProduceUpdateReq req) {
        sampleInfoService.sendBack(req);
        return R.ok();
    }

    @Operation(summary = "PCR切胶", description = "PCR切胶")
    @PreAuthorize("@ss.hasPermi('order:sample:template')")
    @PostMapping("/template/produce/pcrGelCut")
    public R<List<PCRGelCutResp>> pcrGelCut(@RequestBody PCRGelCutReq req) {
        return R.ok(sampleInfoService.pcrGelCut(req));
    }

    @Operation(summary = "查询重抽样品列表", description = "通过模板板号查询模板重抽、报告重抽的样品列表")
    @PreAuthorize("@ss.hasPermi('order:sample:template')")
    @PostMapping("/template/produce/resampleList")
    public R<List<ResampleResp>> resampleList(@RequestBody ResampleQueryReq req) {
        return R.ok(sampleInfoService.queryResampleList(req));
    }

    @Operation(summary = "根据板号获取已经使用的孔号", description = "根据板号获取已经使用的孔号")
    @PreAuthorize("@ss.hasPermi('order:sample:template')")
    @PostMapping("/template/produce/getUserTemplateHole")
    public R<List<UsedTemplateHoleResp>> getUserTemplateHole(@RequestBody HoleNoUpdateReq req) {
        return R.ok(sampleInfoService.getUserTemplateHole(req));
    }

    @Operation(summary = "查询模板失败样品列表", description = "查询所有流程在模板邮件的样品列表")
    @PreAuthorize("@ss.hasPermi('order:sample:template')")
    @GetMapping("/template/produce/templateFailedList")
    public R<List<TemplateFailedResp>> templateFailedList() {
        return R.ok(sampleInfoService.queryTemplateFailedList());
    }

    //=============================================反应生产============================================

    @Operation(summary = "反应生产-设置原浓度", description = "根据生产编号设置原浓度")
    @PreAuthorize("@ss.hasPermi('order:sample:reactionProduce')")
    @Log(title = "反应生产-设置原浓度", businessType = BusinessType.UPDATE)
    @PostMapping("/reactionProduce/originConcentration")
    public R<Void> updateReactionProduceOriginConcentration(@RequestBody ReactionProduceOriginConcentrationReq req) {
        sampleInfoService.updateReactionProduceOriginConcentration(req);
        return R.ok();
    }

    @Operation(summary = "反应生产-添加板号和孔号", description = "根据生产编号批量添加板号和孔号")
    @PreAuthorize("@ss.hasPermi('order:sample:reactionProduce')")
    @Log(title = "反应生产-添加板号和孔号", businessType = BusinessType.UPDATE)
    @PostMapping("/reactionProduce/plate")
    public R<Void> updateReactionProducePlate(@RequestBody ReactionProducePlateReq req) {
        sampleInfoService.updateReactionProducePlate(req);
        return R.ok();
    }

    @Operation(summary = "反应生产-单个添加孔号", description = "根据生产编号单个添加孔号")
    @PreAuthorize("@ss.hasPermi('order:sample:reactionProduce')")
    @Log(title = "反应生产-单个添加孔号", businessType = BusinessType.UPDATE)
    @PostMapping("/reactionProduce/holeNo")
    public R<Void> updateReactionProduceHoleNo(@RequestBody ReactionProduceHoleNoReq req) {
        sampleInfoService.updateReactionProduceHoleNo(req);
        return R.ok();
    }

    @Operation(summary = "测序BDT", description = "根据板号查询测序BDT")
    @PreAuthorize("@ss.hasPermi('order:sample:reactionProduce')")
    @PostMapping("/reactionProduce/sequencingBDT")
    public R<List<SequencingBDTResp>> sequencingBDT(@RequestBody SequencingBDTReq req) {
        return R.ok(sampleInfoService.sequencingBDT(req));
    }

}
