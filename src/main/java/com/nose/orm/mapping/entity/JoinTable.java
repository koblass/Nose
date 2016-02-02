package com.nose.orm.mapping.entity;

import java.util.List;

/**
 * A join value
 * Created by Daniel on 31.01.2016.
 */
public class JoinTable extends Join {
    
    protected String tableName;

    protected List<Join> joins;
    
    protected List<Join> inverseJoins;

    protected JoinTable(String tableName, List<Join> joins, List<Join> inverseJoins) {
        this.tableName = tableName;
        this.joins = joins;
        this.inverseJoins = inverseJoins;
    }

    public String getTableName() {
        return tableName;
    }

    public List<Join> getJoins() {
        return joins;
    }

    public List<Join> getInverseJoins() {
        return inverseJoins;
    }
}
