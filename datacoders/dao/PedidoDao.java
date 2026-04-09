package datacoders.dao;

import datacoders.modelo.Pedido;
import datacoders.modelo.excepciones.ArticuloNoEncontradoException;
import datacoders.modelo.excepciones.DuplicadoException;
import datacoders.modelo.excepciones.PedidoNoCancelableException;
import datacoders.modelo.excepciones.PedidoNoEncontradoException;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoDAO {
    Pedido crearPedido(String emailCliente, String datosCliente, String codigoArticulo, int cantidad, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException;

    boolean eliminarPedido(int numPedido, LocalDateTime ahora)
            throws PedidoNoEncontradoException, PedidoNoCancelableException;

    List<Pedido> findPendientes(String emailCliente);
    List<Pedido> findEnviados(String emailCliente);
}