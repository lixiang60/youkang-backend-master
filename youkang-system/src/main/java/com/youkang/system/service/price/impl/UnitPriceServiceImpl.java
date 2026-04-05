package com.youkang.system.service.price.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.system.domain.UnitPrice;
import com.youkang.system.mapper.UnitPriceMapper;
import com.youkang.system.service.price.IUnitPriceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课题组测序单价Service实现
 *
 * @author youkang
 */
@Service
public class UnitPriceServiceImpl extends ServiceImpl<UnitPriceMapper, UnitPrice> implements IUnitPriceService {

    @Override
    public UnitPrice getByGroupId(Integer groupId) {
        return this.getById(groupId);
    }

    @Override
    public void saveOrUpdateUnitPrice(Integer groupId, BigDecimal unitPrice, String username) {
        UnitPrice existing = this.getById(groupId);
        LocalDateTime now = LocalDateTime.now();
        if (existing != null) {
            existing.setUnitPrice(unitPrice);
            existing.setUpdateBy(username);
            existing.setUpdateTime(now);
            this.updateById(existing);
        } else {
            UnitPrice newPrice = new UnitPrice();
            newPrice.setGroupId(groupId);
            newPrice.setUnitPrice(unitPrice);
            newPrice.setCreateBy(username);
            newPrice.setCreateTime(now);
            newPrice.setUpdateBy(username);
            newPrice.setUpdateTime(now);
            this.save(newPrice);
        }
    }
}
