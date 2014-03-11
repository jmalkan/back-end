package com.jiggy.base.exception;

/**
 * Security exception.
 * 
 * @author jmalkan
 */
public class SecurityException extends AbstractException {
  private static final long serialVersionUID = 1L;
  
  public SecurityException(Error error) {
    super(error);
  }

  public SecurityException(final Error error, final Throwable cause) {
    super(error, cause);
  }
}