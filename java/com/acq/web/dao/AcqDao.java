package com.acq.web.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AcqDao {

	@Autowired
	private SessionFactory sessionFactory;

	Session session;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getNewSession() {
		session = sessionFactory.openSession();
		return session;
	}

	/*public void closeSession() {
		session.close();
	}*/

	/**
	 * #Note: Remove below lines of codes from every DB transaction # -Object
	 * updatedEntity = getCurrentSession().merge(entity); #
	 * -getCurrentSession.flush(); #Note: replace with below lines of codes #
	 * +Session session=getCurerntSession(); # +Object
	 * obj=session.<methods>(<--->); # +session.flush(); # +session.close();
	 * 
	 * @param entity
	 * @return
	 */
	@Transactional
	public Object save(Object entity) {
		// -- Object updatedEntity = getCurrentSession().merge(entity);
		Session session = null;
		try{
			session = getNewSession();
			// session.setFlushMode(FlushMode.MANUAL);
			// Transaction tx=session.beginTransaction();
			Object updatedEntity = session.merge(entity);
			session.flush();
			return updatedEntity;			
			// tx.commit();
			// --getCurrentSession().flush();
			// finally close()
		}catch(Exception e){
			return null;
		}finally{			
			session.close();
		}
	}

	@Transactional
	public Object saveOrUpdate(Object entity) {
		Session session = null;
		try{
			session = getNewSession();
			session.saveOrUpdate(entity);
			session.flush();			
		}catch(Exception e){
			System.err.println();
		}finally{
			session.close();
		}
		//getNewSession().saveOrUpdate(entity);
		//getNewSession().flush();
		return entity;
	}
	
	@Transactional
	public int update(String query) {
		Session sessoin = null;
		int numberOfUpdate=0;
		try {
			session = getNewSession();
			Query q = session.createQuery(query);
			numberOfUpdate=q.executeUpdate();
			session.flush();
			return numberOfUpdate;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());

		} finally {
			session.close();
		}
		return numberOfUpdate;

	}

	@Transactional
	public void delete(Object entity) {
		Session session = null;
		try{
			session = getNewSession();
			session.delete(entity);
			session.flush();
		}catch(Exception e){
			System.err.println(e);
		}finally{
			session.close();
		}
	}

	/*@Transactional(readOnly = true)
	public <T> T get(Class<T> c, String str) {
		return (T) getNewSession().get(c, str);
	}

	@Transactional(readOnly = true)
	public <T> T get(Class<T> c, Serializable key) {
		return (T) getNewSession().get(c, key);
	}

	public void rollback() {
		getNewSession().getTransaction().rollback();
	}*/

}
