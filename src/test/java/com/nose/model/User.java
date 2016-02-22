package com.nose.model;

import com.nose.orm.mapping.annotation.*;
import com.nose.orm.mapping.entity.Direction;
import lombok.Data;

import java.util.Calendar;
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

    @Transcient
    private Date lastModificationDate;

    public int getAge() {
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        birthDate.setTime(this.birthDate);
        return today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR) - (today.get(Calendar.MONTH) - birthDate.get(Calendar.MONTH) < 0 ? 1 : 0);
    }

    @Column(name = "birth")
    private Date birthDate;

    @Join(sourceColumn = "address_id", targetTable = "address", targetColumn = "id")
    private Address address;

    @Join(sourceColumn = "id", targetTable = "invoice", targetColumn = "user_id")
    @Order(column = "date", direction = Direction.DESC)
    private Collection<Invoice> invoices;

    @Join(sourceColumn = "id", targetTable = "user_access", targetColumn = "user_id")
    @Column(table = "user_access")
    private Collection<Date> lastAccess;

    @JoinTable(
            name = "user_role",
            joins = @Join(sourceColumn = "id", targetTable = "user_role", targetColumn = "user_id"),
            inverseJoins = @Join(sourceColumn = "role_id", targetTable = "role", targetColumn = "id")
    )
    private Role[] roles;
}
