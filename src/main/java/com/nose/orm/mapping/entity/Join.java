package com.nose.orm.mapping.entity;

/**
 * The join representation between two entities
 * Created by Daniel on 31.01.2016.
 */
public abstract class Join {

    protected String targetTable;

    protected String targetColumn;

    public static Join create(com.nose.orm.mapping.annotation.Join joinAnnotation) {
        if (joinAnnotation.sourceColumn().isEmpty()) {
            return new JoinValue(joinAnnotation.targetTable(), joinAnnotation.targetColumn(), joinAnnotation.value());
        } else {
            return new JoinColumn(joinAnnotation.targetTable(), joinAnnotation.targetColumn(), joinAnnotation.sourceColumn());
        }
    }

    protected Join(String targetTable, String targetColumn) {
        this.targetTable = targetTable;
        this.targetColumn = targetColumn;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public String getTargetColumn() {
        return targetColumn;
    }
}
