package com.exemplo.folha.model;

import com.exemplo.folha.exception.DadoInvalidoException;
import com.exemplo.folha.util.FormatadorMoeda;

/**
 * Prestador de serviço (PJ): recebe um valor por projeto realizado.
 * Não implementa {@link Tributavel}, pois o PJ recolhe os próprios impostos.
 * O salário-base é sempre zero.
 */
public class FuncionarioPJ extends Funcionario {

    private double valorPorProjeto;
    private int quantidadeProjetos;

    public FuncionarioPJ(int codigo, String nome, String cpf, double valorPorProjeto, int quantidadeProjetos) {
        super(codigo, nome, cpf, 0.0);
        setValorPorProjeto(valorPorProjeto);
        setQuantidadeProjetos(quantidadeProjetos);
    }

    public final double getValorPorProjeto() {
        return valorPorProjeto;
    }

    public final void setValorPorProjeto(double valorPorProjeto) {
        this.valorPorProjeto = validarNaoNegativo(valorPorProjeto, "valor por projeto");
    }

    public final int getQuantidadeProjetos() {
        return quantidadeProjetos;
    }

    public final void setQuantidadeProjetos(int quantidadeProjetos) {
        if (quantidadeProjetos < 0) {
            throw new DadoInvalidoException("A quantidade de projetos não pode ser negativa.");
        }
        this.quantidadeProjetos = quantidadeProjetos;
    }

    @Override
    public double calcularRemuneracao() {
        return valorPorProjeto * quantidadeProjetos;
    }

    @Override
    protected String detalhesAdicionais() {
        return "Valor por projeto: " + FormatadorMoeda.formatar(valorPorProjeto) + "\n"
                + "Projetos realizados: " + quantidadeProjetos + "\n";
    }
}
