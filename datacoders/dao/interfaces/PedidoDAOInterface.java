package datacoders.dao;

import datacoders.modelo.Pedido;
import java.sql.SQLException;
import java.util.List;

public interface PedidoDAOInterface {
    void crearPedido(int idCliente, String codigoArticulo, int cantidad) throws SQLException;
    void eliminarPedido(int numeroPedido) throws SQLException;
    List<Pedido> listarPendientes(String email) throws SQLException;
    List<Pedido> listarEnviados(String email) throws SQLException;
}