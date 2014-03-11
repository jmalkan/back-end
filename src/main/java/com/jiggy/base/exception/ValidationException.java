package com.jiggy.base.exception;

import java.util.List;


/**
 * Base class for Validation Exceptions.
 * 
 * @author jmalkan
 */
public class ValidationException extends AbstractException {
  private static final long serialVersionUID = 1L;

  public ValidationException(final Error error) {
    super(error);
  }

  public ValidationException(final List<Error> errors) {
    super(errors);
  }
}