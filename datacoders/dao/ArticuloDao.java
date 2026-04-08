package datacoders.dao;

import datacoders.modelo.Articulo;
import datacoders.modelo.excepciones.ArticuloNoEncontradoException;
import datacoders.modelo.excepciones.DuplicadoException;

import java.util.List;

public interface ArticuloDao {
    boolean insert(Articulo a) throws DuplicadoException;
    Articulo findByCodigo(String codigo) throws ArticuloNoEncontradoException;
    List<Articulo> findAll();
}