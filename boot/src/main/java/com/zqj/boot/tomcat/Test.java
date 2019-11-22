package com.zqj.boot.tomcat;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

/**
 * @author zqj
 * @create 2019-10-09 11:03
 */
public class Test {

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        Connector connector = new Connector("HTTP/1.1");
        connector.setPort(8033);
        tomcat.setConnector(connector);
        tomcat.start();
        tomcat.getServer().await();
    }
}
