package com.exemplo.folha.exception;

/**
 * Lançada quando um dado informado é inválido: CPF inválido, valores negativos,
 * percentuais fora do intervalo ou dados obrigatórios ausentes.
 */
public class DadoInvalidoException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public DadoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
