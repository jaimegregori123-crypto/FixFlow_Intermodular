package com.fixflow.main.MainView;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 1. Cargamos el diseño
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = fxmlLoader.load();

        // 2. Creamos la escena SIN dimensiones fijas
        // Al no poner números, Java intentará usar el tamaño que definiste en Scene Builder
        Scene scene = new Scene(root);

        // 3. Configuramos la ventana (Stage)
        stage.setTitle("FixFlow - Gestión de Activos e Incidencias");

        // --- AQUÍ LA MAGIA ---
        stage.setMaximized(true); // Se abre ocupando toda la pantalla
        // ---------------------

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
