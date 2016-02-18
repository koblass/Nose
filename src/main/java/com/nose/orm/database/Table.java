package com.nose.orm.database;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Daniel on 10.02.2016.
 */
public class Table extends ArrayList<Row> {






    /**
     * Convert a flat rows table into a key/values associative table
     */
    public ColumnValues getKeyValues() {
        ColumnValues columnValues = new ColumnValues();
        if (!isEmpty()) {
            for (Row row : this) {
                for (Map.Entry<String, String> entry : row.entrySet()) {
                    if (!columnValues.containsKey(entry.getKey())) {
                        columnValues.put(entry.getKey(), new ArrayList<String>());
                    }
                    if (!columnValues.get(entry.getKey()).contains(entry.getValue())) {
                        columnValues.get(entry.getKey()).add(entry.getValue());
                    }
                }
            }
        }
        return columnValues;
    }
}
