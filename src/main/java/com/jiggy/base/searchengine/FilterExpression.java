package com.jiggy.base.searchengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * This class holds a search expression used to filter JDO query results.
 * 
 * @author jmalkan
 */
public class FilterExpression {
  private Pattern QUERY_PARSER = Pattern.compile("([^!]*)(!?+=)(.*)");
  
  public enum QueryMode {
    JPA("AND", "="), JDO("&&", "=="), HQL("AND", "=");
    
    private final String andToken;
    private final String equalsToken;
    
    QueryMode(String andToken, String equalsToken) {
      this.andToken = andToken;
      this.equalsToken = equalsToken;
    }
  }
  
  private final QueryMode mode;
  private final Map<String, FilterTerm> terms;
  
  public FilterExpression(final QueryMode mode) {
    this.mode = mode;
    this.terms = new LinkedHashMap<String, FilterTerm>();
  }
  
  public FilterExpression() {
    this(QueryMode.JDO);
  }
  
  public FilterExpression(final Map<String, String> terms) {
    this(QueryMode.JDO);
    for (Entry<String, String> entry : terms.entrySet()) {
      this.terms.put(entry.getKey(), new FilterTerm(entry.getKey(), entry.getValue()));
    }
  }
  
  public void addTerm(FilterTerm term) {
    terms.put(term.getKey(), term);
  }
  
  public void addTerms(final String filterString) {
    if (filterString != null && !SearchCriteria.WILDCARD_TOKEN.equals(filterString.trim())) {
      String[] filterPairs = filterString.trim().split("&");
      
      for (String filterPair : filterPairs) {
        Matcher matcher = QUERY_PARSER.matcher(filterPair);
        if (matcher.matches() && matcher.groupCount() == 3) {
          String key = matcher.group(1);
          String operator = matcher.group(2);
          String value = matcher.group(3);
          FilterTerm term = null;
          if ("!=".equals(operator)) {
            term = new FilterTerm(Op.EXCLUDE, key, value, mode);
          } else if ("=".equals(operator)) {
            term = new FilterTerm(Op.INCLUDE, key, value, mode);
          } else {
            throw new IllegalArgumentException("Invalid operator  '" + operator + "' for term " + filterPair);
          }
          this.addTerm(term);
        }
      }
    }
  }
  
  public Collection<FilterTerm> terms() {
    return terms.values();
  }
  
  public int numTerms() {
    return terms.size();
  }
  
  public FilterTerm getTerm(final String key) {
    return terms.get(key);
  }
  
  public Map<String, String> toMap() {
    Map<String, String> result = new HashMap<String, String>(terms.size());
    for (Entry<String, FilterTerm> entry : terms.entrySet()) {
      result.put(entry.getKey(), entry.getValue().getValue());
    }
    return result;
  }
  
  public String toQueryString(final FilterAdvice handler) {
    StringBuilder filterBuffer = new StringBuilder();
    for (FilterTerm term : terms()) {
      if (filterBuffer.length() > 0) {
        filterBuffer.append(' ').append(mode.andToken).append(' ');
      }
      filterBuffer.append(handler.getQueryString(term));
    }
    return filterBuffer.toString();
  }
  
  // public void appendToQuery(Query q, FilterAdvice advice) {
  // if (numTerms() > 0) {
  //
  // StringBuilder whereClause = new StringBuilder();
  // String sql = q.toString();
  // int whereDelim = sql.indexOf("WHERE");
  // if (whereDelim != -1) {
  // whereClause.append(sql.substring(whereDelim + "WHERE".length()));
  // }
  // q.setFilter(whereClause + " " + mode.andToken + " " + toQueryString(advice));
  // }
  // }
  
  public String toHttpString() {
    StringBuilder result = new StringBuilder();
    Collection<FilterTerm> termValues = this.terms.values();
    Iterator<FilterTerm> iter = termValues.iterator();
    while (iter.hasNext()) {
      FilterTerm term = iter.next();
      term.toHttpString(result);
      if (iter.hasNext()) {
        result.append('&');
      }
    }
    return result.toString();
  }
  
  public Map<Object, Object> getQueryParams(final FilterAdvice handler) {
    
    Map<Object, Object> queryParams = new HashMap<Object, Object>();
    for (FilterTerm entry : terms()) {
      entry.toQueryParams(queryParams, handler);
    }
    return queryParams;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((mode == null) ? 0 : mode.hashCode());
    result = prime * result + ((terms == null) ? 0 : terms.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    FilterExpression other = (FilterExpression) obj;
    if (mode != other.mode) return false;
    if (terms == null) {
      if (other.terms != null) return false;
    } else if (!terms.equals(other.terms)) return false;
    return true;
  }
  
  /**
   * FilterAdvice provides a way for the caller to influence the execution of a FilterExpression. It is mainly used to perform type conversion on
   * particular fields within the filter.
   */
  public interface FilterAdvice {
    List<Object> getParamValues(FilterTerm term);
    
    String getQueryString(FilterTerm term);
  }
  
  /**
   * FilterTerm Operator
   */
  public enum Op {
    EXCLUDE, INCLUDE;
  }
  
  /**
   * This class holds a term within a filter expression. It is essentially just a name-value pair.
   */
  public static class FilterTerm {
    
    private final QueryMode mode;
    private final String key;
    private final Op operator;
    private String value;
    
    public FilterTerm(final String key, final String value) {
      this(Op.INCLUDE, key, value, QueryMode.JDO);
    }
    
    public FilterTerm(final Op operator, final String key, final String value, final QueryMode mode) {
      rejectNull(key, "Filter Keys cannot be null");
      this.key = key.trim();
      this.value = (value == null) ? null : value.trim();
      this.operator = operator;
      this.mode = mode;
    }
    
    private void rejectNull(final String string, final String rejection) {
      if (string == null) {
        throw new IllegalArgumentException(rejection);
      }
    }
    
    public String getKey() {
      return key;
    }
    
    public String getParameterizedKey() {
      return key.replace('.', '_');
    }
    
    public String getValue() {
      return value;
    }
    
    public Op getOperator() {
      return operator;
    }
    
    public String toQueryString() {
      StringBuilder result = new StringBuilder();
      String paramName = getParameterizedKey();
      
      // If we are dealing with a set of values or the null set, use the "contains" operator,
      // otherwise, use an "equals" operator
      
      if (StringUtils.contains(value, ',')) {
        result.append(" :").append(paramName).append(".contains(").append(key).append(")");
        if (operator == Op.EXCLUDE) {
          result.append(" == false");
        }
      } else {
        result.append(key);
        if (operator == Op.EXCLUDE) {
          result.append(" != :");
        } else {
          result.append(' ').append(mode.equalsToken).append(" :");
        }
        result.append(paramName);
      }
      return result.toString();
    }
    
    private void toQueryParams(final Map<Object, Object> queryParams, final FilterAdvice handler) {
      String paramName = getParameterizedKey();
      List<Object> valuesList = handler.getParamValues(this);
      if (valuesList.size() > 1) {
        queryParams.put(paramName, valuesList);
      } else if (valuesList.size() == 1) {
        Object firstValue = valuesList.get(0);
        queryParams.put(paramName, firstValue);
      }
    }
    
    public void addValue(final String toAdd) {
      if (value == null || "".equals(value.trim())) {
        value = toAdd;
      } else {
        String[] values = value.split(",");
        if (!ArrayUtils.contains(values, toAdd)) {
          value = value + "," + toAdd;
        }
      }
    }
    
    public void removeValue(final String toRemove) {
      if (value != null) {
        String[] values = value.split(",");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
          if (!values[i].equals(toRemove)) {
            if (builder.length() > 0) {
              builder.append(',');
            }
            builder.append(values[i]);
          }
        }
        value = builder.toString();
      }
    }
    
    public List<Long> getLongValues() {
      List<Long> result = new ArrayList<Long>(1);
      String[] values = value.split(",");
      for (String splitValue : values) {
        if (StringUtils.isNotBlank(splitValue)) result.add(Long.valueOf(splitValue));
      }
      return result;
    }
    
    public void toHttpString(final StringBuilder buffer) {
      buffer.append(key);
      if (operator == Op.EXCLUDE) buffer.append("!");
      buffer.append("=");
      buffer.append(value);
    }
    
    @Override
    public String toString() {
      StringBuilder buffer = new StringBuilder();
      toHttpString(buffer);
      return buffer.toString();
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((key == null) ? 0 : key.hashCode());
      result = prime * result + ((mode == null) ? 0 : mode.hashCode());
      result = prime * result + ((operator == null) ? 0 : operator.hashCode());
      result = prime * result + ((value == null) ? 0 : value.hashCode());
      return result;
    }
    
    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      FilterTerm other = (FilterTerm) obj;
      if (key == null) {
        if (other.key != null) return false;
      } else if (!key.equals(other.key)) return false;
      if (mode != other.mode) return false;
      if (operator != other.operator) return false;
      if (value == null) {
        if (other.value != null) return false;
      } else if (!value.equals(other.value)) return false;
      return true;
    }
  }
}