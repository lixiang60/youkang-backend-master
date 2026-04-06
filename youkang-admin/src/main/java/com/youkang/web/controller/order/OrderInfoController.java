package com.youkang.web.controller.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.youkang.common.annotation.Log;
import com.youkang.common.core.domain.PageResp;
import com.youkang.common.core.domain.YKResponse;
import com.youkang.common.enums.BusinessType;
import com.youkang.common.utils.SecurityUtils;
import com.youkang.common.utils.poi.ExcelUtil;
import com.youkang.system.domain.OrderInfo;
import com.youkang.system.domain.req.order.OrderAddReq;
import com.youkang.system.domain.req.order.OrderQueryReq;
import com.youkang.system.domain.req.order.OrderRangeQueryReq;
import com.youkang.system.domain.req.order.OrderUpdateReq;
import com.youkang.system.domain.req.order.SampleAddReq;
import com.youkang.system.domain.req.order.SampleBatchAddReq;
import com.youkang.system.domain.resp.order.OrderResp;
import com.youkang.system.domain.resp.order.OrderWithSamplesResp;
import com.youkang.system.domain.resp.order.OrderPriceCalcResp;
import com.youkang.system.service.order.IOrderInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * 订单信息Controller
 *
 * @author youkang
 */
@Tag(name = "订单信息管理", description = "订单信息的增删改查操作")
@RestController
@RequestMapping("/order/info")
public class OrderInfoController {

    @Autowired
    private IOrderInfoService orderInfoService;

    /**
     * 查询订单信息列表
     */
    @Operation(summary = "查询订单信息列表", description = "分页查询订单信息列表")
    @PreAuthorize("@ss.hasPermi('order:info:list')")
    @PostMapping("/list")
    public YKResponse<PageResp> list(@Parameter(description = "订单查询条件") @RequestBody OrderQueryReq req) {
        IPage<OrderResp> page = orderInfoService.queryPage(req);
        return YKResponse.ok(PageResp.of(page.getRecords(), page.getTotal()));
    }

    /**
     * 新增订单信息
     */
    @Operation(summary = "新增订单", description = "新增订单信息")
    @PreAuthorize("@ss.hasPermi('order:info:add')")
    @Log(title = "订单信息", businessType = BusinessType.INSERT)
    @PostMapping("/addOrder")
    public YKResponse<Void> add(@Parameter(description = "订单信息") @RequestBody OrderAddReq req) {
        orderInfoService.addOrder(req);
        return YKResponse.ok();
    }

    /**
     * 通过订单新增样品
     */
    @Operation(summary = "通过订单新增样品", description = "通过订单新增样品")
    @PostMapping("/addSample")
    public YKResponse<Void> addSample(@Valid @RequestBody SampleAddReq req){
        orderInfoService.addSample(req);
        return YKResponse.ok();
    }

    /**
     * 通过订单批量新增样品
     */
    @Operation(summary = "通过订单批量新增样品", description = "通过订单批量新增样品")
    @PostMapping("/batchAddSample")
    public YKResponse<Void> batchAddSample( @RequestBody SampleBatchAddReq req){
        orderInfoService.batchAddSample(req);
        return YKResponse.ok();
    }



    /**
     * 导出订单信息列表
     */
    @Operation(summary = "导出订单信息", description = "导出订单信息列表为Excel文件")
    @PreAuthorize("@ss.hasPermi('order:info:export')")
    @Log(title = "订单信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @Parameter(description = "订单查询条件") OrderQueryReq req) {
        List<OrderResp> list = orderInfoService.queryList(req);
        ExcelUtil<OrderResp> util = new ExcelUtil<>(OrderResp.class);
        util.exportExcel(response, list, "订单信息数据");
    }

    /**
     * 导入订单信息
     */
    @Operation(summary = "导入订单信息", description = "从Excel文件导入订单信息")
    @PreAuthorize("@ss.hasPermi('order:info:import')")
    @Log(title = "订单信息", businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    public YKResponse<String> importData(@Parameter(description = "Excel文件") MultipartFile file, @Parameter(description = "是否更新已存在的数据")
                                @RequestParam(defaultValue = "false") boolean updateSupport
    ) throws Exception {
        ExcelUtil<OrderInfo> util = new ExcelUtil<>(OrderInfo.class);
        List<OrderInfo> orderList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = orderInfoService.importOrder(orderList, updateSupport, operName);
        return YKResponse.ok(message);
    }

    /**
     * 下载导入模板
     */
    @Operation(summary = "下载导入模板", description = "下载订单信息导入Excel模板")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<OrderInfo> util = new ExcelUtil<>(OrderInfo.class);
        util.importTemplateExcel(response, "订单信息数据");
    }

    /**
     * 获取订单详细信息
     */
    @Operation(summary = "获取订单详情", description = "根据订单ID获取订单详细信息")
    @PreAuthorize("@ss.hasPermi('order:info:query')")
    @GetMapping(value = "/{orderId}")
    public YKResponse<OrderResp> getInfo(@Parameter(description = "订单ID") @PathVariable("orderId") String orderId) {
        return YKResponse.ok(orderInfoService.queryById(orderId));
    }


    /**
     * 修改订单信息
     */
    @Operation(summary = "修改订单", description = "修改订单信息")
    @PreAuthorize("@ss.hasPermi('order:info:edit')")
    @Log(title = "订单信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public YKResponse<Void> edit(@Parameter(description = "订单更新请求") @RequestBody OrderUpdateReq req) {
        boolean result = orderInfoService.updateOrder(req);
        return result ? YKResponse.ok() : YKResponse.fail("修改订单信息失败");
    }

    /**
     * 删除订单信息
     */
    @Operation(summary = "删除订单", description = "批量删除订单信息")
    @PreAuthorize("@ss.hasPermi('order:info:remove')")
    @Log(title = "订单信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderIds}")
    public YKResponse<Void> remove(@Parameter(description = "订单ID数组") @PathVariable String[] orderIds) {
        boolean result = orderInfoService.removeByIds(Arrays.asList(orderIds));
        return result ? YKResponse.ok() : YKResponse.fail("删除订单信息失败");
    }

    /**
     * 根据条件范围查询订单信息
     */
    @Operation(summary = "范围查询订单", description = "根据订单号范围、创建时间范围等条件查询订单信息")
    @PreAuthorize("@ss.hasPermi('order:info:list')")
    @PostMapping("/queryByRange")
    public YKResponse<List<OrderResp>> queryByRange(@Parameter(description = "范围查询条件") @RequestBody OrderRangeQueryReq req) {
        return YKResponse.ok(orderInfoService.queryByRange(req));
    }

    /**
     * 根据订单号查询订单及样品信息
     */
    @Operation(summary = "查询订单及样品", description = "根据订单号查询订单信息及其关联的所有样品信息")
    @PreAuthorize("@ss.hasPermi('order:info:query')")
    @GetMapping("/withSamples/{orderId}")
    public YKResponse<OrderWithSamplesResp> queryOrderWithSamples(@Parameter(description = "订单号") @PathVariable String orderId) {
        return YKResponse.ok(orderInfoService.queryOrderWithSamples(orderId));
    }

    /**
     * 根据条件范围查询订单及样品信息
     */
    @Operation(summary = "范围查询订单及样品", description = "根据订单号范围、时间范围等条件查询订单及其关联的样品信息")
    @PreAuthorize("@ss.hasPermi('order:info:list')")
    @PostMapping("/withSamples/queryByRange")
    public YKResponse<List<OrderWithSamplesResp>> queryOrderWithSamplesByRange(@Parameter(description = "范围查询条件") @RequestBody OrderRangeQueryReq req) {
        return YKResponse.ok(orderInfoService.queryOrderWithSamplesByRange(req));
    }

    /**
     * 计算订单价格
     */
    @Operation(summary = "计算订单价格", description = "根据订单号计算各样品的单价和总价，按样品编号分组数量叠加")
    @PreAuthorize("@ss.hasPermi('order:info:query')")
    @GetMapping("/calcPrice/{orderId}")
    public YKResponse<List<OrderPriceCalcResp>> calcOrderPrice(@Parameter(description = "订单号") @PathVariable String orderId) {
        return YKResponse.ok(orderInfoService.calcOrderPrice(orderId));
    }

    /**
     * 订单状态回退
     */
    @Operation(summary = "订单状态回退", description = "将订单状态回退到上一级：销售回款→订单完成→订单出库→订单生成")
    @PreAuthorize("@ss.hasPermi('order:info:edit')")
    @Log(title = "订单信息", businessType = BusinessType.UPDATE)
    @PutMapping("/resetStatus/{orderId}")
    public YKResponse<Void> resetOrderStatus(@Parameter(description = "订单号") @PathVariable String orderId) {
        orderInfoService.resetOrderStatus(orderId);
        return YKResponse.ok();
    }
}
