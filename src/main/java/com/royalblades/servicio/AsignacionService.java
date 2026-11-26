package com.royalblades.servicio;

import java.util.Map;

public class AsignacionService {

    public String seleccionarBarberoEquitativo(Map<String, Integer> cargaPorBarbero) {
        if (cargaPorBarbero == null || cargaPorBarbero.isEmpty()) {
            throw new IllegalArgumentException("No hay barberos en turno disponibles");
        }
        String barberoElegido = null;
        int minCitas = Integer.MAX_VALUE;

        for (Map.Entry<String, Integer> entry : cargaPorBarbero.entrySet()) {
            if (entry.getValue() < minCitas) {
                minCitas = entry.getValue();
                barberoElegido = entry.getKey();
            }
        }
        return barberoElegido;
    }
}
