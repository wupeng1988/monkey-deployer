package org.singledog.monkey.commands.exec;

import org.singledog.monkey.comm.GlobalConstants;
import org.singledog.monkey.commands.BasicCommand;
import org.singledog.monkey.model.server.Group;
import org.singledog.monkey.model.server.ServerCredential;
import org.singledog.monkey.model.server.ServerInfo;
import org.singledog.monkey.service.server.ServerCredentialService;
import org.singledog.monkey.service.server.ServerGroupService;
import org.singledog.monkey.service.ssh.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by adam on 6/18/16.
 */
@Component
public class RunCommand extends BasicCommand {

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private ServerCredentialService credentialService;
    @Autowired
    private ServerGroupService groupService;

    @CliAvailabilityIndicator("run")
    public boolean commandAvailable() {
        return true;
    }

    @CliCommand("run")
    public String run(
            @CliOption(key = "", mandatory = true)
            String groupInfo,
            @CliOption(key = "pause", mandatory = false, specifiedDefaultValue = "y")
            String pause,
            @CliOption(key = "exec", mandatory = true)
            String command) {

        pause = defaultValue(pause, "y");
        command = trimValue(command.trim());

        String user = "root";
        String group = null;
        int port = 22;

        int index = groupInfo.indexOf("@");
        if (index >= 0) {
            if (index > 0) {
                user = groupInfo.substring(0, index);
            }
            group = groupInfo.substring(index + 1);
        } else {
            group = groupInfo;
        }

        index = group.indexOf(":");
        if (index > 0) {
            port = Integer.valueOf(group.substring(index + 1));
            group = group.substring(0, index);
        }

        Group group1 = groupService.getGroup(group);
        if (group1 == null)
            return returnValue("No group [" + group + "] found !");

        Collection<ServerInfo> serverInfos = group1.getServers().values();
        if (serverInfos.size() == 0)
            return returnValue("No servers found in group [ " + group + " ]");

        List<ServerCredential> credentials = new ArrayList<>(serverInfos.size());
        for (ServerInfo serverInfo : serverInfos) {
            String ip = serverInfo.getIp();
            ServerCredential credential = serverInfo.getCredential(group);
            if (credential == null)
                credential = credentialService.getCredential(ip);

            if (credential == null) {
                //TODO prompt user to enter password and save to credentialService
               return returnValue("No credentials found for host " + ip);
            }

            credentials.add(credential);
        }

        for (ServerCredential credential : credentials) {
            try {
                String message = sessionManager.execute(credentialService.decodeCredential(credential), command);
                System.out.println("------------------------------------ "  + credential.getHost() + " ------------------------------------");
                System.out.println(message);
            } catch (Exception e) {
                if (GlobalConstants.isDebugEnabled) {
                    e.printStackTrace();
                } else {
                    System.out.println("execute command failed ! " + e.getMessage());
                }
            }
        }

        return "execute success !";
    }

}
