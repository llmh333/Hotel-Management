package org.example.hotel_management.constant;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.scene.image.Image;
import org.example.hotel_management.util.DotEnvUtil;
import org.example.hotel_management.util.LoadIconUtil;

public class AppConstant {

    public static final Image APP_ICON = LoadIconUtil.loadIcon("/org/example/hotel_management/icon/hotel_icon.png");

    public static class Database {
        private static final Dotenv dotenv = DotEnvUtil.getDotenv();
        public static final String username = dotenv.get("DB_USERNAME");
        public static final String password =  dotenv.get("DB_PASSWORD");
        public static final String host = dotenv.get("DB_HOST");
        public static final String port = dotenv.get("DB_PORT");
        public static final String type = dotenv.get("DB_TYPE");
        public static final String database = dotenv.get("DB_DATABASE");
        public static final String url = "jdbc:" + type + "://" + host + ":" + port + "/" + database + "?createDatabaseIfNotExist=true";
    }

    public static class View {
        public static final String loginCardPath = "/org/example/hotel_management/fxml/card/loginCard.fxml";
        public static final String registerCardPath = "/org/example/hotel_management/fxml/card/registerCard.fxml";
        public static final String forgotCardPath = "/org/example/hotel_management/fxml/card/forgotCard.fxml";

        public static final String dashboardCardPath = "/org/example/hotel_management/fxml/card/dashboardCard.fxml";
        public static final String roomsCardPath = "/org/example/hotel_management/fxml/card/roomsCard.fxml";

        public static final String itemRoomsPath = "/org/example/hotel_management/fxml/itemRoom.fxml";

        public static final String authDashboard =  "/org/example/hotel_management/fxml/authDashboard.fxml";
        public static final String homeDashboard = "/org/example/hotel_management/fxml/homeDashboard.fxml";
    }
}
