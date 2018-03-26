package com.gameapi.rha.HibernateUtil;


import java.io.File;

import com.gameapi.rha.models.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    @Deprecated
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            AnnotationConfiguration aconf = new AnnotationConfiguration().addAnnotatedClass(User.class);
            aconf.configure(new File("/home/chapay/TechPark/sem2/java/RHA-02-2018/src/main/java/com/gameapi/rha/HibernateUtil/hibernate.cfg.xml"));
            return aconf.buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        // Close caches and connection pools 
        getSessionFactory().close();
    }
} 