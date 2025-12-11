package org.example.hotel_management.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.hotel_management.dto.response.DashboardResponseDTO;
import org.example.hotel_management.util.HibernateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAO {

    private static final DashboardDAO INSTANCE = new DashboardDAO();
    private DashboardDAO() {}
    public static DashboardDAO getInstance() { return INSTANCE; }


    public Long countTotalBookings() {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
            return em.createQuery("SELECT COUNT(b) FROM Booking b", Long.class).getSingleResult();
        } catch (Exception e) { e.printStackTrace(); return 0L; }
    }

    public Long countAvailableRooms() {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
            // Lưu ý: Sửa 'AVAILABLE' thành Enum tương ứng trong code bạn (VD: RoomStatus.AVAILABLE)
            return em.createQuery("SELECT COUNT(r) FROM Room r WHERE r.status = 'AVAILABLE'", Long.class).getSingleResult();
        } catch (Exception e) { e.printStackTrace(); return 0L; }
    }

    public Double getMonthlyRevenue() {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
            LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
            LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(LocalTime.MAX);

            String hql = "SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.createdAt BETWEEN :start AND :end";

            TypedQuery<Double> query = em.createQuery(hql, Double.class);
            query.setParameter("start", startOfMonth);
            query.setParameter("end", endOfMonth);

            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } catch (Exception e) { e.printStackTrace(); return 0.0; }
    }

    public List<DashboardResponseDTO.RecentCheckInDTO> getRecentCheckIns() {
        try (EntityManager em = HibernateUtil.getEntityManager()) {

            String hql = "SELECT new org.example.hotel_management.dto.response.DashboardResponseDTO$RecentCheckInDTO(" +
                    "   b.customer.fullName, " +
                    "   b.room.roomNumber, " +
                    "   str(b.room.roomType), " +
                    "   b.checkIn " +
                    ") " +
                    "FROM Booking b " +
                    "ORDER BY b.checkIn DESC";

            return em.createQuery(hql, DashboardResponseDTO.RecentCheckInDTO.class)
                    .setMaxResults(7)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}