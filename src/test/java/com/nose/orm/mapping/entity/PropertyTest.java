package com.nose.orm.mapping.entity;


import com.nose.model.Address;
import com.nose.model.Order;
import com.nose.model.User;
import com.nose.orm.mapping.Entity;
import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
        assertThat(userEntity.getProperty("age"), hasColumnName("age"));
        assertThat(userEntity.getProperty("lastName"), hasColumnName("last_name"));
        assertThat(userEntity.getProperty("birthDate"), hasColumnName("birth"));

        assertThat(addressEntity.getProperty("country"), hasColumnName("name"));
    }

    @Test
    public void testGetTableName() throws Exception {
        assertThat(userEntity.getProperty("id"), hasTableName("user"));
        assertThat(userEntity.getProperty("firstName"), hasTableName("user"));
        assertThat(userEntity.getProperty("address"), hasTableName("personal_address"));

        assertThat(addressEntity.getProperty("country"), hasTableName("country"));
    }

    @Test
    public void testIsTranscient() throws Exception {
        assertThat(userEntity.getProperty("lastName"), is(not(transcient())));
        assertThat(userEntity.getProperty("lastAccessDate"), is(transcient()));
    }

    @Test
    public void testIsEntity() throws Exception {
        assertThat(userEntity.getProperty("id").isEntity(), is(false));
        assertThat(userEntity.getProperty("address").isEntity(), is(true));
        assertThat(userEntity.getProperty("orders").isEntity(), is(false));
    }

    @Test
    public void testGetType() throws Exception {
        assertThat(userEntity.getProperty("id").getType(), CoreMatchers.<Class>equalTo(Long.class));
        assertThat(userEntity.getProperty("address").getType(), CoreMatchers.<Class>equalTo(Address.class));
        assertThat(userEntity.getProperty("orders").getType(), CoreMatchers.<Class>equalTo(Order.class));
    }

    @Test
    public void testIsList() {
        assertThat(userEntity.getProperty("id").isList(), is(false));
        assertThat(userEntity.getProperty("orders").isList(), is(true));
        assertThat(userEntity.getProperty("roles").isList(), is(true));
    }

    @Test
    public void testGetJoins() {
        Property addressProperty = userEntity.getProperty("address");
        assertThat(addressProperty.getJoins().size(), is(equalTo(1)));
        assertThat(((JoinColumn)addressProperty.getJoins().get(0)).getSourceColumn(), is(equalTo("address_id")));
        assertThat(addressProperty.getJoins().get(0).getTargetTable(), is(equalTo("address")));
        assertThat(addressProperty.getJoins().get(0).getTargetColumn(), is(equalTo("id")));

        Property countryProperty = addressEntity.getProperty("country");
        assertThat(countryProperty.getJoins().size(), is(equalTo(2)));
        JoinColumn firstJoin = (JoinColumn)countryProperty.getJoins().get(0);
        assertThat(firstJoin.getTargetTable(), is(equalTo("country")));
        assertThat(firstJoin.getTargetColumn(), is(equalTo("code")));
        assertThat(firstJoin.getSourceColumn(), is(equalTo("country_code")));
        JoinValue secondJoin = (JoinValue)countryProperty.getJoins().get(1);
        assertThat(secondJoin.getTargetTable(), is(equalTo("country")));
        assertThat(secondJoin.getTargetColumn(), is(equalTo("language")));
        assertThat(secondJoin.getValue(), is(equalTo("fr_CH")));
    }



    /**
     * Hamcrest matcher
     *
     * @param name
     * @return
     */
    public static Matcher<Property> hasPropertyName(final String name) {
        return new BaseMatcher<Property>() {
            @Override
            public boolean matches(final Object item) {
                return ((Property) item).getName().equals(name);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("getName should return ").appendValue(name);
            }
        };
    }

    /**
     * Hamcrest matcher
     *
     * @param name
     * @return
     */
    public static Matcher<Property> hasTableName(final String name) {
        return new BaseMatcher<Property>() {
            @Override
            public boolean matches(final Object item) {
                return ((Property) item).getTableName().equals(name);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("getTableName should return ").appendValue(name);
            }
        };
    }

    /**
     * Hamcrest matcher
     *
     * @param name
     * @return
     */
    public static Matcher<Property> hasColumnName(final String name) {
        return new BaseMatcher<Property>() {
            @Override
            public boolean matches(final Object item) {
                return ((Property) item).getColumnName().equals(name);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("getColumnName should return ").appendValue(name);
            }
        };
    }

    /**
     * Hamcrest matcher
     *
     * @return
     */
    public static Matcher<Property> transcient() {
        return new BaseMatcher<Property>() {
            @Override
            public boolean matches(final Object item) {
                return ((Property) item).isTranscient();
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("transcient should be true");
            }
        };
    }
}