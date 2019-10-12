/**
 * SecurityUtils
 */
package com.dbs.lib.security;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;
import javax.servlet.http.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.dbs.lib.Defaults;
import com.google.common.net.HttpHeaders;

/**
 * @author dbs on Nov 2, 2016 10:09:25 AM
 * @since 0.1.4
 * @version 1.0
 *
 */
@lombok.experimental.UtilityClass
public class SecurityUtils {

  public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";
  
  public static final String ADMIN = "ROLE_ADMIN";

  public static final String USER = "ROLE_USER";

  public static final String ANONYMOUS = "ROLE_ANONYMOUS";
  
  public static final String SYSADMIN = "ROLE_SYSADMIN";
  
  public static final String API = "ROLE_API";
  
  public static final String SYSTEM_ACCOUNT = "system";

  /**
   * Specialised URI builder {@link UriComponentsBuilder} which expect some headers set by NGINX proxy
   * 
   * * http://docs.spring.io/spring-boot/docs/current/reference/html/howto-security.html#howto-enable-https
   * <br>
   * to activate do not forget to add following in properties
   * server.tomcat.remote-ip-header=x-forwarded-for
   * server.tomcat.protocol-header=x-forwarded-proto
   * <br>
   * Nginx configuration in /etc/nginx/nginx.conf<br>
   * 
   * <pre>
   *  location / {
        proxy_pass http://10.76.231.68:10014/;
        proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header   X-Forwarded-Host $server_name;
        proxy_set_header   X-Forwarded-Proto $scheme;
        proxy_set_header   Host $http_addr;
        proxy_set_header   X-Forwarded-Server $host;
        proxy_set_header   X-Forwarded-Port $server_port;
        proxy_set_header   X-Real-IP $remote_addr;
        #proxy_read_timeout 3600;
        proxy_redirect     http://10.76.231.68:10014 https://inno-dev.sampoernatelekom.com;
      }
   * </pre>
   * 
   * <br>
   * Example header values:<br>
   * x-forwarded-host=[inno-dev.sampoernatelekom.com]
   * x-forwarded-proto=[https]
   * x-forwarded-server=[inno-dev.sampoernatelekom.com]
   * x-real-ip=[172.16.12.62]
   * x-forwarded-port=[443]
   * 
   * @param request {@link HttpServletRequest}
   * @param url URL path if null, "/" will be used
   * @return full url encoded, or empty string if request is null
   */
  public static String buildUrlForReverseProxyRequest(@Nullable HttpServletRequest request, @Nullable String url) {
    if (null == request) {
      return "";
    }
    // HttpHeaders httpHeaders = new ServletServerHttpRequest(request).getHeaders();
    String scheme = StringUtils.defaultString(request.getHeader(HttpHeaders.X_FORWARDED_PROTO), "https");
    String serverName = StringUtils.defaultString(request.getHeader(HttpHeaders.X_FORWARDED_HOST));
    int serverPort = NumberUtils.toInt(request.getHeader(HttpHeaders.X_FORWARDED_PORT), 443);

    // detection if url contains query string
    String[] a = StringUtils.split(StringUtils.defaultString(url, "/"), '?');
    String requestUri = a[0];
    String queryString = request.getQueryString();
    if (a.length > 1 && StringUtils.isNotBlank(a[1])) {
      queryString = a[1];
    }
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme(scheme)
        .host(serverName)
        .port(serverPort)
        .path(requestUri)
        .query(queryString)
        .build()
        .encode(StandardCharsets.UTF_8);
    return uriComponents.toUriString();
  }
  
  /**
   * extract from org.springframework.boot.context.properties.ConfigurationProperties a triplet of multiple values for configurable creation of security users
   * <br>
   * for roles if the list is not equal will use the first one
   * @param userNames {@link Set} of user name
   * @param userPasswords {@link Set} of user password
   * @param userRoles  set of roles
   * @param i number in the list to retrieve
   * @return to {@link Triple} of username, password and roles array
   */
  public static Triple<String, String, String[]> extractUser(Collection<String> userNames, Collection<String> userPasswords, String userRoles, int i) {
    Assert.notEmpty(userNames, "userNames cannot be empty");
    Assert.notEmpty(userPasswords, "userPasswords cannot be empty");
    Assert.state(userNames.size() == userPasswords.size(), "user names and user passwords not same length");
    String username = (String) userNames.toArray()[i];
    String pw = (String) userPasswords.toArray()[i];
    String[] roles = StringUtils.splitPreserveAllTokens(StringUtils.splitPreserveAllTokens(userRoles, ',')[i], Defaults.FIELD_SEPARATOR);
    return Triple.of(username, pw, roles);
  }
  
  /**
   * extract from org.springframework.boot.context.properties.ConfigurationProperties a triplet of multiple values for configurable creation of security users
   * <br>
   * for roles if the list is not equal will use the first one
   * @param userNames  of user names
   * @param userPasswords  of user passwords
   * @param userRoles  set of roles
   * @param i number in the list to retrieve
   * @return to {@link Triple} of username, password and roles array
   */
  public static Triple<String, String, String[]> extractUser(String[] userNames, String[] userPasswords, String userRoles, int i) {
    Assert.notEmpty(userNames, "userNames cannot be empty");
    Assert.notEmpty(userPasswords, "userPasswords cannot be empty");
    Assert.state(userNames.length == userPasswords.length, "userNames and userPasswords not same length");
    String[] roles = StringUtils.splitPreserveAllTokens(StringUtils.splitPreserveAllTokens(userRoles, ',')[i], Defaults.FIELD_SEPARATOR);
    return Triple.of(userNames[i], userPasswords[i], roles);
  }
  
  /**
   * Get the login of the current user.
   *
   * @return the login of the current user
   */
  public static String getCurrentUserLogin() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    String userName = null;
    if (authentication != null) {
      if (authentication.getPrincipal() instanceof UserDetails) {
        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
        userName = springSecurityUser.getUsername();
      } else if (authentication.getPrincipal() instanceof String) {
        userName = (String) authentication.getPrincipal();
      } /*else if (authentication.getPrincipal() instanceof LdapUserDetails) {
        LdapUserDetails ldapUser = (LdapUserDetails) authentication.getPrincipal();
        return ldapUser.getUsername();
      }*/
    }
    return userName;
  }

  /**
   * Check if a user is authenticated.
   *
   * @return true if the user is authenticated, false otherwise
   */
  public static boolean isAuthenticated() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    if (authentication != null) {
      Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
      if (authorities != null) {
        for (GrantedAuthority authority : authorities) {
          if (authority.getAuthority().equals(ANONYMOUS)) {
            return false;
          }
        }
      }
      return true;
    }
    return false;
  }

  /**
   * If the current user has a specific authority (security role).
   *
   * <p>The name of this method comes from the isUserInRole() method in the Servlet API</p>
   *
   * @param authority the authority to check
   * @return true if the current user has the authority, false otherwise
   */
  public static boolean isCurrentUserInRole(String authority) {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    if (authentication != null) {
      if (authentication.getPrincipal() instanceof UserDetails) {
        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
        return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(authority));
      }
    }
    return false;
  }
  
  /**
   * Get the JWT of the current user.
   *
   * @return the JWT of the current user.
   */
  public static Optional<String> getCurrentUserJWT() {
      SecurityContext securityContext = SecurityContextHolder.getContext();
      return Optional.ofNullable(securityContext.getAuthentication())
          .filter(authentication -> authentication.getCredentials() instanceof String)
          .map(authentication -> (String) authentication.getCredentials());
  }
}
