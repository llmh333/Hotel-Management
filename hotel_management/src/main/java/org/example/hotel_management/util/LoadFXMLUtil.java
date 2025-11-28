package org.example.hotel_management.util;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.Map;
import java.util.logging.Logger;

public class LoadFXMLUtil {

    private static final Logger logger = Logger.getLogger(LoadFXMLUtil.class.getName());

    public static <T> Map<Parent, T> loadFXML(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(LoadFXMLUtil.class.getResource(path));
            Parent root = loader.load();
            T controller = loader.getController();
            return Map.of(root, controller);
        } catch (Exception e) {
            logger.warning("Failed to load FXML " + path);
        }
        return null;
    }
}
