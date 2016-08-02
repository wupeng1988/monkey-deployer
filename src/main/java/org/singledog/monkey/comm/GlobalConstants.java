package org.singledog.monkey.comm;

import org.springframework.shell.support.util.StringUtils;

/**
 * Created by adam on 6/18/16.
 */
public interface GlobalConstants {

    String version = "1.0";
    String baseDir = JavaOpts.lookupKey("baseDir", JavaOpts.lookupKey("user.home", "")) + "/.monkey/";
    String historyFileName = baseDir + "monkey.log";
    String promoter = "monkey>";
    String dateFormatPattern = "date.format.pattern";
    String charset = "UTF-8";
    int connectTimeout = 30000;
    boolean isDebugEnabled = true;//Boolean.valueOf(JavaOpts.lookupKey("debug","false"));
    String OS = StringUtils.isEmpty(JavaOpts.lookupKey("OS")) ? JavaOpts.lookupKey("os.name") : JavaOpts.lookupKey("OS");
    String LINE_SEPARATOR = JavaOpts.lookupKey("line.separator");
    String COMMAND_SEPARATOR = " \n";
}
