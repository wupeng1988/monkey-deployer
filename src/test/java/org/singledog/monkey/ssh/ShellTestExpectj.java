package org.singledog.monkey.ssh;

import com.jcraft.jsch.*;
import expect4j.Closure;
import expect4j.Expect4j;
import expect4j.ExpectState;
import expect4j.matches.Match;
import expect4j.matches.RegExpMatch;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam.Wu on 2016/8/2.
 */
public class ShellTestExpectj {

    //正则匹配，用于处理服务器返回的结果
    public static String[] linuxPromptRegEx = new String[] { "~]#", "~#", "#",
            ":~#", "/$" };

    public static void main(String[] args) throws Exception {
        JSch jSch = new JSch();

        Session session=jSch.getSession("root", "192.168.1.12", 22);
        String passwd = JOptionPane.showInputDialog("Enter password");
        session.setPassword(passwd);

        UserInfo ui = new MyUserInfo(){
            public void showMessage(String message){
                JOptionPane.showMessageDialog(null, message);
            }
            public boolean promptYesNo(String message){
                Object[] options={ "yes", "no" };
                int foo=JOptionPane.showOptionDialog(null,
                        message,
                        "Warning",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                return foo==0;
            }

            // If password is not given before the invocation of Session#connect(),
            // implement also following methods,
            //   * UserInfo#getPassword(),
            //   * UserInfo#promptPassword(String message) and
            //   * UIKeyboardInteractive#promptKeyboardInteractive()

        };

        session.setUserInfo(ui);
        session.setTimeout(60000);
        session.connect(60000);
        Channel channel=session.openChannel("shell");
        channel.connect();

        StringBuilder buffer = new StringBuilder();

        Closure closure = new Closure() {
            public void run(ExpectState expectState) throws Exception {
                buffer.delete(0, buffer.length());
                buffer.append(expectState.getBuffer());// buffer is string
                // buffer for appending
                // output of executed
                // command
                expectState.exp_continue();

            }
        };

        List<Match> lstPattern = new ArrayList<Match>();
        for (String s : linuxPromptRegEx) {
            RegExpMatch mat = new RegExpMatch(s, closure);
            lstPattern.add(mat);
        }


        Expect4j expect4j = new Expect4j(channel.getInputStream(), channel.getOutputStream());
        int i = expect4j.expect(lstPattern);
        expect4j.send("cd /tmp " + "\r");

        expect4j.expect(lstPattern);
        System.out.println("1>>>> " + buffer.toString());

        expect4j.expect(lstPattern);
        expect4j.send("pwd " + "\r");

        expect4j.expect(lstPattern);
        System.out.println("2>>>> " + buffer.toString());

        expect4j.expect(lstPattern);
        expect4j.send(" echo 'SDFSSDGFSDGDFG' >> test.a " + "\r");

        expect4j.expect(lstPattern);
        System.out.println("3>>>> " + buffer.toString());

        expect4j.send(" cat test.a " + "\r");

        expect4j.expect(lstPattern);
        System.out.println("4>>>> " + buffer.toString());

        expect4j.send("exit \r");
        expect4j.expect(lstPattern);
        System.out.println("5>>>> " + buffer.toString());
    }

    public static abstract class MyUserInfo
            implements UserInfo, UIKeyboardInteractive {
        public String getPassword(){ return null; }
        public boolean promptYesNo(String str){ return false; }
        public String getPassphrase(){ return null; }
        public boolean promptPassphrase(String message){ return false; }
        public boolean promptPassword(String message){ return false; }
        public void showMessage(String message){ }
        public String[] promptKeyboardInteractive(String destination,
                                                  String name,
                                                  String instruction,
                                                  String[] prompt,
                                                  boolean[] echo){
            return null;
        }
    }

}
