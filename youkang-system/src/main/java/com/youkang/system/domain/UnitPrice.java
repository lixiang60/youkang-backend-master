package com.youkang.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课题组测序单价对象 yk_unit_price
 *
 * @author youkang
 */
@Data
@TableName("yk_unit_price")
public class UnitPrice {

    /** 课题组ID（主键） */
    @TableId(value = "group_id", type = IdType.INPUT)
    private Integer groupId;

    /** 测序单价 */
    @TableField("unit_price")
    private BigDecimal unitPrice;

    /** 创建人 */
    @TableField("create_by")
    private String createBy;

    /** 创建时间 */
    @TableField("create_time")
    private LocalDateTime createTime;

    /** 更新人 */
    @TableField("update_by")
    private String updateBy;

    /** 更新时间 */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
