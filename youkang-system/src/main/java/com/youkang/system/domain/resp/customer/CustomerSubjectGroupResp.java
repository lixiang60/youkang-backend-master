package com.youkang.system.domain.resp.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema
public class CustomerSubjectGroupResp {
    @Schema(description = "关联主键id")
    private Integer id;

    @Schema(description = "客户id")
    private Integer customerId;

    private String customerName;

    private String region;

    private Integer subjectGroupId;

    private String subjectGroupName;

    private Integer status;

    private String paymentMethod;

    private String salesPerson;

    private String customerAddress;
    private String customerPhone;
    private String customerEmail;
    private String createUser;

    private LocalDateTime createTime;

    private String company;

}
