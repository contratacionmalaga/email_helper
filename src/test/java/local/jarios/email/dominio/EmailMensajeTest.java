package local.jarios.email.dominio;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmailMensajeTest {

    @Test
    void debeCrearEmailMensajeConDatosCorrectos() {
        var mensaje = new EmailMensaje(
                "juan@correo.com",
                List.of("ana@correo.com", "luis@empresa.com"),
                "Asunto importante",
                "Contenido del correo"
        );

        assertEquals("juan@correo.com", mensaje.remitente());
        assertEquals(2, mensaje.destinatarios().size());
        assertEquals("Asunto importante", mensaje.asunto());
        assertEquals("Contenido del correo", mensaje.cuerpo());
    }

    @Test
    void toStringDebeIncluirCamposConCuerpoCorto() {
        var mensaje = new EmailMensaje(
                "remitente@dominio.com",
                List.of("destino@dominio.com"),
                "Test Asunto",
                "Texto corto"
        );

        String salida = mensaje.toString();
        assertTrue(salida.contains("remitente@dominio.com"));
        assertTrue(salida.contains("destino@dominio.com"));
        assertTrue(salida.contains("Test Asunto"));
        assertTrue(salida.contains("Texto corto"));
    }

    @Test
    void toStringDebeResumirCuerpoLargo() {
        String cuerpoLargo = "A".repeat(150);
        var mensaje = new EmailMensaje(
                "remitente@dominio.com",
                List.of("destino@dominio.com"),
                "Asunto largo",
                cuerpoLargo
        );

        String salida = mensaje.toString();
        assertTrue(salida.contains("remitente@dominio.com"));
        assertTrue(salida.contains("destino@dominio.com"));
        assertTrue(salida.contains("Asunto largo"));
        assertTrue(salida.contains("AAA")); // Parte del contenido
        assertTrue(salida.contains("...")); // Indica que fue truncado
        assertTrue(salida.length() < cuerpoLargo.length() + 100); // Truncado efectivamente
    }

    @Test
    void toStringDebeManejarCuerpoNulo() {
        var mensaje = new EmailMensaje(
                "remitente@dominio.com",
                List.of("destino@dominio.com"),
                "Asunto con cuerpo nulo",
                null
        );

        String salida = mensaje.toString();
        assertTrue(salida.contains("cuerpo=''" ));
    }
}
