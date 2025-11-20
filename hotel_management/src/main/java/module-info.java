module org.example.hotel_management {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.hotel_management to javafx.fxml;
    opens org.example.hotel_management.controller to javafx.fxml;

    exports org.example.hotel_management;
}