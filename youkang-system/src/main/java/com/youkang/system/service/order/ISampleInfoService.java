package com.youkang.system.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youkang.system.domain.SampleInfo;
import com.youkang.system.domain.req.order.SampleItemReq;
import com.youkang.system.domain.req.order.SampleQueryReq;
import com.youkang.system.domain.req.order.SampleUpdateReq;
import com.youkang.system.domain.resp.order.SampleResp;

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
}
