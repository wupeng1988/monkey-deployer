使用方法
----

服务器组管理
----

**1. 显示版本号**
    
    > show version
    deploy-tool, version 1.0.0.1
    
**2. 显示服务器组**  

    > show groups
     group1, group2, group3
    
**3. 显示服务器组下面的服务器** 

    > show servers group1
    192.168.1.2, 192.168.1.3, 192.168.1.4
    
**4. 创建服务器组** 
    
    > group-add group5
    group created
    
**5. 删除服务器组**
    
    > group-del group5
    group removed
    
**6. 向服务器组增加server**    
    
    > server-add group5 192.168.1.6  192.168.1.9 
    server added 
    
**7. 向服务器组删除server**    
    
    > server-del group5 192.168.1.9 192.168.1.6 --with-auth
    server removed
    
    --with-auth 同时删除保存的用户名密码信息
    
**8. 设置服务器默认登陆用户和密码**

    > server-auth -g group5 -h 192.168.1.3 -u root -p password -P 22
    credentials saved
    
    -g 设置组名， 可忽略， 如不设置， 则对所有的组有效
    -h 设置主机名
    -u 登陆用户名, 默认root
    -p 登陆密码
    -P 登陆端口, 默认22

    
**9. 删除服务器登陆信息**
    
    > server-auth-del -g group5 -h 192.168.1.3
    credentials deleted 
    
    -g 设置组名， 如不设置，则删除所有的组
    -h 主机名
    


变量管理
--------
**1. 设置变量**

    > set [-p] java_opts = -Xmx4096m -Xms1024m
    -Xmx4096m -Xms1024m

    -p : permanent
    
**2. 使用变量**

    > echo $java_opts
    -Xmx4096m -Xms1024m
    
**3. 删除变量**
    
    > del java_opts
    deleted
    

执行命令
-----
**1. 执行命令： run [-y] [user@]server-group command**

可以使用设置的变量, 例如 $java_opts.

    > run -y group5 echo hello
    running on 192.168.1.2------------------------------
    hello
    
    running on 192.168.1.3------------------------------
    hello
    
    running on 192.168.1.4-------------------------------
    hello
    
    默认情况下每执行玩一个机器都会暂停下来等待确认， 
    -y 表示无需等待确认
    
    > run group5 echo hello
    running on 192.168.1.2-------------------------------
    hello 
    
    cotinue ? [Y/N]:Y
    
    running on 192.168.1.3-------------------------------
    hello
    
    continue ? [Y/N]:Y
    running on 192.168.1.4--------------------------------
    hello
    
    
**2. 分发文件： distribute local-file [user@]server-group ** 

    > distribute /home/test/aa-1.0-SNAPSHOT.jar root@group1:/tmp
    copy to 192.168.1.1:/tmp/aa-1.0-SNAPSHOT.jar
    copy to 192.168.1.2:/tmp/aa-1.0-SNAPSHOT.jar
    copy to 192.168.1.3:/tmp/aa-1.0-SNAPSHOT.jar
    
    
**3. kill 进程**
kill -n -g server-group [-y] keywords 
-n kill signal num,
-g server group name,
keywords 

    > kill -9 -g group1 aa-1.0-SNAPSHOT.jar
    192.168.1.2: aa-1.0-SNAPSHOT.jar killed.
    192.168.1.3: aa-1.0-SNAPSHOT.jar killed.
    192.168.1.4: aa-1.0-SNAPSHOT.jar killed.
    
**4. deploy jar** 

deploy -jar local-file.jar --JAVA_OPTS=$java_opts [-y] [-kill keywords] [user@]server-group:/deploy-path


    > deploy -jar /home/test/aa-1.1-SNAPSHOT.jar -y -kill aa-1.0-SNAPSHOT.jar --JAVA_OPTS=$java_opts group5:/data/deploy/
    
    
**5. deploy tomcat**

    
    
    
    
    
    
    
        