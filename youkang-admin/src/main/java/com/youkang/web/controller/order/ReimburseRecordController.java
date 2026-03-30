package com.youkang.web.controller.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.youkang.common.core.domain.PageResp;
import com.youkang.common.core.domain.YKResponse;
import com.youkang.system.domain.req.order.ReimburseCommonReq;
import com.youkang.system.domain.req.order.ReimburseRecordQueryReq;
import com.youkang.system.domain.resp.order.ReimburseRecordResp;
import com.youkang.system.service.order.IReimburseRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 返还记录Controller
 *
 * @author youkang
 */
@Tag(name = "返还记录管理", description = "返还记录的增删改查操作")
@RestController
@RequestMapping("/order/reimburse")
public class ReimburseRecordController {

    @Autowired
    private IReimburseRecordService reimburseRecordService;

    /**
     * 分页查询返还记录列表
     */
    @Operation(summary = "查询返还记录列表", description = "分页查询返还记录列表")
    @PreAuthorize("@ss.hasPermi('order:reimburse:list')")
    @PostMapping("/list")
    public YKResponse<PageResp> list(@Parameter(description = "返还记录查询条件") @RequestBody ReimburseRecordQueryReq req) {
        IPage<ReimburseRecordResp> page = reimburseRecordService.queryPage(req);
        return YKResponse.ok(PageResp.of(page.getRecords(), page.getTotal()));
    }

    /**
     * 根据ID查询返还记录详情
     */
    @Operation(summary = "获取返还记录详情", description = "根据ID获取返还记录详细信息")
    @PreAuthorize("@ss.hasPermi('order:reimburse:query')")
    @GetMapping("/{id}")
    public YKResponse<ReimburseRecordResp> getInfo(@Parameter(description = "记录ID") @PathVariable Long id) {
        return YKResponse.ok(reimburseRecordService.queryById(id));
    }

    /**
     * 确认返还
     */
    @Operation(summary = "返还管理-安排返还", description = "确认返还操作")
    @PreAuthorize("@ss.hasPermi('order:reimburse:confirm')")
    @PostMapping("/confirm")
    public YKResponse<Void> confirm(@Parameter(description = "返还确认信息") @RequestBody ReimburseCommonReq req) {
        reimburseRecordService.confirm(req);
        return YKResponse.ok();
    }

    /**
     * 删除返还记录
     */
    @Operation(summary = "删除返还记录", description = "删除返还记录")
    @PreAuthorize("@ss.hasPermi('order:reimburse:remove')")
    @PostMapping("/remove")
    public YKResponse<Void> remove(@RequestBody ReimburseCommonReq req) {
        reimburseRecordService.delete(req);
        return YKResponse.ok();
    }
}
