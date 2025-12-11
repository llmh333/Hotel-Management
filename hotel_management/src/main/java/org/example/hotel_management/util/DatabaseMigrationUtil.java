package org.example.hotel_management.util;

import org.example.hotel_management.constant.AppConstant;
import org.flywaydb.core.Flyway;

import java.util.logging.Logger;

public class DatabaseMigrationUtil {

    private static final Logger logger = Logger.getLogger(DatabaseMigrationUtil.class.getName());

    public static void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(
                        AppConstant.Database.url,
                        AppConstant.Database.username,
                        AppConstant.Database.password)
                .locations("classpath:/org/example/hotel_management/db")
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load();

        logger.info("Running Database Migration...");
        flyway.migrate();
        logger.info("Database Migration Complete!");

    }
}
