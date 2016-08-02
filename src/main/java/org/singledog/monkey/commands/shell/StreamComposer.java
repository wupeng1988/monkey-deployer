package org.singledog.monkey.commands.shell;

import org.singledog.monkey.comm.GlobalConstants;
import org.singledog.monkey.util.CircularByteBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Adam.Wu on 2016/7/21.
 */
public class StreamComposer {

    private CircularByteBuffer buffer = new CircularByteBuffer();

    public OutputStream getOutputStream() {
        return buffer.getOutputStream();
    }

    public InputStream getInputStream() {
        return buffer.getInputStream();
    }

    public void close() {
        buffer.clear();
        try {
            buffer.getInputStream().close();
        } catch (IOException e) {
            if (GlobalConstants.isDebugEnabled) {
                e.printStackTrace();
            }
        }
        try {
            buffer.getOutputStream().close();
        } catch (IOException e) {
            if (GlobalConstants.isDebugEnabled) {
                e.printStackTrace();
            }
        }
    }
}
