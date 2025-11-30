package org.example.hotel_management.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class AlertUtil {

    public static void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        if (Platform.isFxApplicationThread()) {
            doShowAlert(alertType, title, header, content);
        } else {
            Platform.runLater(() -> doShowAlert(alertType, title, header, content));
        }
    }

    private static void doShowAlert(Alert.AlertType alertType, String title, String header, String content) {
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

    public static boolean showConfirmation(String title, String header, String content) {
        if (Platform.isFxApplicationThread()) {
            return doShowConfirmation(title, header, content);
        } else {
            AtomicBoolean resultHolder = new AtomicBoolean(false);
            final Object lock = new Object();

            Platform.runLater(() -> {
                boolean result = doShowConfirmation(title, header, content);
                resultHolder.set(result);
                synchronized (lock) {
                    lock.notify();
                }
            });

            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return resultHolder.get();
        }
    }

    private static boolean doShowConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                AlertUtil.class.getResource("/org/example/hotel_management/css/alert.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("my-dialog");

        alert.initStyle(StageStyle.UNDECORATED);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}