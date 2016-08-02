package org.singledog.monkey.service.server;

import com.alibaba.fastjson.JSON;
import org.singledog.monkey.comm.GlobalConstants;
import org.singledog.monkey.service.Persistable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.io.*;

/**
 * Created by adam on 6/26/16.
 */
public abstract class AbstractPersistable implements Persistable, InitializingBean {

    protected abstract String persistFileName();

    protected abstract void initialize(String content);

    @Override
    public void afterPropertiesSet() throws Exception {
        String json = this.readFile();
        if (!StringUtils.isEmpty(json)) {
            initialize(json);
        }
    }

    @Override
    public void close() {
        persist();
    }

    public String readFile() {
        File file = new File(GlobalConstants.baseDir + persistFileName());
        if (!file.exists() || !file.canRead()) {
            return null;
        }
        FileInputStream reader = null;
        String json = null;
        try {
            reader = new FileInputStream(GlobalConstants.baseDir + persistFileName());
            byte[] bytes = new byte[reader.available()];
            reader.read(bytes);
            json = new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return json;
    }

    public void writeFile(Object object) {
        File file = new File(GlobalConstants.baseDir + persistFileName());

        FileWriter fos = null;
        try {
            if (!file.exists())
                file.createNewFile();
            fos = new FileWriter(file);
            fos.write(JSON.toJSONString(object, true));
            fos.flush();
        } catch (Exception e) {
            System.out.println("persist failed ! " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
