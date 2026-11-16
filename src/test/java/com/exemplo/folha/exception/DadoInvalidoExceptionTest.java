package com.exemplo.folha.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DadoInvalidoExceptionTest {

    @Test
    void guardaAMensagem() {
        assertEquals("inválido", new DadoInvalidoException("inválido").getMessage());
    }

    @Test
    void eUmaExcecaoNaoVerificadaDeArgumentoIlegal() {
        RuntimeException e = new DadoInvalidoException("x");
        assertTrue(e instanceof IllegalArgumentException);
    }

    @Test
    void podeSerLancadaECapturada() {
        DadoInvalidoException e = assertThrows(DadoInvalidoException.class, () -> {
            throw new DadoInvalidoException("falhou");
        });
        assertEquals("falhou", e.getMessage());
    }
}
