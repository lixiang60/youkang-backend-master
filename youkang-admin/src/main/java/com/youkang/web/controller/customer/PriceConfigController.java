package com.youkang.web.controller.customer;

import com.youkang.common.annotation.Log;
import com.youkang.common.core.domain.YKResponse;
import com.youkang.common.enums.BusinessType;
import com.youkang.common.utils.poi.ExcelUtil;
import com.youkang.system.domain.export.PriceConfigExportVO;
import com.youkang.system.domain.req.price.PriceConfigBatchUpdateReq;
import com.youkang.system.domain.req.price.PriceConfigUpdateReq;
import com.youkang.system.domain.resp.price.PriceConfigResp;
import com.youkang.system.service.price.IPriceConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 价格配置Controller
 *
 * @author youkang
 */
@Tag(name = "价格配置管理", description = "价格配置的查询和修改操作")
@RestController
@RequestMapping("/customer/priceConfig")
public class PriceConfigController {

    @Autowired
    private IPriceConfigService priceConfigService;

    /**
     * 查询课题组价格列表
     */
    @Operation(summary = "查询课题组价格列表", description = "返回全部价格配置，自定义优先，带is_custom标识")
    @PreAuthorize("@ss.hasPermi('customer:priceConfig:list')")
    @GetMapping("/list/{groupId}")
    public YKResponse<List<PriceConfigResp>> list(
            @Parameter(description = "课题组ID") @PathVariable Integer groupId) {
        List<PriceConfigResp> list = priceConfigService.getPriceListByGroupId(groupId);
        return YKResponse.ok(list);
    }

    /**
     * 修改价格配置
     */
    @Operation(summary = "修改价格配置", description = "修改价格配置（范围、单价、计算方式、状态），联动更新相邻范围")
    @PreAuthorize("@ss.hasPermi('customer:priceConfig:edit')")
    @Log(title = "价格配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public YKResponse<Void> update(@Parameter(description = "价格配置修改请求") @Validated @RequestBody PriceConfigUpdateReq req) {
        priceConfigService.updatePriceConfig(req);
        return YKResponse.ok();
    }

    /**
     * 批量修改价格配置
     */
    @Operation(summary = "批量修改价格配置", description = "批量修改多个价格配置")
    @PreAuthorize("@ss.hasPermi('customer:priceConfig:edit')")
    @Log(title = "价格配置", businessType = BusinessType.UPDATE)
    @PutMapping("/batch")
    public YKResponse<Void> batchUpdate(@Parameter(description = "批量修改请求") @Validated @RequestBody PriceConfigBatchUpdateReq req) {
        priceConfigService.batchUpdatePriceConfig(req);
        return YKResponse.ok();
    }

    /**
     * 重置价格配置
     */
    @Operation(summary = "重置价格配置", description = "删除自定义价格，恢复为模板默认")
    @PreAuthorize("@ss.hasPermi('customer:priceConfig:edit')")
    @Log(title = "价格配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public YKResponse<Void> reset(@Parameter(description = "自定义价格ID") @PathVariable Long id) {
        priceConfigService.resetPriceConfig(id);
        return YKResponse.ok();
    }

    /**
     * 导出课题组价格列表
     */
    @Operation(summary = "导出课题组价格列表", description = "根据课题组ID导出价格配置为Excel文件")
    @PreAuthorize("@ss.hasPermi('customer:priceConfig:export')")
    @Log(title = "价格配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export/{groupId}")
    public void export(HttpServletResponse response,
                       @Parameter(description = "课题组ID") @PathVariable Integer groupId) {
        List<PriceConfigResp> list = priceConfigService.getPriceListByGroupId(groupId);
        List<PriceConfigExportVO> exportList = list.stream().map(resp -> {
            PriceConfigExportVO vo = new PriceConfigExportVO();
            BeanUtils.copyProperties(resp, vo);
            return vo;
        }).toList();
        ExcelUtil<PriceConfigExportVO> util = new ExcelUtil<>(PriceConfigExportVO.class);
        util.exportExcel(response, exportList, "课题组价格配置");
    }
}
