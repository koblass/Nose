package com.nose.orm.mapping.annotation;

import com.nose.orm.adapter.Default;
import com.nose.orm.adapter.IAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Daniel on 02.11.15.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name() default "";
    String table() default "";
    Class<? extends IAdapter> adapter() default Default.class;
}
