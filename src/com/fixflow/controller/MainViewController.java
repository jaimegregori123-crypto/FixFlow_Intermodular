package com.fixflow.controller;

import com.fixflow.dao.IncidenciaDAO;
import com.fixflow.dao.UsuarioDAO;
import com.fixflow.dao.ActivoDAO; // Asegúrate de importar esto
import com.fixflow.modelos.Incidencia;
import com.fixflow.modelos.Usuario;
import com.fixflow.modelos.Activo;  // Y esto
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import java.util.List;

public class MainViewController {

    @FXML
    private BorderPane rootPane;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ActivoDAO activoDAO = new ActivoDAO(); // Instanciado
    private IncidenciaDAO incidenciaDAO = new IncidenciaDAO();

    private void cargarVista(String archivoFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + archivoFxml));
            Node vista = loader.load();
            rootPane.setCenter(vista);

            // Organizamos por archivo cargado
            switch (archivoFxml) {
                case "tabla_usuarios.fxml":
                    configurarTablaUsuarios(vista);
                    break;
                case "tabla_activos.fxml":
                    configurarTablaActivos(vista);
                    break;
                // ESTO ES LO QUE TE FALTA:
                case "tabla_incidencias.fxml":
                    configurarTablaIncidencias(vista);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarTablaUsuarios(Node vista) {
        TableView<Usuario> tabla = (TableView<Usuario>) vista.lookup("#tablaUsuariosView");
        if (tabla != null) {
            tabla.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
            tabla.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tabla.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("rol"));
            tabla.setItems(FXCollections.observableArrayList(usuarioDAO.listarUsuarios()));
        }
    }

    private void configurarTablaActivos(Node vista) {
        System.out.println("DEBUG: Intentando configurar tabla de activos...");

        // 1. IMPORTANTE: 'vista' es el AnchorPane que cargamos del FXML.
        // Buscamos la TableView dentro de ese AnchorPane por su ID.
        Node componente = vista.lookup("#tablaActivosView");

        if (componente instanceof TableView) {
            @SuppressWarnings("unchecked")
            TableView<Activo> tabla = (TableView<Activo>) componente;

            // 2. Vinculamos columnas (Usando tus nombres de Activo.java)
            tabla.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idActivo"));
            tabla.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tabla.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
            tabla.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("estadoOperativo"));

            // 3. Cargamos los datos
            List<Activo> lista = activoDAO.listarActivos();
            tabla.setItems(FXCollections.observableArrayList(lista));

            System.out.println("✅ ÉXITO: Se han cargado " + lista.size() + " activos en la tabla.");
        } else {
            // Este mensaje te dirá la verdad si sigue fallando
            System.out.println("❌ ERROR CRÍTICO: El ID #tablaActivosView no es una TableView o no se encuentra.");
            if (componente != null) {
                System.out.println("El componente encontrado es de tipo: " + componente.getClass().getName());
            }
        }
    }

    private void configurarTablaIncidencias(Node vista) {
        System.out.println("🔍 Intentando buscar la tabla #tablaIncidenciasView...");
        Node componente = vista.lookup("#tablaIncidenciasView");

        if (componente instanceof TableView) {
            @SuppressWarnings("unchecked")
            TableView<Incidencia> tabla = (TableView<Incidencia>) componente;

            // 1. Vinculamos las columnas
            tabla.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idIncidencia"));
            tabla.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("titulo"));
            tabla.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            tabla.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("prioridad"));
            // La columna 4 (Fecha) se queda vacía porque no está en tu clase Java
            tabla.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("idActivo"));

            // 2. Cargamos los datos y comprobamos si vienen vacíos
            List<Incidencia> lista = incidenciaDAO.listarIncidencias();

            // --- ESTO ES LO QUE NOS DIRÁ QUÉ PASA ---
            System.out.println("📊 DEBUG: El DAO ha devuelto " + (lista != null ? lista.size() : "NULL") + " incidencias.");

            if (lista != null && !lista.isEmpty()) {
                tabla.setItems(FXCollections.observableArrayList(lista));
                System.out.println("✅ Datos de incidencias cargados en la TableView.");
            } else {
                System.out.println("⚠️ ALERTA: La lista está vacía. Comprueba la conexión a la DB o el SELECT del DAO.");
            }
        } else {
            System.out.println("❌ ERROR: No se encontró la TableView. ¿Pusiste el fx:id en Scene Builder?");
        }
    }

    @FXML private void onUsuariosClick() { cargarVista("tabla_usuarios.fxml"); }
    @FXML private void onActivosClick() { cargarVista("tabla_activos.fxml"); }
    @FXML
    private void onIncidenciasClick() {
        System.out.println("DEBUG: Pulsado botón Incidencias"); // Para confirmar que funciona
        cargarVista("tabla_incidencias.fxml");
    }}