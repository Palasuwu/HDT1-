import static org.junit.Assert.*;
import org.junit.Test;

public class RadioGUITest {

    @Test
    public void testEncenderApagar() {
        RadioGUI.Radio radio = new RadioGUI.Radio();
        assertFalse(radio.estaEncendida()); // La radio debería estar apagada al principio

        radio.toggleEncendidoApagado();
        assertTrue(radio.estaEncendida()); // La radio debería estar encendida después de pulsar el botón

        radio.toggleEncendidoApagado();
        assertFalse(radio.estaEncendida()); // La radio debería estar apagada después de pulsar el botón nuevamente
    }
}
