package com.nose.orm.mapping.entity;

/**
 * A join performed on a column
 * Created by Daniel on 31.01.2016.
 */
public class JoinColumn extends Join {

    protected String targetTable;

    protected String targetColumn;

    protected String sourceColumn;

    protected JoinColumn(String targetTable, String targetColumn, String sourceColumn) {
        this.targetTable = targetTable;
        this.targetColumn = targetColumn;
        this.sourceColumn = sourceColumn;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public String getTargetColumn() {
        return targetColumn;
    }

    public String getSourceColumn() {
        return sourceColumn;
    }
}
