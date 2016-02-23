package com.nose.utils.hamcrest.matchers.entity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by Daniel on 16.02.2016.
 */
public class Property {


    /**
     * Hamcrest matcher
     *
     * @param name
     * @return
     */
    public static Matcher<com.nose.orm.mapping.entity.Property> hasPropertyName(final String name) {
        return new TypeSafeMatcher<com.nose.orm.mapping.entity.Property>() {
            @Override
            public boolean matchesSafely(final com.nose.orm.mapping.entity.Property property) {
                return property.getName().equals(name);
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
    public static Matcher<com.nose.orm.mapping.entity.Property> hasTableName(final String name) {
        return new TypeSafeMatcher<com.nose.orm.mapping.entity.Property>() {
            @Override
            public boolean matchesSafely(final com.nose.orm.mapping.entity.Property property) {
                return property.getTableName().equals(name);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("getTable should return ").appendValue(name);
            }
        };
    }

    /**
     * Hamcrest matcher
     *
     * @param name
     * @return
     */
    public static Matcher<com.nose.orm.mapping.entity.Property> hasColumnName(final String name) {
        return new TypeSafeMatcher<com.nose.orm.mapping.entity.Property>() {
            @Override
            public boolean matchesSafely(final com.nose.orm.mapping.entity.Property property) {
                return property.getColumnName().equals(name);
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
    public static Matcher<com.nose.orm.mapping.entity.Property> transcient() {
        return new TypeSafeMatcher<com.nose.orm.mapping.entity.Property>() {
            @Override
            public boolean matchesSafely(final com.nose.orm.mapping.entity.Property property) {
                return property.isTranscient();
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("transcient should be true");
            }
        };
    }
}
