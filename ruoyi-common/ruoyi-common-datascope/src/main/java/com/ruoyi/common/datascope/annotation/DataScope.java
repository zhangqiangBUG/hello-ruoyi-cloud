package com.ruoyi.common.datascope.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限过滤注解
 * 
 * @author ruoyi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope
{
    /**
     * 部门表的别名|部门的字段
     */
    public String deptAlias() default "";

    /**
     * 用户表的别名|用户的字段
     */
    public String userAlias() default "";

    /**
     * 权限字符（用于多个角色匹配符合要求的权限）默认根据权限注解@RequiresPermissions获取，多个权限用逗号分隔开来
     */
    public String permission() default "";

    /**
     * 用于区别是否为system子服务还是其他子服务，因为system子服务数据里包含了dept、user表，所以可以使用默认的数据过滤方式，其他子服务可能会另外创建数据库，导致无法与dept、user进行关联过滤，因此增加此情况下的处理方式
     */
    public boolean subModule() default false;
}
