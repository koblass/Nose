package com.nose;

import com.nose.model.User;
import com.nose.orm.Session;
import com.nose.orm.session.InvalidKeyException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

/**
 * Created by Daniel on 02.11.15.
 */
@Ignore
public class SessionTest {

    private Session session;

    @Before
    public void setUp() {
        session = new Session();
    }

    @Test(expected = InvalidKeyException.class)
    public void testGetWithInvalidId() throws InstantiationException, IllegalAccessException {
        session.get(User.class, 0);
    }

    @Test
    public void testGetWithValidId() throws InstantiationException, IllegalAccessException {
        final User user = session.get(User.class, 1);
        assertThat(user.getId(), is(1L));
    }
}
