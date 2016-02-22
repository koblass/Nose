package com.nose.orm.mapping.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 11.02.2016.
 */
public class Order {

    private String columnName;
    private Direction direction;

    public Order(String columnName, Direction direction) {
        this.columnName = columnName;
        this.direction = direction;
    }

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

    /**
     * Creates a list of orders from the given array of annotations
     * @param orderAnnotations
     * @return
     */
    public static List<Order> create(com.nose.orm.mapping.annotation.Order[] orderAnnotations) {
        List<Order> orders = new ArrayList<Order>(orderAnnotations.length);
        for (com.nose.orm.mapping.annotation.Order orderAnnotation : orderAnnotations) {
            orders.add(Order.create(orderAnnotation));
        }
        return orders;
    }

    /**
     * Creates a order from the given annotation
     * @param orderAnnotation
     * @return
     */
    public static Order create(com.nose.orm.mapping.annotation.Order orderAnnotation) {
        return new Order(orderAnnotation.column(), orderAnnotation.direction());
    }

}
