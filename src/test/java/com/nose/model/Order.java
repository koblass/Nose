package com.nose.model;

import com.nose.orm.mapping.annotation.Entity;
import com.nose.orm.mapping.annotation.Join;
import lombok.Data;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Daniel on 29.01.2016.
 */
@Entity
@Data
public class Order {

    private Long id;

    private Date date;

    @Join(sourceColumn = "id", targetTable = "order_item", targetColumn = "order_id")
    private Collection<OrderItem> items;
}
