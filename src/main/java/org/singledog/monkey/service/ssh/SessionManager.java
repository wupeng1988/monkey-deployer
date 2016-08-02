package org.singledog.monkey.service.ssh;

import com.jcraft.jsch.*;
import org.apache.oro.text.regex.MalformedPatternException;
import org.singledog.monkey.comm.Closeable;
import org.singledog.monkey.comm.GlobalConstants;
import org.singledog.monkey.model.server.ServerCredential;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Adam.Wu on 2016/6/29.
 */
@Service
@EnableScheduling
public class SessionManager implements Closeable {

    private static final Map<String, Session> sessions = new HashMap<>();

    private JSch jSch = new JSch();

    /**
     *
     * execute a command on remote server, then exit
     *
     * @param credential
     * @param command
     * @return
     * @throws JSchException
     * @throws IOException
     */
    public String execute(ServerCredential credential, String command) throws JSchException, IOException {
        return this.execute(credential, new ChannelCallback<String>() {
            @Override
            public String doWithChannel(Channel channel) throws JSchException, IOException {
                ((ChannelExec)channel).setCommand(command);
                channel.setInputStream(null);
                ((ChannelExec) channel).setErrStream(System.err);
                InputStream is = channel.getInputStream();
                channel.connect(GlobalConstants.connectTimeout);
                byte[] buffer = new byte[1024 * 10];
                int flag = -1;
                StringBuilder stringBuilder = new StringBuilder();
                while ((flag = is.read(buffer)) != -1) {
                    stringBuilder.append(new String(buffer, 0, flag));
                }

                return stringBuilder.toString();
            }
        });
    }

//    @Scheduled(cron = "0/5 * * * * ?")
    public void keepAlive() {
        sessions.forEach((key, session) -> {
            try {
                if (!session.isConnected()) {
                    session.connect(GlobalConstants.connectTimeout);
                }
                session.sendKeepAliveMsg();
            } catch (JSchException e) {
                if (GlobalConstants.isDebugEnabled) {
                    System.err.println("error to send keep alive signal !");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                if (GlobalConstants.isDebugEnabled) {
                    System.err.println("error to send keep alive signal !");
                    e.printStackTrace();
                }
            }
        });
    }

    public Session getSession(ServerCredential credential) throws JSchException {
        String key = credential.getUser() + credential.getHost();
        Session session = sessions.get(key);
        if (session == null) {
            String password = credential.getPassword();
            session = jSch.getSession(credential.getUser(), credential.getHost(), credential.getLoginPort());
            if (!StringUtils.isEmpty(password)) {
                session.setPassword(password);
            }

            Hashtable<String,String> config = new Hashtable<String,String>();
            config.put("StrictHostKeyChecking", "no");
            config.put("PreferredAuthentications",
                    "publickey,keyboard-interactive,password");//makes kerberos happy
            session.setConfig(config);
            session.setUserInfo(new SimpleUserInfo(password));
            sessions.put(key, session);
        }

        if (!session.isConnected())
            session.connect(GlobalConstants.connectTimeout);

        return session;
    }

    public ShellExecutor getShellExecutor(ServerCredential credential) throws Exception {
        ChannelShell channel = (ChannelShell) getSession(credential).openChannel("shell");
        channel.connect(GlobalConstants.connectTimeout);
        return new ShellExecutor(channel);
    }

    public <T> T execute(ServerCredential credential, ChannelCallback<T> callback) throws JSchException, IOException {
        Channel channel = null;
        try {
            channel = getSession(credential).openChannel("exec");
            return callback.doWithChannel(channel);
        } finally {
            if (channel != null && !channel.isClosed())
                channel.disconnect();
        }
    }

    @Override
    public void close() {
        if (sessions.size() > 0) {
            for (Map.Entry<String, Session> entry : sessions.entrySet()) {
                Session session = entry.getValue();
                if (session.isConnected())
                    session.disconnect();
            }
        }
    }
}
