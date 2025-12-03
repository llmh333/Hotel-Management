package org.example.hotel_management.controller.card;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.hotel_management.constant.AppConstant;
import org.example.hotel_management.controller.dialog.OrderServiceController;
import org.example.hotel_management.controller.dialog.ServiceDialogController;
import org.example.hotel_management.dao.ServiceDAO;
import org.example.hotel_management.dto.response.ServiceResponseDTO;
import org.example.hotel_management.entity.UserSessionUtil;
import org.example.hotel_management.enums.ServiceCategory;
import org.example.hotel_management.service.IServicesService;
import org.example.hotel_management.service.impl.IServicesServiceImpl;
import org.example.hotel_management.util.AlertUtil;
import org.example.hotel_management.util.TaskUtil;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ServicesCardController {

    // --- FXML FIELDS ---
    @FXML private ToggleButton btnAllServices;
    @FXML private ToggleButton btnDrinks;
    @FXML private ToggleButton btnFood;
    @FXML private ToggleButton btnOthers;
    @FXML private Button btnNewService;
    @FXML private ToggleGroup categoryGroup;
    @FXML private TextField filedSearchService;
    @FXML private Pagination pagination;

    @FXML private TableView<ServiceResponseDTO> tableServices;
    @FXML private TableColumn<ServiceResponseDTO, Long> colId;
    @FXML private TableColumn<ServiceResponseDTO, String> colName;
    @FXML private TableColumn<ServiceResponseDTO, String> colCategory;
    @FXML private TableColumn<ServiceResponseDTO, String> colPrice;
    @FXML private TableColumn<ServiceResponseDTO, Integer> colQuantity;
    @FXML private TableColumn<ServiceResponseDTO, Void> colAction;

    private static final ServiceDAO serviceDAO = ServiceDAO.getInstance();
    private static final IServicesService serviceService = IServicesServiceImpl.getInstance();
    private final int PAGE_SIZE = 10;

    private String currentCategory = "ALL";
    private String currentKeyword = "";

    public void initialize() {

        if (btnAllServices.getToggleGroup() != null) {
            btnAllServices.getToggleGroup().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle == null) {
                    oldToggle.setSelected(true);
                }
            });
        }

        setupTableColumns();
        setupFilters();
        setupActions();

        loadData(0);
    }

    // --- 1. SETUP CỘT & ACTION ---
    private void setupTableColumns() {
        colId.setCellValueFactory(cell -> new SimpleObjectProperty<Long>(cell.getValue().getId()));
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colCategory.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategory().name()));
        colPrice.setCellValueFactory(cell -> new SimpleStringProperty("$" + cell.getValue().getPrice()));
        colQuantity.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getQuantity()));

        // CẤU HÌNH CỘT ACTION (Chứa 3 nút: Order, Edit, Delete)
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnOrder = createIconButton("fas-cart-plus", "btn-action-primary", "Order");
            private final Button btnEdit = createIconButton("fas-edit", "btn-action-warning", "Edit");
            private final Button btnDelete = createIconButton("fas-trash-alt", "btn-action-danger", "Delete");

            private final HBox pane = new HBox(10, btnOrder, btnEdit, btnDelete);

            {
                pane.setAlignment(Pos.CENTER);

                btnOrder.setOnAction(event -> openOrderDialog(getTableView().getItems().get(getIndex())));

                btnEdit.setOnAction(event -> openServiceDialog(getTableView().getItems().get(getIndex())));

                btnDelete.setOnAction(event -> handleDelete(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    // --- 2. SETUP BỘ LỌC & TÌM KIẾM ---
    private void setupFilters() {
        categoryGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                ToggleButton btn = (ToggleButton) newVal;
                this.currentCategory = mapButtonTextToCategory(btn.getText());
                loadData(0);
            }
        });

        PauseTransition debounceTimer = new PauseTransition(Duration.millis(500));
        debounceTimer.setOnFinished(event -> {
            this.currentKeyword = filedSearchService.getText().trim();
            loadData(0);
        });

        filedSearchService.textProperty().addListener((obs, oldVal, newVal) -> {
            debounceTimer.playFromStart();
        });


        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            loadData(newIndex.intValue());
        });
    }

    private void setupActions() {
        // Nút "New Service"
        btnNewService.setOnAction(event -> openServiceDialog(null)); // null = Tạo mới
    }

    // --- 3. LOGIC LOAD DỮ LIỆU (TaskUtil) ---
    private void loadData(int pageIndex) {
        ServiceCategory categoryEnum = parseCategory(currentCategory);


        // Dùng TaskUtil để không bị đơ giao diện khi query DB
        TaskUtil.run(
                null, // Loading overlay (nếu có)
                () -> {
                    // Bước A: Đếm tổng số bản ghi để chia trang
                    long totalItems = serviceDAO.countByCategory(categoryEnum);
                    // Bước B: Lấy dữ liệu trang hiện tại
                    List<ServiceResponseDTO> list = serviceService.findAll(currentKeyword, categoryEnum
                            , pageIndex + 1, PAGE_SIZE); // DB thường tính page từ 1
                    return new PageData<>(list, totalItems);
                },
                (result) -> {
                    // Bước C: Cập nhật UI
                    tableServices.setItems(FXCollections.observableList(result.data));

                    // Tính số trang
                    int pageCount = (int) Math.ceil((double) result.totalItems / PAGE_SIZE);
                    pagination.setPageCount(pageCount == 0 ? 1 : pageCount);
                    pagination.setCurrentPageIndex(pageIndex);
                },
                (error) -> {
                    AlertUtil.showAlert(Alert.AlertType.ERROR,"Error", "Failed to load services: " + error.getMessage(), null);
                }
        );
    }

    // --- 4. CÁC HÀM XỬ LÝ NGHIỆP VỤ ---

    // Mở Dialog Thêm/Sửa
    private void openServiceDialog(ServiceResponseDTO service) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(AppConstant.View.serviceDialogPath));
            Parent root = loader.load();

            ServiceDialogController controller = loader.getController();

            if (service != null) {
                controller.setServiceData(service); // Chế độ Update
            }

            // Đăng ký Callback: Khi lưu thành công thì reload bảng
            controller.setOnSuccessCallback(() -> loadData(pagination.getCurrentPageIndex()));

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Mở Dialog Order (Gọi đồ)
    private void openOrderDialog(ServiceResponseDTO service) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(AppConstant.View.bookingServicePath));
            Parent root = loader.load();

            OrderServiceController controller = loader.getController();
            controller.setServiceData(service);

            controller.setOnSuccessCallback(() -> {
                int currentPage = pagination.getCurrentPageIndex();
                loadData(currentPage);
            });

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Xóa dịch vụ
    private void handleDelete(ServiceResponseDTO serviceResponseDTO) {

        if (!UserSessionUtil.getInstance().isAdmin()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR,"Access Denied", "Only Admin can perform this action.", null);
            return;
        }

        boolean confirm = AlertUtil.showConfirmation(
                "Delete Service",
                "Are you sure you want to delete '" + serviceResponseDTO.getName() + "'?",
                null
        );

        if (confirm) {
            TaskUtil.run(
                    null,
                    () -> serviceService.deleteServiceById(serviceResponseDTO.getId()),
                    (success) -> {
                        if (success) {
                            AlertUtil.showAlert(
                                    Alert.AlertType.INFORMATION,
                                    "Deleted",
                                    "Service deleted successfully.",
                                    null
                            );
                            loadData(pagination.getCurrentPageIndex());
                        } else {
                            AlertUtil.showAlert(
                                    Alert.AlertType.ERROR,
                                    "Could not delete service",
                                    "Service delete failed",
                                    "The service might not exist or may be in use."
                            );
                        }
                    },
                    (error) -> {
                        AlertUtil.showAlert(
                                Alert.AlertType.ERROR,
                                "Error",
                                "An unexpected error occurred while deleting the service.",
                                error.getMessage()
                        );
                    }
            );
        }
    }


    private Button createIconButton(String iconLiteral, String styleClass, String tooltip) {
        Button btn = new Button();
        FontIcon icon = new FontIcon(iconLiteral);
        icon.setIconSize(14);
        icon.setIconColor(Color.WHITE); // Hoặc màu theo style class
        btn.setGraphic(icon);
        btn.getStyleClass().addAll("btn-icon", styleClass); // CSS class
        btn.setTooltip(new Tooltip(tooltip));
        return btn;
    }

    private String mapButtonTextToCategory(String text) {
        if (text.contains("All")) return "ALL";
        if (text.contains("Food")) return "FOOD";
        if (text.contains("Drink")) return "DRINK";
        if (text.contains("Other")) return "OTHER";
        return "ALL";
    }

    private ServiceCategory parseCategory(String category) {
        if (category == null || "ALL".equalsIgnoreCase(category)) {
            return null;
        }
        try {
            return ServiceCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }


    // Class nội bộ để chứa kết quả trả về từ Task
    private static class PageData<T> {
        List<T> data;
        long totalItems;
        public PageData(List<T> data, long totalItems) {
            this.data = data;
            this.totalItems = totalItems;
        }
    }
}