package datacoders.dao;

/**
 * NOTA:
 * Esta clase pertenecía a una versión anterior de pruebas DAO.
 * Queda temporalmente fuera de uso porque utilizaba una API antigua:
 * - ArticuloDAOInterface
 * - métodos como listar() e insertar()
 * - una factoría DAO anterior
 *
 * La arquitectura activa del proyecto para Producto 3 es:
 * - datacoders.factory.DAOFactory
 * - datacoders.dao.ArticuloDAO
 * - datacoders.dao.ClienteDAO
 * - datacoders.dao.PedidoDAO
 *
 * Si más adelante se quieren rehacer pruebas manuales DAO,
 * habrá que adaptarlas a la API actual.
 */
public class PruebaDaos {
    public static void main(String[] args) {
        System.out.println("PruebaDaos deshabilitada temporalmente. Usar la arquitectura DAO actual.");
    }
}