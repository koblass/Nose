package com.nose.utils.hamcrest.matchers.entity;

import com.nose.orm.mapping.entity.Direction;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by Daniel on 23.02.2016.
 */
public class Order {


    /**
     * Hamcrest matcher
     */
    public static Matcher<com.nose.orm.mapping.entity.Order> order(final String columnName, final Direction direction) {

        return new TypeSafeMatcher<com.nose.orm.mapping.entity.Order>() {
            @Override
            public boolean matchesSafely(final com.nose.orm.mapping.entity.Order model) {
                return  model.getColumnName().equals(columnName) &&
                        model.getDirection().equals(direction);
            }

            @Override
            public void describeTo(final Description desc) {
                desc.appendText("columnName ").appendValue(columnName).appendText(" and direction ").appendValue(direction);
            }
        };
    }
}
