package com.exemplo.folha.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FuncionarioJaCadastradoExceptionTest {

    @Test
    void guardaAMensagem() {
        FuncionarioJaCadastradoException e = new FuncionarioJaCadastradoException("duplicado");
        assertEquals("duplicado", e.getMessage());
    }

    @Test
    void eUmaExcecaoVerificada() {
        Exception e = new FuncionarioJaCadastradoException("x");
        assertTrue(!(e instanceof RuntimeException));
    }

    @Test
    void podeSerLancadaECapturada() {
        FuncionarioJaCadastradoException e = assertThrows(FuncionarioJaCadastradoException.class, () -> {
            throw new FuncionarioJaCadastradoException("falhou");
        });
        assertEquals("falhou", e.getMessage());
    }
}
