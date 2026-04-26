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

    public List<Intervencion> obtenerIntervenciones(){
        List<Intervencion> lista = new ArrayList<>();
        String sql = "SELECT * FROM intervenciones";

        try (Connection connection = Conexion.obtenerConexion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Intervencion intervencion = new Intervencion();
                intervencion.setIdIntervencion(resultSet.getInt("id_intervencion"));
                intervencion.setIdIncidencia(resultSet.getInt("id_incidencia"));
                intervencion.setIdUsuario(resultSet.getInt("id_usuario"));
                intervencion.setObservaciones(resultSet.getString("descripcion_tecnica"));

                lista.add(intervencion);
            }
        } catch (SQLException e){
            System.out.println("❌ Error al listar intervenciones: " + e.getMessage());
        }
        return lista;
    }
}
