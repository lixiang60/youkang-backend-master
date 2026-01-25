# 查询模板失败样品列表 API 文档

**基础路径**: `/order/sample`
**说明**: SampleInfoController 自动生成文档

## 目录
- [查询样品信息列表](#查询样品信息列表)
- [导入样品信息](#导入样品信息)
- [获取样品详情](#获取样品详情)
- [新增样品](#新增样品)
- [修改样品](#修改样品)
- [删除样品](#删除样品)
- [获取模板列表](#获取模板列表)
- [添加模板板号和孔号](#添加模板板号和孔号)
- [单个添加模板孔号](#单个添加模板孔号)
- [排版忽略](#排版忽略)
- [获取特定板号剩余孔数量](#获取特定板号剩余孔数量)
- [获取模板生产列表](#获取模板生产列表)
- [设置模板状态](#设置模板状态)
- [设置原浓度以及添加版号孔号](#设置原浓度以及添加版号孔号)
- [退回](#退回)

---

### 1. 查询样品信息列表
- **路径**: `/list` (完整: `/order/sample/list`)
- **方法**: `POSTMAPPING`
- **说明**: 分页查询样品信息列表
- **权限**: `order:sample:list`
- **请求参数**: `[SampleQueryReq](#samplequeryreq)`
- **响应结构**: `R<SampleInfoResp>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
{
  "pageNum": 0,
  "pageSize": 0,
  "orderId": "string",
  "orderHistory": "string",
  "sampleId": "string",
  "sampleType": "string",
  "primer": "string",
  "primerType": "string",
  "project": "string",
  "carrierName": "string",
  "antibioticType": "string",
  "testResult": "string",
  "performance": "string",
  "returnState": "string",
  "flowName": "string",
  "plateNo": "string",
  "belongCompany": "string",
  "produceCompany": "string",
  "produceId": 0,
  "createUser": "string"
}
```

---

### 2. 导入样品信息
- **路径**: `/import` (完整: `/order/sample/import`)
- **方法**: `POSTMAPPING`
- **说明**: 从Excel文件导入样品信息
- **权限**: `order:sample:import`
- **响应结构**: `R<String>` (分页时外层为 PageResp，数据位于 data.rows)

---

### 3. 获取样品详情
- **路径**: `/{sampleId}` (完整: `/order/sample/{sampleId}`)
- **方法**: `GETMAPPING`
- **说明**: 根据样品ID获取样品详细信息
- **权限**: `order:sample:query`
- **响应结构**: `R<SampleResp>` (分页时外层为 PageResp，数据位于 data.rows)

**响应数据结构示例 JSON (Data 节点内)**:
```json
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
  "createUser": "string",
  "createTime": "string",
  "updateUser": "string",
  "updateTime": "string",
  "remark": "string"
}
```

---

### 4. 新增样品
- **路径**: `` (完整: `/order/sample`)
- **方法**: `POSTMAPPING`
- **说明**: 新增样品信息
- **权限**: `order:sample:add`
- **请求参数**: `[SampleItemReq](#sampleitemreq)`
- **响应结构**: `R<Void>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
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
```

---

### 5. 修改样品
- **路径**: `` (完整: `/order/sample`)
- **方法**: `PUTMAPPING`
- **说明**: 修改样品信息
- **权限**: `order:sample:edit`
- **请求参数**: `[SampleUpdateReq](#sampleupdatereq)`
- **响应结构**: `R<Void>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
{
  "orderId": "string",
  "orderHistory": "string",
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
```

---

### 6. 删除样品
- **路径**: `/{sampleIds}` (完整: `/order/sample/{sampleIds}`)
- **方法**: `DELETEMAPPING`
- **说明**: 批量删除样品信息
- **权限**: `order:sample:remove`
- **响应结构**: `R<Void>` (分页时外层为 PageResp，数据位于 data.rows)

---

### 7. 获取模板列表
- **路径**: `/template/list` (完整: `/order/sample/template/list`)
- **方法**: `POSTMAPPING`
- **说明**: 分页获取模板列表
- **权限**: `order:sample:template:list`
- **请求参数**: `[SampleTemplateQueryReq](#sampletemplatequeryreq)`
- **响应结构**: `R<SampleInfoResp>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
{
  "pageNum": 0,
  "pageSize": 0,
  "orderId": "string",
  "sampleId": "string",
  "customerName": "string",
  "returnState": "string"
}
```

---

### 8. 添加模板板号和孔号
- **路径**: `/template/updateTemplateNo` (完整: `/order/sample/template/updateTemplateNo`)
- **方法**: `POSTMAPPING`
- **说明**: 添加模板板号
- **权限**: `order:sample:template:update`
- **请求参数**: `[SampleTemplateUpdateReq](#sampletemplateupdatereq)`
- **响应结构**: `R<Void>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
{
  "templateInfo": [
    {}
  ],
  "templateStype": "string",
  "templatePlateNo": "string",
  "templateHoleNo": "string",
  "remark": "string"
}
```

---

### 9. 单个添加模板孔号
- **路径**: `/template/updateTemplateHoleNo` (完整: `/order/sample/template/updateTemplateHoleNo`)
- **方法**: `POSTMAPPING`
- **说明**: 添加模板孔号
- **权限**: `order:sample:template:update`
- **请求参数**: `[HoleNoUpdateReq](#holenoupdatereq)`
- **响应结构**: `R<Void>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
{
  "orderId": "string",
  "sampleId": "string",
  "templatePlateNo": "string",
  "templateHoleNo": "string",
  "remark": "string"
}
```

---

### 10. 排版忽略
- **路径**: `/template/ignoreTemp` (完整: `/order/sample/template/ignoreTemp`)
- **方法**: `GETMAPPING`
- **说明**: 排版忽略
- **权限**: `order:sample:template:ignoreTemp`
- **请求参数**: `[SampleTemplateUpdateReq](#sampletemplateupdatereq)`
- **响应结构**: `R<Void>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
{
  "templateInfo": [
    {}
  ],
  "templateStype": "string",
  "templatePlateNo": "string",
  "templateHoleNo": "string",
  "remark": "string"
}
```

---

### 11. 获取特定板号剩余孔数量
- **路径**: `/template/getHoleNum` (完整: `/order/sample/template/getHoleNum`)
- **方法**: `GETMAPPING`
- **说明**: 获取特定板号剩余孔数量
- **权限**: `order:sample:template:getHoleNum`
- **响应结构**: `R<Integer>` (分页时外层为 PageResp，数据位于 data.rows)

---

### 12. 获取模板生产列表
- **路径**: `/template/produce/list` (完整: `/order/sample/template/produce/list`)
- **方法**: `POSTMAPPING`
- **说明**: 分页获取模板列表
- **权限**: `order:sample:template`
- **请求参数**: `[TemplateProduceQueryReq](#templateproducequeryreq)`
- **响应结构**: `R<SampleInfoResp>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
{
  "pageNum": 0,
  "pageSize": 0,
  "templateNo": "string",
  "orderId": "string",
  "customerName": "string",
  "sampleId": "string",
  "sampleType": "string",
  "createUser": "string"
}
```

---

### 13. 设置模板状态
- **路径**: `/template/produce/tempStatus` (完整: `/order/sample/template/produce/tempStatus`)
- **方法**: `POSTMAPPING`
- **说明**: 设置模板状态
- **权限**: `order:sample:template`
- **请求参数**: `[TemplateProduceUpdateReq](#templateproduceupdatereq)`
- **响应结构**: `R<?>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
{
  "produceIdList": [
    "<Long>"
  ],
  "returnState": "string",
  "remark": "string"
}
```

---

### 14. 设置原浓度以及添加版号孔号
- **路径**: `/template/produce/originConcentration` (完整: `/order/sample/template/produce/originConcentration`)
- **方法**: `POSTMAPPING`
- **说明**: 设置原浓度以及添加版号孔号
- **权限**: `order:sample:template`
- **请求参数**: `[OriginConcentrationUpdateReq](#originconcentrationupdatereq)`
- **响应结构**: `R<?>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
{
  "produceIdList": [
    "<Long>"
  ],
  "originConcentration": "string",
  "templateStype": "string",
  "plateNo": "string",
  "remark": "string"
}
```

---

### 15. 退回
- **路径**: `/template/produce/sendBack` (完整: `/order/sample/template/produce/sendBack`)
- **方法**: `POSTMAPPING`
- **说明**: 模板生产退回
- **权限**: `order:sample:template`
- **请求参数**: `[TemplateProduceUpdateReq](#templateproduceupdatereq)`
- **响应结构**: `R<?>` (分页时外层为 PageResp，数据位于 data.rows)

**请求示例 JSON**:
```json
{
  "produceIdList": [
    "<Long>"
  ],
  "returnState": "string",
  "remark": "string"
}
```

---

## 附录：数据结构 (DTO)

#### HoleNoUpdateReq

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| orderId | `String` | 订单号 |
| sampleId | `String` | 样品编号 |
| templatePlateNo | `String` | 模板板号 |
| templateHoleNo | `String` | 模板孔号 |
| remark | `String` | 备注 |

#### OriginConcentrationUpdateReq

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| produceIdList | `List<Long>` | 生产编号 |
| originConcentration | `String` | 原浓度 |
| templateStype | `String` | 排版方式-横排；竖排 |
| plateNo | `String` | 板号 |
| remark | `String` | 备注 |

#### SampleItemReq

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
| remark | `String` | 备注 |

#### SampleQueryReq

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| pageNum | `Integer` | (继承自 PageReq) 页码 (默认 1) |
| pageSize | `Integer` | (继承自 PageReq) 每页数量 (默认 96) |
| orderId | `String` | 订单号 |
| orderHistory | `String` | 历史订单号 |
| sampleId | `String` | 样品编号 |
| sampleType | `String` | 样品类型 |
| primer | `String` | 引物 |
| primerType | `String` | 引物类型 |
| project | `String` | 测序项目 |
| carrierName | `String` | 载体名称 |
| antibioticType | `String` | 抗生素类型 |
| testResult | `String` | 是否测通 |
| performance | `String` | 完成情况 |
| returnState | `String` | 返回状态 |
| flowName | `String` | 流程名称 |
| plateNo | `String` | 板号 |
| belongCompany | `String` | 所属公司 |
| produceCompany | `String` | 生产公司 |
| produceId | `Long` | 生产编号 |
| createUser | `String` | 创建人 |

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
| createUser | `String` | 创建人 |
| createTime | `String` | 创建时间 |
| updateUser | `String` | 更新人 |
| updateTime | `String` | 更新时间 |
| remark | `String` | 备注 |

#### SampleTemplateQueryReq

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| pageNum | `Integer` | (继承自 PageReq) 页码 (默认 1) |
| pageSize | `Integer` | (继承自 PageReq) 每页数量 (默认 96) |
| orderId | `String` | 订单号 |
| sampleId | `String` | 样品编号 |
| customerName | `String` | 客户名称 |
| returnState | `String` | 返回状态 |

#### SampleTemplateUpdateReq

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| templateInfo | `List<TemplateInfoReq>` | 模板ID |
| templateStype | `String` | 排版方式-横排；竖排 |
| templatePlateNo | `String` | 模板板号 |
| templateHoleNo | `String` | 模板孔号 |
| remark | `String` | 备注 |

#### SampleUpdateReq

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| orderId | `String` | 订单号 |
| orderHistory | `String` | 历史订单号 |
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
| remark | `String` | 备注 |

#### TemplateProduceQueryReq

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| pageNum | `Integer` | (继承自 PageReq) 页码 (默认 1) |
| pageSize | `Integer` | (继承自 PageReq) 每页数量 (默认 96) |
| templateNo | `String` | 模板板号 |
| orderId | `String` | 订单号 |
| customerName | `String` | 客户名称 |
| sampleId | `String` | 样品编号 |
| sampleType | `String` | 样品类型 |
| createUser | `String` | 创建人 |

#### TemplateProduceUpdateReq

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| produceIdList | `List<Long>` | 订单号 |
| returnState | `String` | 返回状态 |
| remark | `String` | 备注 |

