-- Creo la base de datos
CREATE DATABASE IF NOT EXISTS online_store;
USE online_store;

-- Creo la tabla Artículos
CREATE TABLE articulos (
                           id_articulo INT AUTO_INCREMENT PRIMARY KEY,
                           codigo VARCHAR(50) NOT NULL UNIQUE, -- Identificador funcional
                           descripcion VARCHAR(255),
                           precio_venta DECIMAL(10, 2) NOT NULL,
                           gastos_envio DECIMAL(10, 2) NOT NULL,
                           tiempo_preparacion_min INT NOT NULL
) ENGINE=InnoDB;

-- 2. Creo la tabla Clientes
-- Combino Cliente, ClienteEstandar y ClientePremium
CREATE TABLE clientes (
                          id_cliente INT AUTO_INCREMENT PRIMARY KEY,
                          nif VARCHAR(20) NOT NULL UNIQUE,
                          nombre VARCHAR(100) NOT NULL,
                          domicilio VARCHAR(255),
                          email VARCHAR(100) NOT NULL UNIQUE, -- Requisito enunciado
                          tipo ENUM('Estandar', 'Premium') NOT NULL DEFAULT 'Estandar',
                          cuota_anual DECIMAL(10, 2) DEFAULT 0.00, -- Solo para Premium
                          descuento_envio DECIMAL(5, 2) DEFAULT 0.00 -- 0.20 para Premium (20%)
) ENGINE=InnoDB;

-- 3. Creo la tabla Pedidos
CREATE TABLE pedidos (
                         num_pedido INT AUTO_INCREMENT PRIMARY KEY,
                         id_cliente INT NOT NULL,
                         id_articulo INT NOT NULL,
                         cantidad INT NOT NULL,
                         fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    -- Claves foráneas para mantener la integridad
                         CONSTRAINT fk_pedido_cliente FOREIGN KEY (id_cliente)
                             REFERENCES clientes(id_cliente) ON DELETE RESTRICT,
                         CONSTRAINT fk_pedido_articulo FOREIGN KEY (id_articulo)
                             REFERENCES articulos(id_articulo) ON DELETE RESTRICT
) ENGINE=InnoDB;