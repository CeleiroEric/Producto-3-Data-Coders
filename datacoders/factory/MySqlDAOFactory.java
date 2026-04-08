package datacoders.factory;

import datacoders.dao.ArticuloDao;
import datacoders.dao.ClienteDao;
import datacoders.dao.PedidoDao;
import datacoders.dao.mysql.MySqlArticuloDAO;
import datacoders.dao.mysql.MySqlClienteDAO;
import datacoders.dao.mysql.MySqlPedidoDAO;


public class MySqlDAOFactory extends DAOFactory {

    @Override
    public ClienteDao getClienteDAO() {
        return new MySqlClienteDAO();
    }

    @Override
    public ArticuloDao getArticuloDAO() {
        return new MySqlArticuloDAO();
    }

    @Override
    public PedidoDao getPedidoDAO() {
        return new MySqlPedidoDAO();
    }
}