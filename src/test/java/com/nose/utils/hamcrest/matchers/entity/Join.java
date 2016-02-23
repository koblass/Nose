package com.nose.utils.hamcrest.matchers.entity;

import com.nose.orm.mapping.entity.JoinColumn;
import com.nose.orm.mapping.entity.JoinTable;
import com.nose.orm.mapping.entity.JoinValue;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


/**
 * Created by Daniel on 17.02.2016.
 */
public class Join {


    /**
     * The join column matcher
     *
     * @param sourceColumn
     * @param targetTable
     * @param targetColumn
     * @return
     */
    public static Matcher<com.nose.orm.mapping.entity.Join> joinColumn(final String sourceColumn, final String targetTable, final String targetColumn) {
        return new TypeSafeMatcher<com.nose.orm.mapping.entity.Join>() {
            @Override
            public boolean matchesSafely(final com.nose.orm.mapping.entity.Join join) {
                JoinColumn joinColumn = (JoinColumn) join;
                return joinColumn.getSourceColumn().equals(sourceColumn) &&
                        joinColumn.getTargetTable().equals(targetTable) &&
                        joinColumn.getTargetColumn().equals(targetColumn);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("sourceColummn is ").appendValue(sourceColumn);
                description.appendText(" targetTable is ").appendValue(targetTable);
                description.appendText(" targetColumn is ").appendValue(targetColumn);
            }
        };
    }


    /**
     * The join column matcher
     *
     * @param targetTable
     * @param targetColumn
     * @param value
     * @return
     */
    public static Matcher<com.nose.orm.mapping.entity.Join> joinValue(final String targetTable, final String targetColumn, final String value) {
        return new TypeSafeMatcher<com.nose.orm.mapping.entity.Join>() {
            @Override
            public boolean matchesSafely(final com.nose.orm.mapping.entity.Join join) {
                JoinValue joinValue = (JoinValue) join;
                return joinValue.getTargetTable().equals(targetTable) &&
                        joinValue.getTargetColumn().equals(targetColumn) &&
                        joinValue.getValue().equals(value);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("targetTable is ").appendValue(targetTable);
                description.appendText(" targetColumn is ").appendValue(targetColumn);
                description.appendText(" value is ").appendValue(value);
            }
        };
    }

    /**
     * @param table
     * @param joinsMatcher
     * @param inverseJoinsMatcher
     * @return
     */
    public static Matcher<com.nose.orm.mapping.entity.Join> joinTable(final String table, final Matcher<Iterable<? extends com.nose.orm.mapping.entity.Join>> joinsMatcher, final Matcher<Iterable<? extends com.nose.orm.mapping.entity.Join>> inverseJoinsMatcher) {
        return new TypeSafeMatcher<com.nose.orm.mapping.entity.Join>() {

            @Override
            public boolean matchesSafely(final com.nose.orm.mapping.entity.Join join) {
                JoinTable joinTable = (JoinTable) join;
                return joinTable.getTable().equals(table) &&
                        joinsMatcher.matches(joinTable.getJoins()) &&
                        inverseJoinsMatcher.matches(joinTable.getInverseJoins());
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("table is ").appendValue(table).appendText(" and joins ")
                        .appendDescriptionOf(joinsMatcher).appendText(" and inverseJoins ").appendDescriptionOf(inverseJoinsMatcher);
            }
        };
    }
}
