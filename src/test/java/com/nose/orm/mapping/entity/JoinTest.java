package com.nose.orm.mapping.entity;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.List;

import static com.nose.utils.hamcrest.matchers.entity.Join.joinColumn;
import static com.nose.utils.hamcrest.matchers.entity.Join.joinValue;
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

}