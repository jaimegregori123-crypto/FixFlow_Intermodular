package com.fixflow.controller;

import com.fixflow.dao.IncidenciaDAO;
import com.fixflow.dao.IntervencionDAO;
import com.fixflow.dao.UsuarioDAO;
import com.fixflow.dao.ActivoDAO;
import com.fixflow.main.MainView.App;
import com.fixflow.modelos.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.sql.Date;
import java.time.LocalDate;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class MainViewController {

    @FXML
    private BorderPane rootPane;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ActivoDAO activoDAO = new ActivoDAO();
    private IncidenciaDAO incidenciaDAO = new IncidenciaDAO();
    private IntervencionDAO intervencionDAO = new IntervencionDAO();

    private void cargarVista(String archivoFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + archivoFxml));
            Parent vista = loader.load();

            // Inyectamos el CSS global antes de añadir al BorderPane
            if (App.CSS_GLOBAL != null) {
                vista.getStylesheets().add(App.CSS_GLOBAL);
            }

            // Forzar que la vista ocupe todo el espacio disponible del BorderPane
            if (vista instanceof VBox) {
                VBox vbox = (VBox) vista;
                vbox.setMaxWidth(Double.MAX_VALUE);
                vbox.setMaxHeight(Double.MAX_VALUE);
                BorderPane.setAlignment(vbox, javafx.geometry.Pos.TOP_LEFT);
            }

            rootPane.setCenter(vista);

            switch (archivoFxml) {
                case "tabla_usuarios.fxml":
                    configurarTablaUsuarios(vista);
                    break;
                case "tabla_activos.fxml":
                    configurarTablaActivos(vista);
                    break;
                case "tabla_incidencias.fxml":
                    configurarTablaIncidencias(vista);
                    break;
                case "tabla_intervenciones.fxml":
                    configurarTablaIntervenciones(vista);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarTablaUsuarios(Node vista) {
        System.out.println("DEBUG: Configurando tabla de usuarios y conectando formulario...");

        txtUserNombre = (TextField) vista.lookup("#txtUserNombre");
        txtUserRol = (ComboBox<String>) vista.lookup("#txtUserRol");
        txtUserPassword = (PasswordField) vista.lookup("#txtUserPassword");
        btnAgregarUsuario = (Button) vista.lookup("#btnAgregarUsuario");

        if (btnAgregarUsuario != null) {
            btnAgregarUsuario.setOnAction(event -> onAgregarUsuarioClick());
            System.out.println("✅ Botón de usuarios vinculado.");
        }

        if (txtUserRol != null) {
            txtUserRol.getItems().setAll("Administrador", "Técnico");
            txtUserRol.setValue("Técnico");
        }

        TableView<Usuario> tabla = (TableView<Usuario>) vista.lookup("#tablaUsuariosView");
        if (tabla != null) {
            tabla.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
            tabla.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tabla.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("rol"));
            tabla.setItems(FXCollections.observableArrayList(usuarioDAO.listarUsuarios()));
            System.out.println("✅ Tabla de usuarios cargada con éxito.");
        } else {
            System.out.println("❌ Error: No se encontró #tablaUsuariosView");
        }
    }

    private void configurarTablaActivos(Node vista) {
        System.out.println("DEBUG: Configurando tabla y conectando botón manualmente...");

        txtNombre = (TextField) vista.lookup("#txtNombre");
        txtUbicacion = (TextField) vista.lookup("#txtUbicacion");
        txtEstado = (TextField) vista.lookup("#txtEstado");

        Button btnAgregar = (Button) vista.lookup("#btnAgregarActivo");
        if (btnAgregar != null) {
            btnAgregar.setOnAction(event -> onAgregarActivoClick());
            System.out.println("✅ Botón vinculado correctamente.");
        } else {
            System.out.println("❌ No se encontró el botón con ID #btnAgregarActivo");
        }

        Node componente = vista.lookup("#tablaActivosView");
        if (componente instanceof TableView) {
            @SuppressWarnings("unchecked")
            TableView<Activo> tabla = (TableView<Activo>) componente;

            tabla.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idActivo"));
            tabla.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tabla.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
            tabla.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("estadoOperativo"));

            List<Activo> lista = activoDAO.listarActivos();
            tabla.setItems(FXCollections.observableArrayList(lista));
        }
    }

    private void configurarTablaIncidencias(Node vista) {
        System.out.println("🔍 Configurando módulo de incidencias...");

        txtIncidenciaTitulo = (TextField) vista.lookup("#txtIncidenciaTitulo");
        txtIncidenciaDesc = (TextField) vista.lookup("#txtIncidenciaDesc");
        txtIncidenciaActivo = (TextField) vista.lookup("#txtIncidenciaActivo");
        comboPrioridad = (ComboBox<String>) vista.lookup("#comboPrioridad");
        btnAgregarIncidencia = (Button) vista.lookup("#btnAgregarIncidencia");

        if (btnAgregarIncidencia != null) {
            btnAgregarIncidencia.setOnAction(event -> onAgregarIncidenciaClick());
        }

        if (comboPrioridad != null) {
            comboPrioridad.getItems().setAll("Baja", "Media", "Alta", "Crítica");
        }

        Node componente = vista.lookup("#tablaIncidenciasView");
        if (componente instanceof TableView) {
            @SuppressWarnings("unchecked")
            TableView<Incidencia> tabla = (TableView<Incidencia>) componente;

            tabla.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idIncidencia"));
            tabla.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("titulo"));
            tabla.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            tabla.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("prioridad"));
            tabla.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("fecha"));
            tabla.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("idActivo"));

            List<Incidencia> lista = incidenciaDAO.listarIncidencias();
            if (lista != null) {
                tabla.setItems(FXCollections.observableArrayList(lista));
            }
        }
    }

    private void configurarTablaIntervenciones(Node vista) {
        txtIntervencionDesc = (TextField) vista.lookup("#txtIntervencionDesc");
        txtIntervencionIncidencia = (TextField) vista.lookup("#txtIntervencionIncidencia");
        txtIntervencionUsuario = (TextField) vista.lookup("#txtIntervencionUsuario");
        btnAgregarIntervencion = (Button) vista.lookup("#btnAgregarIntervencion");

        if (btnAgregarIntervencion != null) {
            btnAgregarIntervencion.setOnAction(event -> onAgregarIntervencionClick());
        }

        TableView<Intervencion> tabla = (TableView<Intervencion>) vista.lookup("#tablaIntervencionesView");
        if (tabla != null) {
            tabla.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idIntervencion"));
            tabla.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("observaciones"));
            tabla.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("fecha"));
            tabla.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("idIncidencia"));
            tabla.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("idUsuario"));

            tabla.setItems(FXCollections.observableArrayList(intervencionDAO.obtenerIntervenciones()));
            System.out.println("📊 Datos de intervenciones cargados.");
        }
    }

    private void onAgregarIntervencionClick() {
        try {
            Intervencion nuevaInter = new Intervencion();
            nuevaInter.setObservaciones(txtIntervencionDesc.getText());
            nuevaInter.setIdIncidencia(Integer.parseInt(txtIntervencionIncidencia.getText()));
            nuevaInter.setIdUsuario(Integer.parseInt(txtIntervencionUsuario.getText()));

            if (nuevaInter.getObservaciones().isEmpty()) {
                System.out.println("⚠️ Escribe una descripción técnica.");
                return;
            }

            if (intervencionDAO.registraIntervencion(nuevaInter)) {
                System.out.println("✅ Intervención guardada con éxito.");
                onIntervencionesClick();
            }

        } catch (NumberFormatException e) {
            System.out.println("⚠️ ERROR: El ID de incidencia y de usuario deben ser números enteros.");
        } catch (Exception e) {
            System.out.println("❌ Error inesperado: " + e.getMessage());
        }
    }

    @FXML
    private TableView<Incidencia> tablaIncidenciasView;

    @FXML private void onUsuariosClick()     { cargarVista("tabla_usuarios.fxml"); }
    @FXML private void onActivosClick()      { cargarVista("tabla_activos.fxml"); }

    @FXML
    private void onIncidenciasClick() {
        System.out.println("DEBUG: Pulsado botón Incidencias");
        cargarVista("tabla_incidencias.fxml");
    }

    @FXML
    private void onIntervencionesClick() {
        cargarVista("tabla_intervenciones.fxml");
    }

    @FXML
    private void onAgregarActivoClick() {
        System.out.println("🚀 Botón pulsado, procesando guardado...");

        String nom = txtNombre.getText();
        String ubi = txtUbicacion.getText();
        String est = txtEstado.getText();

        if (nom.isEmpty()) {
            System.out.println("⚠️ El nombre es obligatorio");
            return;
        }

        Activo a = new Activo();
        a.setNombre(nom);
        a.setUbicacion(ubi);
        a.setEstadoOperativo(est);

        if (activoDAO.insertarActivo(a)) {
            System.out.println("✅ Guardado con éxito en MySQL");
            onActivosClick();
        }
    }

    @FXML
    private void onAgregarUsuarioClick() {
        String nombre = txtUserNombre.getText();
        String rol = (txtUserRol.getValue() != null) ? txtUserRol.getValue().toString() : "";
        String pass = txtUserPassword.getText();

        if (nombre.isEmpty() || rol.isEmpty() || pass.isEmpty()) {
            System.out.println("⚠️ Rellena todos los campos de usuario");
            return;
        }

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setRol(rol);
        u.setPassword(pass);

        if (usuarioDAO.insertarUsuario(u)) {
            System.out.println("✅ Usuario guardado");
            txtUserNombre.clear();
            txtUserRol.getSelectionModel().clearSelection();
            txtUserPassword.clear();
            onUsuariosClick();
        }
    }

    @FXML
    private void onAgregarIncidenciaClick() {
        String titulo = txtIncidenciaTitulo.getText();
        String desc = txtIncidenciaDesc.getText();
        String prio = comboPrioridad.getValue();
        String idAct = txtIncidenciaActivo.getText();

        if (titulo.isEmpty() || prio == null || idAct.isEmpty()) {
            System.out.println("⚠️ Faltan datos obligatorios");
            return;
        }

        try {
            Incidencia i = new Incidencia();
            i.setTitulo(titulo);
            i.setDescripcion(desc);
            i.setPrioridad(prio);
            i.setIdActivo(Integer.parseInt(idAct));
            i.setFecha(Date.valueOf(LocalDate.now()));

            if (incidenciaDAO.insertarIncidencia(i)) {
                System.out.println("✅ Incidencia '" + titulo + "' creada con fecha: " + i.getFecha());
                txtIncidenciaTitulo.clear();
                txtIncidenciaDesc.clear();
                txtIncidenciaActivo.clear();
                comboPrioridad.getSelectionModel().clearSelection();
                onIncidenciasClick();
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️ El ID del activo debe ser un número.");
        }
    }

    @FXML
    private void onCerrarSesionClick() {
        try {
            Sesion.nombreUsuario = null;
            Sesion.rolUsuario = null;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();

            Scene scene = new Scene(root);

            // Reaplicamos el CSS global al volver al login
            if (App.CSS_GLOBAL != null) {
                scene.getStylesheets().add(App.CSS_GLOBAL);
            }

            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.show();

            System.out.println("🚪 Sesión cerrada y ventana forzada a pantalla completa.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void aplicarPermisos() {
        String rol = Sesion.rolUsuario;
        System.out.println("🔐 Rol detectado: " + rol);

        if (rol != null && rol.equalsIgnoreCase("técnico")) {
            Node btnUsuarios = rootPane.lookup("#btnMenuUsuarios");

            if (btnUsuarios != null) {
                btnUsuarios.setVisible(false);
                btnUsuarios.setManaged(false);
                System.out.println("🚫 Botón de Usuarios ocultado correctamente.");
            } else {
                System.out.println("⚠️ No se pudo encontrar el botón #btnMenuUsuarios para ocultarlo.");
            }
        }
    }

    @FXML
    public void initialize() {
        aplicarPermisos();
    }

    @FXML private TextField txtNombre;
    @FXML private TextField txtUbicacion;
    @FXML private TextField txtEstado;

    @FXML private TextField txtUserNombre;
    @FXML private ComboBox<String> txtUserRol;
    @FXML private PasswordField txtUserPassword;
    @FXML private Button btnAgregarUsuario;

    @FXML private TextField txtIncidenciaTitulo, txtIncidenciaDesc, txtIncidenciaActivo;
    @FXML private ComboBox<String> comboPrioridad;
    @FXML private Button btnAgregarIncidencia;

    @FXML private TextField txtIntervencionDesc, txtIntervencionIncidencia, txtIntervencionUsuario;
    @FXML private Button btnAgregarIntervencion;

    @FXML private Button btnCerrarSesion;
}
