package com.jiggy.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * Utility to access Shiro session info.
 * 
 * @author jmalkan
 */
public class SessionUtil {
  public static final String USER_PROFILE_ATTRIBUTE = UserProfile.class.getName();
  public static final String REFRESH_PERMISSION = "refreshPermission";
  
  public static Subject getCurrentUser() {
      return SecurityUtils.getSubject();
  }
  
  public static Session getSession() {
      return getCurrentUser().getSession();
  }
  
  public static Object getAttribute(final String attribute) {
      return getSession().getAttribute(attribute);
  }
  
  public static void setAttribute(final String attribute, final Object value) {
      getSession().setAttribute(attribute, value);
  }
  
  public static UserProfile getUserProfile() {
      return (UserProfile) getAttribute(USER_PROFILE_ATTRIBUTE);
  }
  
  public static void setUserProfile(final UserProfile profile) {
      setAttribute(USER_PROFILE_ATTRIBUTE, profile);
  }
  
  public static void setRefreshPermission() {
    setAttribute(REFRESH_PERMISSION, Boolean.TRUE);
  }

  public static void clearPermissionRefreshNeeded() {
    getSession().removeAttribute(REFRESH_PERMISSION);
  }

  public static boolean isPermissionRefreshNeeded() {
    return getSession().getAttribute(REFRESH_PERMISSION) == Boolean.TRUE;
  }
}