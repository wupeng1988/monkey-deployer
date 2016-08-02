package org.singledog.monkey.model.server;

/**
 * Created by adam on 6/26/16.
 */
public class ServerCredential {

    private String host;
    private String user = "root";
    private String password;
    private int loginPort = 22;

    public ServerCredential(String host, String password) {
        this.host = host;
        this.password = password;
    }

    public ServerCredential(String host, String user, String password, int loginPort) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.loginPort = loginPort;
    }

    public ServerCredential() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLoginPort() {
        return loginPort;
    }

    public void setLoginPort(int loginPort) {
        this.loginPort = loginPort;
    }
}
