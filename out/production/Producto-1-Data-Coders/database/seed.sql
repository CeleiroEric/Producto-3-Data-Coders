USE online_store;

-- Insertar Artículos
INSERT INTO articulos (codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion_min) VALUES
                                                                                                    ('MON-24', 'Monitor 24 Pulgadas LED', 150.00, 10.00, 60),
                                                                                                    ('TECL-RGB', 'Teclado Mecánico RGB', 85.50, 5.00, 30);

-- Insertar Clientes (uno de cada tipo)
INSERT INTO clientes (nif, nombre, domicilio, email, tipo, cuota_anual, descuento_envio) VALUES
                                                                                             ('12345678A', 'Juan Perez', 'Galceran de Pinos', 'juan@email.com', 'Estandar', 0.00, 0.00),
                                                                                             ('87654321B', 'Marta Oro', 'Santiago Rusiñol', 'marta@email.com', 'Premium', 30.00, 0.20);

-- Insertar un Pedido inicial
CALL sp_crear_pedido('juan@email.com', 'MON-24', 1);