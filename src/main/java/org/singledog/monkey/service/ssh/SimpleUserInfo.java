package org.singledog.monkey.service.ssh;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * Created by Adam.Wu on 2016/6/29.
 */
public class SimpleUserInfo implements UserInfo, UIKeyboardInteractive {

    private String password;

    public SimpleUserInfo() {}

    public SimpleUserInfo(String password) {
        this.password = password;
    }

    @Override
    public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
        return new String[0];
    }

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean promptPassword(String message) {
        return false;
    }

    @Override
    public boolean promptPassphrase(String message) {
        return false;
    }

    @Override
    public boolean promptYesNo(String message) {
        return true;
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }
}
