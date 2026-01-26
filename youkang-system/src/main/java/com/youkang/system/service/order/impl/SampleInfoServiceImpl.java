package com.youkang.system.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.common.exception.ServiceException;
import com.youkang.common.utils.SecurityUtils;
import com.youkang.common.utils.StringUtils;
import com.youkang.system.domain.SampleInfo;
import com.youkang.system.domain.req.order.*;
import com.youkang.system.domain.resp.order.SampleResp;
import com.youkang.system.domain.resp.order.SampleTemplateResp;
import com.youkang.system.mapper.SampleInfoMapper;
import com.youkang.system.service.order.ISampleInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * @param sampleList      样品数据列表
     * @param isUpdateSupport 是否支持更新已存在的数据
     * @param operName        操作人
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

    //=====================================模板排版======================================

    @Override
    public IPage<SampleTemplateResp> queryTemplatePage(SampleTemplateQueryReq req) {
        Page<SampleTemplateResp> page = new Page<>(req.getPageNum(), req.getPageSize());
        return sampleInfoMapper.queryTemplatePage(page, req);
    }

    @Override
    public void updateTemplate(SampleTemplateUpdateReq req) {
        List<TemplateInfoReq> templateInfo = req.getTemplateInfo();
        if (templateInfo == null || templateInfo.isEmpty()) {
            throw new ServiceException("模板信息不能为空");
        }

        // 获取可用孔号列表（按横排/竖排排序）
        List<String> availableHoles = getAvailableHoles(req.getTemplatePlateNo(), req.getTemplateStype());

        if (availableHoles.size() < templateInfo.size()) {
            throw new ServiceException("当前板号剩余孔号数量不足,剩余：" + availableHoles.size() + "个，请重新选择");
        }

        String username = SecurityUtils.getUsername();
        LocalDateTime now = LocalDateTime.now();

        // 遍历模板信息，为每个样品分配孔号并更新
        for (int i = 0; i < templateInfo.size(); i++) {
            TemplateInfoReq info = templateInfo.get(i);
            String holeNo = availableHoles.get(i);
            LambdaUpdateWrapper<SampleInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(SampleInfo::getTemplatePlateNo, req.getTemplatePlateNo())
                    .set(SampleInfo::getTemplateHoleNo, holeNo)
                    .set(SampleInfo::getLayout, req.getTemplateStype())
                    .set(SampleInfo::getRemark, req.getRemark())
                    .set(SampleInfo::getPerformance, "模板排版")
                    .set(SampleInfo::getUpdateUser, username)
                    .set(SampleInfo::getUpdateTime, now)
                    .eq(SampleInfo::getOrderId, info.getOrderId())
                    .eq(SampleInfo::getSampleId, info.getSampleId());
            this.update(updateWrapper);
        }
    }

    @Override
    public void updateTemplateHoleNo(HoleNoUpdateReq req) {
        //查看当前板号孔号有无被占用
        boolean exists = this.lambdaQuery().eq(SampleInfo::getTemplatePlateNo, req.getTemplatePlateNo())
                .eq(SampleInfo::getTemplateHoleNo, req.getTemplateHoleNo()).exists();
        if (exists) {
            throw new ServiceException("当前板号孔号已被占用");
        }
        String username = SecurityUtils.getUsername();
        this.lambdaUpdate()
                .set(SampleInfo::getTemplatePlateNo, req.getTemplatePlateNo())
                .set(SampleInfo::getTemplateHoleNo, req.getTemplateHoleNo())
                .set(SampleInfo::getUpdateUser, username)
                .set(SampleInfo::getUpdateTime, LocalDateTime.now())
                .eq(SampleInfo::getOrderId, req.getOrderId())
                .eq(SampleInfo::getSampleId, req.getSampleId())
                .update();
    }

    @Override
    public List<SampleTemplateResp> templateBDT(TemplateQueryReq req) {
        return sampleInfoMapper.templateBDT(req);
    }

    /**
     * 获取指定板号的下一个可用孔号
     *
     * @param plateNo 板号
     * @return 可用孔号，如 "A1"，无空位返回 null
     */
    private String getNextAvailableHole(String plateNo) {
        List<String> usedHoles = sampleInfoMapper.selectUsedHolesByPlateNo(plateNo);
        Set<String> usedSet = new HashSet<>(usedHoles);

        // 生成所有孔号 A1-H12 (8行12列 = 96个孔)
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (String row : rows) {
            for (int col = 1; col <= 12; col++) {
                String hole = row + col;
                if (!usedSet.contains(hole)) {
                    return hole;  // 找到第一个空位
                }
            }
        }
        return null;  // 板子已满
    }

    /**
     * 获取指定板号的所有可用孔号列表
     *
     * @param plateNo  板号
     * @param sortType 排序方式：横排/竖排
     * @return 可用孔号列表
     */
    private List<String> getAvailableHoles(String plateNo, String sortType) {
        List<String> usedHoles = sampleInfoMapper.selectUsedHolesByPlateNo(plateNo);
        Set<String> usedSet = new HashSet<>(usedHoles);

        List<String> availableHoles = new ArrayList<>();
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H"};

        boolean isHorizontal = "横排".equals(sortType);

        if (isHorizontal) {
            // 横排：A1-A12, B1-B12, ..., H1-H12 (按行优先)
            for (String row : rows) {
                for (int col = 1; col <= 12; col++) {
                    String hole = row + col;
                    if (!usedSet.contains(hole)) {
                        availableHoles.add(hole);
                    }
                }
            }
        } else {
            // 竖排：A1-H1, A2-H2, ..., A12-H12 (按列优先)
            for (int col = 1; col <= 12; col++) {
                for (String row : rows) {
                    String hole = row + col;
                    if (!usedSet.contains(hole)) {
                        availableHoles.add(hole);
                    }
                }
            }
        }
        return availableHoles;
    }


}
