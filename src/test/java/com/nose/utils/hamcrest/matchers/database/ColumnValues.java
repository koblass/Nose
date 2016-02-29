package com.nose.utils.hamcrest.matchers.database;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by Daniel on 29.02.2016.
 */
public class ColumnValues {


    /**
     * Hamcrest matcher
     */
    public static Matcher<com.nose.orm.database.ColumnValues> hasEntry(final String columnName, final Matcher<Iterable<String>> valuesMatcher) {

        return new TypeSafeMatcher<com.nose.orm.database.ColumnValues>() {
            @Override
            public boolean matchesSafely(final com.nose.orm.database.ColumnValues model) {
                return  model.containsKey(columnName) &&
                        valuesMatcher.matches(model.get(columnName));
            }

            @Override
            public void describeTo(final Description desc) {
                desc.appendText("columnName ").appendValue(columnName).appendDescriptionOf(valuesMatcher);
            }
        };
    }
}
