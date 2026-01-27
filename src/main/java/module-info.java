module com.example.world2d {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.world2d to javafx.fxml;
    exports com.example.world2d;
}