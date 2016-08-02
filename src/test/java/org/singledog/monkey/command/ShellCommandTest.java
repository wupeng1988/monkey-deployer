package org.singledog.monkey.command;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Adam.Wu on 2016/7/22.
 */

public class ShellCommandTest extends AbstractShellIntegrationTest {

    @Test
    public void test() {
        getShell().executeCommand("shell-start test");
        getShell().executeCommand("shell-run 'cd /tmp' ");
        getShell().executeCommand("shell-run 'pwd' ");
        getShell().executeCommand("shell-run 'echo \"AAAAAAAAAAAAA\" >> test.aa'");
        getShell().executeCommand("shell-run 'cat test.aa'");
        getShell().executeCommand("shell-close");
    }

}
