package com.nose.orm.mapping.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * The join representation between two entities
 * Created by Daniel on 31.01.2016.
 */
public abstract class Join {

    /**
     * Creates a list of joins from the given array of annotations
     * @param joinAnnotations
     * @return
     */
    public static List<Join> create(com.nose.orm.mapping.annotation.Join[] joinAnnotations) {
        List<Join> joins = new ArrayList<Join>(joinAnnotations.length);
        for (com.nose.orm.mapping.annotation.Join joinAnnotation : joinAnnotations) {
            joins.add(Join.create(joinAnnotation));
        }
        return joins;
    }

    /**
     * Creates a join from the given annotation
     * @param joinAnnotation
     * @return
     */
    public static Join create(com.nose.orm.mapping.annotation.Join joinAnnotation) {
        if (joinAnnotation.sourceColumn().isEmpty()) {
            return new JoinValue(joinAnnotation.targetTable(), joinAnnotation.targetColumn(), joinAnnotation.value());
        } else {
            return new JoinColumn(joinAnnotation.targetTable(), joinAnnotation.targetColumn(), joinAnnotation.sourceColumn());
        }
    }

    /**
     * Creates a join table from the given annotation
     * @param joinTableAnnotation
     * @return
     */
    public static Join create(com.nose.orm.mapping.annotation.JoinTable joinTableAnnotation) {
        return new JoinTable(joinTableAnnotation.name(), Join.create(joinTableAnnotation.joins()), Join.create(joinTableAnnotation.inverseJoins()));
    }
}
