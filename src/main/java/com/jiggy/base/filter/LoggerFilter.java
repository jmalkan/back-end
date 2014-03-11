package com.jiggy.base.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.jiggy.security.SessionUtil;
import com.jiggy.security.User;
import com.jiggy.security.UserProfile;

/**
 * This filter registers Diagnostic information to the Logger.
 * 
 * @author jmalkan
 */
public class LoggerFilter implements Filter {
  private final static Logger logger = LoggerFactory.getLogger(LoggerFilter.class);

  private static final String USER_KEY = "userName";
  private static final String SESSION_KEY = "sessionId";
  private final static String REQUEST_USER_AGENT_KEY = "req.userAgent";
  private final static String REQUEST_REMOTE_HOST_KEY = "req.remoteHost";
  private final static String REQUEST_REQUEST_URI_KEY = "req.requestURI";
  private final static String REQUEST_REQUEST_URL_KEY = "req.requestURL";
  private final static String REQUEST_QUERY_STRING_KEY = "req.queryString";
  private final static String REQUEST_X_FORWARDED_FOR_KEY = "req.xForwardedFor";
  
  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    // NO-OP
  }
  
  @Override
  public void destroy() {
    // NO-OP
  }
  
  @Override
  public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
    logger.trace("************insertIntoMDC************");
    
    insertIntoMDC(request);
    
    try {
      filterChain.doFilter(request, response);
    } finally {
      logger.trace("************clearMDC************");
      clearMDC();
    }
  }
  
  protected void insertIntoMDC(final ServletRequest request) {
    this.register(REQUEST_REMOTE_HOST_KEY, request.getRemoteHost());
    
    if (request instanceof HttpServletRequest) {
      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      StringBuffer requestURL = httpServletRequest.getRequestURL();
      
      User user = null;
      String userName = null;
      UserProfile userProfile = null;
      
      try {
        userProfile = SessionUtil.getUserProfile();
      } catch (Exception e) { /*Ignore*/ }
      
      if (userProfile != null) {
        user = userProfile.getUser();
        
        if (user != null)
          userName = user.getUserName();
      }
      
      if (userName == null) {
        Principal principal = httpServletRequest.getUserPrincipal();
        
        if (principal != null)
          userName = principal.getName();
        
        logger.trace("************principal************{}", principal);
      }
      
      if (userName == null) {
        logger.trace("************userName************{}", userName);
        this.register(USER_KEY, userName);
      }
      
      this.register(REQUEST_REQUEST_URI_KEY, httpServletRequest.getRequestURI());
      
      this.register(REQUEST_REQUEST_URL_KEY, requestURL.toString());
      this.register(SESSION_KEY, httpServletRequest.getSession().getId().toString());
      
      this.register(REQUEST_QUERY_STRING_KEY, httpServletRequest.getQueryString());
      this.register(REQUEST_USER_AGENT_KEY, httpServletRequest.getHeader("User-Agent"));
      this.register(REQUEST_X_FORWARDED_FOR_KEY, httpServletRequest.getHeader("X-Forwarded-For"));
    }
  }
  
  /**
   * Clears MDC entries.
   */
  protected void clearMDC() {
    MDC.clear();
//    MDC.remove(SESSION_KEY);
//    MDC.remove(USER_KEY);
//    
//    MDC.remove(REQUEST_REMOTE_HOST_KEY);
//    MDC.remove(REQUEST_REQUEST_URI_KEY);
//    MDC.remove(REQUEST_QUERY_STRING_KEY);
//    
//    // removing possibly inexistent item is OK
//    MDC.remove(REQUEST_USER_AGENT_KEY);
//    MDC.remove(REQUEST_REQUEST_URL_KEY);
//    MDC.remove(REQUEST_X_FORWARDED_FOR_KEY);
  }
  
  /**
   * Register the given key/value in the MDC.
   * 
   * @param key The Key
   * @param value The Value
   * 
   * @return true if the user can be successfully registered
   */
  private boolean register(final String key, final String value) {
    boolean returnValue = false;
    
    logger.trace("************registering=={}={}", key, value);
    
    if ((!StringUtils.isBlank(key)) && (!StringUtils.isBlank(value))) {
      logger.trace("************registered=={}={}", key, value);
      MDC.put(key, value);
      returnValue = true;
    }
    
    return returnValue;
  }
}
