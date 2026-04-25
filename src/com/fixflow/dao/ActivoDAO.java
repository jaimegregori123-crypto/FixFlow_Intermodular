package com.fixflow.dao;

import com.fixflow.database.Conexion;
import com.fixflow.modelos.Activo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
