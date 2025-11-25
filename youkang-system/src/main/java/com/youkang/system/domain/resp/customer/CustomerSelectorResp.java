package com.youkang.system.domain.resp.customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSelectorResp {

    private Integer customerId;

    private String customerName;

    private Integer subjectGroupId;

    private String subjectGroupName;

    private String address;

    private String email;

    private String phone;

    private String customerUnit;

    private String resultString;
}
