package com.zjkj.provider.annotation;

import com.zjkj.provider.fancy.FancyProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Repeatable(value = FancyApis.class)
public @interface FancyApi {
    String group() default "";
    Class<? extends FancyProvider.Factory> cla();
    String alias() default "";
    String path() default "";
}
