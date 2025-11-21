package com.youkang.common.core.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageResp {
    private Long total;
    private List<?> rows;

    public static PageResp of(Page<?> page) {
        PageResp pageResp = new PageResp();
        pageResp.setTotal(page.getTotal());
        pageResp.setRows(page.getRecords());
        return pageResp;
    }

    public static PageResp of(List<?> rows,Long total) {
        PageResp pageResp = new PageResp();
        pageResp.setTotal(total);
        pageResp.setRows(rows);
        return pageResp;
    }
}
