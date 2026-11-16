package com.exemplo.folha.exception;

/**
 * Lançada quando uma busca ou remoção não encontra o funcionário solicitado.
 */
public class FuncionarioNaoEncontradoException extends Exception {

    private static final long serialVersionUID = 1L;

    public FuncionarioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
