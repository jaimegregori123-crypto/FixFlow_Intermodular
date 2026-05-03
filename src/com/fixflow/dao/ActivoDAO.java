package com.fixflow.dao;

import com.fixflow.database.Conexion;
import com.fixflow.modelos.Activo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivoDAO {

    public boolean insertarActivo(Activo activo) {
        // SQL ajustado a: nombre, ubicacion, estado_operativo
        String sql = "INSERT INTO activos (nombre, ubicacion, estado_operativo) VALUES (?, ?, ?)";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, activo.getNombre());
            pstmt.setString(2, activo.getUbicacion());
            pstmt.setString(3, activo.getEstadoOperativo());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar activo: " + e.getMessage());
            return false;
        }
    }

    String sql = "SELECT * FROM activos";

    public List<Activo> listarActivos() {
        List<Activo> lista = new ArrayList<>();
        String sql = "SELECT id_activo, nombre, ubicacion, estado_operativo FROM activos";

        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Activo a = new Activo();
                // Mapeamos del SQL a tus setters de Java
                a.setIdActivo(rs.getInt("id_activo"));
                a.setNombre(rs.getString("nombre"));
                a.setUbicacion(rs.getString("ubicacion"));
                // Asegúrate de tener un setEstadoOperativo en tu clase Activo
                a.setEstadoOperativo(rs.getString("estado_operativo"));

                lista.add(a);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al listar activos: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminarActivo(int idActivo) {
        String sql = "DELETE FROM activos WHERE id_activo = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idActivo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar activo: " + e.getMessage());
            return false;
        }
    }

}
