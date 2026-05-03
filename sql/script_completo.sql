-- =============================================
-- FIXFLOW - Script completo de base de datos
-- Autor: Jaime Gregori Almiñana
-- =============================================

CREATE DATABASE IF NOT EXISTS fixflow_db;
USE fixflow_db;

-- =============================================
-- CREACIÓN DE TABLAS
-- =============================================

-- 1. Tabla de ACTIVOS
CREATE TABLE activos (
    id_activo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(100),
    estado_operativo VARCHAR(50) DEFAULT 'Operativo'
);

-- 2. Tabla de USUARIOS
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL
);

-- 3. Tabla de INCIDENCIAS
CREATE TABLE incidencias (
    id_incidencia INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    prioridad VARCHAR(20),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_activo INT,
    estado VARCHAR(20) DEFAULT 'Abierta',
    FOREIGN KEY (id_activo) REFERENCES activos(id_activo) ON DELETE CASCADE
);

-- 4. Tabla de INTERVENCIONES
CREATE TABLE intervenciones (
    id_intervencion INT AUTO_INCREMENT PRIMARY KEY,
    descripcion_tecnica TEXT,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    id_incidencia INT,
    id_usuario INT,
    FOREIGN KEY (id_incidencia) REFERENCES incidencias(id_incidencia) ON DELETE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- =============================================
-- INSERCIÓN DE DATOS DE EJEMPLO
-- =============================================

INSERT INTO usuarios (username, password, rol) VALUES
('admin_jaime', '12345', 'Administrador'),
('Juan', '123', 'Administrador'),
('Tecnico1', '123', 'Técnico');

INSERT INTO activos (nombre, ubicacion, estado_operativo) VALUES
('Caldera Central', 'Sótano -1', 'Operativo'),
('Ascensor Planta 2', 'Edificio A - Planta 2', 'Operativo'),
('Grupo Electrógeno', 'Sala Técnica Planta Baja', 'En Mantenimiento'),
('Sistema de Climatización', 'Cubierta', 'Operativo'),
('Cuadro Eléctrico Principal', 'Sótano -1', 'Operativo');

INSERT INTO incidencias (titulo, descripcion, prioridad, fecha_creacion, id_activo, estado) VALUES
('Ruido extraño en caldera', 'La caldera emite un pitido continuo al arrancar', 'Alta', NOW(), 1, 'Abierta'),
('Ascensor parado en planta 2', 'El ascensor no responde al botón de llamada', 'Crítica', NOW(), 2, 'Abierta'),
('Fallo en climatización', 'El sistema no enfría correctamente en verano', 'Media', NOW(), 4, 'Abierta'),
('Revisión periódica grupo electrógeno', 'Revisión trimestral programada', 'Baja', NOW(), 3, 'Resuelta');

INSERT INTO intervenciones (descripcion_tecnica, fecha, id_incidencia, id_usuario) VALUES
('Incidencia resuelta: Revisión periódica grupo electrógeno', NOW(), 4, 1);

-- =============================================
-- CONSULTAS ÚTILES DEL PROYECTO
-- =============================================

-- Listar todas las incidencias abiertas con el nombre del activo
SELECT i.id_incidencia, i.titulo, i.prioridad, i.fecha_creacion, a.nombre AS activo
FROM incidencias i
JOIN activos a ON i.id_activo = a.id_activo
WHERE i.estado = 'Abierta'
ORDER BY i.prioridad DESC;

-- Listar intervenciones con nombre del técnico y título de la incidencia
SELECT iv.id_intervencion, iv.descripcion_tecnica, iv.fecha,
       inc.titulo AS incidencia, u.username AS tecnico
FROM intervenciones iv
JOIN incidencias inc ON iv.id_incidencia = inc.id_incidencia
JOIN usuarios u ON iv.id_usuario = u.id_usuario;

-- Contar incidencias por estado
SELECT estado, COUNT(*) AS total
FROM incidencias
GROUP BY estado;

-- Contar incidencias por prioridad
SELECT prioridad, COUNT(*) AS total
FROM incidencias
GROUP BY prioridad
ORDER BY FIELD(prioridad, 'Crítica', 'Alta', 'Media', 'Baja');

-- Activos con más incidencias registradas
SELECT a.nombre, COUNT(i.id_incidencia) AS total_incidencias
FROM activos a
LEFT JOIN incidencias i ON a.id_activo = i.id_activo
GROUP BY a.nombre
ORDER BY total_incidencias DESC;

-- Técnicos con más intervenciones realizadas
SELECT u.username, COUNT(iv.id_intervencion) AS total_intervenciones
FROM usuarios u
LEFT JOIN intervenciones iv ON u.id_usuario = iv.id_usuario
GROUP BY u.username
ORDER BY total_intervenciones DESC;