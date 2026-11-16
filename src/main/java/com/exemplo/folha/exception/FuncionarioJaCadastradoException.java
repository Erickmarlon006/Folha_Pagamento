package com.exemplo.folha.exception;

/**
 * Lançada ao tentar cadastrar um funcionário cujo identificador (ou CPF) já existe.
 */
public class FuncionarioJaCadastradoException extends Exception {

    private static final long serialVersionUID = 1L;

    public FuncionarioJaCadastradoException(String mensagem) {
        super(mensagem);
    }
}
