package org.example.hotel_management.util;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationUtil {

    public static void addClickEffect(Node node) {

        node.setOnMousePressed(event -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
            st.setToX(0.95);
            st.setToY(0.95);
            st.play();
        });


        node.setOnMouseReleased(event -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        node.setOnMouseExited(event -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }


    public static void applyFor(Node... nodes) {
        for (Node node : nodes) {
            addClickEffect(node);
        }
    }
}
