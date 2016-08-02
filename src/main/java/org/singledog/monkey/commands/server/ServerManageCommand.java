package org.singledog.monkey.commands.server;

import org.singledog.monkey.comm.AES;
import org.singledog.monkey.commands.BasicCommand;
import org.singledog.monkey.model.server.Group;
import org.singledog.monkey.model.server.ServerCredential;
import org.singledog.monkey.model.server.ServerInfo;
import org.singledog.monkey.service.server.ServerCredentialService;
import org.singledog.monkey.service.server.ServerGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 *
 * > show groups
 *   group1, group2, group3
 *  > show servers group1
 *  192.168.1.2, 192.168.1.3, 192.168.1.4
 *
 * Created by adam on 6/18/16.
 */
@Component
public class ServerManageCommand extends BasicCommand {

    @Autowired
    private ServerGroupService groupService;
    @Autowired
    private ServerCredentialService credentialService;

    @CliAvailabilityIndicator({"show", "group-add", "server-del", "server-auth", "server-auth-del"})
    public boolean isCommandValid() {
        return true;
    }

    /**
     * show groups
     * group1, group2
     * @return
     */
    @CliCommand(value = "show groups", help = "show all server groups")
    public  String showGroups() {
        return returnValue(Arrays.toString(groupService.getGroupNames().toArray()));
    }

    @CliCommand(value = "show server-auths")
    public String showServerAuth() {
        return returnValue(Arrays.toString(credentialService.getAllHosts().toArray()));
    }

    /**
     * show servers group1
     * 192.168.1.2, 192.168.1.3
     * @param group
     * @return
     */
    @CliCommand(value = "show servers", help = "show servers in group")
    public String showServers(@CliOption(key = "", mandatory = true, help = "group name")
                              String group) {

        Group group1 = groupService.getGroup(group);
        if (group1 == null)
            return returnValue("group not exists");

        return returnValue(Arrays.toString(group1.allServers().toArray()));
    }

    @CliCommand(value = "group-add", help = "create a group")
    public String addGroup(
            @CliOption(key = "", mandatory = true, help = "group name")
            String group) {

        if (groupService.contains(group))
            return returnValue("group already exists");

        groupService.addGroup(new Group(group));
        return returnValue("group created");
    }

    @CliCommand(value = "group-del", help = "delete a group")
    public String delGroup(
            @CliOption(key = "", mandatory = true, help = "group name")
            String group) {

        Group group1 = groupService.removeGroup(group);
        if (group1 == null) {
            return returnValue("group not exists !");
        }

        return returnValue("group deleted");
    }

    @CliCommand(value = "server-add", help = "add server to group")
    public String addServer(
            @CliOption(key = "servers", mandatory = true, help = "comma splited server list")
            String servers,
            @CliOption(key = "group", mandatory = true, help = "which group added to")
            String group) {

        Group group1 = groupService.getGroup(group);
        if (group1 == null)
            return returnValue("group not exists");

        String[] serverList = servers.split(",");
        for (String server : serverList) {
            if (group1.containsServer(server))
                continue;

            group1.addServerInfo(new ServerInfo(server));
        }

        return returnValue("server added");
    }


    @CliCommand(value = "server-del", help = "delete server from group")
    public String delServer(
            @CliOption(key = "servers", mandatory = true, help = "comma spilted server list")
            String servers,
            @CliOption(key = "group", mandatory = true)
            String group) {

        Group group1 = groupService.getGroup(group);
        if (group1 == null)
            return returnValue("group not exists");

        String[] serverArray = servers.split(",");
        for (String server : serverArray) {
            group1.removeServerInfo(server);
        }

        return returnValue("server removed");
    }

    @CliCommand(value = "server-auth-add")
    public String serverAuth(
            @CliOption(key = "host", mandatory = true)
            String host,
            @CliOption(key = "user", mandatory = false, specifiedDefaultValue = "root")
            String user,
            @CliOption(key = "password", mandatory = true)
            String password,
            @CliOption(key = "port", mandatory = false, specifiedDefaultValue = "22")
            String port,
            @CliOption(key = "group", mandatory = false)
            String group) {

        if (credentialService.contains(host)) {
            return returnValue("Credential already exists");
        }

        user = defaultValue(user, "root");
        port = defaultValue(port, "22");

        password = AES.aesEncrypt(password);
        ServerCredential credential = new ServerCredential(host, user, password, Integer.valueOf(port));
        if (!StringUtils.isEmpty(group)) {
            Group group1 = groupService.getGroup(group);
            if (group1 != null) {
                ServerInfo serverInfo = group1.getServerInfo(host);
                if (serverInfo == null) {
                    serverInfo = new ServerInfo(host);
                    group1.addServerInfo(serverInfo);
                }
                serverInfo.putCredential(group, credential);
            } else {
                System.out.println("group [" + group + " ] not exists");
            }
        } else {
            credentialService.addCredential(credential);
        }

        return returnValue("credentials saved");
    }

    @CliCommand(value = "server-auth-del")
    public String serverAuthDel(
            @CliOption(key = "host", mandatory = true)
            String host,
            @CliOption(key = "group", mandatory = false)
            String group) {

        if (!StringUtils.isEmpty(group)) {
            Group group1 = groupService.getGroup(group);
            if (group1 == null)
                System.out.println("group [ " + group + " ] not exists");
            else {
                ServerInfo serverInfo = group1.getServerInfo(host);
                if (serverInfo != null)
                    serverInfo.removeCredential(group);
            }
        } else {
            credentialService.removeCredential(host);
        }

        return returnValue("credentials deleted");
    }
}
