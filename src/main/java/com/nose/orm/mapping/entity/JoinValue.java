package com.nose.orm.mapping.entity;

/**
 * A join value
 * Created by Daniel on 31.01.2016.
 */
public class JoinValue extends Join {

    protected String targetTable;

    protected String targetColumn;

    protected String value;

    protected JoinValue(String targetTable, String targetColumn, String value) {
        this.targetTable = targetTable;
        this.targetColumn = targetColumn;
        this.value = value;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public String getTargetColumn() {
        return targetColumn;
    }

    public String getValue() {
        return value;
    }
}
