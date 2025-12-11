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

    private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = validatorFactory.getValidator();
    private static final Logger log = Logger.getLogger(ValidatorUtil.class.getName());


    public static ValidatorFactory getValidatorFactory() {
        if (validatorFactory == null) {
            return Validation.buildDefaultValidatorFactory();
        }
        return null;
    }

    public static <T> String validate(T object) {

        if (object == null) {
            log.warning("Validating object: null");
            return "Object to validate is null";
        }

        log.info("Validating object " + object.toString());
        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (violations.isEmpty()) {
            return null;
        }

        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("\n"));
    }
}
