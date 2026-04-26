package com.fixflow.dao;

import com.fixflow.database.Conexion;
import com.fixflow.modelos.Intervencion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IntervencionDAO {


    public boolean registraIntervencion(Intervencion intervencion) {
        String sql = "INSERT INTO intervenciones (id_incidencia, id_usuario, descripcion_tecnica ) VALUES (?, ?, ?)";

        try (Connection connection = Conexion.obtenerConexion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, intervencion.getIdIncidencia());
            preparedStatement.setInt(2, intervencion.getIdUsuario());
            preparedStatement.setString(3, intervencion.getObservaciones());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e){
            System.out.println("❌ Error al registrar intervención: " + e.getMessage());        }
        return false;

    }
}
