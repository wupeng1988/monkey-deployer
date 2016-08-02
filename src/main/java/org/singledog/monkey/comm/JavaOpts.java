package org.singledog.monkey.comm;

import org.springframework.util.StringUtils;

/**
 * Created by adam on 6/26/16.
 */
public class JavaOpts {

    public static String lookupKey(String key) {
        return lookupKey(key,null);
    }

    public static String lookupKey(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (StringUtils.isEmpty(value)) {
            value = System.getenv(key);
            if (StringUtils.isEmpty(value)) {
                value = defaultValue;
            }
        }

        return value;
    }

}
