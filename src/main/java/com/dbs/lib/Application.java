/**
 * Application
 */
package com.dbs.lib;

import java.lang.invoke.MethodHandles;
import java.net.UnknownHostException;
import java.time.ZoneId;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.boot.web.support.SpringBootServletInitializer;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.env.Environment;

import com.dbs.lib.net.NetUtils;


/**
 * Helper to write Spring Boot application info in console mode
 * <br>Do NOT forget to set 
 * &lt;logger name="com.dbs.lib.Application" level="INFO"/&gt;
 * to activate this logger feature in your logback.xml for example
 * @author dbs on Mar 24, 2018 5:16:16 PM
 * @since 1.0.0
 * @version 1.0

 */
public class Application /*extends SpringBootServletInitializer*/ {

  protected static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  protected static void showAppInfo(final Environment env) throws UnknownHostException {
    String isSslEnabled = env.getProperty("server.ssl.enabled");
    String protocol = "http";
    Properties properties = System.getProperties();
    if (isSslEnabled != null && "true".equalsIgnoreCase(isSslEnabled)) {
      protocol = "https";
    }
    LOG.info(
      "Access URLs:\n----------------------------------------------------------\n\t" 
      + "Local: \t\t" + protocol + "://127.0.0.1:{}\n\t"
      + "External: \t" + protocol + "://{}:{}\n\t" 
      + "Memory Total/Free/Max: {}/{}/{}, processors: {}\n\t"
      + "webapp: {} ({}), environment: {}, V{}, build date {}"
      + "\n\tOS Architecture: {}, Name: {}, Version: {}, TZ: {}, Temp Dir: {}"
      + "\n\tJava Vendor: {} Java Version: {}"
      + "\n\tJava Home: {}"
      + "\n----------------------------------------------------------", new Object[]{
        env.getProperty("server.port"), 
        NetUtils.getLocalHostAddress(), 
        env.getProperty("server.port"), 
        Utils.humanReadableByteCount(Runtime.getRuntime().totalMemory(), false),
        Utils.humanReadableByteCount(Runtime.getRuntime().freeMemory(), false),
        Utils.humanReadableByteCount(Runtime.getRuntime().maxMemory(), false),
        Runtime.getRuntime().availableProcessors(),
        env.getProperty("spring.application.name"), 
        env.getProperty("info.description", "no description"), 
        env.getProperty("info.stage", "unknown"), 
        env.getProperty("info.version", "add your version in application.properties info.version=${project.version}"),
        env.getProperty("info.buildDate", "add your build date in application.properties info.buildDate=${maven.build.timestamp}"),
        properties.get("os.arch"),
        properties.get("os.name"),
        properties.get("os.version"),
        ZoneId.systemDefault().toString(),
        FileUtils.getTempDirectoryPath(),
        properties.get("java.vendor"),
        properties.get("java.version"),
        properties.get("java.home")
      });
  }
}
