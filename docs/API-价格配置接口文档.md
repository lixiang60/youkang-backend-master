# 价格配置接口文档

## 概述

价格配置模块用于管理课题组的价格信息。系统提供一个基础价格模板，每个课题组可以基于模板自定义价格。

### 核心设计

- **基础模板**：`group_id = NULL`，包含所有价格组合（约100+条）
- **课题组自定义价格**：`group_id = 课题组ID`，只存储课题组修改过的价格
- **课题组测序单价**：`yk_unit_price` 表，每个课题组一条记录，存储基础测序单价
- **查询时合并**：返回模板价格，如果课题组有自定义则覆盖；若模板 `unit_price` 为空，则从 `yk_unit_price` 表兜底取课题组单价

---

## 接口列表

### 1. 查询课题组价格列表

**请求**
```
GET /customer/priceConfig/list/{groupId}
```

**权限**
```
customer:priceConfig:list
```

**路径参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | Integer | 是 | 课题组ID |

**响应示例**
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "templateId": 1,
      "customId": null,
      "groupId": 1,
      "category": "测序",
      "chargeName": "质粒测序",
      "sampleType": "质粒",
      "project": "测序",
      "plasmidLengthMin": 0,
      "plasmidLengthMax": 3000,
      "fragmentSizeMin": 0,
      "fragmentSizeMax": 3000,
      "unitPrice": 10.00,
      "calcMethod": "按次",
      "status": 1,
      "isCustom": 0,
      "createBy": "admin",
      "createTime": "2025-01-01 10:00:00",
      "updateBy": null,
      "updateTime": null,
      "remark": null
    }
  ]
}
```

**响应字段说明**

| 字段 | 类型 | 说明 |
|------|------|------|
| templateId | Long | 模板ID |
| customId | Long | 自定义价格ID（有值表示已自定义） |
| groupId | Integer | 课题组ID |
| category | String | 类别 |
| chargeName | String | 收费名称 |
| sampleType | String | 样品类型 |
| project | String | 测序项目 |
| plasmidLengthMin | Integer | 质粒长度下限 |
| plasmidLengthMax | Integer | 质粒长度上限（null表示无上限） |
| fragmentSizeMin | Integer | 片段大小下限 |
| fragmentSizeMax | Integer | 片段大小上限（null表示无上限） |
| unitPrice | BigDecimal | 单价 |
| calcMethod | String | 计算方式 |
| status | Integer | 状态（1启用 0禁用） |
| isCustom | Integer | 是否自定义（1是 0否） |

---

### 2. 修改价格配置

**请求**
```
PUT /customer/priceConfig
```

**权限**
```
customer:priceConfig:edit
```

**请求体**
```json
{
  "id": null,
  "groupId": 1,
  "category": "测序",
  "chargeName": "质粒测序",
  "sampleType": "质粒",
  "project": "测序",
  "plasmidLengthMin": 0,
  "plasmidLengthMax": 3000,
  "fragmentSizeMin": 0,
  "fragmentSizeMax": 3000,
  "unitPrice": 12.00,
  "calcMethod": "按次",
  "status": 1,
  "remark": "调整价格"
}
```

**请求字段说明**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 否 | 自定义价格ID（修改已有自定义价格时传入） |
| groupId | Integer | 是 | 课题组ID |
| category | String | 否 | 类别 |
| chargeName | String | 否 | 收费名称 |
| sampleType | String | 是 | 样品类型 |
| project | String | 是 | 测序项目 |
| plasmidLengthMin | Integer | 否 | 质粒长度下限 |
| plasmidLengthMax | Integer | 否 | 质粒长度上限 |
| fragmentSizeMin | Integer | 否 | 片段大小下限 |
| fragmentSizeMax | Integer | 否 | 片段大小上限 |
| unitPrice | BigDecimal | 是 | 单价 |
| calcMethod | String | 是 | 计算方式 |
| status | Integer | 否 | 状态（1启用 0禁用） |
| remark | String | 否 | 备注 |

**响应示例**
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

**联动修改说明**

当修改模板的范围（质粒长度或片段大小）时，会自动联动更新相邻的记录：

- 修改 `plasmidLengthMax` → 自动更新下一条记录的 `plasmidLengthMin = newMax + 1`
- 修改 `plasmidLengthMin` → 自动更新上一条记录的 `plasmidLengthMax = newMin - 1`
- `fragmentSize` 同理（范围间隙差1）

**单价更新逻辑**

- 模板 `unit_price` **有值** → 写入 `yk_price_config` 自定义记录（原有逻辑）
- 模板 `unit_price` **为空** → 写入 `yk_unit_price` 表（课题组测序单价，一个课题组一条记录）

---

### 3. 批量修改价格配置

**请求**
```
PUT /customer/priceConfig/batch
```

**权限**
```
customer:priceConfig:edit
```

**请求体**
```json
{
  "groupId": 1,
  "items": [
    {
      "sampleType": "质粒",
      "project": "测序",
      "plasmidLengthMin": 0,
      "plasmidLengthMax": 3000,
      "fragmentSizeMin": 0,
      "fragmentSizeMax": 3000,
      "unitPrice": 12.00,
      "calcMethod": "按次",
      "status": 1
    },
    {
      "sampleType": "质粒",
      "project": "测序",
      "plasmidLengthMin": 0,
      "plasmidLengthMax": 3000,
      "fragmentSizeMin": 3000,
      "fragmentSizeMax": 20000,
      "unitPrice": 15.00,
      "calcMethod": "按次",
      "status": 1
    }
  ]
}
```

**响应示例**
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

---

### 4. 重置价格配置

**请求**
```
DELETE /customer/priceConfig/{id}
```

**权限**
```
customer:priceConfig:edit
```

**路径参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 自定义价格ID |

**响应示例**
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

**说明**

删除自定义价格后，该价格配置将恢复为模板默认值。

---

## 数据库表结构

```sql
CREATE TABLE yk_price_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    group_id INT COMMENT '课题组ID（NULL = 基础模板）',
    category VARCHAR(50) COMMENT '类别',
    charge_name VARCHAR(100) COMMENT '收费名称',
    sample_type VARCHAR(50) COMMENT '样品类型',
    project VARCHAR(50) COMMENT '测序项目',
    plasmid_length_min INT COMMENT '质粒长度下限',
    plasmid_length_max INT COMMENT '质粒长度上限（NULL = 无上限）',
    fragment_size_min INT COMMENT '片段大小下限',
    fragment_size_max INT COMMENT '片段大小上限（NULL = 无上限）',
    unit_price DECIMAL(10,2) COMMENT '单价',
    calc_method VARCHAR(50) COMMENT '计算方式',
    status TINYINT DEFAULT 1 COMMENT '状态（1启用 0禁用）',
    create_by VARCHAR(64) COMMENT '创建人',
    create_time DATETIME COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新人',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='价格配置表';

CREATE TABLE yk_unit_price (
    group_id INT PRIMARY KEY COMMENT '课题组ID（主键）',
    unit_price DECIMAL(10,2) NOT NULL COMMENT '测序单价',
    create_by VARCHAR(64) COMMENT '创建人',
    create_time DATETIME COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新人',
    update_time DATETIME COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课题组测序单价表';
```

**`yk_unit_price` 说明**

模板 `yk_price_config` 中有部分记录的 `unit_price` 为空（如 `calc_method=800` 的基础测序档位），这些记录的价格由课题组自行定价。`yk_unit_price` 表存储每个课题组的基础测序单价（一个课题组一条记录），查询时作为兜底数据源。

**查询优先级**：自定义价格 > 模板价格 > 课题组测序单价（`yk_unit_price`）

---

## 后续扩展：订单计费

订单计费时，根据以下步骤计算价格：

1. 获取订单所属的课题组ID
2. 遍历订单中的样品
3. 根据样品的 `sampleType`、`project`、`plasmidLength`、`fragmentSize` 匹配价格
4. 匹配逻辑：
   ```sql
   SELECT * FROM yk_price_config
   WHERE (group_id = #{groupId} OR group_id IS NULL)
     AND sample_type = #{sampleType}
     AND project = #{project}
     AND #{plasmidLength} >= plasmid_length_min
     AND (#{plasmidLength} < plasmid_length_max OR plasmid_length_max IS NULL)
     AND #{fragmentSize} >= fragment_size_min
     AND (#{fragmentSize} < fragment_size_max OR fragment_size_max IS NULL)
     AND status = 1
   ORDER BY group_id IS NULL  -- 优先课题组自己的
   LIMIT 1;
   ```
5. 根据计算方式（`calc_method`）计算费用
