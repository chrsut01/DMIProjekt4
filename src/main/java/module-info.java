module com.example.dmiprojekt4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.dmiprojekt4 to javafx.fxml;
    exports com.example.dmiprojekt4;
}