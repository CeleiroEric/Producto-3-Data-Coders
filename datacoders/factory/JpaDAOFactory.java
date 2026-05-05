package datacoders.factory;

// 1. IMPORTANTE: Usamos las interfaces de la carpeta interfaces
import datacoders.dao.interfaces.ArticuloDAOInterface;
import datacoders.dao.interfaces.ClienteDAOInterface;
import datacoders.dao.interfaces.PedidoDAOInterface;

// 2. Importamos las implementaciones de JPA
import datacoders.dao.jpa.ArticuloDAOJPA;
import datacoders.dao.jpa.ClienteDAOJPA;
import datacoders.dao.jpa.PedidoDAOJPA;

public class JpaDAOFactory extends DAOFactory {

    @Override
    public ArticuloDAOInterface getArticuloDAO() {
        // Asegúrate de que ArticuloDAOJPA implemente ArticuloDAOInterface
        return new ArticuloDAOJPA();
    }

    @Override
    public ClienteDAOInterface getClienteDAO() {
        return new ClienteDAOJPA();
    }

    @Override
    public PedidoDAOInterface getPedidoDAO() {
        return new PedidoDAOJPA();
    }
}