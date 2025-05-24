module org.example.chessui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;


    opens org.example.chessui to javafx.fxml;
    opens org.example.chessui.Controllers to javafx.fxml;
    exports org.example.chessui;
}