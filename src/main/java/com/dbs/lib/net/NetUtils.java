/**
 * NetUtils
 */
package com.dbs.lib.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.lang.Nullable;

import com.dbs.lib.Utils;
import com.dbs.lib.exception.PingException;
import com.google.common.net.HttpHeaders;


/**
 * @author dbs on Sep 13, 2016 10:07:57 AM
 * @since 1.0.0
 * @version 1.0
 *
 */
@lombok.experimental.UtilityClass
public class NetUtils {

  /**
   * default timeout in milliseconds
   */
  public static int defaultTimeout = 10000;
  /**
   * default number of ping count to execute
   */
  public static int defaultPingCount = 3;

  static boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

  /**
   * execute default 3 ping count toward a host
   * 
   * @param host
   *          hostname or ip
   * @return true if command executed successfully
   * @throws PingException if ping fails
   */
  public static Pair<Boolean, String> ping(String host) throws PingException {
    return ping(host, defaultPingCount);
  }

  /**
   * 
   * @param host
   *          hostname or ip
   * @param count
   *          number of ping to execute
   * @return true if command executed successfully
   * @throws PingException if ping fails
   */
  public static Pair<Boolean, String> ping(String host, int count) throws PingException {
    if (StringUtils.isBlank(host)) {
      throw new PingException("host is invalid");
    }
    boolean success = false;
    String output = "";
    try {
      ProcessBuilder pb = new ProcessBuilder("ping", isWindows ? "-n" : "-c", "" + count, host);
      pb.redirectErrorStream(true);
      Process proc = pb.start();
      success = proc.waitFor(defaultTimeout, TimeUnit.MILLISECONDS);
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      output = IOUtils.toString(stdInput);
    } catch (IOException | InterruptedException e) {
      throw new PingException(e);
    }
    return Pair.of(success, output);
  }

  /**
   * get Host address which try to detect non docker interfaces
   * @return Host Address
   */
  @Nullable
  public static String getLocalHostAddress() {
    String result = null;
    Enumeration<NetworkInterface> interfaces = null;
    try {
      interfaces = NetworkInterface.getNetworkInterfaces();
    } catch (SocketException e) {
      return "";
    }

    if (interfaces != null) {
      while (interfaces.hasMoreElements() && StringUtils.isEmpty(result)) {
        NetworkInterface i = interfaces.nextElement();
        Enumeration<InetAddress> addresses = i.getInetAddresses();
        while (addresses.hasMoreElements() && (result == null || result.isEmpty())) {
          InetAddress address = addresses.nextElement();
          if (!address.isLoopbackAddress() && address.isSiteLocalAddress()) {
            if (StringUtils.containsAny(i.getName(), "docker", "br-", "virbr")) {
              continue;
            }
            //verbose(i, address);
            result = address.getHostAddress();
          }
        }
      }
    }
    return result;
  }

  /**
   * 
   * @param i {@link NetworkInterface}
   * @param address {@link InetAddress}
   */
  static void verbose(NetworkInterface i, InetAddress address) {
    System.out.println();
    System.out.println(i.getName() + " " + address.getHostAddress());
    System.out.println(Utils.toStringFormatted(i));
    System.out.println("  isAnyLocalAddress " + BooleanUtils.toStringYesNo(address.isAnyLocalAddress()));
    System.out.println("  isLinkLocalAddress " + BooleanUtils.toStringYesNo(address.isLinkLocalAddress()));
    System.out.println("  isMCGlobal " + BooleanUtils.toStringYesNo(address.isMCGlobal()));
    System.out.println("  isMCLinkLocal " + BooleanUtils.toStringYesNo(address.isMCLinkLocal()));
    System.out.println("  isMCNodeLocal " + BooleanUtils.toStringYesNo(address.isMCNodeLocal()));
    System.out.println("  isMCOrgLocal " + BooleanUtils.toStringYesNo(address.isMCOrgLocal()));
    System.out.println("  isMCSiteLocal " + BooleanUtils.toStringYesNo(address.isMCSiteLocal()));
    try {
      System.out.println("  isReachable " + BooleanUtils.toStringYesNo(address.isReachable(2000)));
    } catch (IOException e) {
      //
    }
  }

  /**
   * get an  address from an HTTP request 
   * @param request {@link HttpServletRequest}
   * @return ipAddress
   */
  public static String getHttpRequestRemoteHostAddress(HttpServletRequest request) {
    String ipAddress = ObjectUtils.firstNonNull(
    		request.getHeader(HttpHeaders.X_FORWARDED_FOR), 
    		request.getHeader(HttpHeaders.X_FORWARDED_HOST),
    		request.getRemoteAddr(),
    		request.getServerName());  
    return ipAddress;
  }
  
  /**
   * get an address from HTTP headers
   * @param httpHeaders
   * @return ipAddress
   */
  public static String getHttpRequestRemoteHostAddress(org.springframework.http.HttpHeaders httpHeaders) {
  	InetSocketAddress host = httpHeaders.getHost();
  	String ipAddress = host.getHostName();
  	if(host.isUnresolved()) {
  		InetSocketAddress ip = new InetSocketAddress(host.getHostName(), host.getPort());
  		ipAddress = ip.getAddress() != null ? ip.getAddress().getHostAddress() : ip.getHostName();
  	}
  	if (httpHeaders.containsKey(HttpHeaders.X_FORWARDED_FOR)) {
  		String ip = httpHeaders.get(HttpHeaders.X_FORWARDED_FOR).get(0);
  		ipAddress = resolveIp(ip);
  	}
  	if (httpHeaders.containsKey(HttpHeaders.X_FORWARDED_HOST)) {
  		String ip = httpHeaders.get(HttpHeaders.X_FORWARDED_HOST).get(0);
  		ipAddress = resolveIp(ip);
  	}
    return ipAddress;
  }
  
  private static String resolveIp(String ip) {
  	String value = ip;
  	String host = null;
		int port = 0;
		int separator = (value.startsWith("[") ? value.indexOf(':', value.indexOf(']')) : value.lastIndexOf(':'));
		if (separator != -1) {
			host = value.substring(0, separator);
			String portString = value.substring(separator + 1);
			try {
				port = Integer.parseInt(portString);
			}
			catch (NumberFormatException ex) {
				// ignore
			}
		}
		if (host == null) {
			host = value;
		}
		InetSocketAddress isa = new InetSocketAddress(host, port);
		String ipAddress = isa.getAddress() != null ? isa.getAddress().getHostAddress() : isa.getHostName();
		return ipAddress;
	}
}
