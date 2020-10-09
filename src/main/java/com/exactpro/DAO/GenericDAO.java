package com.exactpro.DAO;

import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
//TODO: сделать логирование
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.SQLException;
import java.util.List;


public class GenericDAO {
    public <T> int insertEntity(T entity) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            transaction.commit();
            return (int) session.save(entity);
        }
    }

    public <T> void updateTableByEntity(T entity) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        }
    }

    //Перегрузки
    public <T> T selectByID(Class<T> clazz, Long id) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try(Session session = sessionFactory.openSession()){
            return session.load(clazz, id);
        }
    }
    public <T> T selectByID(Class<T> clazz, Integer id) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try(Session session = sessionFactory.openSession()){
            return session.load(clazz, id);
        }
    }

    public <T> List<T> gelAllEntities(Class<T> clazz) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = builder.createQuery(clazz);
            Root<T> customerRoot = criteriaQuery.from(clazz);
            criteriaQuery.select(customerRoot);
            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    public <T> void deleteEntityFromTable(T entity) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        }
    }

}
