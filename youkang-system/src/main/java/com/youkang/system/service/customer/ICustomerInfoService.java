package com.youkang.system.service.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youkang.system.domain.CustomerInfo;

/**
 * 客户信息Service接口
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
 * @date 2025-11-20
 */
public interface ICustomerInfoService extends IService<CustomerInfo>
{
    /**
     * 分页查询客户信息列表
     *
     * @param customerInfo 查询条件（包含分页参数）
     * @return 分页结果
     */
    IPage<CustomerInfo> queryPage(CustomerInfo customerInfo);
}
