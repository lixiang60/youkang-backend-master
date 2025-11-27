package com.youkang.system.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Schema(description = "批量删除请求")
@Getter
public class DeleteReq {
    @Schema(description = "ID列表", example = "[1, 2, 3]", required = true)
    private List<Integer> ids;
}
