package com.tekklabs.memoriapolitica.domain;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by PC on 10/07/2015.
 */
public class QueryMatcherTest {

	@Test
    public void test() {
        QueryMatcher matcher = new QueryMatcher("Jair Bolsonaro", "PP");
        boolean result = matcher.matches("bolsonaro");

        Assert.assertTrue(result);
    }
}
