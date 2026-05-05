package datacoders.dao.interfaces;

import datacoders.modelo.Pedido;
import datacoders.modelo.excepciones.*;
import java.time.LocalDateTime;
import java.util.List;

public interface PedidoDAOInterface {
    Pedido crearPedido(String emailCliente, String datosCliente, String codigoArticulo, int cantidad, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException;

    boolean eliminarPedido(int numPedido, LocalDateTime ahora)
            throws PedidoNoEncontradoException, PedidoNoCancelableException;

    List<Pedido> findPendientes(String emailCliente);
    List<Pedido> findEnviados(String emailCliente);
}