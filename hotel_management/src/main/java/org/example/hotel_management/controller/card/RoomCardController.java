package org.example.hotel_management.controller.card;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;
import org.example.hotel_management.constant.AppConstant;
import org.example.hotel_management.controller.ItemRoomController;
import org.example.hotel_management.dto.response.RoomResponseDTO;
import org.example.hotel_management.entity.Room;
import org.example.hotel_management.service.IRoomService;
import org.example.hotel_management.service.impl.IRoomServiceImpl;
import org.example.hotel_management.util.TaskUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RoomCardController {

    @FXML private FlowPane containerRooms;
    @FXML private ScrollPane scrollPane;
    @FXML private ProgressIndicator loadingOverlay;
    @FXML private Button btnRefresh;
    @FXML private TextField txtSearchRoom;

    private final IRoomService roomService = IRoomServiceImpl.getInstance();

    private int pageNum = 1;
    private final int pageSize = 12;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String currentKeyword = "";

    public void initialize() throws IOException {

        loadItemRooms(1);

        PauseTransition debounceTimer = new PauseTransition(Duration.millis(500));

        debounceTimer.setOnFinished(event -> {
            this.currentKeyword = txtSearchRoom.getText().trim();

            // QUAN TRỌNG: Khi tìm kiếm thì phải Reset lại từ đầu
            this.pageNum = 1;
            this.isLastPage = false;
            this.isLoading = false;

            containerRooms.getChildren().clear(); // Xóa danh sách cũ đi
            scrollPane.setVvalue(0); // Cuộn lên đầu

            loadItemRooms(1); // Load lại trang 1 với từ khóa mới
        });

        txtSearchRoom.textProperty().addListener((obs, oldVal, newVal) -> {
            debounceTimer.playFromStart(); // Reset đồng hồ mỗi khi gõ phím
        });

        btnRefresh.setOnAction(event -> {
            pageNum = 1;
            isLoading = false;
            isLastPage = false;

            containerRooms.getChildren().clear();
            scrollPane.setVvalue(0);

            loadItemRooms(1);

            String originalText = btnRefresh.getText();

            btnRefresh.setDisable(true);
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    for(int i = 5; i > 0; i--) {

                        final int secondsLeft = i;
                        Platform.runLater(() -> {
                           btnRefresh.setText(secondsLeft + "s");
                        });
                        Thread.sleep(1000);
                    }
                    return null;
                }
            };

            task.setOnSucceeded(e -> {
                btnRefresh.setText(originalText);
                btnRefresh.setDisable(false);
            });

            Thread threadBtnRefresh = new Thread(task);
            threadBtnRefresh.setDaemon(true);
            threadBtnRefresh.start();
        });

        scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue.doubleValue() > 0.9 && !isLoading && !isLastPage) {
               loadItemRooms(pageNum+1);
           }
        });
    }

    private void loadItemRooms(int pageToLoad) {
        isLoading = true;
        if (pageToLoad == 1) loadingOverlay.setVisible(true);
        int finalPageNum = pageToLoad;
        TaskUtil.run(
                loadingOverlay,
                () -> roomService.getRoomsPagination(currentKeyword, pageToLoad, pageSize),
                (newRooms) -> {
                    isLoading = false;

                    if (pageToLoad == 1) loadingOverlay.setVisible(false);

                    if (newRooms == null || newRooms.isEmpty()) {
                        isLastPage = true;
                        return;
                    }

                    if (newRooms.size() < pageSize) {
                        isLastPage = true;
                    }

                    this.pageNum = finalPageNum;
                    for (RoomResponseDTO room : newRooms) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource(AppConstant.View.itemRoomsPath));
                            Parent item = loader.load();

                            ItemRoomController itemRoomController = loader.getController();
                            itemRoomController.loadData(room);

                            containerRooms.getChildren().add(item);
                        } catch (IOException e) { e.printStackTrace(); }
                    }
                },
                (error) -> {
                    isLoading = false;
                    if (pageToLoad == 1) loadingOverlay.setVisible(false);
                    error.printStackTrace();
                }
        );
    }
}
