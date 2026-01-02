package com.youkang.system.domain.resp.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 客户信息响应
 *
 * @author youkang
 */
@Schema(description = "客户信息响应")
@Data
public class CustomerResp {

    @Schema(description = "客户ID", example = "1")
    private Integer id;

    @Schema(description = "客户姓名", example = "张三")
    private String customerName;

    @Schema(description = "课题组ID", example = "1")
    private Integer subjectGroupId;

    @Schema(description = "课题组名称", example = "课题组A")
    private String subjectGroupName;

    @Schema(description = "地区", example = "北京")
    private String region;

    @Schema(description = "地址", example = "北京市朝阳区XX路XX号")
    private String address;

    @Schema(description = "电话", example = "13888888888")
    private String phone;

    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

    @Schema(description = "微信ID", example = "wx_zhangsan")
    private String wechatId;

    @Schema(description = "客户等级编码", example = "VIP")
    private String customerLevel;

    @Schema(description = "客户等级名称", example = "VIP客户")
    private String customerLevelName;

    @Schema(description = "状态编码", example = "1")
    private String status;

    @Schema(description = "状态名称", example = "正常")
    private String statusName;

    @Schema(description = "销售员", example = "李四")
    private String salesPerson;

    @Schema(description = "客户单位", example = "XX科技公司")
    private String customerUnit;

    @Schema(description = "结算方式编码", example = "monthly")
    private String paymentMethod;

    @Schema(description = "结算方式名称", example = "月结")
    private String paymentMethodName;

    @Schema(description = "发票种类", example = "增值税专用发票")
    private String invoiceType;

    @Schema(description = "备注", example = "重要客户")
    private String remarks;

    @Schema(description = "所属公司", example = "有康科技")
    private String company;

    @Schema(description = "创建者", example = "admin")
    private String createBy;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者", example = "admin")
    private String updateBy;

    @Schema(description = "更新时间")
    private Date updateTime;
}
