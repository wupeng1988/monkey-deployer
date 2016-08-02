package org.singledog.monkey.commands.var;

import org.singledog.monkey.commands.BasicCommand;
import org.singledog.monkey.service.var.VariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by adam on 6/18/16.
 */
@Component
public class VariableCommand extends BasicCommand {

    @Autowired
    private VariableService variableService;

    @CliAvailabilityIndicator({"set", "del"})
    public boolean commandAvailable() {
        return true;
    }

    @CliCommand("set")
    public String setVar(
            @CliOption(key = "", mandatory = true)
            String expression,
            @CliOption(key = "permanent", mandatory = false, specifiedDefaultValue = "false")
            String permanent) {

        permanent = defaultValue(permanent, "false");

        int index = expression.indexOf("=");
        String key = expression.substring(0, index).trim();
        String value = trimValue(expression.substring(index + 1).trim());

        variableService.setVar(key, value, Boolean.valueOf(permanent));
        return returnValue("key: " + key + ", value : " + value);
    }

    @CliCommand("del")
    public String deleteVar(
            @CliOption(key = "", mandatory = true)
            String name) {

        variableService.removeVar(name);
        return returnValue(name + "  deleted");
    }

    @CliCommand("echo")
    public String getVar(
            @CliOption(key = "", mandatory = true)
            String name) {

        return returnValue(variableService.replaceVariables(name));
    }

}
