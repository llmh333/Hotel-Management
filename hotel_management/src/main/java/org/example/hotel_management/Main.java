package org.example.hotel_management;

import jakarta.persistence.EntityManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.hotel_management.config.DataInitialize;
import org.example.hotel_management.constant.AppConstant;
import org.example.hotel_management.util.DatabaseMigrationUtil;
import org.example.hotel_management.util.HibernateUtil;
import org.example.hotel_management.util.LoadIconUtil;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        try {
            EntityManager entityManager = HibernateUtil.getEntityManager();
            entityManager.close();

            DatabaseMigrationUtil.migrate();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);

            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(AppConstant.APP_ICON);

            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Kết nối tới cơ sở dữ liệu thất bại");

            alert.showAndWait();
            System.exit(1);
        }
        DataInitialize.init();
        FXMLLoader authDashboardLoader = new FXMLLoader(getClass().getResource("/org/example/hotel_management/fxml/auth_dashboard.fxml"));
        Parent root = authDashboardLoader.load();
        Scene scene = new Scene(root);

        stage.getIcons().add(LoadIconUtil.loadIcon("/org/example/hotel_management/icon/hotel_icon.png"));
        stage.setTitle("Hotel Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}