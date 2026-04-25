package com.fixflow.main;

import com.fixflow.dao.ActivoDAO;
import com.fixflow.dao.UsuarioDAO;
import com.fixflow.modelos.Activo;
import com.fixflow.modelos.Usuario;

public class Main {
    public static void main(String[] args) {
        ActivoDAO activoDao = new ActivoDAO();

        // Creamos una caldera con estado "Operativo"
        // Asegúrate de que el constructor de Activo.java acepte: (id, nombre, ubicacion, estado)
        Activo caldera = new Activo(0, "Caldera Central", "Sótano -1", "Operativo");

        if (activoDao.insertarActivo(caldera)) {
            System.out.println("✅ ¡Activo insertado correctamente!");
        } else {
            System.out.println("❌ Fallo en la inserción del activo.");
        }
    }
}
