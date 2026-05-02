package com.fixflow.dao;

import com.fixflow.database.Conexion;
import com.fixflow.modelos.Intervencion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IntervencionDAO {

    public boolean registraIntervencion(Intervencion intervencion) {
        String sqlInsert = "INSERT INTO intervenciones (id_incidencia, id_usuario, descripcion_tecnica, fecha) VALUES (?, ?, ?, ?)";

        try (Connection connection = Conexion.obtenerConexion()) {
            connection.setAutoCommit(false);

            try (PreparedStatement psInsert = connection.prepareStatement(sqlInsert)) {

                psInsert.setInt(1, intervencion.getIdIncidencia());
                psInsert.setInt(2, intervencion.getIdUsuario());
                psInsert.setString(3, intervencion.getObservaciones());
                psInsert.setTimestamp(4, intervencion.getFecha());
                psInsert.executeUpdate();

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                System.out.println("❌ Error en la transacción: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al registrar intervención: " + e.getMessage());
        }
        return false;
    }

    public List<Intervencion> obtenerIntervenciones() {
        List<Intervencion> lista = new ArrayList<>();

        // JOIN para obtener nombre de usuario y título de incidencia
        String sql = "SELECT i.id_intervencion, i.id_incidencia, inc.titulo AS titulo_incidencia, " +
                "i.id_usuario, u.username AS nombre_usuario, " +
                "i.descripcion_tecnica, i.fecha " +
                "FROM intervenciones i " +
                "LEFT JOIN usuarios u ON i.id_usuario = u.id_usuario " +
                "LEFT JOIN incidencias inc ON i.id_incidencia = inc.id_incidencia";

        try (Connection connection = Conexion.obtenerConexion();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Intervencion intervencion = new Intervencion();
                intervencion.setIdIntervencion(rs.getInt("id_intervencion"));
                intervencion.setIdIncidencia(rs.getInt("id_incidencia"));
                intervencion.setIdUsuario(rs.getInt("id_usuario"));
                intervencion.setObservaciones(rs.getString("descripcion_tecnica"));
                intervencion.setFecha(rs.getTimestamp("fecha"));

                // Campos extra para mostrar en tabla
                intervencion.setNombreUsuario(rs.getString("nombre_usuario"));
                intervencion.setTituloIncidencia(rs.getString("titulo_incidencia"));

                lista.add(intervencion);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al listar intervenciones: " + e.getMessage());
        }
        return lista;
    }
}
