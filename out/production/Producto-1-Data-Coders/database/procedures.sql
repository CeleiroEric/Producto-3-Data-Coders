USE online_store;

DELIMITER //

-- Procedimiento para crear un pedido
CREATE PROCEDURE sp_crear_pedido(
    IN p_email_cliente VARCHAR(100),
    IN p_codigo_articulo VARCHAR(50),
    IN p_cantidad INT
)
BEGIN
    DECLARE v_id_cliente INT;
    DECLARE v_id_articulo INT;

    -- Manejo de errores para transacciones
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
ROLLBACK;
SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error al crear el pedido: transacción abortada';
END;

START TRANSACTION;

-- Obtener IDs internos
SELECT id_cliente INTO v_id_cliente FROM clientes WHERE email = p_email_cliente;
SELECT id_articulo INTO v_id_articulo FROM articulos WHERE codigo = p_codigo_articulo;

-- Validar que existan
IF v_id_cliente IS NULL OR v_id_articulo IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cliente o Artículo no encontrado';
END IF;

    -- Insertar el pedido
INSERT INTO pedidos (id_cliente, id_articulo, cantidad, fecha_hora)
VALUES (v_id_cliente, v_id_articulo, p_cantidad, NOW());

COMMIT;
END //

-- Procedimiento para eliminar un pedido si es cancelable
CREATE PROCEDURE sp_eliminar_pedido(
    IN p_num_pedido INT
)
BEGIN
    DECLARE v_minutos_transcurridos INT;
    DECLARE v_tiempo_preparacion INT;

    -- Obtengo el tiempo transcurrido y el límite del artículo
SELECT
    TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()),
    a.tiempo_preparacion_min
INTO v_minutos_transcurridos, v_tiempo_preparacion
FROM pedidos p
         JOIN articulos a ON p.id_articulo = a.id_articulo
WHERE p.num_pedido = p_num_pedido;

-- Lógica de cancelación
IF v_minutos_transcurridos < v_tiempo_preparacion THEN
DELETE FROM pedidos WHERE num_pedido = p_num_pedido;
ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El pedido no se puede cancelar: ya está en preparación/enviado';
END IF;
END //

DELIMITER ;