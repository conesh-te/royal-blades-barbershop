package com.royalblades.servicio;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AsignacionServiceTest {

    @Test
    public void testAsignacionAlBarberoConMenosCitas() {
        AsignacionService service = new AsignacionService();
        Map<String, Integer> barberos = new LinkedHashMap<>();
        barberos.put("Carlos", 4);
        barberos.put("Javier", 1);
        barberos.put("Miguel", 3);

        String resultado = service.seleccionarBarberoEquitativo(barberos);
        assertEquals("Javier", resultado, "Debe asignar al barbero con menos citas");
    }
}
