package com.youkang.system.service.customer;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youkang.system.domain.CustomerSubjectGroup;
import com.youkang.system.domain.req.CustomerUpdateReq;
import com.youkang.system.domain.req.DeleteReq;
import com.youkang.system.domain.req.customer.CustomerSubjectGroupReq;
import com.youkang.system.domain.resp.customer.CustomerSubjectGroupResp;

/**
 * 客户科目组关系Service接口
 *
 * 继承 IService 后自动拥有以下方法：
 * - save(T entity)：插入一条记录
 * - saveBatch(Collection<T> entityList)：批量插入
 * - saveOrUpdate(T entity)：存在更新记录，否则插入
 * - remove(Wrapper<T> queryWrapper)：根据条件删除记录
 * - removeById(Serializable id)：根据ID删除
 * - removeByIds(Collection<? extends Serializable> idList)：批量删除
 * - update(Wrapper<T> updateWrapper)：根据条件更新
 * - updateById(T entity)：根据ID更新
 * - updateBatchById(Collection<T> entityList)：批量更新
 * - getById(Serializable id)：根据ID查询
 * - listByIds(Collection<? extends Serializable> idList)：批量查询
 * - list()：查询所有
 * - list(Wrapper<T> queryWrapper)：条件查询列表
 * - page(IPage<T> page)：分页查询
 * - page(IPage<T> page, Wrapper<T> queryWrapper)：条件分页查询
 * - count()：查询总记录数
 * - count(Wrapper<T> queryWrapper)：条件查询总记录数
 *
 * @author youkang
 * @date 2025-11-25
 */
public interface ICustomerSubjectGroupService extends IService<CustomerSubjectGroup> {

    void add(CustomerSubjectGroup customerSubjectGroup);
    void update(CustomerUpdateReq req);
    void delete(DeleteReq req);
    Page<CustomerSubjectGroupResp> query(CustomerSubjectGroupReq req);
}
