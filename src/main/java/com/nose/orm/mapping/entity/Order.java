package com.nose.orm.mapping.entity;

/**
 * Created by Daniel on 11.02.2016.
 */
public class Order {

    private String columnName;
    private Direction direction;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
