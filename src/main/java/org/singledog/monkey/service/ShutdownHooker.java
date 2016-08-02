package org.singledog.monkey.service;

import org.singledog.monkey.comm.Closeable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by adam on 6/27/16.
 */
@Service
@EnableScheduling
public class ShutdownHooker implements InitializingBean {

    @Autowired
    private List<Closeable> closeables;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void persist() {
        for (Closeable closeable : closeables) {
            closeable.close();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                persist();
            }
        });
    }
}
