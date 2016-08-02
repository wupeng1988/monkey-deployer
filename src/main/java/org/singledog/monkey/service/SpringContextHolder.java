package org.singledog.monkey.service;

import org.singledog.monkey.comm.GlobalConstants;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by adam on 6/26/16.
 */
@Service
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
        File dir = new File(GlobalConstants.baseDir);
        if (!dir.exists())
            dir.mkdirs();
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
