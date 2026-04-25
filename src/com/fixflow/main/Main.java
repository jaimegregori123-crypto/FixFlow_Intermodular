package com.fixflow.main;

import com.fixflow.dao.UsuarioDAO;
import com.fixflow.modelos.Usuario;

public class Main {
    public static void main(String[] args) {
        // 1. Creamos el objeto usuario en Java
        Usuario nuevoUsuario = new Usuario(0, "admin_jaime", "12345", "administrador");

        // 2. Usamos el DAO para mandarlo a MySQL
        UsuarioDAO dao = new UsuarioDAO();

        System.out.println("Intentando insertar usuario...");
        if (dao.insertarUsuario(nuevoUsuario)) {
            System.out.println("✅ ¡Usuario guardado en la base de datos con éxito!");
        } else {
            System.out.println("❌ No se pudo guardar el usuario.");
        }
    }
}
