package com.syml.hibernate.utils;


import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import play.Logger;

import com.syml.Constants;

public class HibernateUtils {
	
	private static SessionFactory SESSION_FACTORY;

	public static synchronized SessionFactory getSessionFactory() {

		if (SESSION_FACTORY == null) {
			Logger.debug("Before creating SESSION_FACTORY " + SESSION_FACTORY);
			try {
				SESSION_FACTORY = getSessionFactoryForDBOne();
			} catch (HibernateException e) {
				SESSION_FACTORY = getSessionFactoryForDBTwo();

			}
			Logger.debug("After creating SESSION_FACTORY " + SESSION_FACTORY);
		}

		return SESSION_FACTORY;
	}

	public static SessionFactory getSessionFactoryForDBOne() {
		SESSION_FACTORY = new Configuration().configure(
				Constants.HIBERNET_CONFIG_FILE_ONE).buildSessionFactory();
		return SESSION_FACTORY;
	}

	public static SessionFactory getSessionFactoryForDBTwo() {
		SESSION_FACTORY = new Configuration().configure(
				Constants.HIBERNET_CONFIG_FILE_TWO).buildSessionFactory();
		return SESSION_FACTORY;
	}

}
