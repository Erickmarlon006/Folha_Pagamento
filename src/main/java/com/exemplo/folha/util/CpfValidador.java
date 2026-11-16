package com.exemplo.folha.util;

/**
 * Utilitário para normalizar, validar e formatar CPFs.
 */
public final class CpfValidador {

    private CpfValidador() {
    }

    /** Remove tudo que não for dígito. Retorna "" se o CPF for nulo. */
    public static String normalizar(String cpf) {
        if (cpf == null) {
            return "";
        }
        return cpf.replaceAll("\\D", "");
    }

    /** Valida o CPF (com ou sem máscara) pelos dígitos verificadores. */
    public static boolean isValido(String cpf) {
        String digitos = normalizar(cpf);
        if (digitos.length() != 11) {
            return false;
        }
        if (todosIguais(digitos)) {
            return false;
        }
        return calcularDigito(digitos, 9) == digitos.charAt(9) - '0'
                && calcularDigito(digitos, 10) == digitos.charAt(10) - '0';
    }

    /** Formata como 000.000.000-00. Se não tiver 11 dígitos, devolve o valor original. */
    public static String formatar(String cpf) {
        String d = normalizar(cpf);
        if (d.length() != 11) {
            return cpf;
        }
        return d.substring(0, 3) + "." + d.substring(3, 6) + "." + d.substring(6, 9) + "-" + d.substring(9);
    }

    private static int calcularDigito(String digitos, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (digitos.charAt(i) - '0') * (tamanho + 1 - i);
        }
        int resto = (soma * 10) % 11;
        if (resto == 10) {
            return 0;
        }
        return resto;
    }

    /** CPFs como 111.111.111-11 passam na conta dos dígitos, mas não são válidos. */
    private static boolean todosIguais(String digitos) {
        for (int i = 1; i < digitos.length(); i++) {
            if (digitos.charAt(i) != digitos.charAt(0)) {
                return false;
            }
        }
        return true;
    }
}
