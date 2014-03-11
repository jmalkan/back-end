package com.jiggy.base.web.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

import com.jiggy.base.exception.AbstractException;
import com.jiggy.base.exception.Error;
import com.jiggy.base.exception.SystemException;
import com.jiggy.base.exception.ValidationException;

/**
 * Renders a response with a RESTful Error representation.
 * 
 * @author jmalkan
 */
public class RestExceptionHandler extends AbstractHandlerExceptionResolver implements InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);
  
  private HttpMessageConverter<?>[] messageConverters = null;
  private List<HttpMessageConverter<?>> allMessageConverters = null;
  
  
  public RestExceptionHandler() {
    super();
  }
  
  public void setMessageConverters(final HttpMessageConverter<?>[] messageConverters) {
    this.messageConverters = messageConverters.clone();
  }
  
  @Override
  public void afterPropertiesSet() throws Exception {
    ensureMessageConverters();
  }
  
  @SuppressWarnings("unchecked")
  private void ensureMessageConverters() {
    List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
    
    // user configured values take precedence:
    if (this.messageConverters != null && this.messageConverters.length > 0)
      converters.addAll(CollectionUtils.arrayToList(this.messageConverters));
    
    // defaults next:
    new HttpMessageConverterHelper().addDefaults(converters);
    
    this.allMessageConverters = converters;
  }
  
  // leverage Spring's existing default setup behavior:
  private static final class HttpMessageConverterHelper extends WebMvcConfigurationSupport {
    public void addDefaults(final List<HttpMessageConverter<?>> converters) {
      addDefaultHttpMessageConverters(converters);
    }
  }
  
  /**
   * Actually resolve the given exception that got thrown during on handler execution, returning a ModelAndView that
   * represents a specific error page if appropriate.
   * <p/>
   * May be overridden in subclasses, in order to apply specific
   * exception checks. Note that this template method will be invoked <i>after</i> checking whether this resolved applies
   * ("mappedHandlers" etc), so an implementation may simply proceed with its actual exception handling.
   *
   * @param request  current HTTP request
   * @param response current HTTP response
   * @param handler  the executed handler, or <code>null</code> if none chosen at the time of the exception (for example,
   *                 if multipart resolution failed)
   * @param ex       the exception that got thrown during handler execution
   * @return a corresponding ModelAndView to forward to, or <code>null</code> for default processing
   */
  @Override
  protected ModelAndView doResolveException(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) {
    ModelAndView mav = null;
    ServletWebRequest webRequest = new ServletWebRequest(request, response);
    
    try {
      mav = getModelAndView(webRequest, handler, ex);
    } catch (Exception invocationEx) {
      logger.error("Acquiring ModelAndView for Exception [" + ex + "] resulted in an exception.", invocationEx);
    }
    
    return mav;
  }

  protected ModelAndView getModelAndView(final ServletWebRequest webRequest, final Object handler, final Exception ex) throws Exception {
    Object body = null;
    applyStatusIfPossible(webRequest, ex);
    
    if (ex instanceof AbstractException)
      body = ((AbstractException) ex).getErrorsMap();
    else
      body = new SystemException(Error.DEFAULT).getErrorsMap();
    
    return handleResponseBody(body, webRequest);
  }
  
  private void applyStatusIfPossible(final ServletWebRequest webRequest, final Exception ex) {
    if (!WebUtils.isIncludeRequest(webRequest.getRequest())) {
      HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      
      if (ex instanceof ValidationException) {
        httpStatus = HttpStatus.PRECONDITION_FAILED;
        logger.debug("Data Validation Failed : ", ex);
      } else {
        logger.error("Resolving view for the Exception : ", ex);
        
        if (ex instanceof SecurityException)
          httpStatus = HttpStatus.FORBIDDEN;
      }
      
      webRequest.getResponse().setStatus(httpStatus.value());
    }
  }
  
  @SuppressWarnings("unchecked")
  private ModelAndView handleResponseBody(final Object body, final ServletWebRequest webRequest) throws ServletException, IOException {
    HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());
    HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());
    List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
    Class<?> bodyType = body.getClass();
    
    if (acceptedMediaTypes.isEmpty())
      acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
    
    MediaType.sortByQualityValue(acceptedMediaTypes);
    
    if (this.allMessageConverters != null) {
      for (MediaType acceptedMediaType : acceptedMediaTypes) {
        for (@SuppressWarnings("rawtypes") final HttpMessageConverter messageConverter : this.allMessageConverters) {
          if (messageConverter.canWrite(bodyType, acceptedMediaType)) {
            messageConverter.write(body, acceptedMediaType, outputMessage);
            // return empty model and view to short circuit the iteration and to let Spring know that we've rendered the view ourselves:
            return new ModelAndView();
          }
        }
      }
    }
    
    logger.warn("Could not find HttpMessageConverter that supports return type {} and mediaTypes {}", bodyType, acceptedMediaTypes);
    
    return null;
  }
}
