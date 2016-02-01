package com.nose.orm;

/**
 * Created by Daniel on 02.11.15.
 */
public class Nose {


    private static Session ACTIVE_SESSION = null;

    public static Session getSession() {
        if (ACTIVE_SESSION == null) {
            ACTIVE_SESSION = new Session();
        }
        return ACTIVE_SESSION;
    }
}
