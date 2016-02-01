package com.nose.model;

import com.nose.orm.mapping.annotation.Entity;
import lombok.Data;

/**
 * Created by Daniel on 29.01.2016.
 */
@Entity
@Data
public class OrderItem {

    private Long id;

    private String quantity;

    private String name;

    private String description;

    private Long price;
}
