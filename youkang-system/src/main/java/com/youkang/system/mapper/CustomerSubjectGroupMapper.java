package com.youkang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youkang.system.domain.CustomerSubjectGroup;
import com.youkang.system.domain.req.customer.CustomerSubjectGroupReq;
import com.youkang.system.domain.resp.customer.CustomerSubjectGroupResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 客户科目组关系Mapper接口
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
 * @date 2025-11-25
 */
@Mapper
public interface CustomerSubjectGroupMapper extends BaseMapper<CustomerSubjectGroup> {

    Page<CustomerSubjectGroupResp> query(Page<CustomerSubjectGroupResp> page, @Param("req") CustomerSubjectGroupReq req);
}
