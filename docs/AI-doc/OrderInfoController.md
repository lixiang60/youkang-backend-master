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

