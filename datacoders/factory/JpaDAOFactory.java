package datacoders.factory;

import datacoders.dao.*; // Para las interfaces
import datacoders.dao.jpa.*; // Aquí está el cambio: importa la carpeta jpa directamente


public class JpaDAOFactory extends DAOFactory {
    @Override
    public ArticuloDao getArticuloDAO() {
        return new ArticuloDAOJPA();
    }

    @Override
    public ClienteDao getClienteDAO() {
        return new ClienteDAOJPA();
    }

    @Override
    public PedidoDao getPedidoDAO() {
        return new PedidoDAOJPA();
    }
}