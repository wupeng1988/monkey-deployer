package org.singledog.monkey.model.server;

import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by adam on 6/26/16.
 */
public class ServerInfo {

    private String ip;

    private Map<String, ServerCredential> credentials = new TreeMap<String, ServerCredential>();

    public ServerInfo() {
    }

    public ServerInfo(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void putCredential(String groupName, ServerCredential credential) {
        this.credentials.put(groupName, credential);
    }

    public ServerCredential getCredential(String group) {
        return credentials.get(group);
    }

    public ServerCredential removeCredential(String group) {
        return credentials.remove(group);
    }

    public Map<String, ServerCredential> getCredentials() {
        return credentials;
    }

    public void setCredentials(Map<String, ServerCredential> credentials) {
        this.credentials = credentials;
    }
}
