package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 批量添加样品请求
 *
 * @author youkang
 */
@Getter
@Setter
@Schema(description = "批量添加样品请求")
public class SampleBatchAddReq {

    @Schema(description = "样品列表")
    private List<SampleItemReq> sampleList;
}
