package com.jiggy.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;

/**
 * AbstractEntity.java This is an abstract base class for the Entity class to extend from.
 * Common functionality used by descendants is implemented here.
 * 
 * @author jmalkan
 */
public abstract class AbstractEntity implements Entity {
  private static final long serialVersionUID = 1L;
  
  @Id
  private Long id;
  private Long version;
  private Long createdBy;
  private Long createDate;
  private Long lastModifiedBy;
  private Long lastModifiedDate;
  
  /**
   * Creates a new instance of com.jiggy.base.entity.AbstractEntity.java and Performs Initialization.
   */
  public AbstractEntity() {
    super();
  }
  
  /**
   * Creates a new instance of com.jiggy.base.entity.AbstractEntity.java and Performs Initialization.
   * 
   * @param id The ID for this domain object.
   * 
   */
  public AbstractEntity(final Long id) {
    super();
    this.id = id;
  }
  
  @Override
  public Long getId() {
    return id;
  }
  
  /**
   * Setter of the property <tt>id</tt>
   * 
   * @param id the id to set
   */
  public void setId(final Long id) {
    this.id = id;
  }
  
  @Override
  public void setVersion(final Long version) {
    this.version = version;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
  
  @Override
  public Long getCreatedBy() {
    return createdBy;
  }
  
  @Override
  public void setCreatedBy(final Long createdBy) {
    this.createdBy = createdBy;
  }
  
  @Override
  public Long getCreateDate() {
    return createDate;
  }
  
  @Override
  public void setCreateDate(final Long createDate) {
    this.createDate = createDate;
  }
  
  @Override
  public Long getLastModifiedBy() {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(final Long lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public Long getLastModifiedDate() {
    return lastModifiedDate;
  }
  
  @Override
  public void setLastModifiedDate(final Long lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }
  
  @Override
  public Long getVersion() {
    return version;
  }
  
  @Override
  public boolean equals(final Object entity) {
    if (this == entity)
      return true;
    
    if (entity instanceof AbstractEntity) {
      final Long id1 = ((AbstractEntity) entity).getId();
      final Long id2 = getId();
      
      return id1 == null && id2 == null || id1 != null && id1.equals(id2);
    }
    
    return false;
  }
  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}