package org.singledog.monkey.commands.exec;

import org.singledog.monkey.commands.BasicCommand;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/**
 * Created by adam on 6/19/16.
 */
@Component
public class FileDistributeCommand extends BasicCommand {

    @CliAvailabilityIndicator("scp")
    public boolean commandAvailable() {
        return true;
    }

    @CliCommand("scp")
    public String distributeFile(
            @CliOption(key = "path", mandatory = true)
            String filePath,
            @CliOption(key = "", mandatory = true)
            String groupInfo) {

        //TODO
        return "copy from " + filePath + " to " + groupInfo;
    }

}
