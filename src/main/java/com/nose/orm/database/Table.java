package com.nose.orm.database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Daniel on 10.02.2016.
 */
public class Table extends ArrayList<Row> {


    /**
     * Convert a flat rows table into a key/values associative table
     * The key is the column name and the values represent the row values
     */
    public ColumnValues getColumnValues() {
        ColumnValues columnValues = new ColumnValues();
        if (!isEmpty()) {
            for (Row row : this) {
                for (Map.Entry<String, String> entry : row.entrySet()) {
                    if (!columnValues.containsKey(entry.getKey())) {
                        columnValues.put(entry.getKey(), new HashSet<String>());
                    }
                    columnValues.get(entry.getKey()).add(entry.getValue());
                }
            }
        }
        return columnValues;
    }
}
