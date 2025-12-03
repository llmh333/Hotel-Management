package org.example.hotel_management.controller.card;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import org.example.hotel_management.constant.AppConstant;
import org.example.hotel_management.controller.dialog.ItemOccupiedRoomDialogController;
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

    // --- FXML FIELDS ---
    @FXML private TextField txtSearchRoom;
    @FXML private FlowPane containerActiveRooms;

    @FXML private TextField txtSearchPhone;
    @FXML private Label lblInvoiceDate;

    // Info Khách
    @FXML private Label lblCustomerName;
    @FXML private Label lblCustomerPhone;
    @FXML private Label lblRoomNumber;
    @FXML private Label lblCheckInTime;

    // Bảng Hóa Đơn
    @FXML private TableView<InvoiceItemResponseDto> tableInvoice;
    @FXML private TableColumn<InvoiceItemResponseDto, String> colItem;
    @FXML private TableColumn<InvoiceItemResponseDto, String> colDescription;
    @FXML private TableColumn<InvoiceItemResponseDto, Integer> colQty;
    @FXML private TableColumn<InvoiceItemResponseDto, String> colPrice;
    @FXML private TableColumn<InvoiceItemResponseDto, String> colTotal;

    // Tổng tiền
    @FXML private Label lblRoomCharge;
    @FXML private Label lblServiceCharge;
    @FXML private Label lblTotalAmount;
    @FXML private Button btnCheckout;

    // --- SERVICES ---
    private final IRoomService roomService = IRoomServiceImpl.getInstance();
    private final IBookingService bookingService = IBookingServiceImpl.getInstance();
    private final IServicesService servicesService = IServicesServiceImpl.getInstance();

    // --- STATE ---
    private List<RoomResponseDTO> allOccupiedRooms = new ArrayList<>(); // Cache để lọc nhanh
    private List<ItemOccupiedRoomDialogController> itemControllers = new ArrayList<>(); // Để quản lý việc select
    private BookingResponseDto currentBooking; // Booking đang xem
    private double currentTotalAmount = 0.0;

    public void initialize() {
        setupTable();
        loadOccupiedRooms();

        // 1. Logic tìm kiếm phòng (Lọc trên UI)
        txtSearchRoom.textProperty().addListener((obs, oldVal, newVal) -> filterRooms(newVal));

        // 2. Logic tìm kiếm theo SĐT (Enter để tìm)
        txtSearchPhone.setOnAction(e -> findBookingByPhone(txtSearchPhone.getText().trim()));

        // 3. Logic Checkout
        btnCheckout.setOnAction(e -> openPaymentDialog());

        // Set ngày hiện tại
        lblInvoiceDate.setText("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM, yyyy")));

        // Mặc định disable nút checkout khi chưa chọn phòng
        btnCheckout.setDisable(true);
    }

    private void setupTable() {
        colItem.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colDescription.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));
        colQty.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getQuantity()).asObject());
        colPrice.setCellValueFactory(cell -> new SimpleStringProperty(String.format("$%.2f", cell.getValue().getPrice())));
        colTotal.setCellValueFactory(cell -> new SimpleStringProperty(String.format("$%.2f", cell.getValue().getTotal())));
    }

    // --- LOAD DANH SÁCH PHÒNG ---
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

                // Sự kiện Click vào thẻ phòng
                item.setOnMouseClicked(e -> {
                    selectRoomItem(itemOccupiedRoomDialogController); // Highlight UI
                    loadBookingDetails(room.getRoomNumber()); // Load dữ liệu bên phải
                });

                containerActiveRooms.getChildren().add(item);
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    // Xử lý hiệu ứng chọn (Chỉ 1 thẻ sáng màu tại 1 thời điểm)
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

    // --- LOAD CHI TIẾT HÓA ĐƠN ---
    private void loadBookingDetails(String roomNumber) {
        TaskUtil.run(
                null, // Có thể thêm loading overlay cho phần bên phải
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
                    List<ServiceBookingResponseDto> services = data.getValue();

                    populateInvoice(currentBooking, services);
                    btnCheckout.setDisable(false);
                },
                (error) -> AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not load booking details.", "Please try again later.")
        );
    }

    private void findBookingByPhone(String phone) {
        // Logic tương tự loadBookingDetails nhưng tìm booking theo Phone
        // Sau khi tìm thấy booking -> Biết được roomId -> Highlight thẻ phòng bên trái tương ứng
    }

    // --- TÍNH TOÁN & HIỂN THỊ HÓA ĐƠN ---
    private void populateInvoice(BookingResponseDto booking, List<ServiceBookingResponseDto> services) {
        // 1. Fill thông tin khách
        lblCustomerName.setText(booking.getCustomerName());
        lblCustomerPhone.setText(booking.getCustomerPhone());
        lblRoomNumber.setText(booking.getRoomNumber());
        lblCheckInTime.setText(booking.getCheckIn().toString());

        List<InvoiceItemResponseDto> items = new ArrayList<>();

        // 2. Tính tiền phòng (Room Charge)
        int totalHours = Math.toIntExact(Duration.between(booking.getCheckIn(), LocalDateTime.now()).toHours());
        double roomPrice = booking.getPricePerHours(); // Giá phòng lúc đặt
        double roomTotal = totalHours * roomPrice;

        items.add(new InvoiceItemResponseDto("Room " + booking.getRoomNumber(), null, totalHours, roomPrice, roomTotal));

        // 3. Tính tiền dịch vụ
        double serviceTotal = 0;
        for (ServiceBookingResponseDto s : services) {
            items.add(new InvoiceItemResponseDto(s.getName(), s.getDescription(), s.getQuantityOrdered(), s.getPrice(), s.getTotalPrice()));
            serviceTotal += s.getTotalPrice();
        }

        // 4. Update UI Bảng và Tổng tiền
        tableInvoice.setItems(FXCollections.observableArrayList(items));

        lblRoomCharge.setText(String.format("$%.2f", roomTotal));
        lblServiceCharge.setText(String.format("$%.2f", serviceTotal));

        this.currentTotalAmount = roomTotal + serviceTotal; // Có thể + VAT nếu muốn
        lblTotalAmount.setText(String.format("$%.2f", currentTotalAmount));
    }

    // --- THANH TOÁN ---
    private void openPaymentDialog() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hotel_management/fxml/dialog/payment_dialog.fxml"));
//            Parent root = loader.load();
//
//            PaymentDialogController controller = loader.getController();
//            controller.setTotalAmount(this.currentTotalAmount);
//
//            // Callback: Khi thanh toán thành công
//            controller.setOnPaymentSuccess(() -> {
//                // 1. Gọi service checkout (cập nhật DB thành PAID/CHECKED_OUT)
//                checkout(currentBooking.getId());
//
//                // 2. Reload lại danh sách phòng (Phòng vừa trả sẽ biến mất khỏi list)
//                loadOccupiedRooms();
//
//                // 3. Clear màn hình bên phải
//                clearRightPane();
//            });
//
//            Stage stage = new Stage();
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.initStyle(StageStyle.TRANSPARENT);
//            Scene scene = new Scene(root);
//            scene.setFill(null);
//            stage.setScene(scene);
//            stage.showAndWait();
//
//        } catch (IOException e) { e.printStackTrace(); }
    }

//    private void checkout(Long bookingId) {
//        TaskUtil.run(null, () -> bookingService.(bookingId), success -> {}, error -> {});
//    }

    private void clearRightPane() {
        lblCustomerName.setText("-");
        lblTotalAmount.setText("$0.00");
        tableInvoice.getItems().clear();
        btnCheckout.setDisable(true);
        // ... reset các field khác
    }

    // Class tiện ích giữ 2 giá trị
    private static class Pair<K, V> {
        private K key; private V value;
        public Pair(K key, V value) { this.key = key; this.value = value; }
        public K getKey() { return key; }
        public V getValue() { return value; }
    }
}