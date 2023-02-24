module com.example.parser {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;


    opens com.example.parser to javafx.fxml;
    exports com.example.parser;
}