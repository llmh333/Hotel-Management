package org.example.hotel_management.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.hotel_management.entity.Room;
import org.example.hotel_management.util.HibernateUtil;

import java.util.List;

public class RoomDAO extends GenericsDAO<Room, Integer> {

    private static final RoomDAO INSTANCE = new RoomDAO();
    private RoomDAO() {
        super(Room.class);
    }

    public static RoomDAO getInstance() {
        return INSTANCE;
    }

    public List<Room> getRoomsPagination(int page, int size) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {

            TypedQuery<Room> query = entityManager.createQuery("FROM Room", Room.class);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);

            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }
}
