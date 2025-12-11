package org.example.hotel_management.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.example.hotel_management.dto.response.DailyRevenueResponseDTO;
import org.example.hotel_management.util.HibernateUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class StatisticalDAO {

    private static final StatisticalDAO INSTANCE = new StatisticalDAO();
    private StatisticalDAO() {}
    public static StatisticalDAO getInstance() { return INSTANCE; }

    public Object[] getInvoiceStats(LocalDateTime start, LocalDateTime end) {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
            // JPQL: Tính tổng trên entity Invoice
            String hql = "SELECT COUNT(i), SUM(i.totalAmount), SUM(i.totalRoomCharge), SUM(i.totalServiceCharge) " +
                    "FROM Invoice i " +
                    "WHERE i.createdAt BETWEEN :start AND :end";

            TypedQuery<Object[]> query = em.createQuery(hql, Object[].class);
            query.setParameter("start", start);
            query.setParameter("end", end);

            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về mảng toàn 0 nếu lỗi
            return new Object[]{0L, 0.0, 0.0, 0.0};
        }
    }

    /**
     * Lấy dữ liệu biểu đồ cột (Group by Date)
     */
    public List<DailyRevenueResponseDTO> getDailyRevenueStats(LocalDateTime start, LocalDateTime end) {
        List<DailyRevenueResponseDTO> resultList = new ArrayList<>();
        try (EntityManager em = HibernateUtil.getEntityManager()) {
            // Dùng Native SQL để dùng hàm DATE()
            String sql = "SELECT DATE(created_at) as day, SUM(total_amount) as amount " +
                    "FROM invoices " +
                    "WHERE created_at BETWEEN :start AND :end " +
                    "GROUP BY DATE(created_at) " +
                    "ORDER BY day ASC";

            Query query = em.createNativeQuery(sql);
            query.setParameter("start", start);
            query.setParameter("end", end);

            List<Object[]> rows = query.getResultList();
            for (Object[] row : rows) {
                Date sqlDate = (java.sql.Date) row[0];
                Double amount = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
                resultList.add(new DailyRevenueResponseDTO(sqlDate.toLocalDate(), amount));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
