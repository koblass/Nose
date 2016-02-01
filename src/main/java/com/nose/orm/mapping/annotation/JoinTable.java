package com.nose.orm.mapping.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The join table
 * This annotation represents the join between two entites
 * Created by Daniel on 31.01.2016
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinTable {
    /**
     * The table name
     * @return
     */
    String name();

    /**
     * The join conditions between the source entity and the "pivot" table
     * @return
     */
    Join[] joins() default {};

    /**
     * The join conditions between the "pivot" table and the target entity
     * @return
     */
    Join[] inverseJoins() default {};
}
