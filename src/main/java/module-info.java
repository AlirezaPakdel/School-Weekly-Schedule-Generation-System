module org.example.schoolschedule {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.schoolschedule to javafx.fxml;
    exports com.example.schoolschedule;
}