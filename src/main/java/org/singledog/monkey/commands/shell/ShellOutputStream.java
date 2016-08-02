package org.singledog.monkey.commands.shell;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by adam on 7/21/16.
 */
public class ShellOutputStream extends OutputStream {

    private String host;

    public ShellOutputStream(String host) {
        this.host = host;
    }

    public void write(byte b[], int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }

        System.out.println(host + "-> " + new String(b, off, len));
    }

    @Override
    public void write(int b) throws IOException {
        throw new IOException("Not Support ! use write(byte b[], int off, int len) instead !");
    }
}
