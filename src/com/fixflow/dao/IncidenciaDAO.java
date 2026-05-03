package com.fixflow.dao;

import com.fixflow.database.Conexion;
import com.fixflow.modelos.Incidencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncidenciaDAO {

    public List<Incidencia> listarIncidencias() {
        List<Incidencia> lista = new ArrayList<>();
        String sql = "SELECT id_incidencia, titulo, descripcion, prioridad, fecha_creacion, id_activo, estado FROM incidencias";

        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Incidencia i = new Incidencia();
                i.setIdIncidencia(rs.getInt("id_incidencia"));
                i.setTitulo(rs.getString("titulo"));
                i.setDescripcion(rs.getString("descripcion"));
                i.setPrioridad(rs.getString("prioridad"));
                i.setIdActivo(rs.getInt("id_activo"));
                i.setFecha(rs.getDate("fecha_creacion"));
                i.setEstado(rs.getString("estado"));
                lista.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean insertarIncidencia(Incidencia i) {
        String sql = "INSERT INTO incidencias (titulo, descripcion, prioridad, fecha_creacion, id_activo, estado) VALUES (?, ?, ?, ?, ?, 'Abierta')";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, i.getTitulo());
            pstmt.setString(2, i.getDescripcion());
            pstmt.setString(3, i.getPrioridad());
            pstmt.setDate(4, i.getFecha());
            pstmt.setInt(5, i.getIdActivo());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar incidencia: " + e.getMessage());
            return false;
        }
    }

    public boolean resolverIncidencia(int idIncidencia) {
        String sql = "UPDATE incidencias SET estado = 'Resuelta' WHERE id_incidencia = ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idIncidencia);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al resolver incidencia: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarIncidencia(int idIncidencia) {
        String sql = "DELETE FROM incidencias WHERE id_incidencia = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idIncidencia);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar incidencia: " + e.getMessage());
            return false;
        }
    }
}
