package com.jiggy.base.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for Validation Exceptions.
 * 
 * @author jmalkan
 */
public abstract class AbstractException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  private Map<String, List<Map<String, String>>> errorsMap;
  
  
  /**
   * Creates a new instance of com.jiggy.base.exception.ValidationException.java and Performs Initialization.
   * 
   * @param error The error object.
   */
  public AbstractException(final Error error) {
    this(Arrays.asList(error));
  }
  
  
  /**
   * Creates a new instance of com.jiggy.base.exception.ValidationException.java and Performs Initialization.
   * 
   * @param errors The list of error objects.
   */
  public AbstractException(final List<Error> errors) {
    this(errors, null);
  }
  
  
  /**
   * Creates a new instance of com.jiggy.base.exception.ValidationException.java and Performs Initialization.
   * 
   * @param errors The list of error objects.
   */
  public AbstractException(final Error error, final Throwable cause) {
    super(cause);
    this.errorsMap = this.generateErrorMap(Arrays.asList(error));
  }
  
  
  /**
   * Creates a new instance of com.jiggy.base.exception.ValidationException.java and Performs Initialization.
   * 
   * @param errors The list of error objects.
   */
  public AbstractException(final List<Error> errors, final Throwable cause) {
    super(cause);
    this.errorsMap = generateErrorMap(errors);
  }

  /**
   * Generates a Map based on the given error object instance.
   * @param error The error object.
   * @return generates a map bases on the error object.
   */
  private Map<String, List<Map<String, String>>> generateErrorMap(final List<Error> errors) {
    List<Map<String, String>> errorsList = new ArrayList<Map<String, String>>();
    Map<String, List<Map<String, String>>> errorsMap = new HashMap<String, List<Map<String, String>>>();
    
    if (errors != null) {
      for (Error error : errors) {
        final Map<String, String> errorMap = new HashMap<String, String>();
        
        errorMap.put("ERROR_CODE", error.getErrorCode());
        errorMap.put("ERROR_DESC", error.getErrorDesc());
        errorMap.put("UI_ELEMENT_NAME", error.getAttributeName());
        
        errorsList.add(errorMap);
      }
    }
    
    errorsMap.put("ERRORS", errorsList);
    
    return errorsMap;
  }
  
  
  public Map<String, List<Map<String, String>>> getErrorsMap() {
    return errorsMap;
  }
}