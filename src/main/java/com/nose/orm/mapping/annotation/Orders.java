package com.nose.orm.mapping.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The joins
 * This annotation represents a list of orders
 * Created by Daniel on 31.01.2016
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Orders {
    Order[] value() default {};
}
