package com.nose.orm.mapping.annotation;

import java.lang.annotation.*;

/**
 * Created by Daniel on 02.11.15.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transcient {}
