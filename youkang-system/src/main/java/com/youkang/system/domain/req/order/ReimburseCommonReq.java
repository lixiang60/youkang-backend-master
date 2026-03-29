package com.youkang.system.domain.req.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReimburseCommonReq {
    private List<ReimburseConfirmReq> reimburseConfirmReqs;
}
