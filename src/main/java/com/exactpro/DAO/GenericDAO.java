package com.exactpro.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

//TODO: сделать логирование


public class GenericDAO {
    public static <T> int insertEntity(T entity) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            transaction.commit();
            return (int) session.save(entity);
        }
    }

    public static <T> void updateTableByEntity(T entity) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        }
    }

    //Перегрузки
    public static <T> T selectByID(Class<T> clazz, Long id) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try(Session session = sessionFactory.openSession()){
            return session.load(clazz, id);
        }
    }
    public static <T> T selectByID(Class<T> clazz, Integer id) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try(Session session = sessionFactory.openSession()){
            return session.load(clazz, id);
        }
    }

    public static <T> List<T> gelAllEntities(Class<T> clazz) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = builder.createQuery(clazz);
            Root<T> customerRoot = criteriaQuery.from(clazz);
            criteriaQuery.select(customerRoot);
            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    public static <T> void deleteEntityFromTable(T entity) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        }
    }

}
