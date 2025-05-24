package org.example.chessui.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import org.example.chessui.auth.AuthService;
import org.example.chessui.auth.RegisterRequest;

public class RegistrationController {
    @FXML TextField usernameField;
    @FXML TextField emailField;
    @FXML PasswordField passwordField;
    @FXML Button registerBtn;
    @FXML Hyperlink loginLink;
    @FXML Label errorLabel;
    private final AuthService auth = new AuthService();
    @FXML public void initialize() {
        registerBtn.setOnAction(e -> handleRegister());
        loginLink.setOnAction(e -> switchToLogin());
    }
    private void handleRegister() {
        try {
             String reg = auth.register(
                    new RegisterRequest(
                            usernameField.getText(),
                            emailField.getText(),
                            passwordField.getText()
                    )
            );
             if(!reg.toString().isEmpty()) {
                 errorLabel.setText("Registered successfully. Please log in.");
             }
             else {
                 errorLabel.setText("Registration failed");
             }
        } catch (Exception ex) {
            errorLabel.setText("Registration failed");
        }
    }
    private void switchToLogin() {
        try {
            FXMLLoader f = new FXMLLoader(getClass().getResource("/org/example/chessui/login.fxml"));
            Stage stage = (Stage) registerBtn.getScene().getWindow();
            Scene c = new Scene(f.load());
            c.getStylesheets().add(getClass().getResource("/org/example/chessui/styles.css").toExternalForm());
            stage.setScene(c);
        } catch (Exception ignored) {}
    }
}