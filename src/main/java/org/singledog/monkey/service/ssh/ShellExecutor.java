package org.singledog.monkey.service.ssh;

import com.jcraft.jsch.ChannelShell;
import expect4j.Expect4j;
import expect4j.matches.Match;
import expect4j.matches.RegExpMatch;
import org.apache.oro.text.regex.MalformedPatternException;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam.Wu on 2016/8/2.
 */
public class ShellExecutor {

    public static String[] linuxPromptRegEx = new String[] { "~]#", "~#", "#",
            ":~#", "/$" };

    private List<Match> matches = new ArrayList<>();

    private StringBuilder builder = new StringBuilder();

    private ChannelShell channelShell;

    private Expect4j expect;

    ShellExecutor(ChannelShell channelShell) throws Exception {
        this.channelShell = channelShell;
        expect = new Expect4j(this.channelShell.getInputStream(), this.channelShell.getOutputStream());
        for (String regexp : linuxPromptRegEx) {
            matches.add(new RegExpMatch(regexp, expectState -> {
                builder.delete(0, builder.length());
                builder.append(expectState.getBuffer());
                expectState.exp_continue();
            }));
        }
        expect.expect(matches);
    }

    public String sendCommand(String command) throws Exception {
        Assert.notNull(command);
        if (!command.endsWith("\r"))
            command += " \r";

        expect.send(command);
        expect.expect(matches);
        return builder.toString();
    }

    public void disconnect() {
        expect.close();
        channelShell.disconnect();
    }

}
