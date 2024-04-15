module com.example.cinema_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.sql.rowset;
    requires java.desktop;


    opens com.example.cinema_app to javafx.fxml;
    exports com.example.cinema_app;
}