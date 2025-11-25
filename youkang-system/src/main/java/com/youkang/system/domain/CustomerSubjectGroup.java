package com.youkang.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@TableName("yk_customer_subject")
@Getter
@Setter
public class CustomerSubjectGroup {
    @TableId("id")
    private Integer id;

    @TableField("customer_id")
    @NotNull
    private Integer customerId;

    @TableField("subject_group_id")
    @NotNull
    private Integer subjectGroupId;

    @TableField("remark")
    private String remark;
}
