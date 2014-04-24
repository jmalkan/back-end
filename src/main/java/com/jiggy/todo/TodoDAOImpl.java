package com.jiggy.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.jiggy.base.dao.AbstractMongoDBDAO;

/*
[
  {
    "id": 1,
    "version": 1,
    "createdBy": 1,
    "createDate": 1398363310061,
    "lastModifiedBy": null,
    "lastModifiedDate": null,
    "name": "wake up"
  },
  {
    "id": 2,
    "version": 1,
    "createdBy": 1,
    "createDate": 1398363534198,
    "lastModifiedBy": null,
    "lastModifiedDate": null,
    "name": "do dishes"
  },
  {
    "id": 3,
    "version": 1,
    "createdBy": 1,
    "createDate": 1398363600284,
    "lastModifiedBy": null,
    "lastModifiedDate": null,
    "name": "take out trash"
  },
  {
    "id": 4,
    "version": 1,
    "createdBy": 1,
    "createDate": 1398364231175,
    "lastModifiedBy": null,
    "lastModifiedDate": null,
    "name": "go to bed"
  }
]
*/
/**
 * Data access implementation for Todo.
 * 
 * Created on Sept 1, 2012
 * 
 * @author jmalkan
 * @version $Revision$
 * 
 */
@Repository("todoDAO")
public class TodoDAOImpl extends AbstractMongoDBDAO<Todo> implements TodoDAO {
  /**
   * Creates a new instance of com.jiggy.todo.TodoDAOImpl.java and Performs Initialization
   * 
   * @param sessionFactory The Hibernate's sessionFactory Object this dao interacts with.
   */
  @Autowired
  public TodoDAOImpl(final MongoOperations mongoOperations) {
    super(mongoOperations);
  }
}