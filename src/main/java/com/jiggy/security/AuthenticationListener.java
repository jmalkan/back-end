package com.jiggy.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Listens for Authentication and sets user profile.
 * 
 * @author jmalkan
 */
public class AuthenticationListener implements org.apache.shiro.authc.AuthenticationListener {
  private static final Logger logger = LoggerFactory.getLogger(AuthenticationListener.class);
  
  @Autowired
  private UserService userService;
  
  @Override
  public void onSuccess(final AuthenticationToken token, final AuthenticationInfo info) {
    logger.info("AuthenticationListener.onSuccess");
    
    Object primaryPrincipal = info.getPrincipals().getPrimaryPrincipal();
    
    if (primaryPrincipal instanceof UserPrincipal) {
      UserPrincipal userPrincipal = (UserPrincipal) primaryPrincipal;
      User user = this.userService.findById(userPrincipal.getId());
      UserProfile userProfile = new UserProfile(user);
      
      SessionUtil.setUserProfile(userProfile);
    }
  }
  
  @Override
  public void onFailure(AuthenticationToken token, AuthenticationException ae) {
    logger.info("AuthenticationListener.onFailure");
    return;
  }
  
  @Override
  public void onLogout(PrincipalCollection principals) {
    logger.info("AuthenticationListener.onLogout");
    return;
  }
}