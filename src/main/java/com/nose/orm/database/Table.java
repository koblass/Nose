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
    public Keys getKeyValues() {
        Keys keys = new Keys();
        if (!isEmpty()) {
            for (Row row : this) {
                for (Map.Entry<String, String> entry : row.entrySet()) {
                    if (!keys.containsKey(entry.getKey())) {
                        keys.put(entry.getKey(), new ArrayList<String>());
                    }
                    if (!keys.get(entry.getKey()).contains(entry.getValue())) {
                        keys.get(entry.getKey()).add(entry.getValue());
                    }
                }
            }
        }
        return keys;
    }
}
