package com.tekklabs.util;

import java.text.Normalizer;

/**
 * Created by taciosd on 8/4/15.
 */
public class StringUtil {

    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
}
