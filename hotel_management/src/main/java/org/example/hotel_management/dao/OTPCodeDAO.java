package org.example.hotel_management.dao;

import jakarta.persistence.EntityManager;
import org.example.hotel_management.entity.OTPCode;
import org.example.hotel_management.util.HibernateUtil;

import java.util.Optional;

public class OTPCodeDAO extends GenericsDAO<OTPCode, Long>{

    public static final OTPCodeDAO INSTANCE = new OTPCodeDAO(OTPCode.class);

    private OTPCodeDAO(Class<OTPCode> entityClass) {
        super(entityClass);
    }

    public static OTPCodeDAO getInstance() {
        return INSTANCE;
    }

    public Optional<OTPCode> findByOTP(String otpCode) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            String jsql = "FROM OTPCode u WHERE u.code = :otpCode";
            return Optional.ofNullable(entityManager.createQuery(jsql, OTPCode.class)
                    .setParameter("otpCode", otpCode)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    public Optional<OTPCode> findByEmail(String email) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            String jsql = "FROM OTPCode u WHERE u.userEmail.email = :email";
            return Optional.ofNullable(entityManager.createQuery(jsql, OTPCode.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }
}
