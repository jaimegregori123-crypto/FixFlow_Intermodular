package com.fixflow.main;

import com.fixflow.dao.IntervencionDAO;
import com.fixflow.modelos.Intervencion;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Instanciamos el DAO y el Modelo
        IntervencionDAO dao = new IntervencionDAO();
        Intervencion repair = new Intervencion();

        // 2. Seteamos los datos (Asegúrate de que coincidan con tus métodos)
        repair.setIdIncidencia(1);
        repair.setIdUsuario(1);

        // ¡OJO AQUÍ! He puesto los nombres estándar.
        // Si te sale rojo, borra hasta el punto y deja que IntelliJ te sugiera el nombre.
        repair.setObservaciones("Limpieza de filtros y ajuste de presión. Todo OK.");
        repair.setTiempo(60);

        System.out.println("Enviando intervención a la base de datos...");

        // 3. Llamamos al método del DAO
        // Si tu método en el DAO se llama 'registraIntervencion', quítale la 'r' final aquí.
        if (dao.registraIntervencion(repair)) {
            System.out.println("✅ ¡CONSEGUIDO! Intervención guardada.");
        } else {
            System.out.println("❌ Algo ha fallado. Revisa la consola.");
        }
    }
}
