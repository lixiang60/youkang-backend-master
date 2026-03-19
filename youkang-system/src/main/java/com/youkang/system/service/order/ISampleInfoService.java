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
     * 根据样品ID查询样品详情
     *
     * @param sampleId 样品ID
     * @return 样品响应
     */
    SampleResp queryById(String sampleId);

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

}
