package com.jiggy.base.interceptor;

import com.jiggy.base.exception.ValidationException;

import java.util.Arrays;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intercepts method invocations on any sub-type of the Resource interface. If exceptions are thrown by Resource methods, this last-chance Exception
 * handler will catch and convert them into WebApplicationExceptions that wrap HTTP fault codes, facilitating the display of user-friendly messages to
 * the client.
 * 
 * Un-handled exceptions should fall through here to be handled appropriately. This is a very limited list of core exception conversions. Rest of the
 * core exceptions are converted to Generic Fault codes.
 * 
 * @version $Revision$
 */
public final class ExceptionInterceptor implements MethodInterceptor {
  private final Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);
  
  @Override
  public Object invoke(final MethodInvocation mthodInvocation) throws Throwable {
    Exception coreException = null;
    
    try {
      return mthodInvocation.proceed();
    } catch (final Exception ex) {
      coreException = ex;
      logger.error("Uncaught Exception from Resource Method: " + ex.toString(), ex);
      throw convertException(ex);
    } finally {
      if (logger.isDebugEnabled()) {
        final String methodName = mthodInvocation.getMethod().getName();
        final String args = Arrays.toString(mthodInvocation.getArguments());
        String className = null;
        
        try {
          className = mthodInvocation.getThis().getClass().getName();
        } catch (final Exception ignored) {
          // prevent re-entrance
        } finally {
          if (coreException != null) {
            if (!(coreException instanceof WebApplicationException)) {
              logger.debug("Resource Method {} on Class {} with Parameters {} threw Core Exception [{}] -- converting into Resource Exception.",
                  new Object[] {methodName, (className == null ? "UNKNOWN" : className), args, coreException.toString()});
              logger.debug("Resource Exception Stack:", coreException);
            }
          }
        }
      }
    }
  }
  
  /**
   * Converts core exception into Resource exception.
   * 
   * @param coreException The Exception thrown from core.
   * @return an instance of the Exception class
   */
  private Exception convertException(final Exception coreException) {
    // Prevent reentrant anomalies
    if (coreException instanceof WebApplicationException) return coreException;
    
    Status status = Status.INTERNAL_SERVER_ERROR; // Internal Server Error
    JSONArray message = new JSONArray();
    
    if (coreException instanceof ValidationException) {
      status = Status.PRECONDITION_FAILED; // Precondition Failed
      //message = ((ValidationException) coreException).toJSON();
    } else {
      try {
        JSONObject errorJSONObject = new JSONObject();
        errorJSONObject.putOpt("ERROR_CODE", "SYS-RUNTIME");
        errorJSONObject.putOpt("ERROR_DESC", coreException.toString());
        message.put(errorJSONObject);
      } catch (JSONException ex) {
        // NO-OP
      }
    }
    
    return new WebApplicationException(Response.status(status).entity(message).type(MediaType.APPLICATION_JSON).build());
  }
}