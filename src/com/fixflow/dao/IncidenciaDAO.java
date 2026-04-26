package com.fixflow.dao;

import com.fixflow.database.Conexion;
import com.fixflow.modelos.Incidencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Incidencia> listarIncidencias() {
        List<Incidencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM incidencias";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Incidencia inc = new Incidencia();
                inc.setIdIncidencia(rs.getInt("id_incidencia"));
                inc.setTitulo(rs.getString("titulo"));
                inc.setDescripcion(rs.getString("descripcion"));
                inc.setPrioridad(rs.getString("prioridad"));
                inc.setIdActivo(rs.getInt("id_activo"));

                lista.add(inc);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al listar incidencias: " + e.getMessage());
        }
        return lista;
    }
}
