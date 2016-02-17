package com.nose.orm.mapping.entity;


import com.nose.model.Address;
import com.nose.model.Order;
import com.nose.model.User;
import com.nose.orm.adapter.Default;
import com.nose.orm.mapping.Entity;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static com.nose.utils.hamcrest.join.Matchers.*;
import static com.nose.utils.hamcrest.property.Matchers.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.junit.MatcherAssert.assertThat;

/**
 * Created by Daniel on 29.01.2016.
 */
public class PropertyTest {

    Entity userEntity;
    Entity addressEntity;

    @Before
    public void setUp() throws IllegalAccessException, InstantiationException {
        userEntity = Entity.factory(User.class);
        addressEntity = Entity.factory(Address.class);
    }


    @Test
    public void testGetName() throws Exception {
        assertThat(userEntity.getProperty("lastName"), hasPropertyName("lastName"));
    }

    @Test
    public void testGetColumnName() throws Exception {
        assertThat(userEntity.getProperty("lastName"), hasColumnName("last_name"));
        assertThat(userEntity.getProperty("birthDate"), hasColumnName("birth"));
        assertThat(userEntity.getProperty("lastAccess"), hasColumnName("last_access"));

        assertThat(addressEntity.getProperty("country"), hasColumnName("name"));
    }

    @Test
    public void testGetTableName() throws Exception {
        assertThat(userEntity.getProperty("id"), hasTableName("user"));
        assertThat(userEntity.getProperty("firstName"), hasTableName("user"));
        assertThat(userEntity.getProperty("address"), hasTableName("personal_address"));
        assertThat(userEntity.getProperty("lastAccess"), hasTableName("user_access"));

        assertThat(addressEntity.getProperty("country"), hasTableName("country"));
    }

    @Test
    public void testIsTranscient() throws Exception {
        assertThat(userEntity.getProperty("lastName"), is(not(transcient())));
        assertThat(userEntity.getProperty("lastModificationDate"), is(transcient()));
    }
    @Test
    public void testGetAdapter() throws Exception {
        MatcherAssert.assertThat(userEntity.getProperty("lastName").getAdapter(), instanceOf(Default.class));
    }

    @Test
    public void testIsEntity() throws Exception {
        assertThat(userEntity.getProperty("id").isEntity(), is(false));
        assertThat(userEntity.getProperty("address").isEntity(), is(true));
        assertThat(userEntity.getProperty("orders").isEntity(), is(true));
    }

    @Test
    public void testGetType() throws Exception {
        assertThat(userEntity.getProperty("id").getType(), CoreMatchers.<Class>equalTo(Long.class));
        assertThat(userEntity.getProperty("address").getType(), CoreMatchers.<Class>equalTo(Address.class));
        assertThat(userEntity.getProperty("orders").getType(), CoreMatchers.<Class>equalTo(Order.class));
        assertThat(userEntity.getProperty("lastAccess").getType(), CoreMatchers.<Class>equalTo(Date.class));
    }

    @Test
    public void testIsList() {
        assertThat(userEntity.getProperty("id").isList(), is(false));
        assertThat(userEntity.getProperty("orders").isList(), is(true));
        assertThat(userEntity.getProperty("roles").isList(), is(true));
    }

    @Test
    public void testGetJoins() {
        // The column join is tested
        Property addressProperty = userEntity.getProperty("address");
        assertThat(addressProperty.getJoins(), contains(joinColumn("address_id", "address", "id")));

        // The table join is tested
        Property rolesProperty = userEntity.getProperty("roles");
        assertThat(rolesProperty.getJoins(), contains(
                joinTable("user_role",
                    contains(
                        joinColumn("id", "user_role", "user_id")
                    ),
                    contains(
                        joinColumn("role_id", "role", "id")
                    )
                )

        ));

        // The combination of column and value joins is tested
        Property countryProperty = addressEntity.getProperty("country");
        assertThat(countryProperty.getJoins(), contains(
                joinColumn("country_code", "country", "code"),
                joinValue("country", "language", "fr")
        ));
    }

}