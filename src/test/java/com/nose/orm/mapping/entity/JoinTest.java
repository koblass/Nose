package com.nose.orm.mapping.entity;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.List;

import static com.nose.utils.hamcrest.matchers.entity.Join.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Daniel on 23.02.2016.
 */
public class JoinTest {


    @Test
    public void testCreateFromJoinAnnotation() {
        assertThat(Join.create(createJoinAnnotation("targetTable", "targetColumn", "sourceColumn", null)), is(joinColumn("sourceColumn", "targetTable", "targetColumn")));
        assertThat(Join.create(createJoinAnnotation("targetTable", "targetColumn", null, "value")), is(joinValue("targetTable", "targetColumn", "value")));
    }


    @Test
    public void testCreateFromJoinAnnotations() {
        List<Join> joins = Join.create(new com.nose.orm.mapping.annotation.Join[]{
                createJoinAnnotation("targetTable", "targetColumn", "sourceColumn", null),
                createJoinAnnotation("targetTable", "targetColumn", null, "value")
        });
        assertThat(joins, contains(
                joinColumn("sourceColumn", "targetTable", "targetColumn"),
                joinValue("targetTable", "targetColumn", "value")
        ));
    }


    @Test
    public void testCreateFromJoinTableAnnotation() {
        assertThat(Join.create(createJoinTableAnnotation("table", new com.nose.orm.mapping.annotation.Join[]{
                createJoinAnnotation("targetTable1", "targetColumn1", "sourceColumn1", null),
                createJoinAnnotation("targetTable2", "targetColumn2", null, "value2")
        }, new com.nose.orm.mapping.annotation.Join[]{
                createJoinAnnotation("targetTable3", "targetColumn3", "sourceColumn3", null),
                createJoinAnnotation("targetTable4", "targetColumn4", null, "value4")
        })), is(joinTable("table", contains(
                    joinColumn("sourceColumn1", "targetTable1", "targetColumn1"),
                    joinValue("targetTable2", "targetColumn2", "value2")
                ),
                contains(
                    joinColumn("sourceColumn3", "targetTable3", "targetColumn3"),
                    joinValue("targetTable4", "targetColumn4", "value4")
                )
        )));
    }


    public com.nose.orm.mapping.annotation.Join createJoinAnnotation(final String targetTable, final String targetColumn, final String sourceColumn, final String value) {
        return new com.nose.orm.mapping.annotation.Join() {
            @Override
            public String targetTable() {
                return targetTable;
            }

            @Override
            public String targetColumn() {
                return targetColumn;
            }

            @Override
            public String sourceColumn() {
                return sourceColumn;
            }

            @Override
            public String value() {
                return value;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return com.nose.orm.mapping.annotation.Join.class;
            }
        };
    }


    public com.nose.orm.mapping.annotation.JoinTable createJoinTableAnnotation(final String table, final com.nose.orm.mapping.annotation.Join[] joins, final com.nose.orm.mapping.annotation.Join[] inverseJoins) {
        return new com.nose.orm.mapping.annotation.JoinTable() {
            @Override
            public String name() {
                return table;
            }

            @Override
            public com.nose.orm.mapping.annotation.Join[] joins() {
                return joins;
            }

            @Override
            public com.nose.orm.mapping.annotation.Join[] inverseJoins() {
                return inverseJoins;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return com.nose.orm.mapping.annotation.JoinTable.class;
            }
        };
    }

}