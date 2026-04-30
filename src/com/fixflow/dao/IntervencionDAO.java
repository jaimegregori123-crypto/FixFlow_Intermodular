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
        // Esta es la línea que hace que la incidencia "desaparezca" al marcarla como Resuelta
        String sqlUpdate = "UPDATE incidencias SET prioridad = 'Resuelta' WHERE id_incidencia = ?";

        try (Connection connection = Conexion.obtenerConexion()) {
            // Iniciamos la transacción
            connection.setAutoCommit(false);

            try (PreparedStatement psInsert = connection.prepareStatement(sqlInsert);
                 PreparedStatement psUpdate = connection.prepareStatement(sqlUpdate)) {

                psInsert.setInt(1, intervencion.getIdIncidencia());
                psInsert.setInt(2, intervencion.getIdUsuario());
                psInsert.setString(3, intervencion.getObservaciones());

                // Mantenemos tu Timestamp como lo tenías originalmente
                psInsert.setTimestamp(4, intervencion.getFecha());
                psInsert.executeUpdate();

                // Marcamos la incidencia como resuelta
                psUpdate.setInt(1, intervencion.getIdIncidencia());
                psUpdate.executeUpdate();

                // Confirmamos ambos cambios
                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback(); // Si algo falla, deshacemos todo
                System.out.println("❌ Error en la transacción: " + e.getMessage());
            }
        } catch (SQLException e){
            System.out.println("❌ Error al registrar intervención: " + e.getMessage());
        }
        return false;
    }

    public List<Intervencion> obtenerIntervenciones(){
        List<Intervencion> lista = new ArrayList<>();
        String sql = "SELECT id_intervencion, id_incidencia, id_usuario, descripcion_tecnica, fecha FROM intervenciones";

        try (Connection connection = Conexion.obtenerConexion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Intervencion intervencion = new Intervencion();
                intervencion.setIdIntervencion(resultSet.getInt("id_intervencion"));
                intervencion.setIdIncidencia(resultSet.getInt("id_incidencia"));
                intervencion.setIdUsuario(resultSet.getInt("id_usuario"));
                intervencion.setObservaciones(resultSet.getString("descripcion_tecnica"));

                // Volvemos a usar getTimestamp como tenías tú
                intervencion.setFecha(resultSet.getTimestamp("fecha"));

                lista.add(intervencion);
            }
        } catch (SQLException e){
            System.out.println("❌ Error al listar intervenciones: " + e.getMessage());
        }
        return lista;
    }
}
