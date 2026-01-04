package com.youkang.system.service.order.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.common.exception.ServiceException;
import com.youkang.common.utils.SecurityUtils;
import com.youkang.common.utils.StringUtils;
import com.youkang.system.domain.SampleInfo;
import com.youkang.system.domain.req.order.SampleItemReq;
import com.youkang.system.domain.req.order.SampleQueryReq;
import com.youkang.system.domain.req.order.SampleUpdateReq;
import com.youkang.system.domain.resp.order.SampleResp;
import com.youkang.system.mapper.SampleInfoMapper;
import com.youkang.system.service.order.ISampleInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 样品信息Service业务层处理
 *
 * @author youkang
 */
@Service
public class SampleInfoServiceImpl extends ServiceImpl<SampleInfoMapper, SampleInfo> implements ISampleInfoService {

    @Autowired
    private SampleInfoMapper sampleInfoMapper;

    /**
     * 分页查询样品信息列表
     *
     * @param req 样品查询请求
     * @return 样品信息分页结果
     */
    @Override
    public IPage<SampleResp> queryPage(SampleQueryReq req) {
        Page<SampleResp> page = new Page<>(req.getPageNum(), req.getPageSize());
        return sampleInfoMapper.queryPage(page, req);
    }

    /**
     * 查询样品信息列表
     *
     * @param req 样品查询请求
     * @return 样品信息列表
     */
    @Override
    public List<SampleResp> queryList(SampleQueryReq req) {
        return sampleInfoMapper.queryList(req);
    }

    /**
     * 根据样品ID查询样品详情
     *
     * @param sampleId 样品ID
     * @return 样品响应
     */
    @Override
    public SampleResp queryById(String sampleId) {
        return sampleInfoMapper.queryById(sampleId);
    }

    /**
     * 新增样品
     *
     * @param req 样品信息请求
     * @return 是否成功
     */
    @Override
    public boolean addSample(SampleItemReq req) {
        SampleInfo sampleInfo = new SampleInfo();
        BeanUtils.copyProperties(req, sampleInfo);
        String username = SecurityUtils.getUsername();
        sampleInfo.setCreateUser(username);
        sampleInfo.setCreateTime(LocalDateTime.now());
        return this.save(sampleInfo);
    }

    /**
     * 修改样品
     *
     * @param req 样品更新请求
     * @return 是否成功
     */
    @Override
    public boolean updateSample(SampleUpdateReq req) {
        SampleInfo sampleInfo = new SampleInfo();
        BeanUtils.copyProperties(req, sampleInfo);
        String username = SecurityUtils.getUsername();
        sampleInfo.setUpdateUser(username);
        sampleInfo.setUpdateTime(LocalDateTime.now());
        return this.updateById(sampleInfo);
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
