package com.exemplo.folha.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FormatadorMoedaTest {

    @Test
    void formataValorComMilharEDuasCasas() {
        assertEquals("R$ 1.234,56", FormatadorMoeda.formatar(1234.56));
    }

    @Test
    void formataZero() {
        assertEquals("R$ 0,00", FormatadorMoeda.formatar(0));
    }

    @Test
    void formataValorInteiroComCentavos() {
        assertEquals("R$ 3.500,00", FormatadorMoeda.formatar(3500));
    }

    @Test
    void formataPercentual() {
        assertEquals("10,00%", FormatadorMoeda.formatarPercentual(10));
        assertEquals("7,50%", FormatadorMoeda.formatarPercentual(7.5));
    }
}
