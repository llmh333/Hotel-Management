package org.example.hotel_management.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ValidatorUtil {

    private static ValidatorFactory validatorFactory = getValidatorFactory();
    private static final Validator validator = validatorFactory.getValidator();
    private static final Logger log = Logger.getLogger(ValidatorUtil.class.getName());
    public static ValidatorFactory getValidatorFactory() {
        if (validatorFactory == null) {
            return Validation.buildDefaultValidatorFactory();
        }
        return null;
    }

    public static <T> String validate(T object) {

        log.info("Validating object " + object.toString());
        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (violations.isEmpty()) {
            return null;
        }

        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("\n"));
    }

    public static void showErrorValidatorAlert(String title, String errors) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Please check the information again");
        alert.setContentText(errors);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                ValidatorUtil.class.getResource("/org/example/hotel_management/css/alert.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("my-dialog");

        alert.initStyle(StageStyle.UNDECORATED);


        alert.showAndWait();
    }
}
