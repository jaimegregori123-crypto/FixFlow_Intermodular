package com.fixflow.dao;

import com.fixflow.database.Conexion;
import com.fixflow.modelos.Incidencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IncidenciaDAO {

    public boolean reportarIncidencia(Incidencia inc) {
        // SQL ajustado a las columnas reales de tu phpMyAdmin
        String sql = "INSERT INTO incidencias (titulo, descripcion, prioridad, id_activo) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inc.getTitulo());
            pstmt.setString(2, inc.getDescripcion());
            pstmt.setString(3, inc.getPrioridad());
            pstmt.setInt(4, inc.getIdActivo());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al reportar incidencia: " + e.getMessage());
            return false;
        }
    }
}
