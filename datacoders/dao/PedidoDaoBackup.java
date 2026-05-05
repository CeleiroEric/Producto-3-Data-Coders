package datacoders.dao.interfaces;

import datacoders.modelo.Pedido;
import datacoders.modelo.excepciones.ArticuloNoEncontradoException;
import datacoders.modelo.excepciones.DuplicadoException;
import datacoders.modelo.excepciones.PedidoNoCancelableException;
import datacoders.modelo.excepciones.PedidoNoEncontradoException;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoDaoBackup {

    // Método para crear el pedido en la base de datos
    Pedido crearPedido(String emailCliente, String datosCliente, String codigoArticulo, int cantidad, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException;

    // Método para cancelar pedidos
    boolean eliminarPedido(int numPedido, LocalDateTime ahora)
            throws PedidoNoEncontradoException, PedidoNoCancelableException;

    // Listado de pedidos pendientes
    List<Pedido> findPendientes(String emailCliente);

    // Listado de pedidos enviados
    List<Pedido> findEnviados(String emailCliente);
}