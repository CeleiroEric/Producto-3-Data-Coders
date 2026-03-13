package datacoders.modelo;

import datacoders.modelo.excepciones.ArticuloNoEncontradoException;
import datacoders.modelo.excepciones.DuplicadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DatosTest {

    private Datos datos;

    @BeforeEach
    void setUp() {
        datos = new Datos();
    }

    @Test
    void testAddArticuloDuplicadoLanzaExcepcion() throws DuplicadoException {
        Articulo art = new Articulo("A01", "Teclado", 45.50, 5.0, 20);
        datos.addArticulo(art);

        assertThrows(DuplicadoException.class, () -> {
            datos.addArticulo(new Articulo("A01", "Otro Teclado", 10.0, 2.0, 5));
        }, "Debería lanzar DuplicadoException si el código ya existe");
    }

    @Test
    void testBuscarArticuloExistente() throws DuplicadoException, ArticuloNoEncontradoException {
        Articulo artOriginal = new Articulo("B02", "Ratón Gaming", 25.0, 3.5, 10);
        datos.addArticulo(artOriginal);

        Articulo artEncontrado = datos.buscarArticuloPorCodigo("B02");

        assertNotNull(artEncontrado);
        assertEquals("Ratón Gaming", artEncontrado.getDescripcion());
        assertEquals(25.0, artEncontrado.getPrecioVenta());
    }
}