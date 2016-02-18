package com.nose.orm.database;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 10.02.2016.
 */
public class Keys extends HashMap<String, String> {

    /**
     * Return true if the given row matches with the keys
     *
     * @param row
     * @return
     */
    public boolean matches(Row row) {
        for (Map.Entry<String, String> entry : this.entrySet()) {
            if (!row.containsKey(entry.getKey()) || !row.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }
}
