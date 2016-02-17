package com.nose.orm.mapping.annotation;

import com.nose.orm.mapping.entity.Direction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The order annotation
 *
 * Created by Daniel on 11.02.2016
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
    /**
     * The column name
     * @return
     */
    String column();

    /**
     * The direction
     * @return
     */
    Direction direction();
}
