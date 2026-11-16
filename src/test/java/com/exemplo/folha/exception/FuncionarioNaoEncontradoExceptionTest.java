package com.exemplo.folha.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FuncionarioNaoEncontradoExceptionTest {

    @Test
    void guardaAMensagem() {
        FuncionarioNaoEncontradoException e = new FuncionarioNaoEncontradoException("não achei");
        assertEquals("não achei", e.getMessage());
    }

    @Test
    void eUmaExcecaoVerificada() {
        Exception e = new FuncionarioNaoEncontradoException("x");
        assertTrue(e instanceof Exception);
        assertTrue(!(e instanceof RuntimeException));
    }

    @Test
    void podeSerLancadaECapturada() {
        FuncionarioNaoEncontradoException e = assertThrows(FuncionarioNaoEncontradoException.class, () -> {
            throw new FuncionarioNaoEncontradoException("falhou");
        });
        assertEquals("falhou", e.getMessage());
    }
}
