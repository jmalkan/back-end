package com.jiggy.base.exception;

/**
 * Enum for Exceptions.
 * 
 * @author jmalkan
 */
public enum Error {
  DEFAULT("SYSTEM", "This is an unexpected error", ""),
  ERROR_REQUIRED("ERROR_REQUIRED", "The %s field is required.", "");
  
  
  private String errorCode;
  private String errorDesc;
  private String attributeName;
  
  /**
   * Creates a new instance of com.cengage.nextbook.ErrorCodeMessage.java and Performs Initialization
   *
   * @param errorCode The Error Code.
   * @param errorDesc The Error Description.
   * @param attributeName Name of the field name.
   */
  private Error(final String errorCode, final String errorDesc, final String attributeName) {
      this.errorCode = errorCode;
      this.errorDesc = errorDesc;
      this.attributeName = attributeName;
  }
  
  /**
   * Getter of the property <tt>errorCode</tt>
   * 
   * @return the errorCode
   */
  public String getErrorCode() {
      return this.errorCode;
  }
  
  /**
   * Setter of the property <tt>errorCode</tt>
   * 
   * @param errorCode the errorCode to set
   */
  public void setErrorCode(final String errorCode) {
      this.errorCode = errorCode;
  }
  
  /**
   * Getter of the property <tt>errorDesc</tt>
   * 
   * @return the errorDesc
   */
  public String getErrorDesc() {
      return this.errorDesc;
  }
  
  /**
   * Setter of the property <tt>errorDesc</tt>
   * 
   * @param errorDesc the errorDesc to set
   */
  public void setErrorDesc(final String errorDesc) {
      this.errorDesc = errorDesc;
  }
  
  /**
   * Getter of the property <tt>attributeName</tt>
   * 
   * @return the attributeName
   */
  public String getAttributeName() {
      return this.attributeName;
  }
  
  /**
   * Setter of the property <tt>attributeName</tt>
   * 
   * @param attributeName the attributeName to set
   */
  public void setAttributeName(final String attributeName) {
      this.attributeName = attributeName;
  }

  
  /**
   * @return error description formatted with any arguments
   */
  public String formatMessage(Object... args) {
      return String.format(this.errorDesc, args);
  }
}