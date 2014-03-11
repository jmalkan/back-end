package com.jiggy.base.searchengine;

import com.jiggy.base.searchengine.FilterExpression.FilterTerm;

import java.util.Map;

/**
 * SearchCriteria.java This is an abstract base class for the search criteria class to extend from.
 * Common functionality used by descendants is implemented here.
 * 
 * @author jmalkan
 */
public class DefaultSearchCriteria implements SearchCriteria {
  private static final long serialVersionUID = 2L;
  
  private String sortBy = null;
  private FilterExpression filter = null;
  
  private int offset = -1;
  private int limit = -1;
  private long rowCount = -1;
  
  private String queryVariables = null;
  
  @Override
  public String getSortBy() {
    return sortBy;
  }
  
  @Override
  public void setSortBy(final String sortBy) {
    this.sortBy = sortBy;
  }
  
  @Override
  public FilterExpression getFilter() {
    return filter;
  }
  
  @Override
  public void setFilter(final FilterExpression filter) {
    this.filter = filter;
  }
  
  @Override
  public void setFilter(final Map<String, String> filter) {
    setFilter(new FilterExpression(filter));
  }
  
  @Override
  public void addFilter(final FilterTerm term) {
    if (filter == null) {
      filter = new FilterExpression();
    }
    
    filter.addTerm(term);
  }
  
  @Override
  public void addFilter(final String key, final String value) {
    addFilter(new FilterTerm(key, value));
  }
  
  @Override
  public void addFilter(final String filterString) {
    if (filter == null) {
      filter = new FilterExpression();
    }
    
    filter.addTerms(filterString);
  }
  
  @Override
  public boolean isPaginationEnabled() {
    return getOffset() > -1 && getLimit() > 0;
  }
  
  @Override
  public int getOffset() {
    return offset;
  }
  
  @Override
  public void setOffset(final int offset) {
    this.offset = offset;
  }
  
  @Override
  public int getLimit() {
    return limit;
  }
  
  @Override
  public void setLimit(final int limit) {
    this.limit = limit;
  }
  
  @Override
  public long getRowCount() {
    return rowCount;
  }
  
  @Override
  public void setRowCount(final long rowCount) {
    this.rowCount = rowCount;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (filter == null ? 0 : filter.hashCode());
    result = prime * result + limit;
    result = prime * result + offset;
    result = (int) (prime * result + rowCount);
    result = prime * result + (sortBy == null ? 0 : sortBy.hashCode());
    result = prime * result + (queryVariables == null ? 0 : queryVariables.hashCode());
    
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    boolean equal = true;
    if (this == obj) {
      equal = true;
    } else if (obj == null) {
      equal = false;
    } else if (getClass() != obj.getClass()) {
      equal = false;
    } else {
      DefaultSearchCriteria other = (DefaultSearchCriteria) obj;
      if (filter == null) {
        if (other.filter != null) {
          equal = false;
        }
      } else if (!filter.equals(other.filter)) {
        equal = false;
      } else if (limit != other.limit) {
        equal = false;
      } else if (offset != other.offset) {
        equal = false;
      } else if (rowCount != other.rowCount) {
        equal = false;
      } else if (sortBy == null) {
        if (other.sortBy != null) {
          equal = false;
        }
      } else if (!sortBy.equals(other.sortBy)) {
        equal = false;
      } else if (queryVariables != null && !queryVariables.equals(other.queryVariables)) {
        equal = false;
      } else {
        equal = true;
      }
    }
    return equal;
  }
  
  @Override
  public String getQueryVariables() {
    return queryVariables;
  }
  
  @Override
  public void setQueryVariables(final String queryVariables) {
    this.queryVariables = queryVariables;
  }
}