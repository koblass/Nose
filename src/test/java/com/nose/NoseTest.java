package com.nose;

import com.nose.orm.Nose;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Daniel on 02.11.15.
 */
public class NoseTest {


    @Test
    public void testGetSession() {
        Assert.assertNotNull(Nose.getSession());
        Assert.assertEquals(Nose.getSession(), Nose.getSession());
    }
}
