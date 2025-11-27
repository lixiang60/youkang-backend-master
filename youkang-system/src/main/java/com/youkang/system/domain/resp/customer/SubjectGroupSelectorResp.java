package com.youkang.system.domain.resp.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "科目组选择器响应")
@Getter
@Setter
public class SubjectGroupSelectorResp {
    @Schema(description = "科目组ID", example = "1")
    private Integer id;

    @Schema(description = "科目组名称", example = "科目组A")
    private String name;

    @Schema(description = "联系地址", example = "北京市朝阳区")
    private String contactAddress;

}
