package com.fixflow.controller;

import com.fixflow.dao.IncidenciaDAO;
import com.fixflow.dao.IntervencionDAO;
import com.fixflow.dao.UsuarioDAO;
import com.fixflow.dao.ActivoDAO; // Asegúrate de importar esto
import com.fixflow.modelos.Incidencia;
import com.fixflow.modelos.Intervencion;
import com.fixflow.modelos.Usuario;
import com.fixflow.modelos.Activo;  // Y esto
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import java.util.List;

public class MainViewController {

    @FXML
    private BorderPane rootPane;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ActivoDAO activoDAO = new ActivoDAO(); // Instanciado
    private IncidenciaDAO incidenciaDAO = new IncidenciaDAO();
    private IntervencionDAO intervencionDAO = new IntervencionDAO();

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

        // 1. Conectamos los campos de texto del formulario (usa los fx:id que pusimos en Scene Builder)
        txtUserNombre = (TextField) vista.lookup("#txtUserNombre");
        txtUserRol = (ComboBox<String>) vista.lookup("#txtUserRol");
        txtUserPassword = (PasswordField) vista.lookup("#txtUserPassword");
        btnAgregarUsuario = (Button) vista.lookup("#btnAgregarUsuario");

        // 2. Conectamos el botón manualmente para evitar errores de FXML
        if (btnAgregarUsuario != null) {
            btnAgregarUsuario.setOnAction(event -> onAgregarUsuarioClick());
            System.out.println("✅ Botón de usuarios vinculado.");
        }

        // Añade esto justo después de los lookups para que aparezcan las palabras:
        if (txtUserRol != null) {
            txtUserRol.getItems().setAll("Administrador", "Técnico");
            txtUserRol.setValue("Técnico");
        }
        // 3. Buscamos y configuramos la tabla
        TableView<Usuario> tabla = (TableView<Usuario>) vista.lookup("#tablaUsuariosView");
        if (tabla != null) {
            // Vinculamos columnas (Asegúrate que en Usuario.java se llamen id, nombre y rol)
            tabla.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
            tabla.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tabla.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("rol"));

            // Cargamos los datos
            tabla.setItems(FXCollections.observableArrayList(usuarioDAO.listarUsuarios()));
            System.out.println("✅ Tabla de usuarios cargada con éxito.");
        } else {
            System.out.println("❌ Error: No se encontró #tablaUsuariosView");
        }
    }

    private void configurarTablaActivos(Node vista) {
        System.out.println("DEBUG: Configurando tabla y conectando botón manualmente...");

        // 1. Lookups de los campos de texto
        txtNombre = (TextField) vista.lookup("#txtNombre");
        txtUbicacion = (TextField) vista.lookup("#txtUbicacion");
        txtEstado = (TextField) vista.lookup("#txtEstado");

        // 2. NUEVO: Conexión manual del botón
        Button btnAgregar = (Button) vista.lookup("#btnAgregarActivo");
        if (btnAgregar != null) {
            btnAgregar.setOnAction(event -> onAgregarActivoClick());
            System.out.println("✅ Botón vinculado correctamente.");
        } else {
            System.out.println("❌ No se encontró el botón con ID #btnAgregarActivo");
        }

        // 3. Configuración de la tabla (lo que ya tenías)
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

        // 1. Vinculamos los nuevos campos del formulario (Lookups)
        txtIncidenciaTitulo = (TextField) vista.lookup("#txtIncidenciaTitulo");
        txtIncidenciaDesc = (TextField) vista.lookup("#txtIncidenciaDesc");
        txtIncidenciaActivo = (TextField) vista.lookup("#txtIncidenciaActivo");
        comboPrioridad = (ComboBox<String>) vista.lookup("#comboPrioridad");
        btnAgregarIncidencia = (Button) vista.lookup("#btnAgregarIncidencia");

        // 2. Rellenamos el ComboBox de Prioridad
        if (comboPrioridad != null) {
            comboPrioridad.getItems().setAll("Baja", "Media", "Alta", "Crítica");
            comboPrioridad.setPromptText("✅ ComboBox de prioridad rellenado");
        } else {
            System.out.println("❌ ERROR: No se encuentra el ComboBox #comboPrioridad");
        }

        // 3. Conectamos el botón de agregar
        if (btnAgregarIncidencia != null) {
            btnAgregarIncidencia.setOnAction(event -> onAgregarIncidenciaClick());
        }

        // 4. Buscamos y configuramos la tabla
        Node componente = vista.lookup("#tablaIncidenciasView");
        if (componente instanceof TableView) {
            @SuppressWarnings("unchecked")
            TableView<Incidencia> tabla = (TableView<Incidencia>) componente;

            // Vinculamos columnas (Asegúrate que coincidan con los nombres en tu clase Incidencia)
            tabla.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idIncidencia"));
            tabla.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("titulo"));
            tabla.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            tabla.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("prioridad"));

            // Columna 4 es Fecha. Si en tu clase Java se llama "fecha", descomenta la línea de abajo:
            // tabla.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("fecha"));

            tabla.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("idActivo"));

            // Carga de datos
            List<Incidencia> lista = incidenciaDAO.listarIncidencias();
            System.out.println("📊 DEBUG: El DAO ha devuelto " + (lista != null ? lista.size() : "NULL") + " incidencias.");

            if (lista != null) {
                tabla.setItems(FXCollections.observableArrayList(lista));
            }
        } else {
            System.out.println("❌ ERROR: No se encontró #tablaIncidenciasView");
        }
    }

    private void configurarTablaIntervenciones(Node vista) {
        // 1. Enlazamos los componentes del FXML (IDs de Scene Builder)
        txtIntervencionDesc = (TextField) vista.lookup("#txtIntervencionDesc");
        txtIntervencionIncidencia = (TextField) vista.lookup("#txtIntervencionIncidencia");
        txtIntervencionUsuario = (TextField) vista.lookup("#txtIntervencionUsuario");
        btnAgregarIntervencion = (Button) vista.lookup("#btnAgregarIntervencion");

        // 2. Asignamos la acción al botón por código (sin usar On Action en Scene Builder)
        if (btnAgregarIntervencion != null) {
            btnAgregarIntervencion.setOnAction(event -> onAgregarIntervencionClick());
        }

        // 3. Configuramos la TableView
        TableView<Intervencion> tabla = (TableView<Intervencion>) vista.lookup("#tablaIntervencionesView");
        if (tabla != null) {
            // Vinculamos cada columna con las variables de tu clase Intervencion.java
            // IMPORTANTE: Los nombres entre comillas deben ser EXACTOS a tus variables en el modelo
            tabla.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idIntervencion"));
            tabla.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("observaciones"));
            tabla.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("fecha"));
            tabla.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("idIncidencia"));
            tabla.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("idUsuario"));

            // Cargamos los datos usando el método de tu DAO
            tabla.setItems(FXCollections.observableArrayList(intervencionDAO.obtenerIntervenciones()));
            System.out.println("📊 Datos de intervenciones cargados.");
        }

    }



    private void onAgregarIntervencionClick() {
        try {
            // Creamos el objeto con los datos de los cuadros de texto
            Intervencion nuevaInter = new Intervencion();
            nuevaInter.setObservaciones(txtIntervencionDesc.getText());
            nuevaInter.setIdIncidencia(Integer.parseInt(txtIntervencionIncidencia.getText()));
            nuevaInter.setIdUsuario(Integer.parseInt(txtIntervencionUsuario.getText()));

            // Validamos que la descripción no esté vacía
            if (nuevaInter.getObservaciones().isEmpty()) {
                System.out.println("⚠️ Escribe una descripción técnica.");
                return;
            }

            // Llamamos al DAO para guardar en la base de datos
            if (intervencionDAO.registraIntervencion(nuevaInter)) {
                System.out.println("✅ Intervención guardada con éxito.");

                // Refrescamos la pestaña actual para que aparezca la nueva fila
                onIntervencionesClick();
            }

        } catch (NumberFormatException e) {
            System.out.println("⚠️ ERROR: El ID de incidencia y de usuario deben ser números enteros.");
        } catch (Exception e) {
            System.out.println("❌ Error inesperado: " + e.getMessage());
        }
    }



    @FXML private void onUsuariosClick() { cargarVista("tabla_usuarios.fxml"); }
    @FXML private void onActivosClick() { cargarVista("tabla_activos.fxml"); }
    @FXML
    private void onIncidenciasClick() {
        System.out.println("DEBUG: Pulsado botón Incidencias"); // Para confirmar que funciona
        cargarVista("tabla_incidencias.fxml");
    }
    @FXML
    private void onIntervencionesClick() {
        cargarVista("tabla_intervenciones.fxml");
    }

    @FXML
    private void onAgregarActivoClick() {
        System.out.println("🚀 Botón pulsado, procesando guardado...");

        // Recoger datos
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
            // Refrescar la vista actual para ver el cambio
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
            onUsuariosClick(); // Refresca la tabla
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

        Incidencia i = new Incidencia();
        i.setTitulo(titulo);
        i.setDescripcion(desc);
        i.setPrioridad(prio);
        i.setIdActivo(Integer.parseInt(idAct));
        // La fecha la puedes gestionar en el DAO con un "CURRENT_DATE" en SQL
        // o poner i.setFecha(new Date()) si usas java.util.Date

        if (incidenciaDAO.insertarIncidencia(i)) {
            System.out.println("✅ Incidencia '" + titulo + "' creada.");
            onIncidenciasClick(); // Refrescar la tabla
        }
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

    // Variables para Intervenciones
    @FXML private TextField txtIntervencionDesc, txtIntervencionIncidencia, txtIntervencionUsuario;
    @FXML private Button btnAgregarIntervencion;
}
