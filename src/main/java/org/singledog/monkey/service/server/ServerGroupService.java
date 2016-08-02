package org.singledog.monkey.service.server;

import com.alibaba.fastjson.JSON;
import org.singledog.monkey.model.server.Group;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by adam on 6/26/16.
 */
@Service
public class ServerGroupService extends AbstractPersistable implements InitializingBean {

    private static Map<String, Group> groups = new TreeMap<String, Group>();

    public void persist() {
        this.writeFile(groups.values());
    }

    @Override
    protected String persistFileName() {
        return "groups.json";
    }

    @Override
    protected void initialize(String content) {
        List<Group> groupList = JSON.parseArray(content, Group.class);
        if (groupList != null && groupList.size() > 0) {
            for (Group group : groupList) {
                groups.put(group.getName(), group);
            }
        }
    }

    public Group getGroup(String groupName) {
        return groups.get(groupName);
    }

    public boolean contains(String groupName) {
        return groups.containsKey(groupName);
    }

    public void addGroup(Group group) {
        groups.put(group.getName(), group);
    }

    public Set<String> getGroupNames() {
        return new TreeSet<>(groups.keySet());
    }

    public Group removeGroup(String name) {
        return groups.remove(name);
    }
}
