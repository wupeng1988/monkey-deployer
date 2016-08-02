package org.singledog.monkey.service;

import org.singledog.monkey.comm.Closeable;

/**
 * Created by adam on 6/26/16.
 */
public interface Persistable extends Closeable {

    public void persist();

}
