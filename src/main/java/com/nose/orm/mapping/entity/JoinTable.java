package com.nose.orm.mapping.entity;

import java.util.List;

/**
 * A join value
 * Created by Daniel on 31.01.2016.
 */
public class JoinTable {
    
    protected String table;

    protected List<Join> joins;
    
    protected List<Join> inverseJoins;

    protected JoinTable(String table, List<Join> joins, List<Join> inverseJoins) {
        this.table = table;
        this.joins = joins;
        this.inverseJoins = inverseJoins;
    }

    public String getTable() {
        return table;
    }

    public List<Join> getJoins() {
        return joins;
    }

    public List<Join> getInverseJoins() {
        return inverseJoins;
    }

    /**
     * Creates a join table from the given annotation
     * @param joinTableAnnotation
     * @return
     */
    public static JoinTable create(com.nose.orm.mapping.annotation.JoinTable joinTableAnnotation) {
        return new JoinTable(joinTableAnnotation.name(), Join.create(joinTableAnnotation.joins()), Join.create(joinTableAnnotation.inverseJoins()));
    }
}
