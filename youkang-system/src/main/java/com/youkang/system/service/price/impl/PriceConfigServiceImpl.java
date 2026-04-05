package com.youkang.system.service.price.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.youkang.common.exception.ServiceException;
import com.youkang.common.utils.SecurityUtils;
import com.youkang.system.domain.PriceConfig;
import com.youkang.system.domain.UnitPrice;
import com.youkang.system.domain.req.price.PriceConfigBatchUpdateReq;
import com.youkang.system.domain.req.price.PriceConfigUpdateReq;
import com.youkang.system.domain.resp.price.PriceConfigResp;
import com.youkang.system.mapper.PriceConfigMapper;
import com.youkang.system.mapper.UnitPriceMapper;
import com.youkang.system.service.price.IPriceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 价格配置Service实现
 *
 * @author youkang
 */
@Slf4j
@Service
public class PriceConfigServiceImpl implements IPriceConfigService {

    @Autowired
    private PriceConfigMapper priceConfigMapper;

    @Autowired
    private UnitPriceMapper unitPriceMapper;

    @Override
    public List<PriceConfigResp> getPriceListByGroupId(Integer groupId) {
        return priceConfigMapper.selectPriceListByGroupId(groupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePriceConfig(PriceConfigUpdateReq req) {
        Integer groupId = req.getGroupId();
        String sampleType = req.getSampleType();
        String project = req.getProject();

        // 查找模板记录
        LambdaQueryWrapper<PriceConfig> templateWrapper = new LambdaQueryWrapper<>();
        templateWrapper.isNull(PriceConfig::getGroupId)
                .eq(PriceConfig::getSampleType, sampleType)
                .eq(PriceConfig::getProject, project)
                .eq(PriceConfig::getPlasmidLengthMin, req.getPlasmidLengthMin())
                .eq(PriceConfig::getPlasmidLengthMax, req.getPlasmidLengthMax())
                .eq(PriceConfig::getFragmentSizeMin, req.getFragmentSizeMin())
                .eq(PriceConfig::getFragmentSizeMax, req.getFragmentSizeMax());
        PriceConfig template = priceConfigMapper.selectOne(templateWrapper);

        if (template == null) {
            throw new ServiceException("未找到对应的价格模板");
        }

        // 判断模板单价是否为空：为空则走 yk_unit_price 逻辑，否则走原有 yk_price_config 自定义逻辑
        if (template.getUnitPrice() == null) {
            updateUnitPrice(groupId, req.getUnitPrice());
        } else {
            updateCustomPrice(groupId, req, template);
        }

        // 处理范围联动修改（只对模板生效）
        handleRangeCascadeUpdate(template, req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdatePriceConfig(PriceConfigBatchUpdateReq req) {
        for (PriceConfigUpdateReq item : req.getItems()) {
            if (req.getGroupId() != null) {
                item.setGroupId(req.getGroupId());
            }
            updatePriceConfig(item);
        }
    }

    @Override
    public void resetPriceConfig(Long id) {
        PriceConfig customPrice = priceConfigMapper.selectById(id);
        if (customPrice == null || customPrice.getGroupId() == null) {
            throw new ServiceException("自定义价格不存在或不能重置模板价格");
        }
        priceConfigMapper.deleteById(id);
    }

    /**
     * 更新课题组基础单价（模板单价为空时走此逻辑）
     */
    private void updateUnitPrice(Integer groupId, java.math.BigDecimal unitPrice) {
        String username = SecurityUtils.getUsername();
        LocalDateTime now = LocalDateTime.now();

        UnitPrice existing = unitPriceMapper.selectById(groupId);
        if (existing != null) {
            existing.setUnitPrice(unitPrice);
            existing.setUpdateBy(username);
            existing.setUpdateTime(now);
            unitPriceMapper.updateById(existing);
        } else {
            UnitPrice newPrice = new UnitPrice();
            newPrice.setGroupId(groupId);
            newPrice.setUnitPrice(unitPrice);
            newPrice.setCreateBy(username);
            newPrice.setCreateTime(now);
            newPrice.setUpdateBy(username);
            newPrice.setUpdateTime(now);
            unitPriceMapper.insert(newPrice);
        }
        log.info("更新课题组基础单价: groupId={}, unitPrice={}", groupId, unitPrice);
    }

    /**
     * 更新自定义价格（模板单价不为空时走此逻辑）
     */
    private void updateCustomPrice(Integer groupId, PriceConfigUpdateReq req, PriceConfig template) {
        // 查找是否已存在自定义价格
        LambdaQueryWrapper<PriceConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PriceConfig::getGroupId, groupId)
                .eq(PriceConfig::getSampleType, req.getSampleType())
                .eq(PriceConfig::getProject, req.getProject())
                .eq(PriceConfig::getPlasmidLengthMin, req.getPlasmidLengthMin())
                .eq(PriceConfig::getPlasmidLengthMax, req.getPlasmidLengthMax())
                .eq(PriceConfig::getFragmentSizeMin, req.getFragmentSizeMin())
                .eq(PriceConfig::getFragmentSizeMax, req.getFragmentSizeMax());

        PriceConfig existingCustom = priceConfigMapper.selectOne(wrapper);

        if (existingCustom != null) {
            updateExistingPrice(existingCustom, req);
            priceConfigMapper.updateById(existingCustom);
        } else {
            PriceConfig newCustom = createCustomPrice(template, req);
            priceConfigMapper.insert(newCustom);
        }
    }

    /**
     * 更新已有的价格配置
     */
    private void updateExistingPrice(PriceConfig price, PriceConfigUpdateReq req) {
        price.setUnitPrice(req.getUnitPrice());
        price.setCalcMethod(req.getCalcMethod());
        if (req.getStatus() != null) {
            price.setStatus(req.getStatus());
        }
        if (req.getRemark() != null) {
            price.setRemark(req.getRemark());
        }
        price.setUpdateBy(SecurityUtils.getUsername());
        price.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 创建自定义价格配置
     */
    private PriceConfig createCustomPrice(PriceConfig template, PriceConfigUpdateReq req) {
        PriceConfig custom = new PriceConfig();
        custom.setGroupId(req.getGroupId());
        custom.setCategory(template.getCategory());
        custom.setChargeName(template.getChargeName());
        custom.setSampleType(template.getSampleType());
        custom.setProject(template.getProject());
        custom.setPlasmidLengthMin(template.getPlasmidLengthMin());
        custom.setPlasmidLengthMax(template.getPlasmidLengthMax());
        custom.setFragmentSizeMin(template.getFragmentSizeMin());
        custom.setFragmentSizeMax(template.getFragmentSizeMax());
        custom.setUnitPrice(req.getUnitPrice());
        custom.setCalcMethod(req.getCalcMethod());
        custom.setStatus(req.getStatus() != null ? req.getStatus() : template.getStatus());
        custom.setRemark(req.getRemark());
        custom.setCreateBy(SecurityUtils.getUsername());
        custom.setCreateTime(LocalDateTime.now());
        return custom;
    }

    /**
     * 处理范围联动修改
     * 当修改模板的范围时，联动更新相邻的模板记录
     */
    private void handleRangeCascadeUpdate(PriceConfig template, PriceConfigUpdateReq req) {
        Integer oldPlasmidMin = template.getPlasmidLengthMin();
        Integer oldPlasmidMax = template.getPlasmidLengthMax();
        Integer oldFragmentMin = template.getFragmentSizeMin();
        Integer oldFragmentMax = template.getFragmentSizeMax();

        Integer newPlasmidMin = req.getPlasmidLengthMin();
        Integer newPlasmidMax = req.getPlasmidLengthMax();
        Integer newFragmentMin = req.getFragmentSizeMin();
        Integer newFragmentMax = req.getFragmentSizeMax();

        String sampleType = template.getSampleType();
        String project = template.getProject();

        // 更新模板本身的范围
        boolean rangeChanged = false;
        if (newPlasmidMin != null && !newPlasmidMin.equals(oldPlasmidMin)) {
            template.setPlasmidLengthMin(newPlasmidMin);
            rangeChanged = true;
        }
        if (newPlasmidMax != null && !newPlasmidMax.equals(oldPlasmidMax)) {
            template.setPlasmidLengthMax(newPlasmidMax);
            rangeChanged = true;
        }
        if (newFragmentMin != null && !newFragmentMin.equals(oldFragmentMin)) {
            template.setFragmentSizeMin(newFragmentMin);
            rangeChanged = true;
        }
        if (newFragmentMax != null && !newFragmentMax.equals(oldFragmentMax)) {
            template.setFragmentSizeMax(newFragmentMax);
            rangeChanged = true;
        }

        if (rangeChanged) {
            template.setUpdateBy(SecurityUtils.getUsername());
            template.setUpdateTime(LocalDateTime.now());
            priceConfigMapper.updateById(template);
        }

        // 联动更新相邻的质粒长度范围（范围间隙差1，如 0-20000, 20001-50000）
        if (newPlasmidMin != null && !newPlasmidMin.equals(oldPlasmidMin)) {
            // 前一条记录的 max = oldMin - 1，更新为 newMin - 1
            updateAdjacentRange(sampleType, project, null, oldPlasmidMin - 1, oldFragmentMin, oldFragmentMax,
                    "plasmid_length_max", newPlasmidMin - 1);
        }
        if (newPlasmidMax != null && !newPlasmidMax.equals(oldPlasmidMax)) {
            // 后一条记录的 min = oldMax + 1，更新为 newMax + 1
            updateAdjacentRange(sampleType, project, oldPlasmidMax + 1, null, oldFragmentMin, oldFragmentMax,
                    "plasmid_length_min", newPlasmidMax + 1);
        }

        // 联动更新相邻的片段大小范围（范围间隙差1）
        if (newFragmentMin != null && !newFragmentMin.equals(oldFragmentMin)) {
            // 前一条记录的 max = oldFragmentMin - 1，更新为 newFragmentMin - 1
            updateAdjacentRange(sampleType, project, oldPlasmidMin, oldPlasmidMax, null, oldFragmentMin - 1,
                    "fragment_size_max", newFragmentMin - 1);
        }
        if (newFragmentMax != null && !newFragmentMax.equals(oldFragmentMax)) {
            // 后一条记录的 min = oldFragmentMax + 1，更新为 newFragmentMax + 1
            updateAdjacentRange(sampleType, project, oldPlasmidMin, oldPlasmidMax, oldFragmentMax + 1, null,
                    "fragment_size_min", newFragmentMax + 1);
        }
    }

    /**
     * 更新相邻记录的范围
     */
    private void updateAdjacentRange(String sampleType, String project,
                                     Integer plasmidMin, Integer plasmidMax,
                                     Integer fragmentMin, Integer fragmentMax,
                                     String field, Integer newValue) {
        LambdaQueryWrapper<PriceConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(PriceConfig::getGroupId)
                .eq(PriceConfig::getSampleType, sampleType)
                .eq(PriceConfig::getProject, project);

        // 质粒长度条件
        if (plasmidMin != null) {
            wrapper.eq(PriceConfig::getPlasmidLengthMin, plasmidMin);
        }
        if (plasmidMax != null) {
            wrapper.eq(PriceConfig::getPlasmidLengthMax, plasmidMax);
        }

        // 片段大小条件
        if (fragmentMin != null) {
            wrapper.eq(PriceConfig::getFragmentSizeMin, fragmentMin);
        }
        if (fragmentMax != null) {
            wrapper.eq(PriceConfig::getFragmentSizeMax, fragmentMax);
        }

        PriceConfig adjacent = priceConfigMapper.selectOne(wrapper);
        if (adjacent != null) {
            switch (field) {
                case "plasmid_length_min" -> adjacent.setPlasmidLengthMin(newValue);
                case "plasmid_length_max" -> adjacent.setPlasmidLengthMax(newValue);
                case "fragment_size_min" -> adjacent.setFragmentSizeMin(newValue);
                case "fragment_size_max" -> adjacent.setFragmentSizeMax(newValue);
            }
            adjacent.setUpdateBy(SecurityUtils.getUsername());
            adjacent.setUpdateTime(LocalDateTime.now());
            priceConfigMapper.updateById(adjacent);
            log.info("联动更新相邻价格范围: id={}, field={}, newValue={}", adjacent.getId(), field, newValue);
        }
    }

}
