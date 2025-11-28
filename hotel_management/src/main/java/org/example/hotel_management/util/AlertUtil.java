package org.example.hotel_management.util;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

public class AlertUtil {

    public static void showAlert(Alert.AlertType alertType, String title, String header, String content) {

        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                AlertUtil.class.getResource("/org/example/hotel_management/css/alert.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("my-dialog");

        alert.initStyle(StageStyle.UNDECORATED);
        alert.showAndWait();


    }
}
