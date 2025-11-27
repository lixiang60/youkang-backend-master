package com.youkang.system.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "客户批量更新请求")
@Getter
@Setter
public class CustomerUpdateReq extends UpdateReq{
    @Schema(description = "区域", example = "华北")
    private String region;

    @Schema(description = "支付方式", example = "月结")
    private String paymentMethod;

    @Schema(description = "销售人员", example = "张三")
    private String salesPerson;

    @Schema(description = "客户单位", example = "北京有康科技有限公司")
    private String customerUnit;
}
