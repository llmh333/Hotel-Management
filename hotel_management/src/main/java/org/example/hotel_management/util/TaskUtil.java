package org.example.hotel_management.util;

import javafx.concurrent.Task;
import javafx.scene.Node;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TaskUtil {

    public static <T> void run(Node loadingNode,
                               Supplier<T> heavyLogic,
                               Consumer<T> onSuccess,
                               Consumer<Throwable> onError) {

        if (loadingNode != null) {
            loadingNode.setVisible(true);
        }

        Task<T> task = new Task<>() {
            @Override
            protected T call() throws Exception {
                return heavyLogic.get();
            }
        };

        task.setOnSucceeded(event -> {
            if (loadingNode != null) loadingNode.setVisible(false);
            onSuccess.accept(task.getValue());
        });

        task.setOnFailed(event -> {
            if (loadingNode != null) loadingNode.setVisible(false);

            Throwable error = task.getException();
            error.printStackTrace();

            if (onError != null) {
                onError.accept(error);
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public static <T> void run(Node loadingNode, Supplier<T> heavyLogic, Consumer<T> onSuccess) {
        run(loadingNode, heavyLogic, onSuccess, null);
    }


}
