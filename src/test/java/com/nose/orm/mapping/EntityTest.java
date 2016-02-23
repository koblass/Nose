package com.nose.orm.mapping;

import com.nose.model.Address;
import com.nose.model.User;
import com.nose.orm.adapter.Default;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static com.nose.utils.hamcrest.matchers.entity.Property.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

/**
 * Created by Daniel on 02.11.15.
 */
public class EntityTest {

    Entity userEntity;
    Entity addressEntity;

    @Before
    public void setUp() throws Exception {
        userEntity = Entity.factory(User.class);
        addressEntity = Entity.factory(Address.class);
    }

    @Test
    public void testObjectFactory() throws IllegalAccessException, InstantiationException {
        assertThat(userEntity.getProperty("id"), is(notNullValue()));
        assertThat(userEntity.getProperty("id"), hasPropertyName("id"));
        assertThat(userEntity.getProperty("id"), hasTableName("user"));
        assertThat(userEntity.getProperty("id"), hasColumnName("id"));
    }

    @Test
    public void testFactory() {
        assertThat(userEntity, is(notNullValue()));
        assertThat(addressEntity, is(notNullValue()));
    }

    @Test
    public void testGetCls() throws Exception {
        assertThat(userEntity.getCls(), CoreMatchers.<Class>equalTo(User.class));
    }

    @Test
    public void testGetTableName() throws Exception {
        assertThat(userEntity.getTableName(), is(equalTo("user")));
        assertThat(addressEntity.getTableName(), is(equalTo("personal_address")));
    }

    @Test
    public void testGetAdapter() throws Exception {
        assertThat(userEntity.getAdapter(), instanceOf(Default.class));
    }

    @Test
    public void testGetProperties() throws Exception {
        assertThat(userEntity.getProperties().size(), is(equalTo(9)));
        assertThat(userEntity.getProperties(), containsInAnyOrder(
                hasPropertyName("id"),
                hasPropertyName("firstName"),
                hasPropertyName("lastName"),
                hasPropertyName("birthDate"),
                hasPropertyName("lastModificationDate"),
                hasPropertyName("address"),
                hasPropertyName("invoices"),
                hasPropertyName("lastAccess"),
                hasPropertyName("roles")
        ));
    }

    @Test
    public void testHasProperty() throws Exception {
        assertThat(userEntity.hasProperty("id"), is(true));
        assertThat(userEntity.hasProperty("lastName"), is(true));
        assertThat(userEntity.hasProperty("nonExistingProperty"), is(false));
    }

    @Test
    public void testGetProperty() throws Exception {
        assertThat(userEntity.getProperty("lastName"), hasPropertyName("lastName"));
        assertThat(userEntity.getProperty("lastName"), hasTableName("user"));
        assertThat(userEntity.getProperty("lastName"), hasColumnName("last_name"));
        assertThat(userEntity.getProperty("nonExistingProperty"), is(nullValue()));
    }

    @Test
    public void testGetPropertyFromColumnName() throws Exception {
        assertThat(userEntity.getPropertyFromColumnName("last_name"), hasPropertyName("lastName"));
        assertThat(userEntity.getPropertyFromColumnName("nonExistingProperty"), is(nullValue()));
    }

    @Test
    public void testGetPrimaryKeys() throws Exception {
        assertThat(userEntity.getPrimaryKeys(), is(notNullValue()));
        assertThat(userEntity.getPrimaryKeys().getProperties().size(), is(1));
        assertThat(userEntity.getPrimaryKeys().getProperties(), containsInAnyOrder(hasPropertyName("id")));
    }
}
