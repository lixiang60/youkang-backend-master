package com.youkang.system.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youkang.system.domain.SampleInfo;
import com.youkang.system.domain.req.order.*;
import com.youkang.system.domain.resp.order.*;

import java.util.List;

/**
 * 样品信息Service接口
 *
 * @author youkang
 */
public interface ISampleInfoService extends IService<SampleInfo> {

    /**
     * 分页查询样品信息列表
     *
     * @param req 样品查询请求
     * @return 样品信息分页结果
     */
    IPage<SampleResp> queryPage(SampleQueryReq req);

    /**
     * 查询样品信息列表
     *
     * @param req 样品查询请求
     * @return 样品信息列表
     */
    List<SampleResp> queryList(SampleQueryReq req);

    /**
     * 根据生产编号查询样品详情
     *
     * @param produceId 生产编号
     * @return 样品响应
     */
    SampleResp queryById(Long produceId);

    /**
     * 新增样品
     *
     * @param req 样品信息请求
     * @return 是否成功
     */
    boolean addSample(SampleItemReq req);

    /**
     * 修改样品
     *
     * @param req 样品更新请求
     * @return 是否成功
     */
    boolean updateSample(SampleUpdateReq req);

    /**
     * 导入样品数据
     *
     * @param sampleList 样品数据列表
     * @param isUpdateSupport 是否支持更新已存在的数据
     * @param operName 操作人
     * @return 导入结果信息
     */
    String importSample(List<SampleInfo> sampleList, Boolean isUpdateSupport, String operName);


    //============================================测序样品相关操作=================================================
    void arrangeReturn(SampleReturnReq req);

    int getSampleCount(SampleReturnReq req);

    void updateSampleReimburseStatus(List<Long> produceIds,String reimburseType);

    void removeSampleReimburseStatus(List<Long> produceIds,String reimburseType);
    //============================================模板排版=================================================
    IPage<SampleTemplateResp> queryTemplatePage(SampleTemplateQueryReq req);


    void updateTemplate(SampleTemplateUpdateReq req);


    void updateTemplateHoleNo(HoleNoUpdateReq req);

    void ignoreTemp(SampleTemplateUpdateReq req);

    List<SampleTemplateResp> templateBDT(TemplateQueryReq req);

    int getHoleNum(String templateNo);

    //=============================================模板生产============================================

    IPage<TemplateProduceResp> queryTemplateProudcePage(TemplateProduceQueryReq req);

    void updateTempStatus(TemplateProduceUpdateReq req);

    void updateOriginConcentration(OriginConcentrationUpdateReq req);

    void sendBack(TemplateProduceUpdateReq req);

    List<PCRGelCutResp> pcrGelCut(PCRGelCutReq req);

    /**
     * 查询重抽样品列表（模板重抽、报告重抽）
     *
     * @param req 查询条件
     * @return 重抽样品列表
     */
    List<ResampleResp> queryResampleList(ResampleQueryReq req);

    List<UsedTemplateHoleResp> getUserTemplateHole(HoleNoUpdateReq req);

    /**
     * 查询模板失败样品列表（用于发送邮件通知）
     *
     * @return 模板失败样品列表
     */
    List<TemplateFailedResp> queryTemplateFailedList();

    //=============================================反应生产============================================

    /**
     * 反应生产-设置原浓度
     *
     * @param req 设置原浓度请求
     */
    void updateReactionProduceOriginConcentration(ReactionProduceOriginConcentrationReq req);

    /**
     * 反应生产-批量添加板号和孔号
     *
     * @param req 添加板号和孔号请求
     */
    void updateReactionProducePlate(ReactionProducePlateReq req);

    /**
     * 反应生产-单个添加孔号
     *
     * @param req 添加孔号请求
     */
    void updateReactionProduceHoleNo(ReactionProduceHoleNoReq req);

    /**
     * 测序BDT查询
     *
     * @param req 查询条件（板号）
     * @return 测序BDT响应列表
     */
    List<SequencingBDTResp> sequencingBDT(SequencingBDTReq req);

    void reactionStop(SampleCommonReq req);

    void sampleInsufficient(SampleCommonReq req);

    void reactionPre(SampleCommonReq req);

    void reactionPreSendBack(SampleCommonReq req);
    //=============================================报告生产============================================

    void capillaryAdd(PlateNoCommonReq req);

    /**
     * 修改报告状态
     *
     * @param req 报告状态修改请求
     */
    void updateReportStatus(ReportStatusUpdateReq req);

}
