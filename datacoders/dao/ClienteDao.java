package datacoders.dao;

import datacoders.modelo.Cliente;
import datacoders.modelo.excepciones.ClienteNoEncontradoException;
import datacoders.modelo.excepciones.DuplicadoException;

import java.util.List;

public interface ClienteDAO {
    boolean insertEstandar(String nombre, String domicilio, String nif, String email) throws DuplicadoException;
    boolean insertPremium(String nombre, String domicilio, String nif, String email) throws DuplicadoException;

    Cliente findByEmail(String email) throws ClienteNoEncontradoException;

    List<Cliente> findAll();
    List<Cliente> findAllEstandar();
    List<Cliente> findAllPremium();
}