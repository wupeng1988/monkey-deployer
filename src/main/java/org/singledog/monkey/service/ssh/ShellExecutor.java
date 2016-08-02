package org.singledog.monkey.service.ssh;

import com.jcraft.jsch.ChannelShell;
import expect4j.Expect4j;
import expect4j.ExpectState;
import expect4j.matches.EofMatch;
import expect4j.matches.Match;
import expect4j.matches.RegExpMatch;
import expect4j.matches.TimeoutMatch;
import org.apache.oro.text.regex.MalformedPatternException;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Adam.Wu on 2016/8/2.
 */
public class ShellExecutor {
    private static final int COMMAND_EXECUTION_SUCCESS_OPCODE = -2;
    private static String ENTER_CHARACTER = "\r";

    public static String[] linuxPromptRegEx = new String[] { "~]#", "~#", "#",
            ":~#", "/$" };

    private List<Match> matches = new ArrayList<>();

    private StringBuilder builder = new StringBuilder();

    private ChannelShell channelShell;

    private Expect4j expect;

    int i = 0;

    ShellExecutor(ChannelShell channelShell) throws Exception {
        this.channelShell = channelShell;
        expect = new Expect4j(this.channelShell.getInputStream(), this.channelShell.getOutputStream());
        for (String regexp : linuxPromptRegEx) {
            matches.add(new RegExpMatch(regexp, expectState -> {
                i = 0;
                builder.delete(0, builder.length());
                builder.append(expectState.getBuffer());
                expectState.exp_continue();
            }));
        }

        matches.add(new EofMatch(expectState -> {
            System.out.println("Command EOF !");
            expectState.exp_continue();
        }));

        matches.add(new TimeoutMatch(60000, expectState -> {
            System.out.println("Timeout !");
            expectState.exp_continue();
        }));
    }

    public String sendCommand(String command) throws Exception {
        Assert.notNull(command);
        if (!command.endsWith("\r"))
            command += " \r";

        if(isSuccess(matches, command)) {
            isSuccess(matches, command);
        }
        checkResult(expect.expect(matches));
        return builder.toString();
    }

    private boolean isSuccess(List<Match> objPattern,String strCommandPattern) {
        try {
            boolean isFailed = checkResult(expect.expect(objPattern));

            if (!isFailed) {
                expect.send(strCommandPattern);
                return true;
            }
            return false;
        } catch (MalformedPatternException ex) {
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean checkResult(int intRetVal) {
        if (intRetVal == COMMAND_EXECUTION_SUCCESS_OPCODE) {
            return true;
        }
        return false;
    }

    public void disconnect() {
        expect.close();
        channelShell.disconnect();
    }

}
