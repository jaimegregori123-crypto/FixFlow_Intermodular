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
        // 1. Cargamos el archivo FXML (asegúrate de que el nombre coincide: vista.fxml)
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista.fxml"));

        // 2. Creamos la escena con el diseño cargado
        Scene scene = new Scene(fxmlLoader.load(), 800, 600); // Aquí puedes poner el tamaño que quieras

        // 3. Configuramos y mostramos la ventana
        stage.setTitle("FixFlow - Gestión de Activos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
