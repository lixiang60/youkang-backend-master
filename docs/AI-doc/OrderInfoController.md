# 订单信息管理 API 文档

**基础路径**: `/order/info`
**说明**: 订单信息的增删改查操作

## 目录
- [查询订单信息列表](#查询订单信息列表)
- [新增订单](#新增订单)
- [通过订单新增样品](#通过订单新增样品)
- [通过订单批量新增样品](#通过订单批量新增样品)
- [导入订单信息](#导入订单信息)
- [获取订单详情](#获取订单详情)
- [修改订单](#修改订单)
- [删除订单](#删除订单)
- [范围查询订单](#范围查询订单)
- [查询订单及样品](#查询订单及样品)

---

### 1. 查询订单信息列表
- **路径**: `/list` (完整: `/order/info/list`)
- **方法**: `POSTMAPPING`
- **说明**: 分页查询订单信息列表
- **权限**: `order:info:list`
- **请求参数**: `[OrderQueryReq](#orderqueryreq)`
- **响应结构**: `R<OrderResp>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
{
  "pageNum": 0,
  "pageSize": 0,
  "orderId": "string",
  "customerId": 0,
  "groupId": 0,
  "orderType": "string",
  "isAsync": 0,
  "generation": 0,
  "belongCompany": "string",
  "produceCompany": "string",
  "genNo": "string",
  "createBy": "string"
}
```

**响应数据结构示例 JSON (Data 节点内)**:
```json
{
  "total": 100,
  "rows": [
    {
      "orderId": "string",
      "customerId": 0,
      "customerName": "string",
      "customerAddress": "string",
      "groupId": 0,
      "groupName": "string",
      "generation": 0,
      "orderType": "string",
      "isAsync": 0,
      "belongCompany": "string",
      "produceCompany": "string",
      "genNo": "string",
      "remark": "string",
      "createBy": "string",
      "createTime": "<Date>"
    }
  ]
}
```

---

### 2. 新增订单
- **路径**: `/addOrder` (完整: `/order/info/addOrder`)
- **方法**: `POSTMAPPING`
- **说明**: 新增订单信息
- **权限**: `order:info:add`
- **请求参数**: `[OrderAddReq](#orderaddreq)`
- **响应结构**: `R<Void>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
{
  "customerInfo": {},
  "orderId": "string",
  "isEmail": 0,
  "belongCompany": "string",
  "produceCompany": "string",
  "templateType": 0,
  "sampleInfoList": [
    {
      "orderId": "string",
      "orderHistory": "string",
      "sampleId": "string",
      "sampleType": "string",
      "samplePosition": "string",
      "primer": "string",
      "primerType": "string",
      "primerPosition": "string",
      "primerConcentration": "string",
      "seq": "string",
      "project": "string",
      "carrierName": "string",
      "antibioticType": "string",
      "plasmidLength": "string",
      "fragmentSize": "string",
      "testResult": "string",
      "originConcentration": "string",
      "templatePlateNo": "string",
      "templateHoleNo": "string",
      "performance": "string",
      "returnState": "string",
      "flowName": "string",
      "plateNo": "string",
      "holeNo": "string",
      "belongCompany": "string",
      "produceCompany": "string",
      "produceId": 0,
      "holeNumber": 0,
      "layout": "string",
      "remark": "string"
    }
  ],
  "genNo": "string",
  "remark": "string",
  "generation": 0
}
```

---

### 3. 通过订单新增样品
- **路径**: `/addSample` (完整: `/order/info/addSample`)
- **方法**: `POSTMAPPING`
- **说明**: 通过订单新增样品
- **请求参数**: `[SampleAddReq](#sampleaddreq)`
- **响应结构**: `R<Void>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
{
  "orderId": "string",
  "sampleId": "string",
  "primer": "string",
  "sampleType": "string",
  "antibioticType": "string",
  "carrierName": "string",
  "fragmentSize": "string",
  "testResult": "string",
  "remark": "string",
  "produceId": 0
}
```

---

### 4. 通过订单批量新增样品
- **路径**: `/batchAddSample` (完整: `/order/info/batchAddSample`)
- **方法**: `POSTMAPPING`
- **说明**: 通过订单批量新增样品
- **请求参数**: `[SampleBatchAddReq](#samplebatchaddreq)`
- **响应结构**: `R<Void>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
{
  "sampleList": [
    {
      "orderId": "string",
      "orderHistory": "string",
      "sampleId": "string",
      "sampleType": "string",
      "samplePosition": "string",
      "primer": "string",
      "primerType": "string",
      "primerPosition": "string",
      "primerConcentration": "string",
      "seq": "string",
      "project": "string",
      "carrierName": "string",
      "antibioticType": "string",
      "plasmidLength": "string",
      "fragmentSize": "string",
      "testResult": "string",
      "originConcentration": "string",
      "templatePlateNo": "string",
      "templateHoleNo": "string",
      "performance": "string",
      "returnState": "string",
      "flowName": "string",
      "plateNo": "string",
      "holeNo": "string",
      "belongCompany": "string",
      "produceCompany": "string",
      "produceId": 0,
      "holeNumber": 0,
      "layout": "string",
      "remark": "string"
    }
  ]
}
```

---

### 5. 导入订单信息
- **路径**: `/import` (完整: `/order/info/import`)
- **方法**: `POSTMAPPING`
- **说明**: 从Excel文件导入订单信息
- **权限**: `order:info:import`
- **响应结构**: `R<String>` (分页时外层为 PageResp，数据位于 data.rows)

---

### 6. 获取订单详情
- **路径**: `/{orderId}` (完整: `/order/info/{orderId}`)
- **方法**: `GETMAPPING`
- **说明**: 根据订单ID获取订单详细信息
- **权限**: `order:info:query`
- **响应结构**: `R<OrderResp>` (分页时外层为 PageResp，数据位于 data.rows)

**响应数据结构示例 JSON (Data 节点内)**:
```json
{
  "orderId": "string",
  "customerId": 0,
  "customerName": "string",
  "customerAddress": "string",
  "groupId": 0,
  "groupName": "string",
  "generation": 0,
  "orderType": "string",
  "isAsync": 0,
  "belongCompany": "string",
  "produceCompany": "string",
  "genNo": "string",
  "remark": "string",
  "createBy": "string",
  "createTime": "<Date>"
}
```

---

### 7. 修改订单
- **路径**: `` (完整: `/order/info`)
- **方法**: `PUTMAPPING`
- **说明**: 修改订单信息
- **权限**: `order:info:edit`
- **请求参数**: `[OrderUpdateReq](#orderupdatereq)`
- **响应结构**: `R<Void>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
{
  "customerId": 0,
  "groupId": 0,
  "orderType": "string",
  "isAsync": 0,
  "generation": 0,
  "belongCompany": "string",
  "produceCompany": "string",
  "genNo": "string",
  "remark": "string"
}
```

---

### 8. 删除订单
- **路径**: `/{orderIds}` (完整: `/order/info/{orderIds}`)
- **方法**: `DELETEMAPPING`
- **说明**: 批量删除订单信息
- **权限**: `order:info:remove`
- **响应结构**: `R<Void>` (分页时外层为 PageResp，数据位于 data.rows)

---

### 9. 范围查询订单
- **路径**: `/queryByRange` (完整: `/order/info/queryByRange`)
- **方法**: `POSTMAPPING`
- **说明**: 根据订单号范围、创建时间范围等条件查询订单信息
- **权限**: `order:info:list`
- **请求参数**: `[OrderRangeQueryReq](#orderrangequeryreq)`
- **响应结构**: `R<List<OrderResp>>`

**请求示例 JSON**:
```json
{
  "startOrderId": "2025040100001",
  "endOrderId": "2025040199999",
  "createBy": "admin",
  "startTime": "2025-04-01T00:00:00",
  "endTime": "2025-04-30T23:59:59",
  "belongCompany": "有康科技",
  "produceCompany": "有康科技"
}
```

**响应数据结构示例 JSON (Data 节点内)**:
```json
[
  {
    "orderId": "20250401123456001",
    "customerId": 1,
    "customerName": "张三",
    "customerAddress": "北京市海淀区",
    "groupId": 1,
    "groupName": "基因课题组",
    "generation": 1,
    "orderType": "公司录入",
    "isAsync": 1,
    "belongCompany": "有康科技",
    "produceCompany": "有康科技",
    "genNo": "GEN001",
    "remark": "备注信息",
    "createBy": "admin",
    "createTime": "2025-04-01 12:34:56"
  }
]
```

---

### 10. 查询订单及样品
- **路径**: `/withSamples/{orderId}` (完整: `/order/info/withSamples/{orderId}`)
- **方法**: `GETMAPPING`
- **说明**: 根据订单号查询订单信息及其关联的所有样品信息
- **权限**: `order:info:query`
- **响应结构**: `R<OrderWithSamplesResp>`

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
| :--- | :--- | :--- | :--- |
| orderId | `String` | 是 | 订单号 |

**响应数据结构示例 JSON (Data 节点内)**:
```json
{
  "orderInfo": {
    "orderId": "20250401123456001",
    "customerId": 1,
    "customerName": "张三",
    "customerAddress": "北京市海淀区",
    "groupId": 1,
    "groupName": "基因课题组",
    "generation": 1,
    "orderType": "公司录入",
    "isAsync": 1,
    "belongCompany": "有康科技",
    "produceCompany": "有康科技",
    "genNo": "GEN001",
    "remark": "备注信息",
    "createBy": "admin",
    "createTime": "2025-04-01 12:34:56"
  },
  "sampleList": [
    {
      "produceId": 2504010001,
      "orderId": "20250401123456001",
      "orderHistory": null,
      "sampleId": "S001",
      "sampleType": "质粒",
      "primer": "M13F",
      "seq": "GTAAAACGACGGCCAGT",
      "primerConcentration": "10uM",
      "project": "Sanger测序",
      "carrierName": "pUC19",
      "antibioticType": "Amp",
      "plasmidLength": "3000",
      "fragmentSize": "1000",
      "testResult": "是",
      "originConcentration": "100ng/ul",
      "templatePlateNo": "TP001",
      "templateHoleNo": "A01",
      "performance": "模板排版",
      "returnState": "模板成功",
      "flowName": "反应生产",
      "plateNo": "R001",
      "holeNo": "B01",
      "belongCompany": "有康科技",
      "produceCompany": "有康科技",
      "holeNumber": 1,
      "layout": "横排",
      "createUser": "admin",
      "createTime": "2025-04-01 12:35:00",
      "remark": "加测样品",
      "reportStatus": null,
      "reimburseStatus": null,
      "customerName": "张三",
      "customerId": 1,
      "customerAddress": "北京市海淀区",
      "reportErrorReason": null,
      "sampleCorrespondId": null
    }
  ]
}
```

## 附录：数据结构 (DTO)

#### OrderAddReq

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| customerInfo | `CustomerSelectorResp` | 客户信息 |
| orderId | `String` | 订单id |
| isEmail | `Integer` | 是否发送邮件 |
| belongCompany | `String` | 所属公司 |
| produceCompany | `String` | 生产公司 |
| templateType | `Integer` | 订单模板类型,1:excel文件;2:集合 |
| sampleInfoList | `List<SampleItemReq>` | 样品列表 |
| genNo | `String` | 关联基因号 |
| remark | `String` | 备注 |
| generation | `Integer` | 测序代数 |

#### OrderQueryReq

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| pageNum | `Integer` | (继承自 PageReq) 页码 (默认 1) |
| pageSize | `Integer` | (继承自 PageReq) 每页数量 (默认 96) |
| orderId | `String` | 订单id |
| customerId | `Integer` | 客户id |
| groupId | `Integer` | 课题组id |
| orderType | `String` | 订单类型 |
| isAsync | `Integer` | 是否同步（0:同步康为，1:不同步） |
| generation | `Integer` | 测序代数（1代：1，4代：4） |
| belongCompany | `String` | 所属公司 |
| produceCompany | `String` | 生产公司 |
| genNo | `String` | 关联基因号 |
| createBy | `String` | 创建者 |

#### OrderResp

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| orderId | `String` | 订单id |
| customerId | `Integer` | 客户id |
| customerName | `String` | 客户姓名 |
| customerAddress | `String` | 客户地址 |
| groupId | `Integer` | 课题组id |
| groupName | `String` | 课题组 |
| generation | `Integer` | 测序代数 |
| orderType | `String` | 订单类型 |
| isAsync | `Integer` | 是否同步 |
| belongCompany | `String` | 所属公司 |
| produceCompany | `String` | 生产公司 |
| genNo | `String` | 关联基因号 |
| remark | `String` | 备注 |
| createBy | `String` | 创建者 |
| createTime | `Date` | 创建时间 |

#### OrderUpdateReq

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| customerId | `Integer` | 客户id |
| groupId | `Integer` | 课题组id |
| orderType | `String` | 订单类型 |
| isAsync | `Integer` | 是否同步（0:同步康为，1:不同步） |
| generation | `Integer` | 测序代数（1代：1，4代：4） |
| belongCompany | `String` | 所属公司 |
| produceCompany | `String` | 生产公司 |
| genNo | `String` | 关联基因号 |
| remark | `String` | 备注 |

#### SampleAddReq

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| orderId | `String` | 订单ID |
| sampleId | `String` | 样品编号 |
| primer | `String` | 测序引物 |
| sampleType | `String` | 样品类型 |
| antibioticType | `String` | 抗生素类型 |
| carrierName | `String` | 载体名称 |
| fragmentSize | `String` | 片段大小 |
| testResult | `String` | 是否测通 |
| remark | `String` | 备注 |
| produceId | `Long` | 生产编号 |

#### SampleBatchAddReq

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| sampleList | `List<SampleItemReq>` | 样品列表 |

#### OrderRangeQueryReq

| 字段 | 类型 | 必填 | 说明 |
| :--- | :--- | :--- | :--- |
| startOrderId | `String` | 否 | 开始订单号 |
| endOrderId | `String` | 否 | 结束订单号 |
| createBy | `String` | 否 | 创建人 |
| startTime | `LocalDateTime` | 否 | 创建开始时间 |
| endTime | `LocalDateTime` | 否 | 创建结束时间 |
| belongCompany | `String` | 否 | 所属公司 |
| produceCompany | `String` | 否 | 生产公司 |

#### OrderWithSamplesResp

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| orderInfo | `OrderResp` | 订单信息 |
| sampleList | `List<SampleResp>` | 样品信息列表 |

#### SampleResp

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| orderId | `String` | 订单号 |
| orderHistory | `String` | 历史订单号 |
| sampleId | `String` | 样品编号 |
| sampleType | `String` | 样品类型 |
| samplePosition | `String` | 样品位置 |
| primer | `String` | 引物 |
| primerType | `String` | 引物类型 |
| primerPosition | `String` | 引物位置 |
| primerConcentration | `String` | 引物浓度 |
| seq | `String` | 序列 |
| project | `String` | 测序项目 |
| carrierName | `String` | 载体名称 |
| antibioticType | `String` | 抗生素类型 |
| plasmidLength | `String` | 质粒长度 |
| fragmentSize | `String` | 片段大小 |
| testResult | `String` | 是否测通 |
| originConcentration | `String` | 原浓度 |
| templatePlateNo | `String` | 模板板号 |
| templateHoleNo | `String` | 模板孔号 |
| performance | `String` | 完成情况 |
| returnState | `String` | 返回状态 |
| flowName | `String` | 流程名称 |
| plateNo | `String` | 板号 |
| holeNo | `String` | 孔号 |
| belongCompany | `String` | 所属公司 |
| produceCompany | `String` | 生产公司 |
| produceId | `Long` | 生产编号 |
| holeNumber | `Integer` | 孔号数量 |
| layout | `String` | 排版方式 |
| originHoleNo | `String` | 原孔号 |
| belongLab | `String` | 所属实验室 |
| createUser | `String` | 创建人 |
| createTime | `String` | 创建时间 |
| updateUser | `String` | 更新人 |
| updateTime | `String` | 更新时间 |
| remark | `String` | 备注 |
| reportStatus | `String` | 报告状态 |
| reimburseStatus | `String` | 返还状态 |
| customerName | `String` | 客户姓名 |
| customerId | `Long` | 客户id |
| customerAddress | `String` | 客户地址 |
| reportErrorReason | `String` | 报告异常原因 |
| sampleCorrespondId | `Long` | 样品对应号 |

