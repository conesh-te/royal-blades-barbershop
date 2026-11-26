package com.royalblades.seguridad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    public void testEncriptacionYVerificacionBCrypt() {
        String claveOriginal = "AdminRoyal2026!";
        String hash = PasswordUtil.hashPassword(claveOriginal);

        assertNotEquals(claveOriginal, hash, "El hash no debe ser igual a la clave en texto plano");
        assertTrue(PasswordUtil.checkPassword(claveOriginal, hash), "La clave correcta debe validar");
        assertFalse(PasswordUtil.checkPassword("ClaveIncorrecta", hash), "Una clave errónea debe rechazarse");
    }
}
