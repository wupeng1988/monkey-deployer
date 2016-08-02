package org.singledog.monkey.service.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;

import java.io.IOException;

/**
 * Created by Adam.Wu on 2016/6/29.
 */
public interface ChannelCallback<T> {

    public T doWithChannel(Channel channel) throws JSchException, IOException;

}
