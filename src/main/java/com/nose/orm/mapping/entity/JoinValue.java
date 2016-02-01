package com.nose.orm.mapping.entity;

/**
 * A join value
 * Created by Daniel on 31.01.2016.
 */
public class JoinValue extends Join {

    protected String value;

    protected JoinValue(String targetTable, String targetColumn, String value) {
        super(targetTable, targetColumn);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
