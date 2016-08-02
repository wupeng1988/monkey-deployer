package org.singledog.monkey.command;

import org.singledog.monkey.comm.JavaOpts;

/**
 * Created by adam on 6/19/16.
 */
public class RunCommandTest {

    public static void main(String[] args) {

       System.out.println(JavaOpts.lookupKey("os"));
       System.out.println(JavaOpts.lookupKey("OS"));
       System.out.println(JavaOpts.lookupKey("os.name"));
       System.out.println(JavaOpts.lookupKey("OS.name"));

        System.getProperties().list(System.out);

        System.out.println("-----------------------------");


        System.getenv().forEach((key, value) -> {
            System.out.println(key + "--->" + value);
        });
        System.out.println();

    }

}
