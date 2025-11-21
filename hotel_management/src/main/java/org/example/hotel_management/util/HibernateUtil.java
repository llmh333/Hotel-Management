package org.example.hotel_management.util;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateUtil {

    private static final Logger logger = Logger.getLogger(HibernateUtil.class.getName());
    private static EntityManagerFactory entityManagerFactory;

    private static EntityManagerFactory buildEntityManagerFactory() {
        try {

            Dotenv dotenv = DotEnvUtil.getDotenv();
            logger.info("Loading environment variables for entity manager factory");

            String username = dotenv.get("DB_USERNAME");
            String password =  dotenv.get("DB_PASSWORD");
            String host = dotenv.get("DB_HOST");
            String port = dotenv.get("DB_PORT");
            String type = dotenv.get("DB_TYPE");
            String database = dotenv.get("DB_DATABASE");
            String url = "jdbc:" + type + "://" + host + ":" + port + "/" + database + "?createDatabaseIfNotExist=true";

            Map<String, String> properties = new HashMap<>();
            properties.put("jakarta.persistence.jdbc.url", url);
            properties.put("jakarta.persistence.jdbc.user", username);
            properties.put("jakarta.persistence.jdbc.password", password);
            logger.info("Loading entity manager factory");

             return Persistence.createEntityManagerFactory("hotel-unit", properties);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Khởi tạo Hibernate thất bại");
        }
        return null;
    }

    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            entityManagerFactory = buildEntityManagerFactory();
        }
        return entityManagerFactory.createEntityManager();
    }

    public static void closeEntityManager() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
