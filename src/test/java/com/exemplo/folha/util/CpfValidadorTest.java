package com.exemplo.folha.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CpfValidadorTest {

    @Test
    void aceitaCpfValidoComMascara() {
        assertTrue(CpfValidador.isValido("529.982.247-25"));
    }

    @Test
    void aceitaCpfValidoSemMascara() {
        assertTrue(CpfValidador.isValido("52998224725"));
    }

    @Test
    void rejeitaCpfComDigitoVerificadorErrado() {
        assertFalse(CpfValidador.isValido("529.982.247-24"));
    }

    @Test
    void rejeitaCpfComTodosOsDigitosIguais() {
        assertFalse(CpfValidador.isValido("111.111.111-11"));
    }

    @Test
    void rejeitaCpfComTamanhoErrado() {
        assertFalse(CpfValidador.isValido("123"));
        assertFalse(CpfValidador.isValido("529982247251"));
    }

    @Test
    void rejeitaCpfNuloOuVazio() {
        assertFalse(CpfValidador.isValido(null));
        assertFalse(CpfValidador.isValido(""));
        assertFalse(CpfValidador.isValido("   "));
    }

    @Test
    void normalizarRemoveTudoQueNaoForDigito() {
        assertEquals("52998224725", CpfValidador.normalizar("529.982.247-25"));
        assertEquals("", CpfValidador.normalizar(null));
    }

    @Test
    void formatarAplicaMascara() {
        assertEquals("529.982.247-25", CpfValidador.formatar("52998224725"));
    }

    @Test
    void formatarMantemValorOriginalQuandoNaoTem11Digitos() {
        assertEquals("123", CpfValidador.formatar("123"));
    }
}
