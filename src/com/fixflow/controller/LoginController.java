package com.fixflow.controller;

import com.fixflow.dao.UsuarioDAO;
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
            // Si el login es correcto, cerramos esta ventana y abrimos la principal
            cargarVentanaPrincipal();
        } else {
            mostrarAlerta("Acceso Denegado", "Usuario o contraseña incorrectos.");
        }
    }

    private void cargarVentanaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista.fxml"));
            Parent root = loader.load();

            // 1. Obtenemos el Stage actual
            Stage stage = (Stage) txtUser.getScene().getWindow();

            // 2. Creamos la escena
            Scene scene = new Scene(root);

            // 3. SE LO ASIGNAMOS Y FORZAMOS MAXIMIZADO
            stage.setScene(scene);

            // Esto es lo que te falta: volver a decirle que se estire
            stage.setResizable(true);
            stage.setMaximized(false); // Truco: lo quitamos y ponemos para que refresque
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
