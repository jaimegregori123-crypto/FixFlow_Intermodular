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

            if (App.CSS_GLOBAL != null) {
                vista.getStylesheets().add(App.CSS_GLOBAL);
            }

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

    private void cargarDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            Parent vista = loader.load();

            if (App.CSS_GLOBAL != null) {
                vista.getStylesheets().add(App.CSS_GLOBAL);
            }

            if (vista instanceof VBox) {
                VBox vbox = (VBox) vista;
                vbox.setMaxWidth(Double.MAX_VALUE);
                vbox.setMaxHeight(Double.MAX_VALUE);
            }

            rootPane.setCenter(vista);

            Label lblBienvenida = (Label) vista.lookup("#lblBienvenida");
            Label lblFecha = (Label) vista.lookup("#lblFecha");
            Label lblHora = (Label) vista.lookup("#lblHora");
            Label lblRol = (Label) vista.lookup("#lblRol");
            Label lblActivos = (Label) vista.lookup("#lblTotalActivos");
            Label lblIncidencias = (Label) vista.lookup("#lblTotalIncidencias");
            Label lblIntervenciones = (Label) vista.lookup("#lblTotalIntervenciones");

            if (lblBienvenida != null)
                lblBienvenida.setText("Bienvenido, " + Sesion.nombreUsuario + " 👋");

            if (lblFecha != null) {
                String fecha = java.time.LocalDate.now()
                        .format(java.time.format.DateTimeFormatter
                                .ofPattern("EEEE, d 'de' MMMM 'de' yyyy",
                                        new java.util.Locale("es", "ES")));
                lblFecha.setText(fecha.substring(0, 1).toUpperCase() + fecha.substring(1));
            }

            if (lblHora != null) {
                javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                        new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), e -> {
                            lblHora.setText(java.time.LocalTime.now()
                                    .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
                        })
                );
                timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
                timeline.play();
            }

            if (lblRol != null) {
                lblRol.setText(Sesion.rolUsuario);
                if (Sesion.rolUsuario != null && Sesion.rolUsuario.equalsIgnoreCase("Administrador")) {
                    lblRol.setStyle("-fx-background-color: #0f3460; -fx-text-fill: #e94560; " +
                            "-fx-background-radius: 20; -fx-padding: 4 12; " +
                            "-fx-font-size: 11px; -fx-font-weight: bold;");
                } else {
                    lblRol.setStyle("-fx-background-color: #0f3460; -fx-text-fill: #a8b2d8; " +
                            "-fx-background-radius: 20; -fx-padding: 4 12; " +
                            "-fx-font-size: 11px; -fx-font-weight: bold;");
                }
            }

            if (lblActivos != null)
                lblActivos.setText(String.valueOf(activoDAO.listarActivos().size()));

            if (lblIncidencias != null)
                lblIncidencias.setText(String.valueOf(incidenciaDAO.listarIncidencias().size()));

            if (lblIntervenciones != null)
                lblIntervenciones.setText(String.valueOf(intervencionDAO.obtenerIntervenciones().size()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarTablaUsuarios(Node vista) {
        txtUserNombre = (TextField) vista.lookup("#txtUserNombre");
        txtUserRol = (ComboBox<String>) vista.lookup("#txtUserRol");
        txtUserPassword = (PasswordField) vista.lookup("#txtUserPassword");
        btnAgregarUsuario = (Button) vista.lookup("#btnAgregarUsuario");

        if (btnAgregarUsuario != null) {
            btnAgregarUsuario.setOnAction(event -> onAgregarUsuarioClick());
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

            Button btnEliminar = (Button) vista.lookup("#btnEliminarUsuario");
            if (btnEliminar != null) {
                btnEliminar.setOnAction(event -> {
                    Usuario seleccionado = tabla.getSelectionModel().getSelectedItem();
                    if (seleccionado == null) {
                        mostrarAlerta("Aviso", "Selecciona un usuario primero.");
                        return;
                    }
                    if (seleccionado.getId() == Sesion.idUsuario) {
                        mostrarAlerta("Error", "No puedes eliminar tu propio usuario.");
                        return;
                    }
                    if (usuarioDAO.eliminarUsuario(seleccionado.getId())) {
                        onUsuariosClick();
                    }
                });
            }
        }
    }

    private void configurarTablaActivos(Node vista) {
        txtNombre = (TextField) vista.lookup("#txtNombre");
        txtUbicacion = (TextField) vista.lookup("#txtUbicacion");
        txtEstado = (TextField) vista.lookup("#txtEstado");

        Button btnAgregar = (Button) vista.lookup("#btnAgregarActivo");
        if (btnAgregar != null) {
            btnAgregar.setOnAction(event -> onAgregarActivoClick());
        }

        Node componente = vista.lookup("#tablaActivosView");
        if (componente instanceof TableView) {
            @SuppressWarnings("unchecked")
            TableView<Activo> tabla = (TableView<Activo>) componente;
            tabla.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idActivo"));
            tabla.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tabla.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
            tabla.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("estadoOperativo"));
            tabla.setItems(FXCollections.observableArrayList(activoDAO.listarActivos()));

            Button btnEliminar = (Button) vista.lookup("#btnEliminarActivo");
            if (btnEliminar != null) {
                btnEliminar.setOnAction(event -> {
                    Activo seleccionado = tabla.getSelectionModel().getSelectedItem();
                    if (seleccionado == null) {
                        mostrarAlerta("Aviso", "Selecciona un activo primero.");
                        return;
                    }
                    if (activoDAO.eliminarActivo(seleccionado.getIdActivo())) {
                        onActivosClick();
                    }
                });
            }
        }
    }

    private void configurarTablaIncidencias(Node vista) {
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
            tabla.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("estado"));

            List<Incidencia> lista = incidenciaDAO.listarIncidencias();
            if (lista != null) {
                tabla.setItems(FXCollections.observableArrayList(lista));
            }

            Button btnResolver = (Button) vista.lookup("#btnResolver");
            if (btnResolver != null) {
                btnResolver.setOnAction(event -> {
                    Incidencia seleccionada = tabla.getSelectionModel().getSelectedItem();

                    if (seleccionada == null) {
                        mostrarAlerta("Aviso", "Selecciona una incidencia primero.");
                        return;
                    }

                    if ("Resuelta".equals(seleccionada.getEstado())) {
                        mostrarAlerta("Aviso", "Esta incidencia ya está resuelta.");
                        return;
                    }

                    Intervencion intervencion = new Intervencion();
                    intervencion.setIdIncidencia(seleccionada.getIdIncidencia());
                    intervencion.setIdUsuario(Sesion.idUsuario);
                    intervencion.setObservaciones("Incidencia resuelta: " + seleccionada.getTitulo());
                    intervencion.setFecha(new java.sql.Timestamp(System.currentTimeMillis()));

                    intervencionDAO.registraIntervencion(intervencion);
                    incidenciaDAO.resolverIncidencia(seleccionada.getIdIncidencia());
                    onIncidenciasClick();
                });
            }

            Button btnEliminarIncidencia = (Button) vista.lookup("#btnEliminarIncidencia");
            if (btnEliminarIncidencia != null) {
                btnEliminarIncidencia.setOnAction(event -> {
                    Incidencia seleccionada = tabla.getSelectionModel().getSelectedItem();
                    if (seleccionada == null) {
                        mostrarAlerta("Aviso", "Selecciona una incidencia primero.");
                        return;
                    }
                    if (incidenciaDAO.eliminarIncidencia(seleccionada.getIdIncidencia())) {
                        onIncidenciasClick();
                    }
                });
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
            tabla.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("tituloIncidencia"));
            tabla.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
            tabla.setItems(FXCollections.observableArrayList(intervencionDAO.obtenerIntervenciones()));
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void onAgregarIntervencionClick() {
        try {
            Intervencion nuevaInter = new Intervencion();
            nuevaInter.setObservaciones(txtIntervencionDesc.getText());
            nuevaInter.setIdIncidencia(Integer.parseInt(txtIntervencionIncidencia.getText()));
            nuevaInter.setIdUsuario(Integer.parseInt(txtIntervencionUsuario.getText()));

            if (nuevaInter.getObservaciones().isEmpty()) {
                mostrarAlerta("Aviso", "Escribe una descripción técnica.");
                return;
            }

            if (intervencionDAO.registraIntervencion(nuevaInter)) {
                onIntervencionesClick();
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de incidencia y de usuario deben ser números enteros.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error inesperado: " + e.getMessage());
        }
    }

    @FXML private TableView<Incidencia> tablaIncidenciasView;

    @FXML private void onUsuariosClick()       { cargarVista("tabla_usuarios.fxml"); }
    @FXML private void onActivosClick()        { cargarVista("tabla_activos.fxml"); }
    @FXML private void onIncidenciasClick()    { cargarVista("tabla_incidencias.fxml"); }
    @FXML private void onIntervencionesClick() { cargarVista("tabla_intervenciones.fxml"); }

    @FXML
    private void onAgregarActivoClick() {
        String nom = txtNombre.getText();
        String ubi = txtUbicacion.getText();
        String est = txtEstado.getText();

        if (nom.isEmpty()) {
            mostrarAlerta("Aviso", "El nombre del activo es obligatorio.");
            return;
        }

        Activo a = new Activo();
        a.setNombre(nom);
        a.setUbicacion(ubi);
        a.setEstadoOperativo(est);

        if (activoDAO.insertarActivo(a)) {
            onActivosClick();
        }
    }

    @FXML
    private void onAgregarUsuarioClick() {
        String nombre = txtUserNombre.getText();
        String rol = (txtUserRol.getValue() != null) ? txtUserRol.getValue().toString() : "";
        String pass = txtUserPassword.getText();

        if (nombre.isEmpty() || rol.isEmpty() || pass.isEmpty()) {
            mostrarAlerta("Aviso", "Rellena todos los campos de usuario.");
            return;
        }

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setRol(rol);
        u.setPassword(pass);

        if (usuarioDAO.insertarUsuario(u)) {
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
            mostrarAlerta("Aviso", "Faltan datos obligatorios.");
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
                txtIncidenciaTitulo.clear();
                txtIncidenciaDesc.clear();
                txtIncidenciaActivo.clear();
                comboPrioridad.getSelectionModel().clearSelection();
                onIncidenciasClick();
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID del activo debe ser un número.");
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

            if (App.CSS_GLOBAL != null) {
                scene.getStylesheets().add(App.CSS_GLOBAL);
            }

            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void aplicarPermisos() {
        String rol = Sesion.rolUsuario;

        if (rol != null && rol.equalsIgnoreCase("técnico")) {
            Node btnUsuarios = rootPane.lookup("#btnMenuUsuarios");
            if (btnUsuarios != null) {
                btnUsuarios.setVisible(false);
                btnUsuarios.setManaged(false);
            }
        }
    }

    @FXML
    public void initialize() {
        aplicarPermisos();
        cargarDashboard();
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