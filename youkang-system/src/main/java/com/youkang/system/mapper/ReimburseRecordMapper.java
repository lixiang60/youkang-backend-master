package com.youkang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youkang.system.domain.ReimburseRecord;
import com.youkang.system.domain.req.order.ReimburseRecordQueryReq;
import com.youkang.system.domain.resp.order.ReimburseRecordResp;
import org.apache.ibatis.annotations.Param;

/**
 * 返还记录Mapper接口
 *
 * @author youkang
 */
public interface ReimburseRecordMapper extends BaseMapper<ReimburseRecord> {

    /**
     * 分页查询返还记录
     *
     * @param page 分页参数
     * @param req  查询条件
     * @return 返还记录分页结果
     */
    Page<ReimburseRecordResp> queryPage(Page<ReimburseRecordResp> page, @Param("query") ReimburseRecordQueryReq req);

    /**
     * 根据ID查询详情
     *
     * @param id 记录ID
     * @return 返还记录详情
     */
    ReimburseRecordResp queryById(@Param("id") Long id);
}
