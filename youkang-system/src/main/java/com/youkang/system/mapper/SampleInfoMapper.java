package com.youkang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youkang.system.domain.SampleInfo;
import com.youkang.system.domain.req.order.SampleQueryReq;
import com.youkang.system.domain.resp.order.SampleResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 样品信息Mapper接口
 *
 * @author youkang
 */
public interface SampleInfoMapper extends BaseMapper<SampleInfo> {

    /**
     * 分页查询样品信息
     *
     * @param page 分页参数
     * @param req 查询条件
     * @return 样品响应分页结果
     */
    Page<SampleResp> queryPage(Page<SampleResp> page, @Param("query") SampleQueryReq req);

    /**
     * 查询样品信息列表
     *
     * @param req 查询条件
     * @return 样品响应列表
     */
    List<SampleResp> queryList(@Param("query") SampleQueryReq req);

    /**
     * 根据样品ID查询样品详情
     *
     * @param sampleId 样品ID
     * @return 样品响应
     */
    SampleResp queryById(@Param("sampleId") String sampleId);
}
