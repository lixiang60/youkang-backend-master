package com.youkang.common.core.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageReq {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

}
