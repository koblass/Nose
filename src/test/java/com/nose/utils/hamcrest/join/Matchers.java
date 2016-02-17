package com.nose.utils.hamcrest.join;

import com.nose.orm.mapping.entity.Join;
import com.nose.orm.mapping.entity.JoinColumn;
import com.nose.orm.mapping.entity.JoinTable;
import com.nose.orm.mapping.entity.JoinValue;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


/**
 * Created by Daniel on 17.02.2016.
 */
public class Matchers {


    /**
     * The join column matcher
     * @param sourceColumn
     * @param targetTable
     * @param targetColumn
     * @return
     */
    public static Matcher<Join> joinColumn(final String sourceColumn, final String targetTable, final String targetColumn) {
        return new TypeSafeMatcher<Join>() {
            @Override
            public boolean matchesSafely(final Join join) {
                JoinColumn joinColumn = (JoinColumn)join;
                return joinColumn.getSourceColumn().equals(sourceColumn) &&
                        joinColumn.getTargetTable().equals(targetTable) &&
                        joinColumn.getTargetColumn().equals(targetColumn);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("getSourceColumn should return ").appendValue(sourceColumn);
                description.appendText(" and ");
                description.appendText("getTargetTable should return ").appendValue(targetTable);
                description.appendText(" and ");
                description.appendText("getTargetColumn should return ").appendValue(targetColumn);
            }
        };
    }


    /**
     * The join column matcher
     * @param targetTable
     * @param targetColumn
     * @param value
     * @return
     */
    public static Matcher<Join> joinValue(final String targetTable, final String targetColumn, final String value) {
        return new TypeSafeMatcher<Join>() {
            @Override
            public boolean matchesSafely(final Join join) {
                JoinValue joinValue = (JoinValue)join;
                return joinValue.getTargetTable().equals(targetTable) &&
                        joinValue.getTargetColumn().equals(targetColumn) &&
                        joinValue.getValue().equals(value);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("getTargetTable should return ").appendValue(targetTable);
                description.appendText(" and ");
                description.appendText("getTargetColumn should return ").appendValue(targetColumn);
                description.appendText(" and ");
                description.appendText("getValue should return ").appendValue(value);
            }
        };
    }

    /**
     *
     * @param table
     * @param joinsMatcher
     * @param inverseJoinsMatcher
     * @return
     */
    public static Matcher<Join> joinTable(final String table, final Matcher<Iterable<? extends Join>> joinsMatcher, final Matcher<Iterable<? extends Join>> inverseJoinsMatcher) {
        return new TypeSafeMatcher<Join>() {

            boolean joinsMatcherOk;
            boolean inverseJoinsMatcherOk;
            @Override
            public boolean matchesSafely(final Join join) {
                JoinTable joinTable = (JoinTable)join;
                joinsMatcherOk = joinsMatcher.matches(joinTable.getJoins());
                inverseJoinsMatcherOk = inverseJoinsMatcher.matches(joinTable.getInverseJoins());
                return joinTable.getTable().equals(table) &&
                        joinsMatcherOk &&
                        inverseJoinsMatcherOk;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("getTable should return ").appendValue(table);
                if (!joinsMatcherOk) {
                    description.appendText(" and getJoins ");
                    joinsMatcher.describeTo(description);
                }
                if (!inverseJoinsMatcherOk) {
                    description.appendText(" and getInverseJoins ");
                    inverseJoinsMatcher.describeTo(description);
                }
            }
        };
    }
}
