package com.youkang.system.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youkang.system.domain.SampleInfo;

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
     * @param sampleInfo 样品信息查询条件
     * @return 样品信息分页结果
     */
    IPage<SampleInfo> queryPage(SampleInfo sampleInfo);

    /**
     * 查询样品信息列表
     *
     * @param sampleInfo 样品信息查询条件
     * @return 样品信息列表
     */
    List<SampleInfo> queryList(SampleInfo sampleInfo);

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
