# 订单管理 API 接口文档

**基础路径**: `http://localhost:3564`

---

## 目录

- [一、订单信息管理](#一订单信息管理)
  - [1.1 查询订单列表](#11-查询订单列表)
  - [1.2 新增订单](#12-新增订单)
  - [1.3 通过订单新增样品](#13-通过订单新增样品)
  - [1.4 通过订单批量新增样品](#14-通过订单批量新增样品)
  - [1.5 导出订单](#15-导出订单)
  - [1.6 导入订单](#16-导入订单)
  - [1.7 下载导入模板](#17-下载导入模板)
  - [1.8 获取订单详情](#18-获取订单详情)
  - [1.9 修改订单](#19-修改订单)
  - [1.10 删除订单](#110-删除订单)

- [二、样品信息管理](#二样品信息管理)
  - [2.1 查询样品列表](#21-查询样品列表)
  - [2.2 导出样品](#22-导出样品)
  - [2.3 导入样品](#23-导入样品)
  - [2.4 下载导入模板](#24-下载导入模板)
  - [2.5 获取样品详情](#25-获取样品详情)
  - [2.6 新增样品](#26-新增样品)
  - [2.7 修改样品](#27-修改样品)
  - [2.8 删除样品](#28-删除样品)

- [三、模板排版管理](#三模板排版管理)
  - [3.1 获取模板列表](#31-获取模板列表)
  - [3.2 添加模板板号](#32-添加模板板号)
  - [3.3 添加模板孔号](#33-添加模板孔号)
  - [3.4 排版忽略](#34-排版忽略)
  - [3.5 模板BDT](#35-模板bdt)
  - [3.6 获取剩余孔数量](#36-获取剩余孔数量)

- [四、模板生产管理](#四模板生产管理)
  - [4.1 获取模板生产列表](#41-获取模板生产列表)
  - [4.2 设置模板状态](#42-设置模板状态)

---

## 一、订单信息管理

### 基础路径

```
/order/info
```

---

### 1.1 查询订单列表

**接口描述**: 分页查询订单信息列表

**请求方式**: `POST`

**接口路径**: `/order/info/list`

**权限要求**: `order:info:list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| orderId | String | 否 | 订单ID |
| customerId | Integer | 否 | 客户ID |
| groupId | Integer | 否 | 课题组ID |
| orderType | String | 否 | 订单类型 |
| isAsync | Integer | 否 | 是否同步（0:同步康为，1:不同步） |
| generation | Integer | 否 | 测序代数（1代：1，4代：4） |
| belongCompany | String | 否 | 所属公司 |
| produceCompany | String | 否 | 生产公司 |
| genNo | String | 否 | 关联基因号 |
| createBy | String | 否 | 创建者 |

**请求示例**:

```json
{
  "pageNum": 1,
  "pageSize": 10,
  "customerId": 1,
  "orderType": "公司录入"
}
```

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "rows": [
      {
        "orderId": "20260317143052051",
        "customerId": 1,
        "customerName": "张三",
        "customerAddress": "北京市",
        "groupId": 1,
        "groupName": "课题组A",
        "generation": 1,
        "orderType": "公司录入",
        "isAsync": 1,
        "belongCompany": "有康科技",
        "produceCompany": "生产公司A",
        "genNo": "GEN001",
        "remark": "备注信息",
        "createBy": "admin",
        "createTime": "2026-03-17 14:30:52"
      }
    ],
    "total": 100
  }
}
```

---

### 1.2 新增订单

**接口描述**: 新增订单信息，自动生成订单号

**请求方式**: `POST`

**接口路径**: `/order/info/addOrder`

**权限要求**: `order:info:add`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| customerInfo | Object | 是 | 客户信息 |
| customerInfo.customerId | Integer | 是 | 客户ID |
| customerInfo.customerName | String | 是 | 客户姓名 |
| customerInfo.subjectGroupId | Integer | 是 | 课题组ID |
| customerInfo.email | String | 否 | 客户邮箱 |
| orderId | String | 否 | 订单ID（为空自动生成） |
| isEmail | Integer | 否 | 是否发送邮件（1:发送） |
| belongCompany | String | 否 | 所属公司 |
| produceCompany | String | 否 | 生产公司 |
| templateType | Integer | 否 | 订单模板类型（1:excel文件;2:集合） |
| sampleInfoList | Array | 否 | 样品列表 |
| genNo | String | 否 | 关联基因号 |
| remark | String | 否 | 备注 |
| generation | Integer | 否 | 测序代数 |

**请求示例**:

```json
{
  "customerInfo": {
    "customerId": 1,
    "customerName": "张三",
    "subjectGroupId": 1,
    "email": "test@example.com"
  },
  "isEmail": 1,
  "belongCompany": "有康科技",
  "produceCompany": "生产公司A",
  "generation": 1,
  "genNo": "GEN001",
  "remark": "测试订单",
  "sampleInfoList": [
    {
      "sampleId": "S001",
      "sampleType": "质粒",
      "primer": "M13F",
      "project": "Sanger测序"
    }
  ]
}
```

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

---

### 1.3 通过订单新增样品

**接口描述**: 为指定订单新增单个样品

**请求方式**: `POST`

**接口路径**: `/order/info/addSample`

**权限要求**: 无

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | String | 是 | 订单号 |
| sampleId | String | 是 | 样品编号 |
| sampleType | String | 否 | 样品类型 |
| primer | String | 否 | 引物 |
| project | String | 否 | 测序项目 |
| （其他字段见 SampleItemReq） | | | |

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

---

### 1.4 通过订单批量新增样品

**接口描述**: 为指定订单批量新增样品

**请求方式**: `POST`

**接口路径**: `/order/info/batchAddSample`

**权限要求**: 无

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sampleList | Array | 是 | 样品列表（SampleItemReq数组） |

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

---

### 1.5 导出订单

**接口描述**: 导出订单信息列表为Excel文件

**请求方式**: `POST`

**接口路径**: `/order/info/export`

**权限要求**: `order:info:export`

**请求参数**: OrderQueryReq（同1.1）

**响应**: Excel文件流

---

### 1.6 导入订单

**接口描述**: 从Excel文件导入订单信息

**请求方式**: `POST`

**接口路径**: `/order/info/import`

**权限要求**: `order:info:import`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| file | MultipartFile | 是 | Excel文件 |
| updateSupport | Boolean | 否 | 是否更新已存在的数据，默认false |

**响应示例**:

```json
{
  "code": 200,
  "msg": "恭喜您，数据已全部导入成功！共 10 条，数据如下：<br/>1、订单 20260317143052051 导入成功..."
}
```

---

### 1.7 下载导入模板

**接口描述**: 下载订单信息导入Excel模板

**请求方式**: `POST`

**接口路径**: `/order/info/importTemplate`

**响应**: Excel模板文件流

---

### 1.8 获取订单详情

**接口描述**: 根据订单ID获取订单详细信息

**请求方式**: `GET`

**接口路径**: `/order/info/{orderId}`

**权限要求**: `order:info:query`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | String | 是 | 订单ID |

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "orderId": "20260317143052051",
    "customerId": 1,
    "customerName": "张三",
    "groupId": 1,
    "groupName": "课题组A",
    "generation": 1,
    "orderType": "公司录入",
    "isAsync": 1,
    "belongCompany": "有康科技",
    "produceCompany": "生产公司A",
    "genNo": "GEN001",
    "remark": "备注信息",
    "createBy": "admin",
    "createTime": "2026-03-17 14:30:52"
  }
}
```

---

### 1.9 修改订单

**接口描述**: 修改订单信息

**请求方式**: `PUT`

**接口路径**: `/order/info`

**权限要求**: `order:info:edit`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | String | 是 | 订单ID |
| customerId | Integer | 否 | 客户ID |
| groupId | Integer | 否 | 课题组ID |
| orderType | String | 否 | 订单类型 |
| isAsync | Integer | 否 | 是否同步 |
| generation | Integer | 否 | 测序代数 |
| belongCompany | String | 否 | 所属公司 |
| produceCompany | String | 否 | 生产公司 |
| genNo | String | 否 | 关联基因号 |
| remark | String | 否 | 备注 |

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

---

### 1.10 删除订单

**接口描述**: 批量删除订单信息

**请求方式**: `DELETE`

**接口路径**: `/order/info/{orderIds}`

**权限要求**: `order:info:remove`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderIds | String[] | 是 | 订单ID数组（逗号分隔） |

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

---

## 二、样品信息管理

### 基础路径

```
/order/sample
```

---

### 2.1 查询样品列表

**接口描述**: 分页查询样品信息列表

**请求方式**: `POST`

**接口路径**: `/order/sample/list`

**权限要求**: `order:sample:list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| orderId | String | 否 | 订单号 |
| orderHistory | String | 否 | 历史订单号 |
| sampleId | String | 否 | 样品编号 |
| sampleType | String | 否 | 样品类型 |
| primer | String | 否 | 引物 |
| primerType | String | 否 | 引物类型 |
| project | String | 否 | 测序项目 |
| carrierName | String | 否 | 载体名称 |
| antibioticType | String | 否 | 抗生素类型 |
| testResult | String | 否 | 是否测通 |
| performance | String | 否 | 完成情况 |
| returnState | Integer | 否 | 返回状态 |
| flowName | String | 否 | 流程名称 |
| plateNo | String | 否 | 板号 |
| belongCompany | String | 否 | 所属公司 |
| produceCompany | String | 否 | 生产公司 |
| createUser | String | 否 | 创建人 |

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "rows": [
      {
        "orderId": "20260317143052051",
        "orderHistory": "",
        "sampleId": "S001",
        "sampleType": "质粒",
        "samplePosition": "A1",
        "primer": "M13F",
        "primerType": "通用引物",
        "primerPosition": "正向",
        "primerConcentration": "10μM",
        "seq": "ATCG...",
        "project": "Sanger测序",
        "carrierName": "pUC19",
        "antibioticType": "氨苄青霉素",
        "plasmidLength": "2686",
        "fragmentSize": "1000",
        "testResult": "是",
        "originConcentration": "100ng/μL",
        "templatePlateNo": "P001",
        "templateHoleNo": "A1",
        "performance": "模板排版",
        "returnState": 0,
        "flowName": "模板排版",
        "plateNo": "P001",
        "holeNo": "A1",
        "belongCompany": "有康科技",
        "produceCompany": "生产公司A",
        "holeNumber": 1,
        "layout": "横排",
        "createUser": "admin",
        "createTime": "2026-03-17 14:30:52",
        "remark": "备注"
      }
    ],
    "total": 50
  }
}
```

---

### 2.2 导出样品

**接口描述**: 导出样品信息列表为Excel文件

**请求方式**: `POST`

**接口路径**: `/order/sample/export`

**权限要求**: `order:sample:export`

**响应**: Excel文件流

---

### 2.3 导入样品

**接口描述**: 从Excel文件导入样品信息

**请求方式**: `POST`

**接口路径**: `/order/sample/import`

**权限要求**: `order:sample:import`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| file | MultipartFile | 是 | Excel文件 |
| updateSupport | Boolean | 否 | 是否更新已存在的数据 |

---

### 2.4 下载导入模板

**接口描述**: 下载样品信息导入Excel模板

**请求方式**: `POST`

**接口路径**: `/order/sample/importTemplate`

**响应**: Excel模板文件流

---

### 2.5 获取样品详情

**接口描述**: 根据样品ID获取样品详细信息

**请求方式**: `GET`

**接口路径**: `/order/sample/{sampleId}`

**权限要求**: `order:sample:query`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sampleId | String | 是 | 样品ID |

---

### 2.6 新增样品

**接口描述**: 新增样品信息

**请求方式**: `POST`

**接口路径**: `/order/sample`

**权限要求**: `order:sample:add`

**请求参数**: SampleItemReq（包含样品所有字段）

---

### 2.7 修改样品

**接口描述**: 修改样品信息

**请求方式**: `PUT`

**接口路径**: `/order/sample`

**权限要求**: `order:sample:edit`

**请求参数**: SampleUpdateReq（sampleId为必填）

---

### 2.8 删除样品

**接口描述**: 批量删除样品信息

**请求方式**: `DELETE`

**接口路径**: `/order/sample/{sampleIds}`

**权限要求**: `order:sample:remove`

**路径参数**: sampleIds - 样品ID数组

---

## 三、模板排版管理

### 基础路径

```
/order/sample/template
```

---

### 3.1 获取模板列表

**接口描述**: 分页获取模板排版列表

**请求方式**: `POST`

**接口路径**: `/order/sample/template/list`

**权限要求**: `order:sample:template:list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| orderId | String | 否 | 订单号 |
| sampleId | String | 否 | 样品编号 |
| customerName | String | 否 | 客户名称 |
| returnState | Integer | 否 | 返回状态 |

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "rows": [
      {
        "orderId": "20260317143052051",
        "templateNumber": "5",
        "customerName": "张三",
        "customerAddress": "北京市",
        "sampleId": "S001",
        "sampleType": "质粒",
        "primer": "M13F",
        "primerConcentration": "10μM",
        "carrierName": "pUC19",
        "antibioticType": "氨苄青霉素",
        "fragmentSize": "1000",
        "testResult": "是",
        "originConcentration": "100ng/μL",
        "templatePlateNo": "P001",
        "templateHoleNo": "A1",
        "performance": "模板排版",
        "returnState": 0,
        "flowName": "模板排版",
        "createUser": "admin",
        "remark": "备注"
      }
    ],
    "total": 20
  }
}
```

---

### 3.2 添加模板板号

**接口描述**: 为样品批量分配模板板号和孔号

**请求方式**: `POST`

**接口路径**: `/order/sample/template/updateTemplateNo`

**权限要求**: `order:sample:template:update`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| templateInfo | Array | 是 | 模板信息列表 |
| templateInfo[].orderId | String | 是 | 订单号 |
| templateInfo[].sampleId | String | 是 | 样品编号 |
| templateStype | String | 是 | 排版方式（横排/竖排） |
| templatePlateNo | String | 是 | 模板板号 |
| remark | String | 否 | 备注 |

**请求示例**:

```json
{
  "templateInfo": [
    {"orderId": "20260317143052051", "sampleId": "S001"},
    {"orderId": "20260317143052051", "sampleId": "S002"}
  ],
  "templateStype": "横排",
  "templatePlateNo": "P001",
  "remark": "备注"
}
```

---

### 3.3 添加模板孔号

**接口描述**: 为指定样品分配模板孔号

**请求方式**: `POST`

**接口路径**: `/order/sample/template/updateTemplateHoleNo`

**权限要求**: `order:sample:template:update`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | String | 是 | 订单号 |
| sampleId | String | 是 | 样品编号 |
| templatePlateNo | String | 是 | 模板板号 |
| templateHoleNo | String | 是 | 模板孔号 |

---

### 3.4 排版忽略

**接口描述**: 排版忽略（更新样品备注和流程）

**请求方式**: `GET`

**接口路径**: `/order/sample/template/ignoreTemp`

**权限要求**: `order:sample:template:ignoreTemp`

**请求参数**: SampleTemplateUpdateReq（Body方式）

**注意**: 此接口使用GET但接收Body参数

---

### 3.5 模板BDT

**接口描述**: 获取模板BDT信息

**请求方式**: `POST`

**接口路径**: `/order/sample/template/templateBDT`

**权限要求**: `order:sample:template:update`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| templateNo | String | 是 | 模板编号 |

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "orderId": "20260317143052051"
    }
  ]
}
```

---

### 3.6 获取剩余孔数量

**接口描述**: 获取指定板号的剩余孔数量

**请求方式**: `GET`

**接口路径**: `/order/sample/template/getHoleNum`

**权限要求**: `order:sample:template:getHoleNum`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| templateNo | String | 是 | 板号 |

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": 96
}
```

---

## 四、模板生产管理

### 基础路径

```
/order/sample/template/produce
```

---

### 4.1 获取模板生产列表

**接口描述**: 分页获取模板生产列表

**请求方式**: `POST`

**接口路径**: `/order/sample/template/produce/list`

**权限要求**: `order:sample:template`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| templateNo | String | 否 | 模板板号 |
| orderId | String | 否 | 订单号 |
| customerName | String | 否 | 客户名称 |
| sampleId | String | 否 | 样品编号 |
| sampleType | String | 否 | 样品类型 |
| createUser | String | 否 | 创建人 |

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "rows": [
      {
        "orderId": "20260317143052051",
        "customerName": "张三",
        "customerAddress": "北京市",
        "sampleId": "S001",
        "sampleType": "质粒",
        "primer": "M13F",
        "primerConcentration": "10μM",
        "carrierName": "pUC19",
        "antibioticType": "氨苄青霉素",
        "fragmentSize": "1000",
        "testResult": "是",
        "originConcentration": "100ng/μL",
        "templatePlateNo": "P001",
        "templateHoleNo": "A1",
        "performance": "模板排版",
        "returnState": 0,
        "flowName": "模板排版",
        "originHoleNo": "A1",
        "createUser": "admin",
        "remark": "备注"
      }
    ],
    "total": 30
  }
}
```

---

### 4.2 设置模板状态

**接口描述**: 批量设置模板样品的返回状态

**请求方式**: `POST`

**接口路径**: `/order/sample/template/produce/tempStatus`

**权限要求**: `order:sample:template`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Array | 是 | 订单号列表 |
| returnState | String | 是 | 返回状态 |
| remark | String | 否 | 备注 |

**请求示例**:

```json
{
  "orderId": ["20260317143052051", "20260317143052052"],
  "returnState": "1",
  "remark": "已完成"
}
```

---

## 附录

### 通用响应结构

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 响应码（200:成功，其他:失败） |
| msg | String | 响应消息 |
| data | Object/Array | 响应数据 |

### 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 401 | 未授权 |
| 403 | 无权限 |
| 500 | 服务器错误 |

### 请求头说明

| 请求头 | 说明 | 示例 |
|--------|------|------|
| Authorization | 认证Token | Bearer {token} |
| Content-Type | 内容类型 | application/json |

---

*文档生成时间: 2026-03-17*
