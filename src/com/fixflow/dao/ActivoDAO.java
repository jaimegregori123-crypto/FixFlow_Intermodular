package com.fixflow.dao;

import com.fixflow.database.Conexion;
import com.fixflow.modelos.Activo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

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
        String sql = "SELECT * FROM activos";

        try (Connection connection = Conexion.obtenerConexion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Activo activo = new Activo();
                activo.setIdActivo(rs.getInt("id_activo"));
                activo.setNombre(rs.getString("nombre"));
                // He quitado la línea de setTipo para que no de error
                activo.setUbicacion(rs.getString("ubicacion"));
                activo.setEstadoOperativo(rs.getString("estado_operativo"));

                lista.add(activo);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar activos: " + e.getMessage());
        }
        return lista;
    }
}
