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
public class Invoice {

    private Long id;

    private Date date;

    private InvoiceStatus status;

    @Join(sourceColumn = "id", targetTable = "invoice_item", targetColumn = "invoice_id")
    private Collection<InvoiceItem> items;
}
