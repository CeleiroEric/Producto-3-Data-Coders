package datacoders.dao;

import java.sql.*;

public class PedidoDao {

    // Llamamos al procedimiento sp_crear_pedido que creamos en MySQL
    public void crearPedido(int idCliente, String codigoArticulo, int cantidad) throws SQLException {
        String sql = "{CALL sp_crear_pedido(?, ?, ?)}";
        try (Connection con = Conexion.obtenerConexion();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, idCliente);
            cs.setString(2, codigoArticulo);
            cs.setInt(3, cantidad);
            cs.execute();
        }
    }

    // Llamamos al procedimiento de eliminar
    public void eliminarPedido(int numeroPedido) throws SQLException {
        String sql = "{CALL sp_eliminar_pedido(?)}";
        try (Connection con = Conexion.obtenerConexion();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, numeroPedido);
            cs.execute();
        }
    }
}