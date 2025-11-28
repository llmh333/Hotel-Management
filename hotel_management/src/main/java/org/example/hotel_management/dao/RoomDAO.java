package org.example.hotel_management.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.hotel_management.entity.Room;
import org.example.hotel_management.entity.User;
import org.example.hotel_management.enums.RoomStatus;
import org.example.hotel_management.enums.RoomType;
import org.example.hotel_management.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

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

    public Optional<Room> findByRoomNumber(String roomNumber) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            String jsql = "FROM Room u WHERE u.roomNumber = :roomNumber";
            TypedQuery<Room> query = entityManager.createQuery(jsql, Room.class);
            query.setParameter("roomNumber", roomNumber);

            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    public boolean isAvailability(String roomNumber) {
        Optional<Room> roomOptional = findByRoomNumber(roomNumber);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            if (RoomStatus.AVAILABLE.equals(room.getStatus())) {
                return true;
            }
        }
        return false;
    }
}
