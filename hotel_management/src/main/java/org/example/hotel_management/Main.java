package org.example.hotel_management;

import jakarta.persistence.EntityManager;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.hotel_management.constant.AppConstant;
import org.example.hotel_management.util.HibernateUtil;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        try {
            EntityManager entityManager = HibernateUtil.getEntityManager();
            entityManager.close();
        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);

            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(AppConstant.APP_ICON);

            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Kết nối tới cơ sở dữ liệu thất bại");

            alert.showAndWait();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}