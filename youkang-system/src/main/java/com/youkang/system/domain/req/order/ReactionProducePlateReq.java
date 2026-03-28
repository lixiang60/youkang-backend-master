package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 反应生产-添加板号和孔号请求
 */
@Getter
@Setter
@Schema(description = "反应生产-添加板号和孔号请求")
public class ReactionProducePlateReq {

    @Schema(description = "生产编号列表")
    private List<Long> produceIdList;

    @Schema(description = "排版方式-横排；竖排")
    private String layout;

    @Schema(description = "板号")
    private String plateNo;

    @Schema(description = "备注")
    private String remark;
}
