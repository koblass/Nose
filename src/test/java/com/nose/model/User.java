package com.nose.model;

import com.nose.orm.mapping.annotation.*;
import lombok.Data;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Daniel on 02.11.15.
 */
@Entity
@Data
public class User {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private Long age;

    @Column(name = "birth")
    private String birthDate;

    @Join(sourceColumn = "address_id", targetTable = "address", targetColumn = "id")
    private Address address;

    @Join(sourceColumn = "id", targetTable = "order", targetColumn = "user_id")
    private Collection<Order> orders;

    @Transcient
    private Date lastAccessDate;

    @JoinTable(
            name = "user_role",
            joins = @Join(sourceColumn = "id", targetTable = "user_role", targetColumn = "user_id"),
            inverseJoins = @Join(sourceColumn = "role_id", targetTable = "role", targetColumn = "id")
    )
    private Role[] roles;
}
