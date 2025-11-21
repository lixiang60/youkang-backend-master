package com.youkang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youkang.system.domain.CustomerInfo;

/**
 * 客户信息Mapper接口
 *
 * 继承 BaseMapper 后自动拥有以下方法（无需 XML）：
 * - insert(T entity)：插入一条记录
 * - deleteById(Serializable id)：根据ID删除
 * - deleteBatchIds(Collection<? extends Serializable> idList)：批量删除
 * - updateById(T entity)：根据ID更新
 * - selectById(Serializable id)：根据ID查询
 * - selectBatchIds(Collection<? extends Serializable> idList)：批量查询
 * - selectList(Wrapper<T> queryWrapper)：条件查询列表
 * - selectPage(IPage<T> page, Wrapper<T> queryWrapper)：分页查询
 *
 * @author youkang
 * @date 2025-11-20
 */
public interface CustomerInfoMapper extends BaseMapper<CustomerInfo>
{
    // 继承 BaseMapper 后，基础的 CRUD 方法已经自动拥有
    // 如果需要自定义 SQL 查询，可以在这里添加方法并在 XML 中实现
}
