package datacoders.factory;

import datacoders.dao.ArticuloDAO;
import datacoders.dao.ClienteDAO;
import datacoders.dao.PedidoDAO;

public class MySqlDAOFactory extends DAOFactory {
    @Override public ClienteDAO getClienteDAO() { return null; }
    @Override public ArticuloDAO getArticuloDAO() { return null; }
    @Override public PedidoDAO getPedidoDAO() { return null; }
}
// Provisional para que compile.
// Cuando Marian tenga los DAOs 
// de SQL reales se sustituye los 
// null por las instancias 
