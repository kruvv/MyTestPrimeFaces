package ru.kruvv.primefaces.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * This class create object session for connection by database
 * 
 * @author viktor
 *
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory;

	static {
		try {
			// Create the SessionFactory
			sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
		} catch (HibernateException ex) {
			throw new RuntimeException("Configuration problem: " + ex.getMessage(), ex);
		}
	}

	public static final ThreadLocal session = new ThreadLocal();

	/**
	 * This method uses the session factory interface to create a session.
	 *  
	 * @return The created session.
	 * @throws HibernateException Indicates a problem opening the session; pretty rare here.
	 */
	public static Session currentSession() throws HibernateException {
		Session s = (Session) session.get();
		// Open a new Session, if this Thread has none yet
		if (s == null) {
			s = sessionFactory.openSession();
			session.set(s);
		}
		return s;
	}

	/**
	 * This method close session.
	 * 
	 * @throws HibernateException
	 */
	public static void closeSession() throws HibernateException {
		Session s = (Session) session.get();
		session.set(null);
		if (s != null)
			s.close();
	}
}