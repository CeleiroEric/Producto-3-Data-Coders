package datacoders.factory;
import datacoders.dao.interfaces.*;
import datacoders.dao.mysql.*;

public class MySqlDAOFactory extends DAOFactory {
    @Override
    public ArticuloDAOInterface getArticuloDAO() {
        return new MySqlArticuloDAO(); // ¡Ya no dará error!
    }

    @Override
    public ClienteDAOInterface getClienteDAO() {
        return new MySqlClienteDAO();
    }

    @Override
    public PedidoDAOInterface getPedidoDAO() {
        return new MySqlPedidoDAO();
    }
}