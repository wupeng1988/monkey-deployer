package org.singledog.monkey.commands;

import org.singledog.monkey.comm.GlobalConstants;
import org.singledog.monkey.comm.JavaOpts;
import org.springframework.shell.core.CommandMarker;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by adam on 6/18/16.
 */
public class BasicCommand implements CommandMarker {

    public static final SimpleDateFormat dateformat;
    protected Scanner scanner = new Scanner(System.in);

    static {
        dateformat = new SimpleDateFormat(JavaOpts.lookupKey(GlobalConstants.dateFormatPattern, "yyyy-MM-dd HH:mm:ss"));
    }

    public String returnValue(String value) {
        return new StringBuilder("[ ").append(dateformat.format(new Date())).append(" ] ").append(value).toString();
    }

    /*
        获取默认值
     */
    public String defaultValue(String v, String dv) {
        return StringUtils.isEmpty(v) ? dv : v;
    }

    public String trimValue(String value) {
        if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'")))
            value = value.substring(1).substring(0, value.length() - 2).trim();
        return value;
    }

}
