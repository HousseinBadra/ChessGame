package org.example.chessui.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.chessui.auth.AuthService;
import org.example.chessui.auth.LoginRequest;
import org.example.chessui.auth.SessionManager;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBtn;
    @FXML private Hyperlink registerLink;
    @FXML private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        loginBtn.setOnAction(evt -> handleLogin(evt));
        registerLink.setOnAction(evt -> openRegistration(evt));
    }

    private void handleLogin(ActionEvent e) {
        String user = usernameField.getText();
        String pass = passwordField.getText();
        if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Please enter both username and password.");
            return;
        }
        try {
            String token = authService.login(new LoginRequest(user, pass));
            if(!token.isEmpty()) {
                SessionManager.getInstance().setSession(token, user);
                switchScene("main_menu.fxml", e);
            }
            else {
                errorLabel.setText("Login failed");
            }
        } catch (Exception err) {
            err.printStackTrace();
            errorLabel.setText("Login failed: " + err.getMessage());
        }
    }

    private void openRegistration(ActionEvent e) {
        switchScene("registration.fxml", e);
    }

    private void switchScene(String fxml, ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/chessui/" + fxml));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException err) {
            err.printStackTrace();
            errorLabel.setText("Could not load " + fxml);
        }
    }
}