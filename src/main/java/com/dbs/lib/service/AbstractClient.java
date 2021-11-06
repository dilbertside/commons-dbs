/**
 * AbstractClient
 */
package com.dbs.lib.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NoRouteToHostException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.dbs.lib.Utils;
import com.dbs.lib.dto.NetworkTestRequest;
import com.dbs.lib.dto.NetworkTestResponse;
import com.dbs.lib.dto.SimpleResponse;
import com.dbs.lib.dto.UserDto;
import com.dbs.lib.dto.enumeration.ErrorCode;
import com.dbs.lib.exception.PingException;
import com.dbs.lib.net.NetUtils;


/**
 * REST client base class with common methods
 * 
 * @author dbs at 9 Oct 2019 23:37:22
 * @since 1.0.0
 * @version 1.0
 */
@SuppressWarnings("deprecation")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.extern.slf4j.Slf4j
public class AbstractClient {

  public static final ParameterizedTypeReference<SimpleResponse<String>> typeString =
      new ParameterizedTypeReference<SimpleResponse<String>>() {};
  public static final ParameterizedTypeReference<SimpleResponse<Long>> typeLong =
      new ParameterizedTypeReference<SimpleResponse<Long>>() {};
  public static final ParameterizedTypeReference<SimpleResponse<UserDto>> typeUserDto =
      new ParameterizedTypeReference<SimpleResponse<UserDto>>() {};
  
  protected AsyncRestTemplate asyncRestTemplate;
  protected final RestTemplate restTemplate;
  @NotNull
  protected String scheme;
  @NotNull
  protected String host;
  @NotNull
  protected int port;
  @NotNull
  protected int timeout;
  protected String user;
  protected String pw;
  protected String header;
  protected HttpHeaders headers;
  protected HttpHeaders headersPlain = new HttpHeaders();
  protected String pathPing;
  protected String pathUserCreate;
  protected String pathUserUpdate;
  protected String pathUserGet;
  protected String pathStats;
  protected String pathAuditCsvAll;
  protected String ipOrigin;
  
  public AbstractClient(AsyncRestTemplate asyncRestTemplate) {
    this((RestTemplate)asyncRestTemplate.getRestOperations());
    this.asyncRestTemplate = asyncRestTemplate;
  }

  /**
   * 
   * @param restTemplate {@link RestTemplate} to set
   */
  public AbstractClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    headers = getDefaultJsonHeaders();
    headersPlain.setContentType(MediaType.TEXT_PLAIN);
    headersPlain.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
    headersPlain.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.TEXT_PLAIN));
    pathPing = StringUtils.defaultString(pathPing, "/api/support/ping");
    pathUserCreate = StringUtils.defaultString(pathUserCreate, "/api/support/user/create");
    pathUserUpdate = StringUtils.defaultString(pathUserUpdate, "/api/support/user/update");
    pathUserGet = StringUtils.defaultString(pathUserGet, "/api/support/user/get");
    pathStats = StringUtils.defaultString(pathStats, "/api/support/stats");
    pathAuditCsvAll = StringUtils.defaultString(pathAuditCsvAll, "/api/support/csvAll");
  }

  /**
   * create a HttpHeaders object with  ContentType, AcceptCharset set
   * @return {@link HttpHeaders}
   */
  protected HttpHeaders getDefaultJsonHeaders() {
    HttpHeaders h = new HttpHeaders();
    h.setContentType(MediaType.APPLICATION_JSON_UTF8);
    h.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
    h.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.TEXT_PLAIN));
    return h;
  }
  /**
   * build uri with default {@link #host}, {@link #port}, {@link #scheme} and path
   * @param path url path
   * @return {@link URI}
   * @throws UnsupportedEncodingException if encoding error
   */
  protected URI buildUri(String path) throws UnsupportedEncodingException {
    return buildUri(path, null);
  }
  
  /**
   * build uri with default {@link #host}, {@link #port}, {@link #scheme} and path and params<br>
   * example /xxx/send?username={username}&password={password}&to={to}&text={text}&from={from}
   * 
   * @param path url path
   * @param params url nullable request parameters
   * @return {@link URI}
   * @throws UnsupportedEncodingException if encoding error
   */
  protected URI buildUri(String path, @Nullable MultiValueMap<String, String> params) throws UnsupportedEncodingException {
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme(scheme)
        .host(host)
        .port(port)
        .path(path)
        .queryParams(params)
        .build()
        .encode();
    return uriComponents.toUri();
  }
  
  /**
   * build uri with default {@link #host}, {@link #port}, {@link #scheme} and path and params<br>
   * example /xxx/send?username={username}&password={password}&to={to}&text={text}&from={from}
   * 
   * @param path url path
   * @param params url nullable request parameters
   * @param uriVariableValues ordered URI variables
   * @return {@link URI}
   * @throws UnsupportedEncodingException if encoding error
   */
  protected URI buildUri(String path, @Nullable MultiValueMap<String, String> params, @Nonnull Object... uriVariableValues) throws UnsupportedEncodingException {
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme(scheme)
        .host(host)
        .port(port)
        .path(path)
        .queryParams(params)
        .buildAndExpand(uriVariableValues)
        .encode();
    return uriComponents.toUri();
  }
  /**
   * ping with icmp if the server is up and running
   * 
   * @return true if server is up
   * @throws PingException
   * 
   */
  public Pair<Boolean, String> ping() throws PingException {
    return NetUtils.ping(host);
  }
  
  /**
   * ping or network Test
   * @param request {@link NetworkTestRequest}
   * @return {@link NetworkTestResponse}, if fail will put exception msg in response message
   */
  public NetworkTestResponse ping(NetworkTestRequest request) {
    if (null == request) {
      throw new IllegalArgumentException("argument request missing");
    }
    NetworkTestResponse resp =  new NetworkTestResponse(request);
    try {
      ResponseEntity<NetworkTestResponse> ret = restTemplate.postForEntity(
          buildUri(pathPing), 
          new HttpEntity<NetworkTestRequest>(request, headers), 
          NetworkTestResponse.class);
      if (null != ret && ret.hasBody()) {
        resp = ret.getBody();
      }
    } catch (Exception e) {
      resp.setMessage(handleException(e));
    }
    return resp;
  }

  /**
   * @param scheme the scheme to set
   * 
   */
  public void setScheme(String scheme) {
    if (StringUtils.isNotBlank(host)) {
      this.scheme = scheme;
    }
  }

  /**
   * @param host the host to set
   * 
   */
  public void setHost(String host) {
    if (StringUtils.isNotBlank(host)) {
      this.host = host;
    }
  }

  /**
   * @param user the new user to set
   * @param password the new password to set
   * 
   */
  public void setBasicAuthentication(String user, String password) {
    this.user = user;
    this.pw = password;
    if (StringUtils.isNoneBlank(this.user, this.pw)) {
      updateBasicAuthorization();
    }
  }

  protected void updateBasicAuthorization() {
    for (Iterator<ClientHttpRequestInterceptor> iterator = restTemplate.getInterceptors().iterator(); iterator.hasNext();) {
      ClientHttpRequestInterceptor interceptor = iterator.next();
      if (interceptor instanceof BasicAuthorizationInterceptor || interceptor instanceof BasicAuthenticationInterceptor ) {
        restTemplate.getInterceptors().remove(interceptor);
        break;
      }
    }
    restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user, pw));
  }

  /**
   * update the underlying {@link ClientHttpRequestFactory} Connect Timeout
   * 
   * @param timeout the timeout to set in msec (0 means infinite)
   * 
   */
  public void setTimeout(int timeout) {
    this.timeout = timeout;
    if (restTemplate.getRequestFactory() instanceof HttpComponentsClientHttpRequestFactory) {
      HttpComponentsClientHttpRequestFactory requestFactory = (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
      requestFactory.setConnectTimeout(timeout);
    }
    if (null != asyncRestTemplate) {
      if (asyncRestTemplate.getAsyncRequestFactory() instanceof HttpComponentsAsyncClientHttpRequestFactory) {
        HttpComponentsAsyncClientHttpRequestFactory requestFactory = (HttpComponentsAsyncClientHttpRequestFactory) asyncRestTemplate.getAsyncRequestFactory();
        requestFactory.setConnectTimeout(timeout);
      }
    }
  }

  /**
   * 
   * @return root URI
   */
  public String getRootUri() {
    try {
      return buildUri("/").toString();
    } catch (UnsupportedEncodingException e) {
      return Utils.errorMessage(e);
    }
  }

  /**
   * helper to set at once REST server common parameters
   * @param scheme server
   * @param host server
   * @param port server
   * @param ipOrigin host address from the user perspective, track IP origin from which this client is called
   */
  public void setParams(String scheme, String host, Integer port, String  ipOrigin) {
    if ( null != scheme) {
      this.scheme = scheme;
    }
    if ( null != host) {
      this.host = host;
    }
    if ( null != port) {
      this.port = port;
    }
    if ( null != ipOrigin) {
      this.ipOrigin = ipOrigin;
    }
  }
  
  /**
   * handle {@link HttpServerErrorException} and other to extract a message
   * @param e {@link Exception} to handle
   * @return error message
   */
  public String handleException(Exception e) {
    String msg;
    if (e instanceof HttpServerErrorException) {
      HttpServerErrorException ex = (HttpServerErrorException) e;
      msg = String.format("%s, %s", ex.getStatusCode(), ex.getStatusCode().getReasonPhrase());
    } else if (e instanceof HttpClientErrorException) {
      HttpClientErrorException ex = (HttpClientErrorException) e;
      msg = String.format("%s, %s, body [%s]", ex.getStatusCode(), ex.getStatusCode().getReasonPhrase(), ex.getResponseBodyAsString());
    } else if (e instanceof HttpStatusCodeException) {
      HttpStatusCodeException ex = (HttpStatusCodeException) e;
      msg = String.format("%s, %s", ex.getStatusCode(), ex.getStatusCode().getReasonPhrase());
    } else if (e instanceof RestClientResponseException) {
      RestClientResponseException ex = (RestClientResponseException) e;
      msg = String.format("%d, %s", ex.getRawStatusCode(), ex.getStatusText());
    } else if (e instanceof NoRouteToHostException) {
      NoRouteToHostException ex = (NoRouteToHostException) e;
      msg = String.format("problem with host [%s], error: %s", host, Utils.errorMessage(ex));
    } else {
      msg = Utils.errorMessage(e);
    }
    return msg;
  }

  /**
   * Build a simple response from an {@link Exception}
   * @param e {@link Exception}
   * @return {@link SimpleResponse}
   */
  @SuppressWarnings("rawtypes")
	protected SimpleResponse extractException(Exception e) {
  	SimpleResponse<?> resp = null;
  	if ( e instanceof org.springframework.web.client.HttpClientErrorException) {
  		try {
  			resp = extractData(SimpleResponse.class, (HttpClientErrorException) e);
  		} catch (HttpMessageNotReadableException | IOException e1) {
  			log.error("failed to extract exception payload", e1);
  			resp = new SimpleResponse<>(ErrorCode.getByHttpStatus(((HttpClientErrorException)e).getStatusCode()),
  					((HttpClientErrorException)e).getResponseBodyAsString());
  		}
  	} 
  	if (null == resp)
  		resp = new SimpleResponse<>(ErrorCode.internalError, handleException(e));
		return resp;
	}

  /**
   * Build a simple response from an {@link Exception}
   * @apiNote unstable, not ready
   * @param clazz destination
   * @param e {@link Exception}
   * @return {@link SimpleResponse}
   */
  @SuppressWarnings("rawtypes")
	protected SimpleResponse extractException(Class clazz, Exception e) {
		return extractException(e);
	}

  
  /**
   * reuse message converters from {@link #restTemplate} to extract payload from {@link HttpClientErrorException}
   * @param <T>
   * @param clazz a context class for the target type
   * @param exc {@link HttpClientErrorException}
   * @return the converted object
   * @throws HttpMessageNotReadableException
   * @throws IOException
   */
	@SuppressWarnings("unchecked")
	protected <T> T extractData(Class<T> clazz, HttpClientErrorException exc) throws HttpMessageNotReadableException, IOException {
  	MediaType contentType = exc.getResponseHeaders().getContentType();
  	HttpInputMessage responseWrapper = new MappingJacksonInputMessage(new ByteArrayInputStream(exc.getResponseBodyAsByteArray()), exc.getResponseHeaders());
  	for (HttpMessageConverter<?> messageConverter : restTemplate.getMessageConverters()) {
  		if (messageConverter instanceof GenericHttpMessageConverter) {
  			GenericHttpMessageConverter<?> genericMessageConverter = (GenericHttpMessageConverter<?>) messageConverter;
  			if (genericMessageConverter.canRead(clazz, null, contentType)) {
  				log.debug("Reading to [{}]", ResolvableType.forType(clazz));
  				return (T) genericMessageConverter.read(clazz, null, responseWrapper);
  			}
  		}
		}
		return null;
	}
  /**
   * 
   * @return root URI
   */
  public String getUriPing() {
    try {
      return buildUri(pathPing).toString();
    } catch (UnsupportedEncodingException e) {
      return Utils.errorMessage(e);
    }
  }
  
  /**
   * helper to retrieve a formated URL
   * @return URL path User Create
   */
  public String getUriUserCreate() {
    try {
      return buildUri(pathUserCreate).toString();
    } catch (UnsupportedEncodingException e) {
      return Utils.errorMessage(e);
    }
  }
  
  /**
   * helper to retrieve a formated URL
   * @return URL path User Update
   */
  public String getUriUserUpdate() {
    try {
      return buildUri(pathUserUpdate).toString();
    } catch (UnsupportedEncodingException e) {
      return Utils.errorMessage(e);
    }
  }
  
  /**
   * helper to retrieve a formated URL
   * @return URL path User Get
   */
  public String getUriUserGet() {
    try {
      return buildUri(pathUserGet).toString();
    } catch (UnsupportedEncodingException e) {
      return Utils.errorMessage(e);
    }
  }
  
  /**
   * helper to retrieve a formated URL
   * @return URL path stats
   */
  public String getUriStats() {
    try {
      return buildUri(pathStats).toString();
    } catch (UnsupportedEncodingException e) {
      return Utils.errorMessage(e);
    }
  }
  
  /**
   * helper to retrieve a formated URL
   * @return URL path AuditCsvAll
   */
  public String getUriAuditCsvAll() {
    try {
      return buildUri(pathAuditCsvAll).toString();
    } catch (UnsupportedEncodingException e) {
      return Utils.errorMessage(e);
    }
  }
  /**
   * Security user creation
   * @param request {@link UserDto}
   * @return {@link SimpleResponse} message, and ErrorCode for error if any
   */
  public SimpleResponse<UserDto> userCreate(UserDto request) {
    if (null == request) {
      throw new IllegalArgumentException("argument request missing");
    }
    SimpleResponse<UserDto> resp = null;
    try {
      ResponseEntity<SimpleResponse<UserDto>> ret = restTemplate.exchange(
          buildUri(pathUserCreate), 
          HttpMethod.POST,
          new HttpEntity<UserDto>(request, headers),
          typeUserDto);
      if (null != ret && ret.hasBody()) {
        if (ret.getStatusCode() != HttpStatus.OK) {
          log.warn("Error calling user create with code: {}, msg: {}", ret.getStatusCode(), ret.getBody());
        } else {
          resp = ret.getBody();
        }
      } else {
        resp = new SimpleResponse<>(ErrorCode.internalError, "REST response has no body");
      }
    } catch (Exception e) {
      resp = new SimpleResponse<>(ErrorCode.internalError, handleException(e));
      log.warn("ERROR userCreate {}, [{}]", e.getClass().getSimpleName(), resp);
    }
    return  resp;
  }
  
  /**
   * Security user update
   * @param request {@link UserDto}
   * @return {@link SimpleResponse} message, and ErrorCode for error if any
   */
  public SimpleResponse<UserDto> userUpdate(UserDto request) {
    if (null == request) {
      throw new IllegalArgumentException("argument request missing");
    }
    SimpleResponse<UserDto> resp = null;
    try {
      ResponseEntity<SimpleResponse<UserDto>> ret = restTemplate.exchange(
          buildUri(pathUserUpdate), 
          HttpMethod.POST,
          new HttpEntity<UserDto>(request, headers),
          typeUserDto);
      if (null != ret && ret.hasBody()) {
        if (ret.getStatusCode() != HttpStatus.OK) {
          log.warn("Error calling user update with code: {}, msg: {}", ret.getStatusCode(), ret.getBody());
        } else {
          resp = ret.getBody();
        }
      } else {
        resp = new SimpleResponse<>(ErrorCode.internalError, "REST response has no body");
      }
    } catch (Exception e) {
      resp = new SimpleResponse<>(ErrorCode.internalError, handleException(e));
      log.warn("ERROR userUpdate {}, [{}]", e.getClass().getSimpleName(), resp);
    }
    return  resp;
  }
  
  /**
   * Security user read
   * @param loginId user login ID
   * @return {@link SimpleResponse} message, and ErrorCode for error if any
   */
  public SimpleResponse<UserDto> userGet(String loginId) {
    if (null == loginId) {
      throw new IllegalArgumentException("argument request missing");
    }
    SimpleResponse<UserDto> resp = null;
    try {
      MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
      params.add("login", loginId);
      ResponseEntity<SimpleResponse<UserDto>> ret = restTemplate.exchange(
          buildUri(pathUserGet, params), 
          HttpMethod.GET,
          new HttpEntity<String>(headers),
          typeUserDto);
      if (null != ret && ret.hasBody()) {
        if (ret.getStatusCode() != HttpStatus.OK) {
          log.warn("Error calling user read with code: {}, msg: {}", ret.getStatusCode(), ret.getBody());
        } else {
          resp = ret.getBody();
        }
      } else {
        resp = new SimpleResponse<>(ErrorCode.internalError, "REST response has no body");
      }
    } catch (Exception e) {
      resp = new SimpleResponse<>(ErrorCode.internalError, handleException(e));
      log.warn("ERROR userGet {}, [{}]", e.getClass().getSimpleName(), resp);
    }
    return  resp;
  }
  

//CHECKSTYLE:OFF
  /**
   * <code>
   * new ListenableFutureCallback<ResponseEntity<String>>() {
    &#64;Override
    public void onSuccess(ResponseEntity<String> entity) {
      if (null != entity && entity.hasBody()) {
        msg = ret.getBody();
        if (entity.getStatusCode() != HttpStatus.OK) {
          log.warn("Error calling stats with code: {}, msg: {}", entity.getStatusCode(), entity.getBody());
        }
      }
    }
  
    &#64;Override
    public void onFailure(Throwable t) {
        msg = handleException(e);
    }
   * </code>
   * 
   * @param from {@link LocalDate}
   * @param to {@link LocalDate}
   * @param callback {@link ListenableFuture} set null for synchronous call
   * @return {@link SimpleResponse<?>} with csv content file in message, never null
   */
//CHECKSTYLE:ON
  @SuppressWarnings("unchecked")
  public SimpleResponse<String> stats(LocalDate from, LocalDate to, ListenableFuture<ResponseEntity<String>> callback) {
    SimpleResponse<String> resp = null;
    try {
      MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
      if (null != from) {
        params.add("from", from.toString());
      }
      if (null != to) {
        params.add("to", to.toString());
      }
      if (null != callback) {
        ListenableFuture<ResponseEntity<SimpleResponse<String>>> futureEntity = asyncRestTemplate.exchange(
            buildUri(pathStats, params), 
            HttpMethod.GET,
            new HttpEntity<String>(headers),
            typeString);
        futureEntity.addCallback((ListenableFutureCallback<? super ResponseEntity<SimpleResponse<String>>>) callback);
      } else {
        ResponseEntity<SimpleResponse<String>> ret = restTemplate.exchange(
            buildUri(pathStats, params), 
            HttpMethod.GET,
            new HttpEntity<String>(headers),
            typeString);
        if (null != ret && ret.hasBody()) {
          if (ret.getStatusCode() != HttpStatus.OK) {
            log.warn("Error calling stats with code: {}, msg: {}", ret.getStatusCode(), ret.getBody());
          } else {
            resp = ret.getBody();
          }
        } else {
          resp = new SimpleResponse<>(ErrorCode.internalError, "REST response has no body");
        }
      }
    } catch (Exception e) {
      resp = new SimpleResponse<>(ErrorCode.internalError, handleException(e));
      log.warn("ERROR stats {}, [{}]", e.getClass().getSimpleName(), resp);
    }
    return resp;
  }
  
  /**
   * @param from {@link LocalDate}
   * @param to {@link LocalDate}
   * @return {@link SimpleResponse} with property csv containing the file and count prop for the number of rows
   */
  public SimpleResponse<String> auditCsv(LocalDate from, LocalDate to) {
    SimpleResponse<String> resp = null;
    try {
      MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
      if (null != from) {
        params.add("from", from.toString());
      }
      if (null != to) {
        params.add("to", to.toString());
      }
      ResponseEntity<SimpleResponse<String>> ret = restTemplate.exchange(
          buildUri(pathAuditCsvAll, params), 
          HttpMethod.GET,
          new HttpEntity<String>(headers), 
          typeString);
      if (null != ret && ret.hasBody()) {
        if (ret.getStatusCode() != HttpStatus.OK) {
          log.warn("Error calling audit CSV with code: {}, msg: {}", ret.getStatusCode(), ret.getBody());
        } else {
          resp = ret.getBody();
        }
      } else {
        resp = new SimpleResponse<>(ErrorCode.internalError, "REST response has no body");
      }
    } catch (Exception e) {
      resp = new SimpleResponse<>(ErrorCode.internalError, handleException(e));
      log.warn("ERROR audit CSV {}, [{}]", e.getClass().getSimpleName(), resp);
    }
    return resp;
  }
}