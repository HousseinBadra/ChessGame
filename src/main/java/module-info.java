module org.example.chessui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;
                 // allow usage of Gson library
    exports org.example.chessui.game;
    exports org.example.chessui.engine.types;
    exports org.example.chessui.engine.MoveStrategy;

    opens org.example.chessui to javafx.fxml;
    opens org.example.chessui.Controllers to javafx.fxml;
    exports org.example.chessui;
}