package org.example.hotel_management.constant;

import javafx.scene.image.Image;
import org.example.hotel_management.util.LoadIconUtil;

public class AppConstant {

    public static final Image APP_ICON = LoadIconUtil.loadIcon("/org/example/hotel_management/icon/hotel_icon.png");

    public static class View {
        public static final String loginCardPath = "/org/example/hotel_management/fxml/card/loginCard.fxml";
        public static final String registerCardPath = "/org/example/hotel_management/fxml/card/registerCard.fxml";
        public static final String forgotCardPath = "/org/example/hotel_management/fxml/card/forgotCard.fxml";

        public static final String dashboardCardPath = "/org/example/hotel_management/fxml/card/dashboardCard.fxml";
    }
}
