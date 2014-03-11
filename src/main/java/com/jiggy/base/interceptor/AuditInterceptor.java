package com.jiggy.base.interceptor;

import java.util.Arrays;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intercepts any method that extends AbstractResource and logs audit entry. This class does NOT handle exceptions thrown.
 * 
 * @author jmalkan
 */
public class AuditInterceptor implements MethodInterceptor {
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Override
  public Object invoke(final MethodInvocation mthodInvocation) throws Throwable {
    String args = null;
    String className = null;
    String methodName = null;
    double elapsed = 0;
    Object returnObject = null;
    
    long start = System.nanoTime();
    
    try {
      returnObject = mthodInvocation.proceed();
    } finally {
      methodName = mthodInvocation.getMethod().getName();
      elapsed = ((System.nanoTime() - start) / 1000000.0);
      args = Arrays.toString(mthodInvocation.getArguments());
      
      try {
        className = mthodInvocation.getThis().getClass().getName();
      } catch (final Exception ignored) {
        // Smothered to prevent re entrancy
      }
      
      logger.warn("Method Invocation {} on Class {} with Parameters {} took {} ms.",
                  new Object[] {methodName, className == null ? "UNKNOWN" : className, args, elapsed});
    }
    
    return returnObject;
  }
}