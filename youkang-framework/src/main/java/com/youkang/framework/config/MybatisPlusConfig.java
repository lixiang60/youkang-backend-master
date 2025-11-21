package com.youkang.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置类
 *
 * 功能说明：
 * 1. 配置分页插件 - 实现物理分页，无需手动编写分页SQL
 * 2. 配置乐观锁插件 - 防止并发修改数据时的数据覆盖问题
 * 3. 配置防全表更新删除插件 - 防止误操作导致全表数据被更新或删除
 *
 * @author youkang
 * @date 2025-11-20
 */
@Configuration
public class MybatisPlusConfig
{
    /**
     * MyBatis Plus 拦截器配置
     *
     * 拦截器是 MyBatis Plus 的核心功能入口，所有的插件都需要通过拦截器来注册
     *
     * @return MybatisPlusInterceptor 拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor()
    {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // ========== 1. 分页插件 ==========
        // 作用：自动在SQL后面拼接 LIMIT 语句，实现物理分页
        // 优点：比 PageHelper 性能更好，不需要额外配置
        // 使用场景：所有需要分页的查询
        // 示例SQL：SELECT * FROM user WHERE status = 1 LIMIT 10, 10
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);

        // 设置请求的页面大于最大页后的操作
        // true: 返回首页  false: 继续请求（默认）
        paginationInterceptor.setOverflow(false);

        // 单页分页条数限制，默认无限制
        // 建议设置合理值，防止一次查询过多数据导致内存溢出
        paginationInterceptor.setMaxLimit(1000L);

        interceptor.addInnerInterceptor(paginationInterceptor);

        // ========== 2. 乐观锁插件 ==========
        // 作用：在更新数据时自动比对版本号，防止并发修改导致数据覆盖
        // 原理：UPDATE table SET name='新值', version=version+1 WHERE id=1 AND version=原版本号
        // 使用场景：高并发环境下的数据更新操作
        // 要求：实体类需要有 @Version 注解的字段
        //
        // 举例说明：
        // 1. 用户A查询客户信息，version=1
        // 2. 用户B查询同一客户信息，version=1
        // 3. 用户A修改客户信息并保存，version变为2
        // 4. 用户B修改客户信息并保存，因为version不匹配（期望1实际2），更新失败
        // 5. 这样就避免了用户B的修改覆盖用户A的修改
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        // ========== 3. 防止全表更新与删除插件 ==========
        // 作用：阻止没有 WHERE 条件的 UPDATE 和 DELETE 语句执行
        // 使用场景：防止开发人员误操作导致全表数据被修改或删除
        //
        // 会被拦截的SQL：
        // - UPDATE yk_customer_info SET status = '禁用'  （没有WHERE条件）
        // - DELETE FROM yk_customer_info                  （没有WHERE条件）
        //
        // 不会被拦截的SQL：
        // - UPDATE yk_customer_info SET status = '禁用' WHERE id = 1
        // - DELETE FROM yk_customer_info WHERE id = 1
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        return interceptor;
    }

    /**
     * 字段自动填充处理器（可选）
     *
     * 如果需要自动填充创建时间、更新时间、创建人、更新人等字段，
     * 可以创建一个实现 MetaObjectHandler 接口的类
     *
     * 示例代码见文档中的 MyMetaObjectHandler
     */

    /*
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MyMetaObjectHandler();
    }
    */
}
