package com.chihuo.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T,ID  extends Serializable> {
	 
	T findById(ID id);
	 
    List<T> findAll();
 
    List<T> findByExample(T exampleInstance, String[] excludeProperty);
 
    T saveOrUpdate(T entity);
 
    void delete(T entity);

}