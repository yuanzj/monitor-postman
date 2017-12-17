package net.rokyinfo.ops.monitorman.annotation;

import java.lang.annotation.*;

/**
 * 持久化数据到文件注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PersistenceData2File {

    String value() default "";
}
