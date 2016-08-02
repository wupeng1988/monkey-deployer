package org.singledog.monkey.service.server;

import com.alibaba.fastjson.JSON;
import org.singledog.monkey.comm.AES;
import org.singledog.monkey.model.server.ServerCredential;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by adam on 6/27/16.
 */
@Service
public class ServerCredentialService extends AbstractPersistable implements InitializingBean {

    private static Map<String, ServerCredential> credentials = new TreeMap<>();

    @Override
    protected String persistFileName() {
        return "server-credentials.json";
    }

    public boolean contains(String host) {
        return credentials.containsKey(host);
    }

    @Override
    protected void initialize(String content) {
        List<ServerCredential> credentialList = JSON.parseArray(content, ServerCredential.class);
        if (credentialList != null && credentialList.size() > 0) {
            for (ServerCredential credential : credentialList) {
                credentials.put(credential.getHost(), credential);
            }
        }
    }

    public ServerCredential decodeCredential(ServerCredential credential) {
        ServerCredential credential1 = new ServerCredential(credential.getHost(), credential.getUser(), credential.getPassword(), credential.getLoginPort());
        credential1.setPassword(AES.aesDecrypt(credential1.getPassword()));
        return credential1;
    }

    public List<String> getAllHosts() {
        return new ArrayList<>(credentials.keySet());
    }

    @Override
    public void persist() {
        writeFile(credentials.values());
    }

    public ServerCredential removeCredential(String host) {
        return credentials.remove(host);
    }

    public void addCredential(ServerCredential credential) {
        credentials.put(credential.getHost(), credential);
    }

    public ServerCredential getCredential(String host) {
        return credentials.get(host);
    }
}
