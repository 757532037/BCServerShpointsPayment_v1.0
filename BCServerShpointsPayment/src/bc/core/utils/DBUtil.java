package bc.core.utils;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBUtil {
	private static final Logger log = Logger.getLogger(DBUtil.class);
	private static final SessionFactory sessionFactory;

	static {
		try {
			// create the SessionFactory
			Configuration config = new Configuration();

			config.addResource("mapping.xml");

			sessionFactory = config.buildSessionFactory();
		} catch (Throwable ex) {
			// make sure it is logged
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static final ThreadLocal session = new ThreadLocal();

	public static Session currentSession() throws HibernateException {
		Session s = (Session) session.get();

		// open a new session, if this thread has none yet
		if (s == null) {
			s = sessionFactory.openSession();
			session.set(s);
		}

		return s;
	}

	public static void closeeSession() throws HibernateException {
		Session s = (Session) session.get();
		session.set(null);
		if (s != null) {
			s.clear();
		}
	}

}
