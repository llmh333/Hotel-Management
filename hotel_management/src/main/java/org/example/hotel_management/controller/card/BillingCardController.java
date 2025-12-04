package org.example.hotel_management.controller.card;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.hotel_management.constant.AppConstant;
import org.example.hotel_management.controller.dialog.ItemOccupiedRoomDialogController;
import org.example.hotel_management.controller.dialog.PaymentDialogController;
import org.example.hotel_management.dto.response.*;
import org.example.hotel_management.service.IBookingService;
import org.example.hotel_management.service.IRoomService;
import org.example.hotel_management.service.IServicesService;
import org.example.hotel_management.service.impl.IBookingServiceImpl;
import org.example.hotel_management.service.impl.IRoomServiceImpl;
import org.example.hotel_management.service.impl.IServicesServiceImpl;
import org.example.hotel_management.util.AlertUtil;
import org.example.hotel_management.util.TaskUtil;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BillingCardController {

    @FXML private TextField txtSearchRoom;
    @FXML private FlowPane containerActiveRooms;

    @FXML private TextField txtSearchPhone;
    @FXML private Label lblInvoiceDate;

    @FXML private Label lblCustomerName;
    @FXML private Label lblCustomerPhone;
    @FXML private Label lblRoomNumber;
    @FXML private Label lblCheckInTime;

    @FXML private TableView<InvoiceItemResponseDto> tableInvoice;
    @FXML private TableColumn<InvoiceItemResponseDto, String> colItem;
    @FXML private TableColumn<InvoiceItemResponseDto, String> colDescription;
    @FXML private TableColumn<InvoiceItemResponseDto, Integer> colQty;
    @FXML private TableColumn<InvoiceItemResponseDto, String> colPrice;
    @FXML private TableColumn<InvoiceItemResponseDto, String> colTotal;

    @FXML private Label lblRoomCharge;
    @FXML private Label lblServiceCharge;
    @FXML private Label lblTotalAmount;
    @FXML private Button btnCheckout;

    private final IRoomService roomService = IRoomServiceImpl.getInstance();
    private final IBookingService bookingService = IBookingServiceImpl.getInstance();
    private final IServicesService servicesService = IServicesServiceImpl.getInstance();

    private List<RoomResponseDTO> allOccupiedRooms = new ArrayList<>(); // Cache để lọc nhanh
    private List<ItemOccupiedRoomDialogController> itemControllers = new ArrayList<>(); // Để quản lý việc select
    private BookingResponseDto currentBooking; // Booking đang xem
    private double currentTotalAmount = 0.0;
    private double currentServiceCharge = 0.0;
    private double currentRoomCharge = 0.0;
    private String customerName;
    private String customerPhone;
    private String roomNumber;

    public void initialize() {
        setupTable();
        loadOccupiedRooms();

        txtSearchRoom.textProperty().addListener((obs, oldVal, newVal) -> filterRooms(newVal));

        txtSearchPhone.setOnAction(e -> findBookingByPhone(txtSearchPhone.getText().trim()));

        btnCheckout.setOnAction(e -> openPaymentDialog());

        lblInvoiceDate.setText("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM, yyyy")));

        btnCheckout.setDisable(true);
    }

    private void setupTable() {
        colItem.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colDescription.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));
        colQty.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getQuantity()).asObject());
        colPrice.setCellValueFactory(cell -> new SimpleStringProperty(String.format("$%.2f", cell.getValue().getPrice())));
        colTotal.setCellValueFactory(cell -> new SimpleStringProperty(String.format("$%.2f", cell.getValue().getTotal())));
    }

    private void loadOccupiedRooms() {
        TaskUtil.run(
                null,
                () -> roomService.getOccupiedRooms(),
                (rooms) -> {
                    this.allOccupiedRooms = rooms;
                    renderRoomList(rooms);
                },
                (error) -> AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to load rooms.", null)
        );
    }

    private void renderRoomList(List<RoomResponseDTO> rooms) {
        containerActiveRooms.getChildren().clear();
        itemControllers.clear();

        for (RoomResponseDTO room : rooms) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(AppConstant.View.itemOccupiedRoomPath));
                Parent item = loader.load();

                ItemOccupiedRoomDialogController itemOccupiedRoomDialogController = loader.getController();
                itemOccupiedRoomDialogController.setData(room);
                itemControllers.add(itemOccupiedRoomDialogController);

                item.setOnMouseClicked(e -> {
                    selectRoomItem(itemOccupiedRoomDialogController);
                    loadBookingDetails(room.getRoomNumber());
                });

                containerActiveRooms.getChildren().add(item);
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private void selectRoomItem(ItemOccupiedRoomDialogController selectedCtrl) {
        for (ItemOccupiedRoomDialogController ctrl : itemControllers) {
            ctrl.setSelected(ctrl == selectedCtrl);
        }
    }

    private void filterRooms(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            renderRoomList(allOccupiedRooms);
            return;
        }
        List<RoomResponseDTO> filtered = allOccupiedRooms.stream()
                .filter(r -> r.getRoomNumber().contains(keyword))
                .collect(Collectors.toList());
        renderRoomList(filtered);
    }

    private void loadBookingDetails(String roomNumber) {
        TaskUtil.run(
                null,
                () -> {
                    BookingResponseDto booking = bookingService.getBookingByRoomOccupied(roomNumber);
                    if (booking == null) return null;
                    List<ServiceBookingResponseDto> services = servicesService.getServicesByBookingId(booking.getId());
                    return new Pair<>(booking, services);
                },
                (data) -> {
                    if (data == null || data.getKey() == null) {
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Data error",
                                "No booking found for room " + roomNumber,
                                "Please check room status again");
                        return;
                    }
                    this.currentBooking = data.getKey();
                    this.roomNumber = roomNumber;
                    this.customerName = currentBooking.getCustomerName();
                    this.customerPhone = currentBooking.getCustomerPhone();
                    List<ServiceBookingResponseDto> services = data.getValue();

                    populateInvoice(currentBooking, services);
                    btnCheckout.setDisable(false);
                },
                (error) -> AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not load booking details.", "Please try again later.")
        );
    }

    private void findBookingByPhone(String phone) {
    }

    private void populateInvoice(BookingResponseDto booking, List<ServiceBookingResponseDto> services) {
        lblCustomerName.setText(booking.getCustomerName());
        lblCustomerPhone.setText(booking.getCustomerPhone());
        lblRoomNumber.setText(booking.getRoomNumber());
        lblCheckInTime.setText(booking.getCheckIn().toString());
        this.customerName = booking.getCustomerName();
        this.customerPhone = booking.getCustomerPhone();
        this.roomNumber = booking.getRoomNumber();

        if (booking.getCheckIn().isAfter(LocalDateTime.now())) {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Warning", "Booking is not yet checked in.", null);
            return;
        }

        List<InvoiceItemResponseDto> items = new ArrayList<>();

        int totalHours;
        if (booking.getCheckOut() != null) {
            totalHours = Math.toIntExact(Duration.between(booking.getCheckIn(), LocalDateTime.now()).toHours());
        } else {
            totalHours = Math.toIntExact(Duration.between(booking.getCheckIn(), booking.getCheckOut()).toHours());
        }
        double roomPrice = booking.getPricePerHours();
        double roomTotal = totalHours * roomPrice;

        items.add(new InvoiceItemResponseDto("Room " + booking.getRoomNumber(), null, totalHours, roomPrice, roomTotal));

        double serviceTotal = 0;
        for (ServiceBookingResponseDto s : services) {
            items.add(new InvoiceItemResponseDto(s.getName(), s.getDescription(), s.getQuantityOrdered(), s.getPrice(), s.getTotalPrice()));
            serviceTotal += s.getTotalPrice();
        }

        tableInvoice.setItems(FXCollections.observableArrayList(items));

        lblRoomCharge.setText(String.format("$%.2f", roomTotal));
        lblServiceCharge.setText(String.format("$%.2f", serviceTotal));

        this.currentTotalAmount = roomTotal + serviceTotal;
        this.currentServiceCharge = serviceTotal;
        this.currentRoomCharge = roomTotal;
        lblTotalAmount.setText(String.format("$%.2f", currentTotalAmount));
    }

    private void openPaymentDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hotel_management/fxml/dialog/payment_dialog.fxml"));
            Parent root = loader.load();

            PaymentDialogController controller = loader.getController();
            controller.setData(this.currentTotalAmount, this.customerName, this.customerName , this.currentRoomCharge, this.currentServiceCharge, this.roomNumber, this.currentBooking.getId());

            controller.setOnPaymentSuccess(() -> {
                checkout(currentBooking.getId());

                loadOccupiedRooms();

                clearRightPane();
            });

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root);
            scene.setFill(null);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) { e.printStackTrace(); }
    }

    private void checkout(Long bookingId) {
//        AlertUtil.showConfirmation("Confirm Checkout", "Are you sure you want to checkout?", "This action cannot be undone.");
    }

    private void clearRightPane() {
        lblCustomerName.setText("-");
        lblTotalAmount.setText("$0.00");
        tableInvoice.getItems().clear();
        btnCheckout.setDisable(true);
    }

    private static class Pair<K, V> {
        private K key; private V value;
        public Pair(K key, V value) { this.key = key; this.value = value; }
        public K getKey() { return key; }
        public V getValue() { return value; }
    }
}