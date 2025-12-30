package com.youkang.system.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.common.exception.ServiceException;
import com.youkang.common.utils.StringUtils;
import com.youkang.system.domain.SampleInfo;
import com.youkang.system.mapper.SampleInfoMapper;
import com.youkang.system.service.order.ISampleInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 样品信息Service业务层处理
 *
 * @author youkang
 */
@Service
public class SampleInfoServiceImpl extends ServiceImpl<SampleInfoMapper, SampleInfo> implements ISampleInfoService {

    /**
     * 分页查询样品信息列表
     *
     * @param sampleInfo 样品信息查询条件
     * @return 样品信息分页结果
     */
    @Override
    public IPage<SampleInfo> queryPage(SampleInfo sampleInfo) {
        Page<SampleInfo> page = new Page<>(sampleInfo.getPageNum(), sampleInfo.getPageSize());
        LambdaQueryWrapper<SampleInfo> wrapper = buildQueryWrapper(sampleInfo);
        return this.page(page, wrapper);
    }

    /**
     * 查询样品信息列表
     *
     * @param sampleInfo 样品信息查询条件
     * @return 样品信息列表
     */
    @Override
    public List<SampleInfo> queryList(SampleInfo sampleInfo) {
        LambdaQueryWrapper<SampleInfo> wrapper = buildQueryWrapper(sampleInfo);
        return this.list(wrapper);
    }

    /**
     * 构建查询条件
     *
     * @param sampleInfo 样品信息查询条件
     * @return 查询包装器
     */
    private LambdaQueryWrapper<SampleInfo> buildQueryWrapper(SampleInfo sampleInfo) {
        LambdaQueryWrapper<SampleInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(sampleInfo.getOrderId()),
                        SampleInfo::getOrderId, sampleInfo.getOrderId())
                .like(StringUtils.isNotEmpty(sampleInfo.getOrderHistory()),
                        SampleInfo::getOrderHistory, sampleInfo.getOrderHistory())
                .like(StringUtils.isNotEmpty(sampleInfo.getSampleId()),
                        SampleInfo::getSampleId, sampleInfo.getSampleId())
                .eq(StringUtils.isNotEmpty(sampleInfo.getSampleType()),
                        SampleInfo::getSampleType, sampleInfo.getSampleType())
                .like(StringUtils.isNotEmpty(sampleInfo.getSamplePosition()),
                        SampleInfo::getSamplePosition, sampleInfo.getSamplePosition())
                .like(StringUtils.isNotEmpty(sampleInfo.getPrimer()),
                        SampleInfo::getPrimer, sampleInfo.getPrimer())
                .eq(StringUtils.isNotEmpty(sampleInfo.getPrimerType()),
                        SampleInfo::getPrimerType, sampleInfo.getPrimerType())
                .eq(StringUtils.isNotEmpty(sampleInfo.getProject()),
                        SampleInfo::getProject, sampleInfo.getProject())
                .like(StringUtils.isNotEmpty(sampleInfo.getCarrierName()),
                        SampleInfo::getCarrierName, sampleInfo.getCarrierName())
                .eq(StringUtils.isNotEmpty(sampleInfo.getAntibioticType()),
                        SampleInfo::getAntibioticType, sampleInfo.getAntibioticType())
                .eq(StringUtils.isNotEmpty(sampleInfo.getTestResult()),
                        SampleInfo::getTestResult, sampleInfo.getTestResult())
                .eq(StringUtils.isNotEmpty(sampleInfo.getPerformance()),
                        SampleInfo::getPerformance, sampleInfo.getPerformance())
                .eq(sampleInfo.getReturnState() != null,
                        SampleInfo::getReturnState, sampleInfo.getReturnState())
                .like(StringUtils.isNotEmpty(sampleInfo.getFlowName()),
                        SampleInfo::getFlowName, sampleInfo.getFlowName())
                .like(StringUtils.isNotEmpty(sampleInfo.getPlateNo()),
                        SampleInfo::getPlateNo, sampleInfo.getPlateNo())
                .like(StringUtils.isNotEmpty(sampleInfo.getBelongCompany()),
                        SampleInfo::getBelongCompany, sampleInfo.getBelongCompany());
        return wrapper;
    }

    /**
     * 导入样品数据
     *
     * @param sampleList 样品数据列表
     * @param isUpdateSupport 是否支持更新已存在的数据
     * @param operName 操作人
     * @return 导入结果信息
     */
    @Override
    public String importSample(List<SampleInfo> sampleList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(sampleList) || sampleList.isEmpty()) {
            throw new ServiceException("导入样品数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (SampleInfo sample : sampleList) {
            try {
                // 验证样品id是否已存在
                SampleInfo existSample = this.getById(sample.getSampleId());
                if (StringUtils.isNull(existSample)) {
                    // 新增样品
                    this.save(sample);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、样品 ").append(sample.getSampleId()).append(" 导入成功");
                } else if (isUpdateSupport) {
                    // 更新样品
                    this.updateById(sample);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、样品 ").append(sample.getSampleId()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、样品 ").append(sample.getSampleId()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、样品 " + sample.getSampleId() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
