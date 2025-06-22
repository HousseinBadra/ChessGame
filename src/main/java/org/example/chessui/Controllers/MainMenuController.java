package org.example.chessui.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import org.example.chessui.auth.AuthService;
import org.example.chessui.auth.SessionManager;

public class MainMenuController {
    public Button historyBtn;
    @FXML private Label welcomeLabel;
    @FXML private Button aiBtn, pvpBtn;
    @FXML private Button logoutBtn;
    private final AuthService auth = new AuthService();
    @FXML public void initialize() {
        welcomeLabel.setText("Welcome, " + SessionManager.getInstance().getUsername());
        aiBtn.setOnAction(e -> switchTo("player_vs_ai.fxml", e));
        pvpBtn.setOnAction(e -> switchTo("two_players.fxml", e));
        historyBtn.setOnAction(e -> switchTo("gameList.fxml", e));
        logoutBtn.setOnAction(e -> {
            try {
                auth.logout();
                switchTo("login.fxml", e);
            } catch (Exception ignored) {}
        });
    }
    private void switchTo(String fxml, ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/chessui/" + fxml));;
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene c = new Scene(root);
            c.getStylesheets().add(getClass().getResource("/org/example/chessui/styles.css").toExternalForm());
            stage.setScene(c);
            stage.setFullScreen(true);
            stage.show();
        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        }
    }
}
