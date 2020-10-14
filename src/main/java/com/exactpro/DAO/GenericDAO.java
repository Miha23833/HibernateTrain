package com.exactpro.DAO;

import com.exactpro.loggers.StaticLogger;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;

import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

//TODO: сделать логирование


public class GenericDAO {

    private static final Logger logger = StaticLogger.infoLogger;

    public static <T> int insertEntity(T entity) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {

                int entityID = (int) session.save(entity);
                logger.info(String.format("Entity %s(%s) was saved to database", entity.getClass().toString(), entityID));
                return entityID;
            }
        }
    }

    public static <T> void updateTableByEntity(T entity) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                session.update(entity);
                transaction.commit();
                logger.info(String.format("Entity %s was updated to database", entity.getClass().toString()));
            }
        }
    }

    //Перегрузки
    public static <T> T selectByID(Class<T> clazz, Long id) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                T entity = session.load(clazz, id);
                Hibernate.initialize(entity);
                logger.info(String.format("Entity %s was selected by id (%s)", clazz.toString(), id));
                return entity;
            }
        }
    }
    public static <T> T selectByID(Class<T> clazz, Integer id) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                T entity = session.load(clazz, id);
                Hibernate.initialize(entity);
                logger.info(String.format("Entity %s was selected by id (%s)", clazz.toString(), id));
                return entity;
            }
        }
    }

    public static <T> List<T> gelAllEntities(Class<T> clazz) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<T> criteriaQuery = builder.createQuery(clazz);
                Root<T> customerRoot = criteriaQuery.from(clazz);
                criteriaQuery.select(customerRoot);
                List<T> entities = session.createQuery(criteriaQuery).getResultList();
                logger.info(String.format("List of entities %s was selected", clazz.toString()));
                return entities;
            }
        }
    }

    public static <T> void deleteEntityFromTable(T entity) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                session.delete(entity);
                transaction.commit();
                logger.info(String.format("Entity %s was deleted from DB", entity.getClass().toString()));
            }
        }
    }

}
