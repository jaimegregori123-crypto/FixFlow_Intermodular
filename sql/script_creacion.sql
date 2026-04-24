-- 1. Tabla de ACTIVOS (Equipos)
CREATE TABLE activos (
    id_activo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(100),
    estado_operativo BOOLEAN DEFAULT TRUE
);

-- 2. Tabla de USUARIOS (Personal)
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL
);

-- 3. Tabla de INCIDENCIAS (Tickets de avería)
CREATE TABLE incidencias (
    id_incidencia INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    prioridad VARCHAR(20),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_activo INT,
    FOREIGN KEY (id_activo) REFERENCES activos(id_activo) ON DELETE CASCADE
);

-- 4. Tabla de INTERVENCIONES (Reparaciones realizadas)
CREATE TABLE intervenciones (
    id_intervencion INT AUTO_INCREMENT PRIMARY KEY,
    descripcion_tecnica TEXT,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    id_incidencia INT,
    id_usuario INT,
    FOREIGN KEY (id_incidencia) REFERENCES incidencias(id_incidencia) ON DELETE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);