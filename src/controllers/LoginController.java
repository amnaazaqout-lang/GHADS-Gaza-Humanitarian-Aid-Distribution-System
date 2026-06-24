package controllers;

import dao.UserDAO;
import model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    
    private UserDAO userDAO = new UserDAO();
    
    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        // Check if fields are empty
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password");
            return;
        }
        
        // Authenticate user
        model.User user = userDAO.login(username, password);
        
        if (user == null) {
            errorLabel.setText("Invalid username or password");
            return;
        }
        
        // Redirect based on role
        try {
            FXMLLoader loader;
            if (user.getRole().equals("ADMIN")) {
                loader = new FXMLLoader(getClass().getResource("/resources/views/AdminDashboard.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/resources/views/CoordinatorDashboard.fxml"));
            }
            
            Parent root = loader.load();
            
            // Pass user data to dashboard
            if (user.getRole().equals("ADMIN")) {
                AdminDashboardController controller = loader.getController();
                controller.setCurrentUser(user);
            } else {
                CoordinatorDashboardController controller = loader.getController();
                controller.setCurrentUser(user);
            }
            
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            
            // Add CSS
            String cssPath = getClass().getResource("/resources/styles/style.css").toExternalForm();
            if (cssPath != null) {
                scene.getStylesheets().add(cssPath);
            }
            
            stage.setScene(scene);
            stage.setTitle("GHADS - " + user.getRole() + " Dashboard");
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error loading dashboard: " + e.getMessage());
        }
    }
}