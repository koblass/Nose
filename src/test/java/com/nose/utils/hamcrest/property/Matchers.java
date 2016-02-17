package com.nose.utils.hamcrest.property;

import com.nose.orm.mapping.entity.Property;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by Daniel on 16.02.2016.
 */
public class Matchers {


    /**
     * Hamcrest matcher
     *
     * @param name
     * @return
     */
    public static Matcher<Property> hasPropertyName(final String name) {
        return new TypeSafeMatcher<Property>() {
            @Override
            public boolean matchesSafely(final Property property) {
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
    public static Matcher<Property> hasTableName(final String name) {
        return new TypeSafeMatcher<Property>() {
            @Override
            public boolean matchesSafely(final Property property) {
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
    public static Matcher<Property> hasColumnName(final String name) {
        return new TypeSafeMatcher<Property>() {
            @Override
            public boolean matchesSafely(final Property property) {
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
    public static Matcher<Property> transcient() {
        return new TypeSafeMatcher<Property>() {
            @Override
            public boolean matchesSafely(final Property property) {
                return property.isTranscient();
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("transcient should be true");
            }
        };
    }
}
