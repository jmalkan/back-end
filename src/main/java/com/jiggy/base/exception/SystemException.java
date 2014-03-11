package com.jiggy.base.exception;

/**
 * Base class for System Exceptions.
 * 
 * @author jmalkan
 */
public class SystemException extends AbstractException {
  private static final long serialVersionUID = 1L;

  public SystemException(final Error error) {
    super(error);
  }

  public SystemException(final Error error, final Throwable cause) {
    super(error, cause);
  }
}