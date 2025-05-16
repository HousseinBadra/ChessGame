module org.example.chessui {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.chessui to javafx.fxml;
    exports org.example.chessui;
}