package com.youkang.system.domain.req.subjectgroup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BatchUpdateRep {
    @Schema(description = "课题组ID数组")
    private List<Integer> ids;

    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "地区")
    private String region;

    @Schema(description = "业务员")
    private String salesPerson;

    @Schema(description = "发票抬头")
    private String invoiceTitle;

}
