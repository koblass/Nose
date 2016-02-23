package com.nose.orm.mapping.entity;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.List;

import static com.nose.utils.hamcrest.matchers.entity.Order.order;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Daniel on 23.02.2016.
 */
public class OrderTest {


    @Test
    public void testCreateFromOrderAnnotation() {
        assertThat(Order.create(createOrderAnnotation("column1", Direction.ASC)), is(order("column1", Direction.ASC)));
        assertThat(Order.create(createOrderAnnotation("column2", Direction.DESC)), is(order("column2", Direction.DESC)));
    }



    @Test
    public void testCreateFromOrderAnnotations() {
        List<Order> orders = Order.create(new com.nose.orm.mapping.annotation.Order[]{
                createOrderAnnotation("column1", Direction.ASC),
                createOrderAnnotation("column2", Direction.DESC)
        });
        assertThat(orders, hasItems(
                order("column1", Direction.ASC),
                order("column2", Direction.DESC)
        ));
    }



    public com.nose.orm.mapping.annotation.Order createOrderAnnotation(final String column, final Direction direction) {
        return new com.nose.orm.mapping.annotation.Order()
        {
            @Override
            public Class<? extends Annotation> annotationType() {
                return com.nose.orm.mapping.annotation.Order.class;
            }

            @Override
            public String column() {
                return column;
            }

            @Override
            public Direction direction() {
                return direction;
            }
        };
    }

}