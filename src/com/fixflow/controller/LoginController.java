package com.fixflow.controller;

import com.fixflow.dao.UsuarioDAO;
import com.fixflow.main.MainView.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void handleLogin() {
        String user = txtUser.getText();
        String pass = txtPass.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            mostrarAlerta("Error", "Por favor, rellena todos los campos.");
            return;
        }

        if (usuarioDAO.validarLogin(user, pass)) {
            cargarVentanaPrincipal();
        } else {
            mostrarAlerta("Acceso Denegado", "Usuario o contraseña incorrectos.");
        }
    }

    private void cargarVentanaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) txtUser.getScene().getWindow();

            Scene scene = new Scene(root);

            // Aplicamos el CSS global a la ventana principal
            if (App.CSS_GLOBAL != null) {
                scene.getStylesheets().add(App.CSS_GLOBAL);
            }

            stage.setScene(scene);
            stage.setResizable(true);
            stage.setMaximized(false);
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar vista.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
