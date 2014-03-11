package com.jiggy.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Extension of the basic forms authentication filter which supports: -forcing redirect to change password screen
 * if user is marked as such -default start pages per user role
 * 
 * @author jmalkan
 */
public class UserFormAuthenticationFilter extends FormAuthenticationFilter {
  private static final Logger logger = LoggerFactory.getLogger(UserFormAuthenticationFilter.class);
  
  @Autowired
  private UserService userService;
  
  @Override
  protected boolean onLoginSuccess(final AuthenticationToken token, final Subject subject, final ServletRequest request, final ServletResponse response) throws Exception {
    logger.info("onLoginSuccess");
    
    Object primaryPrincipal = subject.getPrincipals().getPrimaryPrincipal();
    
    if (primaryPrincipal instanceof UserPrincipal) {
      UserPrincipal userPrincipal = (UserPrincipal) primaryPrincipal;
      User user = this.userService.findById(userPrincipal.getId());
      UserProfile userProfile = new UserProfile(user);
      
      SessionUtil.setUserProfile(userProfile);
    }
    return super.onLoginSuccess(token, subject, request, response);
  }
  
  @Override
  protected boolean onLoginFailure(final AuthenticationToken token, final AuthenticationException e, final ServletRequest request, final ServletResponse response) {
    logger.info("onLoginFailure");
    return super.onLoginFailure(token, e, request, response);
  }
  
  @Override
  protected boolean onAccessDenied(final ServletRequest request, final ServletResponse response) throws Exception {
    logger.info("onAccessDenied WebUtils.toHttp(request).getRequestURL()= {}", WebUtils.toHttp(request).getRequestURL());
    
    return super.onAccessDenied(request, response);
  }
  
  @Override
  protected void setFailureAttribute(final ServletRequest request, final AuthenticationException ae) {
    logger.info("setFailureAttribute");
    String message = ae.getMessage();
    request.setAttribute("error", "true");
    request.setAttribute(getFailureKeyAttribute(), message);
  }
}