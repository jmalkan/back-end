package com.jiggy.base.entity;

import java.io.Serializable;

/**
 * Entity.java
 * 
 * @author jmalkan
 */
public interface Entity extends Serializable {
  /** The Entity's Id column name. */
  public static final String ID_COLUMN_NAME = "id";
  
  /**
   * Getter of the property <tt>id</tt> of the domain.
   * 
   * @return the id of the current entity.
   */
  public Long getId();
  
  /**
   * Getter of the property <tt>createdBy</tt>
   * 
   * @return the createdBy
   */
  public Long getCreatedBy();
  
  /**
   * Setter of the property <tt>createdBy</tt>
   * 
   * @param createdBy the createdBy to set
   */
  public void setCreatedBy(final Long createdBy);
  
  /**
   * Getter of the property <tt>createDate</tt>
   * 
   * @return the createDate
   */
  public Long getCreateDate();
  
  /**
   * Setter of the property <tt>createDate</tt>
   * 
   * @param createDate the createDate to set
   */
  public void setCreateDate(final Long createDate);
  
  /**
   * Getter of the property <tt>lastModifiedBy</tt>
   * 
   * @return the lastModifiedBy
   */
  public Long getLastModifiedBy();
  
  /**
   * Setter of the property <tt>lastModifiedBy</tt>
   * 
   * @param lastModifiedBy the lastModifiedBy to set
   */
  public void setLastModifiedBy(final Long lastModifiedBy);
  
  /**
   * Getter of the property <tt>lastModifiedDate</tt>
   * 
   * @return the lastModifiedDate
   */
  public Long getLastModifiedDate();
  
  /**
   * Setter of the property <tt>lastModifiedDate</tt>
   * 
   * @param lastModifiedDate the lastModifiedDate to set
   */
  public void setLastModifiedDate(final Long lastModifiedDate);
  
  /**
   * Getter of the property <tt>version</tt>
   * 
   * @return the version
   */
  public Long getVersion();
  
  /**
   * Setter of the property <tt>version</tt>
   * 
   * @param version the version to set
   */
  public void setVersion(final Long version);
}