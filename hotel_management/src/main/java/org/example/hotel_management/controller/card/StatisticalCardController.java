package org.example.hotel_management.controller.card;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import org.example.hotel_management.dto.response.DailyRevenueResponseDTO;
import org.example.hotel_management.dto.response.StatisticsResponseDTO;
import org.example.hotel_management.service.IStatisticalService;
import org.example.hotel_management.service.impl.IStatisticalServiceImpl;
import org.example.hotel_management.util.AlertUtil;
import org.example.hotel_management.util.TaskUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StatisticalCardController {

    @FXML
    private ComboBox<String> comboPeriod;
    @FXML private DatePicker datePickerStart;
    @FXML private DatePicker datePickerEnd;
    @FXML private Button btnFilter;

    @FXML private Label lblTotalRevenue;
    @FXML private Label lblTotalBookings;

    @FXML private BarChart<String, Number> barChartRevenue;
    @FXML private CategoryAxis xAxis;
    @FXML private PieChart pieChartSource;

    private final IStatisticalService statService = IStatisticalServiceImpl.getInstance();

    public void initialize() {
        comboPeriod.getItems().addAll("This Week", "This Month", "This Year", "Custom");
        comboPeriod.setValue("This Month");

        comboPeriod.setOnAction(e -> handlePeriodChange());

        btnFilter.setOnAction(e -> loadData());

        handlePeriodChange();
    }

    private void handlePeriodChange() {
        String selected = comboPeriod.getValue();
        LocalDate now = LocalDate.now();

        if ("This Week".equals(selected)) {
            datePickerStart.setValue(now.minusDays(now.getDayOfWeek().getValue() - 1));
            datePickerEnd.setValue(now);
        } else if ("This Month".equals(selected)) {
            datePickerStart.setValue(now.withDayOfMonth(1));
            datePickerEnd.setValue(now.withDayOfMonth(now.lengthOfMonth()));
        } else if ("This Year".equals(selected)) {
            datePickerStart.setValue(now.withDayOfYear(1));
            datePickerEnd.setValue(now.withDayOfYear(now.lengthOfYear()));
        }
        loadData();
    }

    private void loadData() {
        LocalDate start = datePickerStart.getValue();
        LocalDate end = datePickerEnd.getValue();

        TaskUtil.run(
                null,
                () -> statService.getRevenueStatistics(start, end),
                (data) -> {
                    lblTotalRevenue.setText("$ " + String.format("%,.2f", data.getTotalRevenue()));
                    lblTotalBookings.setText(String.valueOf(data.getTotalBookings()));

                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName("Revenue");
                    for (DailyRevenueResponseDTO item : data.getDailyRevenues()) {
                        series.getData().add(new XYChart.Data<>(item.getDate().toString(), item.getAmount()));
                    }
                    barChartRevenue.getData().clear();
                    barChartRevenue.getData().add(series);

                    ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                            new PieChart.Data("Room", data.getTotalRoomRevenue()),
                            new PieChart.Data("Service", data.getTotalServiceRevenue())
                    );
                    pieChartSource.setData(pieData);
                },
                (error) -> AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", error.getMessage(), null)
        );
    }

    private void updateBarChart(StatisticsResponseDTO data) {
        barChartRevenue.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        for (DailyRevenueResponseDTO item : data.getDailyRevenues()) {
            series.getData().add(new XYChart.Data<>(item.getDate().format(formatter), item.getAmount()));
        }

        barChartRevenue.getData().add(series);

        barChartRevenue.setBarGap(5);
        barChartRevenue.setCategoryGap(20);
    }

    private void updatePieChart(StatisticsResponseDTO data) {
        double roomRev = data.getTotalRoomRevenue();
        double serviceRev = data.getTotalServiceRevenue();

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        pieData.add(new PieChart.Data("Room Charges", roomRev));
        pieData.add(new PieChart.Data("Services", serviceRev));

        pieChartSource.setData(pieData);

        pieChartSource.setLegendVisible(true);
        pieChartSource.setLabelsVisible(false);
    }
}
