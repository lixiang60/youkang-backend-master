package com.youkang.system.domain.req.price;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 价格配置批量修改请求
 *
 * @author youkang
 */
@Data
@Schema(description = "价格配置批量修改请求")
public class PriceConfigBatchUpdateReq {

    @Schema(description = "课题组ID")
    private Integer groupId;

    @Schema(description = "价格配置列表")
    @NotEmpty(message = "价格配置列表不能为空")
    private List<PriceConfigUpdateReq> items;
}
