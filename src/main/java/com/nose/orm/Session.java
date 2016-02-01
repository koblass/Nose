package com.nose.orm;

import java.util.Collection;
import org.apache.log4j.Logger;

/**
 * Created by Daniel on 02.11.15.
 */
public class Session {

    private final static Logger LOGGER = Logger.getLogger(Session.class);

    public Session() {}

    public <T> T get(Class<T> cls, int privateKey) throws IllegalAccessException, InstantiationException {
        LOGGER.debug("Session::get(" + cls.getName() + ", " + privateKey);
        return cls.newInstance();
    }

//    public <T> Collection<T> load(Class<T> cls, String condition) {
//        LOGGER.debug("Session::load(" + cls.getName() + ", " + condition);
//    }
}
