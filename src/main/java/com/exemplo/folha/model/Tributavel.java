package com.exemplo.folha.model;

/**
 * Contrato para remunerações sobre as quais incidem tributos ou descontos.
 */
public interface Tributavel {

    /** Calcula o valor total de tributos/descontos aplicáveis à remuneração. */
    double calcularTributos();
}
