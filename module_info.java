module com.example._207062_gpa_calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;  // Added this line to fix the error

    opens com.example._207062_gpa_calculator to javafx.fxml;
    exports com.example._207062_gpa_calculator;
}