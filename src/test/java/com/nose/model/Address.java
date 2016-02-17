package com.nose.model;

import com.nose.orm.mapping.annotation.*;
import lombok.Data;

/**
 * Created by Daniel on 02.11.15.
 */
@Entity(table = "personal_address")
@Data
public class Address {

    @Id
    private Long id;
    private String street;
    private String zip;
    private String city;

    @Column(table = "country", name = "name")
    @Joins({
        @Join(sourceColumn = "country_code", targetTable = "country", targetColumn = "code"),
        @Join(targetTable = "country", targetColumn = "language", value = "fr")
    })
    private String country;
}
