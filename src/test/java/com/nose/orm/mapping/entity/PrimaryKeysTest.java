package com.nose.orm.mapping.entity;

import com.nose.model.User;
import com.nose.orm.mapping.Entity;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.junit.MatcherAssert.assertThat;

/**
 * Test the primary key class
 * Created by Daniel on 28.01.2016.
 */
public class PrimaryKeysTest {


    @Test
    public void testPrimaryKey() throws IllegalAccessException, InstantiationException {
        Entity userEntity = Entity.factory(User.class);
        final PrimaryKey primaryKey = new PrimaryKey(userEntity.getProperty("id"), userEntity.getProperty("lastName"));
        assertThat(primaryKey.getProperties().size(), is(equalTo(2)));
        assertThat(primaryKey.getProperties(), containsInAnyOrder(userEntity.getProperty("id"), userEntity.getProperty("lastName")));

        assertThat(primaryKey.getProperty("id"), is(equalTo(userEntity.getProperty("id"))));
        assertThat(primaryKey.getProperty("lastName"), is(equalTo(userEntity.getProperty("lastName"))));
        assertThat(primaryKey.getProperty("nonExtistingProperty"), is(nullValue()));
    }

}
