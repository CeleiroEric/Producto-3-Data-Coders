package datacoders.modelo;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    @Test
    void testCalcularTotalClienteEstandar() {
        // Simulo un artículo de 100€ con 5€ de envío y 20 min de preparación
        Articulo art = new Articulo("A1", "Teclado", 100.0, 5.0, 20);
        // Simulo un cliente estándar, el cual paga el 100% del envío
        Cliente cl = new ClienteEstandar("Juan", "Calle Falsa 123", "12345678X", "juan@mail.com");

        // Simulo un pedido de 2 unidades
        Pedido p = new Pedido(1, cl, art, 2, LocalDateTime.now());

        // Verifico si el resultado es el que debería
        assertEquals(205.0, p.calcularTotal(), 0.001, "El cálculo para cliente estándar debería ser Subtotal + Envío completo");
    }

    @Test
    void testCalcularTotalClientePremium() {
        // Simulo un articulo
        Articulo art = new Articulo("A1", "Teclado", 100.0, 10.0, 30);
        // Simulo un cliente premium el cual tiene descuento en el envío
        Cliente cl = new ClientePremium("Ana", "Av. Mayor 1", "87654321Z", "ana@mail.com");

        // Simulo un pedido de 2 unidades
        Pedido p = new Pedido(2, cl, art, 2, LocalDateTime.now());

        // Verifico si todo esta correcto
        assertEquals(208.0, p.calcularTotal(), 0.001, "El cliente premium debería tener un 20% de descuento en el envío");
    }

    @Test
    void testEsCancelableDentroDelPlazo() {
        // Simulo un articulo que conlleva 60min de preparación
        Articulo art = new Articulo("A1", "Monitor", 200.0, 15.0, 60);
        Cliente cl = new ClienteEstandar("Pepe", "Calle 1", "11122233A", "pepe@mail.com");

        // Simulo un pedido que se hace ahora mismo
        Pedido p = new Pedido(3, cl, art, 1, LocalDateTime.now());

        // Debería ser cancelable pues no han pasado los 60 mins
        assertTrue(p.esCancelable(LocalDateTime.now()), "El pedido debería ser cancelable justo después de crearse");
    }
}