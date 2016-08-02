package org.singledog.monkey.commands.shell;

import org.singledog.monkey.comm.GlobalConstants;
import org.singledog.monkey.commands.BasicCommand;
import org.singledog.monkey.model.server.Group;
import org.singledog.monkey.model.server.ServerCredential;
import org.singledog.monkey.model.server.ServerInfo;
import org.singledog.monkey.service.server.ServerCredentialService;
import org.singledog.monkey.service.server.ServerGroupService;
import org.singledog.monkey.service.ssh.SessionManager;
import org.singledog.monkey.service.ssh.ShellExecutor;
import org.singledog.monkey.service.var.VariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * execute serials commands through shell tunnel. execute a command, waiting for another command, not close connection util a close command is received.
 * Created by Adam.Wu on 2016/7/4.
 */
@Component
public class BashShellCommands extends BasicCommand {

    public static final Map<String, ShellExecutor> executorMap = new HashMap<>();

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private ServerGroupService groupService;
    @Autowired
    private ServerCredentialService credentialService;
    @Autowired
    private VariableService variableService;

    private String group;

    @CliAvailabilityIndicator({"shell-start", "shell-run", "shell-close", "srun"})
    public boolean isCommandValid() {
        return true;
    }

    @CliCommand("shell-start")
    public String shellStart(
            @CliOption(key = "", mandatory = true)
            String group) {

        if (!StringUtils.isEmpty(this.group)) {
            return returnValue("shell already opened for group " + this.group + ", please close it .");
        }

        shellClose();
        Group group1 = groupService.getGroup(group);
        if (group1 == null) {
            return returnValue("group " + group + " not exists !");
        }

        Map<String, ServerInfo> serverInfoMap = group1.getServers();
        if (serverInfoMap.size() == 0) {
            return returnValue("No servers found in group " + group);
        }

        this.group = group;

        try {
            for (Map.Entry<String, ServerInfo> entry : serverInfoMap.entrySet()) {
                ServerCredential credential = entry.getValue().getCredential(group);
                if (credential == null) {
                    credential = credentialService.getCredential(entry.getKey());
                }
                if (credential == null) {
                    return returnValue("No credential found for server : " + entry.getKey());
                }

                executorMap.put(entry.getKey(), sessionManager.getShellExecutor(credentialService.decodeCredential(credential)));
            }
        } catch (Exception e) {
            if (GlobalConstants.isDebugEnabled)
                e.printStackTrace();
            this.shellClose();
            return returnValue("can not open session ! " + e.getMessage());
        }

        return returnValue("session opened !");
    }

    @CliCommand("srun")
    public String srun(
            @CliOption(key = "", mandatory = true)
            String command) {

        return this.shellRun(command);
    }

    @CliCommand("shell-run")
    public String shellRun(
            @CliOption(key = "", mandatory = true)
            String command) {

        if (StringUtils.isEmpty(command)) {
            return returnValue("empty command ! abort !");
        }

        command = trimValue(command);
        final String finalCommand = variableService.replaceVariables(command);

        executorMap.forEach((key, executor) -> {
            try {
                System.out.println("----------------- " + key + " -----------------");
                System.out.println(executor.sendCommand(finalCommand));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        return returnValue("success");
    }

    public String readValue(InputStream is) throws IOException {
        byte[] bytes = new byte[1024 * 10];
        int flag = -1;
        StringBuilder builder = new StringBuilder();
        while ((flag = is.read(bytes)) != -1) {
            builder.append(new String(bytes, 0, flag));
        }

        return builder.toString();
    }

    @CliCommand("shell-close")
    public String shellClose() {

        if (StringUtils.isEmpty(this.group)) {
            return returnValue("no shell opened !");
        }

        executorMap.forEach((key, executor) -> {
            try {
                executor.disconnect();
            } catch (Exception e) {
                if (GlobalConstants.isDebugEnabled) {
                    e.printStackTrace();
                }
            }
        });
        executorMap.clear();

        this.group = null;
        return returnValue("session closed !");
    }

}
