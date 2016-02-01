package com.nose.model;

import com.nose.orm.mapping.annotation.Entity;
import com.nose.orm.mapping.annotation.Id;
import lombok.Data;

/**
 * Created by Daniel on 31.01.2016
 */
@Entity(table = "security_role")
@Data
public class Role {

    @Id
    private String name;
    private String description;
}
