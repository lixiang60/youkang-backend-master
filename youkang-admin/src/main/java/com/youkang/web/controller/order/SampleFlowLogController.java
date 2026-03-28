package com.youkang.web.controller.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.youkang.common.core.domain.PageResp;
import com.youkang.common.core.domain.R;
import com.youkang.system.domain.req.order.SampleFlowLogQueryReq;
import com.youkang.system.domain.resp.order.SampleFlowLogResp;
import com.youkang.system.service.order.ISampleFlowLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 样品流程流转日志Controller
 *
 * @author youkang
 */
@Tag(name = "样品流程流转日志", description = "样品流程流转日志查询操作")
@RestController
@RequestMapping("/order/sample/flowLog")
public class SampleFlowLogController {

    @Autowired
    private ISampleFlowLogService sampleFlowLogService;

    /**
     * 分页查询流程流转日志
     */
    @Operation(summary = "分页查询流程流转日志", description = "分页查询样品流程流转日志")
    @PreAuthorize("@ss.hasPermi('order:sample:flowLog:list')")
    @PostMapping("/list")
    public R<PageResp> list(@Parameter(description = "查询条件") @RequestBody SampleFlowLogQueryReq req) {
        IPage<SampleFlowLogResp> page = sampleFlowLogService.queryPage(req);
        return R.ok(PageResp.of(page.getRecords(), page.getTotal()));
    }

    /**
     * 根据生产编号查询流转历史
     */
    @Operation(summary = "查询流转历史", description = "根据生产编号查询流转历史")
    @PreAuthorize("@ss.hasPermi('order:sample:flowLog:query')")
    @GetMapping("/history/{produceId}")
    public R<List<SampleFlowLogResp>> history(@PathVariable Long produceId) {
        return R.ok(sampleFlowLogService.queryByProduceId(produceId));
    }
}
