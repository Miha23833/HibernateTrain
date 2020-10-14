package com.exactpro.DAO;

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
    public static <T> int insertEntity(T entity) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                return (int) session.save(entity);
            }
        }
    }

    public static <T> void updateTableByEntity(T entity) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                session.update(entity);
                transaction.commit();
            }
        }
    }

    //Перегрузки
    public static <T> T selectByID(Class<T> clazz, Long id) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                return session.load(clazz, id);
            }
        }
    }
    public static <T> T selectByID(Class<T> clazz, Integer id) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                T entity = session.load(clazz, id);
                Hibernate.initialize(entity);
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
                return session.createQuery(criteriaQuery).getResultList();
            }
        }
    }

    public static <T> void deleteEntityFromTable(T entity) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                session.delete(entity);
                transaction.commit();
            }
        }
    }

}
