package com.youkang.web.controller.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.youkang.common.annotation.Log;
import com.youkang.common.core.domain.PageResp;
import com.youkang.common.core.domain.YKResponse;
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
    public YKResponse<PageResp> list(@Parameter(description = "样品查询条件") @RequestBody SampleQueryReq req) {
        IPage<SampleResp> page = sampleInfoService.queryPage(req);
        return YKResponse.ok(PageResp.of(page.getRecords(), page.getTotal()));
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
    public YKResponse<String> importData(
            @Parameter(description = "Excel文件") MultipartFile file,
            @Parameter(description = "是否更新已存在的数据") @RequestParam(defaultValue = "false") boolean updateSupport
    ) throws Exception {
        ExcelUtil<SampleInfo> util = new ExcelUtil<>(SampleInfo.class);
        List<SampleInfo> sampleList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = sampleInfoService.importSample(sampleList, updateSupport, operName);
        return YKResponse.ok(message);
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
    public YKResponse<SampleResp> getInfo(@PathVariable Long produceId) {
        return YKResponse.ok(sampleInfoService.queryById(produceId));
    }

    /**
     * 新增样品信息
     */
    @Operation(summary = "新增样品", description = "新增样品信息")
    @PreAuthorize("@ss.hasPermi('order:sample:add')")
    @Log(title = "样品信息", businessType = BusinessType.INSERT)
    @PostMapping
    public YKResponse<Void> add(@Parameter(description = "样品信息") @RequestBody SampleItemReq req) {
        boolean result = sampleInfoService.addSample(req);
        return result ? YKResponse.ok() : YKResponse.fail("新增样品信息失败");
    }

    /**
     * 修改样品信息
     */
    @Operation(summary = "修改样品", description = "修改样品信息")
    @PreAuthorize("@ss.hasPermi('order:sample:edit')")
    @Log(title = "样品信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public YKResponse<Void> edit(@Parameter(description = "样品更新请求") @RequestBody SampleUpdateReq req) {
        boolean result = sampleInfoService.updateSample(req);
        return result ? YKResponse.ok() : YKResponse.fail("修改样品信息失败");
    }

    /**
     * 删除样品信息
     */
    @Operation(summary = "删除样品", description = "批量删除样品信息")
    @PreAuthorize("@ss.hasPermi('order:sample:remove')")
    @Log(title = "样品信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{produceIds}")
    public YKResponse<Void> remove(@Parameter(description = "生产编号数组") @PathVariable Long[] produceIds) {
        boolean result = sampleInfoService.removeByIds(Arrays.asList(produceIds));
        return result ? YKResponse.ok() : YKResponse.fail("删除样品信息失败");
    }


    //============================================测序样品功能模块=================================================
    @Operation(summary = "安排返还-前端需要做校验，只能传同一orderId下的样品信息", description = "安排返还")
    @PreAuthorize("@ss.hasPermi('order:sample:sequence:arrangeReturn')")
    @PostMapping("/sequence/arrangeReturn")
    public YKResponse<Void> arrangeReturn(@Parameter(description = "安排返还请求") @RequestBody SampleReturnReq req) {
        sampleInfoService.arrangeReturn(req);
        return YKResponse.ok();
    }

    //============================================模板排版=================================================
    @Operation(summary = "获取模板列表", description = "分页获取模板列表")
    @PreAuthorize("@ss.hasPermi('order:sample:template:list')")
    @PostMapping("/template/list")
    public YKResponse<PageResp> templateList(@Parameter(description = "模板查询条件") @RequestBody SampleTemplateQueryReq req) {
        IPage<SampleTemplateResp> page = sampleInfoService.queryTemplatePage(req);
        return YKResponse.ok(PageResp.of(page.getRecords(), page.getTotal()));
    }
    @Operation(summary = "添加模板板号和孔号", description = "添加模板板号")
    @PreAuthorize("@ss.hasPermi('order:sample:template:update')")
    @Log(title = "添加模板板号信息", businessType = BusinessType.UPDATE)
    @PostMapping("/template/updateTemplateNo")
    public YKResponse<Void> updateTemplateNo(@Parameter(description = "板号添加") @RequestBody SampleTemplateUpdateReq req) {
        sampleInfoService.updateTemplate(req);
        return YKResponse.ok();
    }

    @Operation(summary = "单个添加模板孔号", description = "添加模板孔号")
    @PreAuthorize("@ss.hasPermi('order:sample:template:update')")
    @Log(title = "添加模板孔号", businessType = BusinessType.UPDATE)
    @PostMapping("/template/updateTemplateHoleNo")
    public YKResponse<Void> updateTemplateHoleNo(@Parameter(description = "添加模板孔号") @RequestBody HoleNoUpdateReq req) {
        sampleInfoService.updateTemplateHoleNo(req);
        return YKResponse.ok();
    }

    @Operation(summary = "排版忽略", description = "排版忽略")
    @PreAuthorize("@ss.hasPermi('order:sample:template:ignoreTemp')")
    @Log(title = "排版忽略", businessType = BusinessType.UPDATE)
    @PostMapping("/template/ignoreTemp")
    public YKResponse<Void> ignoreTemp(@RequestBody SampleTemplateUpdateReq req) {
        sampleInfoService.ignoreTemp(req);
        return YKResponse.ok();
    }

    @Operation(summary = "模板bdt", description = "模板bdt")
    @PreAuthorize("@ss.hasPermi('order:sample:template:update')")
    @PostMapping("/template/templateBDT")
    public YKResponse<List<SampleTemplateResp>> templateBDT(@Parameter(description = "模板bdt") @RequestBody TemplateQueryReq req) {
        return YKResponse.ok(sampleInfoService.templateBDT(req));
    }

    @Operation(summary = "获取特定模板板号剩余孔数量", description = "获取特定板号剩余孔数量")
    @PreAuthorize("@ss.hasPermi('order:sample:template:getHoleNum')")
    @GetMapping("/template/getHoleNum")
    public YKResponse<Integer> getHoleNum(@RequestParam String templateNo) {
        return YKResponse.ok(sampleInfoService.getHoleNum(templateNo));
    }

    //=============================================模板生产============================================
    @Operation(summary = "获取各流程数据列表", description = "分页获取各流程数据列表")
    @PreAuthorize("@ss.hasPermi('order:sample:flow')")
    @PostMapping("/template/produce/list")
    public YKResponse<PageResp> templateProduceList(@RequestBody TemplateProduceQueryReq req) {
        IPage<TemplateProduceResp> page = sampleInfoService.queryTemplateProudcePage(req);
        return YKResponse.ok(PageResp.of(page.getRecords(), page.getTotal()));
    }

    @Operation(summary = "设置模板状态", description = "设置模板状态")
    @PreAuthorize("@ss.hasPermi('order:sample:template')")
    @PostMapping("/template/produce/tempStatus")
    public YKResponse<?> updateTempStatus(@RequestBody TemplateProduceUpdateReq req) {
        sampleInfoService.updateTempStatus(req);
        return YKResponse.ok();
    }

    @Operation(summary = "设置原浓度以及添加版号孔号", description = "设置原浓度以及添加版号孔号")
    @PreAuthorize("@ss.hasPermi('order:sample:template')")
    @PostMapping("/template/produce/originConcentration")
    public YKResponse<?> updateOriginConcentration(@RequestBody OriginConcentrationUpdateReq req) {
        sampleInfoService.updateOriginConcentration(req);
        return YKResponse.ok();
    }

    @Operation(summary = "模板生产退回", description = "模板生产退回")
    @PreAuthorize("@ss.hasPermi('order:sample:template')")
    @PostMapping("/template/produce/sendBack")
    public YKResponse<?> sendBack(@RequestBody TemplateProduceUpdateReq req) {
        sampleInfoService.sendBack(req);
        return YKResponse.ok();
    }

    @Operation(summary = "PCR切胶", description = "PCR切胶")
    @PreAuthorize("@ss.hasPermi('order:sample:template')")
    @PostMapping("/template/produce/pcrGelCut")
    public YKResponse<List<PCRGelCutResp>> pcrGelCut(@RequestBody PCRGelCutReq req) {
        return YKResponse.ok(sampleInfoService.pcrGelCut(req));
    }

    @Operation(summary = "查询重抽样品列表", description = "通过模板板号查询模板重抽、报告重抽的样品列表")
    @PreAuthorize("@ss.hasPermi('order:sample:template')")
    @PostMapping("/template/produce/resampleList")
    public YKResponse<List<ResampleResp>> resampleList(@RequestBody ResampleQueryReq req) {
        return YKResponse.ok(sampleInfoService.queryResampleList(req));
    }

    @Operation(summary = "根据板号获取已经使用的孔号", description = "根据板号获取已经使用的孔号")
    @PreAuthorize("@ss.hasPermi('order:sample:template')")
    @PostMapping("/template/produce/getUserTemplateHole")
    public YKResponse<List<UsedTemplateHoleResp>> getUserTemplateHole(@RequestBody HoleNoUpdateReq req) {
        return YKResponse.ok(sampleInfoService.getUserTemplateHole(req));
    }

    @Operation(summary = "查询模板失败样品列表", description = "查询所有流程在模板邮件的样品列表")
    @PreAuthorize("@ss.hasPermi('order:sample:template')")
    @GetMapping("/template/produce/templateFailedList")
    public YKResponse<List<TemplateFailedResp>> templateFailedList() {
        return YKResponse.ok(sampleInfoService.queryTemplateFailedList());
    }

    //=============================================反应生产============================================

    @Operation(summary = "反应生产-设置原浓度", description = "根据生产编号设置原浓度")
    @PreAuthorize("@ss.hasPermi('order:sample:reactionProduce')")
    @Log(title = "反应生产-设置原浓度", businessType = BusinessType.UPDATE)
    @PostMapping("/reactionProduce/originConcentration")
    public YKResponse<Void> updateReactionProduceOriginConcentration(@RequestBody ReactionProduceOriginConcentrationReq req) {
        sampleInfoService.updateReactionProduceOriginConcentration(req);
        return YKResponse.ok();
    }

    @Operation(summary = "反应生产-添加板号和孔号", description = "根据生产编号批量添加板号和孔号")
    @PreAuthorize("@ss.hasPermi('order:sample:reactionProduce')")
    @Log(title = "反应生产-添加板号和孔号", businessType = BusinessType.UPDATE)
    @PostMapping("/reactionProduce/plate")
    public YKResponse<Void> updateReactionProducePlate(@RequestBody ReactionProducePlateReq req) {
        sampleInfoService.updateReactionProducePlate(req);
        return YKResponse.ok();
    }

    @Operation(summary = "反应生产-单个添加孔号", description = "根据生产编号单个添加孔号")
    @PreAuthorize("@ss.hasPermi('order:sample:reactionProduce')")
    @Log(title = "反应生产-单个添加孔号", businessType = BusinessType.UPDATE)
    @PostMapping("/reactionProduce/holeNo")
    public YKResponse<Void> updateReactionProduceHoleNo(@RequestBody ReactionProduceHoleNoReq req) {
        sampleInfoService.updateReactionProduceHoleNo(req);
        return YKResponse.ok();
    }

    @Operation(summary = "测序BDT", description = "根据板号查询测序BDT")
    @PreAuthorize("@ss.hasPermi('order:sample:reactionProduce')")
    @PostMapping("/reactionProduce/sequencingBDT")
    public YKResponse<List<SequencingBDTResp>> sequencingBDT(@RequestBody SequencingBDTReq req) {
        return YKResponse.ok(sampleInfoService.sequencingBDT(req));
    }

    @Operation(summary = "反应停止", description = "反应停止")
    @PreAuthorize("@ss.hasPermi('order:sample:reactionStop')")
    @PostMapping("/reactionStop")
    public YKResponse<?> reactionStop(@RequestBody SampleCommonReq req) {
        sampleInfoService.reactionStop(req);
        return YKResponse.ok();
    }

    @Operation(summary = "样品不足", description = "样品不足")
    @PreAuthorize("@ss.hasPermi('order:sample:sampleInsufficient')")
    @PostMapping("/sampleInsufficient")
    public YKResponse<?> insufficient(@RequestBody SampleCommonReq req) {
        sampleInfoService.sampleInsufficient(req);
        return YKResponse.ok();
    }

    @Operation(summary = "反应预做", description = "反应预做")
    @PreAuthorize("@ss.hasPermi('order:sample:reactionPre')")
    @PostMapping("/reactionPre")
    public YKResponse<?> reactionPre(@RequestBody SampleCommonReq req) {
        sampleInfoService.reactionPre(req);
        return YKResponse.ok();
    }

    @Operation(summary = "反应预做退回", description = "反应预做退回")
    @PreAuthorize("@ss.hasPermi('order:sample:reactionPreSendBack')")
    @PostMapping("/reactionPreSendBack")
    public YKResponse<?> reactionPreSendBack(@RequestBody SampleCommonReq req) {
        sampleInfoService.reactionPreSendBack(req);
        return YKResponse.ok();
    }
    //=============================================报告生产============================================
    @Operation(summary = "毛细管添加", description = "毛细管添加")
    @PreAuthorize("@ss.hasPermi('order:sample:capillaryAdd')")
    @PostMapping("/capillaryAdd")
    public YKResponse<?> capillaryAdd(@RequestBody PlateNoCommonReq req) {
        sampleInfoService.capillaryAdd(req);
        return YKResponse.ok();
    }

    @Operation(summary = "修改报告状态", description = "修改报告状态，支持：报告成功、报告取消、报告重做、报告重抽")
    @PreAuthorize("@ss.hasPermi('order:sample:reportStatus')")
    @Log(title = "修改报告状态", businessType = BusinessType.UPDATE)
    @PostMapping("/reportStatus/update")
    public YKResponse<?> updateReportStatus(@RequestBody ReportStatusUpdateReq req) {
        sampleInfoService.updateReportStatus(req);
        return YKResponse.ok();
    }

}
