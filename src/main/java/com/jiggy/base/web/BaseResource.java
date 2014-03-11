package com.jiggy.base.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

/**
 * An base resource containing methods common to all resources Primarily access to HTTP request, response and session
 * that then provides services via its configured bindings.
 * 
 * @author jmalkan
 */
//@RequestScoped
public class BaseResource {
  /** The HttpServlrtRequest Object. */
  @Context
  private HttpServletRequest request;
  
  /** The HttpServletResponse Object. */
  @Context
  private HttpServletResponse response;
  
  /** The ServletContext Object. */
  @Context
  private ServletContext servletContext;
  
  /** The HttpHeaders Object. */
  @Context
  private HttpHeaders headers;
  
  /** The UriInfo Object. */
  @Context
  private UriInfo uriInfo;
  
  /**
   * Getter of the property <tt>request</tt>
   * 
   * @return the request
   */
  public HttpServletRequest getRequest() {
    return request;
  }
  
  /**
   * Setter of the property <tt>request</tt>
   * 
   * @param request the request to set
   */
  public void setRequest(final HttpServletRequest request) {
    this.request = request;
  }
  
  /**
   * Getter of the property <tt>response</tt>
   * 
   * @return the response
   */
  public HttpServletResponse getResponse() {
    return response;
  }
  
  /**
   * Setter of the property <tt>response</tt>
   * 
   * @param response the response to set
   */
  public void setResponse(final HttpServletResponse response) {
    this.response = response;
  }
  
  /**
   * Getter of the property <tt>servletContext</tt>
   * 
   * @return the servletContext
   */
  public ServletContext getServletContext() {
    return servletContext;
  }
  
  /**
   * Setter of the property <tt>servletContext</tt>
   * 
   * @param servletContext the servletContext to set
   */
  public void setServletContext(final ServletContext servletContext) {
    this.servletContext = servletContext;
  }
  
  /**
   * Getter of the property <tt>headers</tt>
   * 
   * @return the headers
   */
  public HttpHeaders getHeaders() {
    return headers;
  }
  
  /**
   * Setter of the property <tt>headers</tt>
   * 
   * @param headers the headers to set
   */
  public void setHeaders(final HttpHeaders headers) {
    this.headers = headers;
  }
  
  /**
   * Getter of the property <tt>uriInfo</tt>
   * 
   * @return the uriInfo
   */
  public UriInfo getUriInfo() {
    return uriInfo;
  }
  
  /**
   * Setter of the property <tt>uriInfo</tt>
   * 
   * @param uriInfo the uriInfo to set
   */
  public void setUriInfo(final UriInfo uriInfo) {
    this.uriInfo = uriInfo;
  }
  
  /**
   * @return the session
   */
  public HttpSession getSession() {
    return getRequest().getSession();
  }
  
  @Override
  public String toString() {
    return "BaseResource [request=" + request + ", response=" + response + ", session=" + getSession() + "]";
  }
}
