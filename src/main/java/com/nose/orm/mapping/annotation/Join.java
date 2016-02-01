package com.nose.orm.mapping.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The join condition between two entities
 *
 * Created by Daniel on 31.01.2016
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Join {
    /**
     * The name of the target table
     * @return
     */
    String targetTable();

    /**
     * The name of the target column
     * @return
     */
    String targetColumn();

    /**
     * The name of the source column
     * @return
     */
    String sourceColumn() default "";

    /**
     * A value can be used instead of a sourceColumn.
     * @return
     */
    String value() default "";
}
