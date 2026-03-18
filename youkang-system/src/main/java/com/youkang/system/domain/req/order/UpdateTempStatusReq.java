package com.youkang.system.domain.req.order;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTempStatusReq {
    @Schema(description = "模板编号")
    private String templateNo;
}
