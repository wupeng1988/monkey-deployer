package org.singledog.monkey.service.var;

import com.alibaba.fastjson.JSON;
import org.singledog.monkey.service.server.AbstractPersistable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by adam on 6/27/16.
 */
@Service
public class VariableService extends AbstractPersistable {
    private static Map<String, String> vars = new TreeMap<>();
    private static Map<String, String> persistVars = new TreeMap<>();


    @Override
    protected String persistFileName() {
        return "var.json";
    }

    public void setVar(String name, String value, boolean persist) {
        vars.put(name, value);
        if (persist)
            persistVars.put(name, value);
    }

    public String getVar(String name) {
        return vars.get(name);
    }

    public Map<String, String> getAllVars() {
        return new HashMap<>(vars);
    }

    public String removeVar(String name) {
        persistVars.remove(name);
        return vars.remove(name);
    }

    public String replaceVariables(String original) {
        if (!StringUtils.isEmpty(original)) {
            for (Map.Entry<String, String> entry : vars.entrySet()) {
                String key = "$" + entry.getKey();
                String value = entry.getValue();
                original = original.replace(key, value);
            }
        }

        return original;
    }

    @Override
    protected void initialize(String content) {
        persistVars.putAll(JSON.parseObject(content, Map.class));
        vars.putAll(persistVars);
    }

    @Override
    public void persist() {
        writeFile(persistVars);
    }
}
