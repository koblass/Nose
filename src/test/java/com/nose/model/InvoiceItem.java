package com.nose.model;

import com.nose.orm.mapping.annotation.Entity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Daniel on 29.01.2016.
 */
@Entity
@Data
public class InvoiceItem {

    private Long id;

    private int quantity;

    private String name;

    private String description;

    private BigDecimal price;
}
