package com.youkang.system.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "批量更新请求")
@Getter
@Setter
public class UpdateReq {
    @Schema(description = "ID列表", example = "[1, 2, 3]")
    List<Integer> ids;
}
