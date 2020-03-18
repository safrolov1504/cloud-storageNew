module com.cloud {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.cloud  to javafx.fxml;
    exports com.cloud;
}