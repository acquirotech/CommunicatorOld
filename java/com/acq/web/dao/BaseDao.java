package com.acq.web.dao;

import java.io.Serializable;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
/**
 * 
 * @author Aranoah Tech
 * Base DAO provides functionality to perform generic CRUD Operationns
 * Some methods return DbDto with exception handling 
 *
 */
@Repository
public class BaseDao {
	
	
	@Autowired
	protected SessionFactory sf;
	
	private Session session;
	
	private SessionFactory getSessionFactory(){
		return sf;
	}	
	public Session getCurrentSession(){
		session = sf.getCurrentSession();
		return session;
	}
	
	public void closeSession(){
		session.close();
	}
	
	@Transactional
	public Object save(Object entity){
		 Object updatedEntity = getCurrentSession().merge(entity);
		 getCurrentSession().flush();
		 return updatedEntity;
	}
	
	@Transactional
	public Object save1(Object entity){
		 Object updatedEntity = getCurrentSession().save(entity);
		 getCurrentSession().flush();
		 return updatedEntity;
	}
		
	@Transactional 
	public Object saveOrUpdate(Object entity){
		 getCurrentSession().saveOrUpdate(entity);
		 getCurrentSession().flush();
		 return entity;
	 }
	 public int update(String query){
		 Query q= getCurrentSession().createQuery(query);
		 return q.executeUpdate();
	 }
	 
	 @Transactional
	 public void delete(Object entity){
		 getCurrentSession().delete(entity);
	 }	 
	 @Transactional(readOnly=true)
	 public <T> T get(Class<T> c, String str) {
	        return (T) getCurrentSession().get(c, str);
	    }
	   
	 @Transactional(readOnly=true)
	 public <T> T get(Class<T> c, Serializable key) {
	        return (T) getCurrentSession().get(c, key);
	    }
	public void rollback(){
		getCurrentSession().getTransaction().rollback();
	}	
}
