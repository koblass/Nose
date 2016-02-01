package com.nose.orm.mapping.entity;

/**
 * A join performed on a column
 * Created by Daniel on 31.01.2016.
 */
public class JoinColumn extends Join {

    protected String sourceColumn;

    protected JoinColumn(String targetTable, String targetColumn, String sourceColumn) {
        super(targetTable, targetColumn);
        this.sourceColumn = sourceColumn;
    }

    public String getSourceColumn() {
        return sourceColumn;
    }
}
