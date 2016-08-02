package org.singledog.monkey.ssh;

import com.jcraft.jsch.*;

import javax.swing.*;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Created by Adam.Wu on 2016/8/2.
 */
public class ShellPirStreamTest {

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

        PipedInputStream pipeIn = new PipedInputStream();
        PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);
        channel.setInputStream( pipeIn );
        channel.setOutputStream(System.out);

        String command = "cd /tmp \n";
        pipeOut.write(command.getBytes());
        pipeOut.flush();

        Thread.sleep(10000);
        command = "pwd \n";
        pipeOut.write(command.getBytes());
        pipeOut.flush();

        Thread.sleep(10000);
        command = "echo 'SDGFSDGFDFSGSEGSDGF' >> aa.txt \n";
        pipeOut.write(command.getBytes());
        pipeOut.flush();

        Thread.sleep(10000);
        command = "cat aa.txt \n";
        pipeOut.write(command.getBytes());
        pipeOut.flush();

        Thread.sleep(10000);
        command = "exit \n";
        pipeOut.write(command.getBytes());
        pipeOut.flush();

        Thread.sleep(10000);
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
