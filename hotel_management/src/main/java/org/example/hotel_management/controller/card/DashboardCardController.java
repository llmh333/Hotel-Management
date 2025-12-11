package org.example.hotel_management.controller.card;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.hotel_management.dto.response.DashboardResponseDTO;
import org.example.hotel_management.service.IDashboardService;
import org.example.hotel_management.service.impl.DashboardServiceImpl;
import org.example.hotel_management.util.AlertUtil;
import org.example.hotel_management.util.TaskUtil;

import java.time.format.DateTimeFormatter;

public class DashboardCardController {

    @FXML private Label lblTotalBookings;
    @FXML private Label lblAvailableRooms;
    @FXML private Label lblMonthlyRevenue;

    @FXML private TableView<DashboardResponseDTO.RecentCheckInDTO> tableRecentCheckIns;
    @FXML private TableColumn<DashboardResponseDTO.RecentCheckInDTO, String> colGuestName;
    @FXML private TableColumn<DashboardResponseDTO.RecentCheckInDTO, String> colRoomNumber;
    @FXML private TableColumn<DashboardResponseDTO.RecentCheckInDTO, String> colRoomType;
    @FXML private TableColumn<DashboardResponseDTO.RecentCheckInDTO, String> colCheckInDate;

    private final IDashboardService dashboardService = DashboardServiceImpl.getInstance();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void initialize() {
        setupTable();
        loadData();
    }

    private void setupTable() {
        colGuestName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGuestName()));
        colRoomNumber.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRoomNumber()));
        colRoomType.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRoomType()));

        colCheckInDate.setCellValueFactory(cell -> {
            if (cell.getValue().getCheckInDate() != null) {
                return new SimpleStringProperty(cell.getValue().getCheckInDate().format(formatter));
            }
            return new SimpleStringProperty("");
        });
    }

    private void loadData() {
        TaskUtil.run(
                null,
                () -> dashboardService.getDashboardStatistics(),
                (data) -> {

                    lblTotalBookings.setText(String.valueOf(data.getTotalBookings()));
                    lblAvailableRooms.setText(String.valueOf(data.getAvailableRooms()));
                    lblMonthlyRevenue.setText(String.format("$ %,.2f", data.getMonthlyRevenue()));

                    tableRecentCheckIns.setItems(FXCollections.observableArrayList(data.getRecentCheckIns()));
                },
                (error) -> AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to load dashboard data.", null)
        );
    }
}