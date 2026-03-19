package com.youkang.system.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.common.core.redis.RedisCache;
import com.youkang.common.exception.ServiceException;
import com.youkang.common.utils.SecurityUtils;
import com.youkang.common.utils.StringUtils;
import com.youkang.system.domain.SampleInfo;
import com.youkang.system.domain.req.order.*;
import com.youkang.system.domain.resp.order.*;
import com.youkang.system.mapper.SampleInfoMapper;
import com.youkang.system.service.order.ISampleInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 样品信息Service业务层处理
 *
 * @author youkang
 */
@Service
public class SampleInfoServiceImpl extends ServiceImpl<SampleInfoMapper, SampleInfo> implements ISampleInfoService {

    private static final DateTimeFormatter PRODUCE_ID_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");
    private static final String PRODUCE_ID_KEY_PREFIX = "sample:produce:";

    @Autowired
    private SampleInfoMapper sampleInfoMapper;

    @Autowired
    private RedisCache redisCache;

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
        // 自动生成生产编号
        if (sampleInfo.getProduceId() == null) {
            sampleInfo.setProduceId(generateProduceId());
        }
        return this.save(sampleInfo);
    }

    /**
     * 生成生产编号
     * 格式：yyMMdd + 4位序号（当日递增）
     * 使用 Redis 原子递增保证并发安全
     *
     * @return 生产编号，如 2503030001
     */
    public Long generateProduceId() {
        // 获取今日日期字符串yyMMdd
        String dateStr = LocalDate.now().format(PRODUCE_ID_FORMATTER);
        String key = PRODUCE_ID_KEY_PREFIX + dateStr;

        // Redis原子递增
        Long seq = redisCache.increment(key);
        if (seq == 1) {
            // 第一次生成时设置2天过期，自动清理旧数据
            redisCache.expire(key, 2, TimeUnit.DAYS);
        }

        // 拼接生成10位生产编号：yyMMdd + 4位序号，转为 Long
        return Long.parseLong(dateStr + String.format("%04d", seq));
    }

    /**
     * 修改样品
     *
     * @param req 样品更新请求
     * @return 是否成功
     */
    @Override
    public boolean updateSample(SampleUpdateReq req) {
        if (req.getProduceId() == null) {
            throw new ServiceException("生产编号不能为空");
        }
        SampleInfo sampleInfo = new SampleInfo();
        BeanUtils.copyProperties(req, sampleInfo);
        sampleInfo.setUpdateUser(SecurityUtils.getUsername());
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
                // 验证生产编号是否已存在
                SampleInfo existSample = this.getById(sample.getProduceId());
                if (StringUtils.isNull(existSample)) {
                    // 新增样品
                    this.save(sample);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、样品 ").append(sample.getProduceId()).append(" 导入成功");
                } else if (isUpdateSupport) {
                    // 更新样品
                    this.updateById(sample);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、样品 ").append(sample.getProduceId()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、样品 ").append(sample.getProduceId()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、样品 " + sample.getProduceId() + " 导入失败：";
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
                    .set(SampleInfo::getFlowName, "模板生产")
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
                .set(SampleInfo::getPerformance, "模板排版")
                .set(SampleInfo::getFlowName, "模板生产")
                .set(SampleInfo::getRemark, req.getRemark())
                .eq(SampleInfo::getOrderId, req.getOrderId())
                .eq(SampleInfo::getSampleId, req.getSampleId())
                .update();
    }

    @Override
    public void ignoreTemp(SampleTemplateUpdateReq req) {
        String username = SecurityUtils.getUsername();
        req.getTemplateInfo().forEach(info -> {
            this.lambdaUpdate()
                    .set(SampleInfo::getRemark, req.getRemark())
                    .set(SampleInfo::getPerformance, "模板排版")
                    .set(SampleInfo::getFlowName, "模板排版")
                    .set(SampleInfo::getUpdateUser, username)
                    .eq(SampleInfo::getOrderId, info.getOrderId())
                    .eq(SampleInfo::getSampleId, info.getSampleId())
                    .update();
        });
    }

    @Override
    public List<SampleTemplateResp> templateBDT(TemplateQueryReq req) {
        return sampleInfoMapper.templateBDT(req);
    }

    @Override
    public int getHoleNum(String plateNo) {
        List<UsedTemplateHoleResp> usedHoles = sampleInfoMapper.selectUsedHolesByPlateNo(plateNo);
        return 96 - usedHoles.size();
    }


    /**
     * 获取指定板号的所有可用孔号列表
     *
     * @param plateNo  板号
     * @param sortType 排序方式：横排/竖排
     * @return 可用孔号列表
     */
    private List<String> getAvailableHoles(String plateNo, String sortType) {
        List<String> usedHoles = sampleInfoMapper.selectUsedHolesByPlateNo(plateNo).stream().map(UsedTemplateHoleResp::getTemplateHoleNo).toList();
        Set<String> usedSet = new HashSet<>(usedHoles);

        List<String> availableHoles = new ArrayList<>();
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H"};

        boolean isHorizontal = "横排".equals(sortType);

        if (isHorizontal) {
            // 横排：A01-A12, B01-B12, ..., H01-H12 (按行优先)
            for (String row : rows) {
                for (int col = 1; col <= 12; col++) {
                    String hole = row + String.format("%02d", col);
                    if (!usedSet.contains(hole)) {
                        availableHoles.add(hole);
                    }
                }
            }
        } else {
            // 竖排：A01-H01, A02-H02, ..., A12-H12 (按列优先)
            for (int col = 1; col <= 12; col++) {
                for (String row : rows) {
                    String hole = row + String.format("%02d", col);
                    if (!usedSet.contains(hole)) {
                        availableHoles.add(hole);
                    }
                }
            }
        }
        return availableHoles;
    }

    //=============================================模板生产============================================
    public Page<TemplateProduceResp> queryTemplateProudcePage(TemplateProduceQueryReq req) {
        return sampleInfoMapper.queryTemplateProducePage(Page.of(req.getPageNum(), req.getPageSize()), req);
    }

    @Override
    public void updateTempStatus(TemplateProduceUpdateReq req) {
        String returnState = req.getReturnState();
        String flowName;
        if (returnState.equals("模板重抽") || returnState.equals("模板重切")){
            flowName = "模板生产";
        } else if (returnState.equals("模板失败")) {
            flowName = "模板邮件";
        }else {
            flowName = "模板成功";
        }
        this.lambdaUpdate().set(SampleInfo::getReturnState, req.getReturnState())
                .set(SampleInfo::getRemark, req.getRemark())
                .set(SampleInfo::getFlowName, flowName)
                .set(SampleInfo::getUpdateUser, SecurityUtils.getUsername())
                .set(SampleInfo::getUpdateTime, LocalDateTime.now())
                .in(SampleInfo::getProduceId, req.getProduceIdList())
                .update();
    }

    @Override
    public void updateOriginConcentration(OriginConcentrationUpdateReq req){

        //如果用户传了板号则flowname，流程名称设置为反应生产；否则为模板成功
        //1.没有传板号
        if (StringUtils.isEmpty(req.getPlateNo())) {
            //更新浓度，更新状态为模板成功
            this.lambdaUpdate().set(SampleInfo::getOriginConcentration, req.getOriginConcentration())
                    .set(SampleInfo::getFlowName, "模板成功")
                    .set(SampleInfo::getReturnState, "模板成功")
                    .set(SampleInfo::getUpdateUser, SecurityUtils.getUsername())
                    .set(SampleInfo::getUpdateTime, LocalDateTime.now())
                    .in(SampleInfo::getProduceId, req.getProduceIdList())
                    .update();
        }else {
            //2.有传板号
            this.lambdaUpdate().set(SampleInfo::getOriginConcentration, req.getOriginConcentration())
                    .set(SampleInfo::getFlowName, "反应生产")
                    .set(SampleInfo::getReturnState, "反应生产")
                    .set(SampleInfo::getUpdateUser, SecurityUtils.getUsername())
                    .set(SampleInfo::getUpdateTime, LocalDateTime.now())
                    .in(SampleInfo::getProduceId, req.getProduceIdList())
                    .update();
        }

    }

    @Override
    public void sendBack(TemplateProduceUpdateReq req){
        this.lambdaUpdate().set(SampleInfo::getReturnState, "模板退回")
                .set(SampleInfo::getFlowName, "0")
                .set(SampleInfo::getTemplatePlateNo,null)
                .set(SampleInfo::getTemplateHoleNo,null)
                .set(SampleInfo::getUpdateUser, SecurityUtils.getUsername())
                .set(SampleInfo::getUpdateTime, LocalDateTime.now())
                .in(SampleInfo::getProduceId, req.getProduceIdList())
                .update();
    }

    @Override
    public List<PCRGelCutResp> pcrGelCut(PCRGelCutReq req) {
        return sampleInfoMapper.pcrGelCut(req);
    }

    @Override
    public List<ResampleResp> queryResampleList(ResampleQueryReq req) {
        return sampleInfoMapper.queryResampleList(req);
    }

    @Override
    public List<UsedTemplateHoleResp> getUserTemplateHole(HoleNoUpdateReq req){
        return sampleInfoMapper.selectUsedHolesByPlateNo(req.getTemplatePlateNo());
    }

    @Override
    public List<TemplateFailedResp> queryTemplateFailedList() {
        return sampleInfoMapper.queryTemplateFailedList();
    }


}
