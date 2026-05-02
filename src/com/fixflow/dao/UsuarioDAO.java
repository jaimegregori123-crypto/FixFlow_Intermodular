package com.fixflow.dao;

import com.fixflow.database.Conexion;
import com.fixflow.modelos.Usuario;
import com.fixflow.modelos.Sesion; // Importamos Sesion para guardar los datos

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    /**
     * Método para validar el acceso al sistema
     */
    public boolean validarLogin(String user, String pass) {
        String sql = "SELECT id_usuario, username, rol FROM usuarios WHERE username = ? AND password = ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Si hay resultado, guardamos los datos en la Sesión global
                Sesion.idUsuario = rs.getInt("id_usuario");
                Sesion.nombreUsuario = rs.getString("username");
                Sesion.rolUsuario = rs.getString("rol");
                System.out.println("✅ Login correcto: " + Sesion.nombreUsuario + " (" + Sesion.rolUsuario + ")");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("❌ Error en el proceso de Login: " + e.getMessage());
        }
        return false; // Si llega aquí, es que los datos están mal o hubo error
    }

    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password, rol) VALUES (?, ?, ?)";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getPassword());
            pstmt.setString(3, usuario.getRol());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, username, password, rol FROM usuarios";

        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRol(rs.getString("rol"));

                lista.add(u);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
}
