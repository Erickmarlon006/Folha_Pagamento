package com.exemplo.folha.util;

import java.util.Locale;

/**
 * Formata valores monetários e percentuais no padrão brasileiro.
 */
public final class FormatadorMoeda {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private FormatadorMoeda() {
    }

    /** Ex.: 1234.5 -> "R$ 1.234,50". */
    public static String formatar(double valor) {
        return "R$ " + String.format(PT_BR, "%,.2f", valor);
    }

    /** Ex.: 10 -> "10,00%". */
    public static String formatarPercentual(double percentual) {
        return String.format(PT_BR, "%.2f%%", percentual);
    }
}
