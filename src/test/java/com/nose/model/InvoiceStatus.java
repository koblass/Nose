package com.nose.model;

/**
 * Created by Daniel on 29.03.2016.
 */
public enum InvoiceStatus {

    OPEN(0),
    PAID(1);

    private int value;

    InvoiceStatus(int value) {
        this.value = value;
    }

    public String toString(){
        return Integer.toString(value);
    }
}
