package org.singledog.monkey.model.server;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by adam on 6/26/16.
 */
public class Group {

    private String name;

    private Map<String, ServerInfo> servers = new TreeMap<String, ServerInfo>();

    public Group(String name) {
        this.name = name;
    }

    public Group() {
    }

    public void addServerInfo(ServerInfo serverInfo) {
        String ip = serverInfo.getIp();
        servers.put(ip, serverInfo);
    }

    public Set<String> allServers() {
        return new TreeSet<>(servers.keySet());
    }

    public boolean containsServer(String host) {
        return servers.containsKey(host);
    }

    public ServerInfo getServerInfo(String ip) {
        return servers.get(ip);
    }

    public ServerInfo removeServerInfo(String  ip) {
        return servers.remove(ip);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, ServerInfo> getServers() {
        return servers;
    }

    public void setServers(Map<String, ServerInfo> servers) {
        this.servers = servers;
    }
}
