package org.example.hotel_management.util;

import javafx.scene.image.Image;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadIconUtil {

    private static final Logger logger = Logger.getLogger(LoadIconUtil.class.getName());

    public static Image loadIcon(String path) {
        try {
            Image image = new Image(LoadIconUtil.class.getResourceAsStream(path));
            return image;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to load icon " + path);
            return null;
        }
    }

}
