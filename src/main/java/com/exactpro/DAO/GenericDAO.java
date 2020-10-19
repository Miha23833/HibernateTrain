package com.exactpro.DAO;

import com.exactpro.loggers.StaticLogger;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class GenericDAO {

    private static final Logger logger = StaticLogger.infoLogger;

    public static <T> int insertEntity(Session session, T entity) {
        int entityID = (int) session.save(entity);
        logger.info(String.format("Entity %s(%s) was saved to database", entity.getClass().toString(), entityID));
        return entityID;
    }

    public static <T> void updateTableByEntity(Session session, T entity) {
        session.update(entity);
        logger.info(String.format("Entity %s was updated to database", entity.getClass().toString()));
    }

    //Перегрузки
    public static <T> T selectByID(Session session, Class<T> clazz, Long id) {
        T entity = session.load(clazz, id);
        logger.info(String.format("Entity %s was selected by id (%s)", clazz.toString(), id));
        return entity;
    }

    public static <T> T selectByID(Session session, Class<T> clazz, Integer id) {
        T entity = session.load(clazz, id);
        logger.info(String.format("Entity %s was selected by id (%s)", clazz.toString(), id));
        return entity;
    }

    public static <T> List<T> gelAllEntities(Session session, Class<T> clazz) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(clazz);
        Root<T> customerRoot = criteriaQuery.from(clazz);
        criteriaQuery.select(customerRoot);
        List<T> entities = session.createQuery(criteriaQuery).getResultList();
        logger.info(String.format("List of entities %s was selected", clazz.toString()));
        return entities;
    }

    public static <T> void deleteEntityFromTable(Session session, T entity) {
        session.delete(entity);
        logger.info(String.format("Entity %s was deleted from DB", entity.getClass().toString()));
    }

}
