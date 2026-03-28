package com.youkang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youkang.system.domain.SampleInfo;
import com.youkang.system.domain.req.order.*;
import com.youkang.system.domain.resp.order.*;
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
     * 根据生产编号查询样品详情
     *
     * @param produceId 生产编号
     * @return 样品响应
     */
    SampleResp queryById(@Param("produceId") Long produceId);

    /**
     * 分页查询模板信息
     *
     * @param page 分页参数
     * @param req 查询条件
     * @return 模板响应分页结果
     */
    Page<SampleTemplateResp> queryTemplatePage(Page<SampleTemplateResp> page, @Param("query") SampleTemplateQueryReq req);

    /**
     * 查询指定板号已被占用的孔号列表
     *
     * @param plateNo 板号
     * @return 已占用的孔号列表
     */
    List<UsedTemplateHoleResp> selectUsedHolesByPlateNo(@Param("plateNo") String plateNo);

    List<SampleTemplateResp> templateBDT(@Param("req") TemplateQueryReq req);

    //======================================模板生产模块======================================
    Page<TemplateProduceResp> queryTemplateProducePage(Page<TemplateProduceResp> page, @Param("query") TemplateProduceQueryReq req);

    /**
     * PCR切胶查询
     *
     * @param req 查询条件
     * @return PCR切胶响应列表
     */
    List<PCRGelCutResp> pcrGelCut(@Param("req") PCRGelCutReq req);

    /**
     * 查询重抽样品列表（模板重抽、报告重抽）
     *
     * @param req 查询条件
     * @return 重抽样品列表
     */
    List<ResampleResp> queryResampleList(@Param("req") ResampleQueryReq req);

    /**
     * 查询模板失败样品列表（用于发送邮件通知）
     *
     * @return 模板失败样品列表
     */
    List<TemplateFailedResp> queryTemplateFailedList();

    /**
     * 测序BDT查询
     *
     * @param req 查询条件（板号）
     * @return 测序BDT响应列表
     */
    List<SequencingBDTResp> sequencingBDT(@Param("req") SequencingBDTReq req);

}
