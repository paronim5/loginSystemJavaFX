package org.example.loginsystem;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtl {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {

            return new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Initial SessionFactory creation failed." + e)    ;
        }
    }

    public static SessionFactory getInstance() {
        return sessionFactory;
    }

    public static Session openSession   () {
        return getInstance().openSession();
    }

    public static void shutdown() {
        // Close caches and connection pools
        getInstance().close();
    }
}
