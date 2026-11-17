package com.exemplo.folha.model;

import com.exemplo.folha.exception.DadoInvalidoException;
import com.exemplo.folha.util.FormatadorMoeda;

/**
 * Funcionário comissionado: salário-base + percentual de comissão sobre as
 * vendas.
 * O percentual é informado de 0 a 100. Desconto simplificado de 11% sobre a
 * remuneração bruta.
 */
public class FuncionarioComissionado extends Funcionario implements Tributavel {

    /** Alíquota simplificada de desconto sobre a remuneração bruta. */
    public static final double ALIQUOTA_DESCONTO = 0.11;

    private double percentualComissao;
    private double totalVendas;

    public FuncionarioComissionado(int codigo, String nome, String cpf, double salarioBase,
            double percentualComissao, double totalVendas) {
        super(codigo, nome, cpf, salarioBase);
        setPercentualComissao(percentualComissao);
        setTotalVendas(totalVendas);
    }

    public final double getPercentualComissao() {
        return percentualComissao;
    }

    public final void setPercentualComissao(double percentualComissao) {
        if (Double.isNaN(percentualComissao) || percentualComissao < 0 || percentualComissao > 100) {
            throw new DadoInvalidoException("O percentual de comissão deve estar entre 0 e 100.");
        }
        this.percentualComissao = percentualComissao;
    }

    public final double getTotalVendas() {
        return totalVendas;
    }

    public final void setTotalVendas(double totalVendas) {
        this.totalVendas = validarNaoNegativo(totalVendas, "total de vendas");
    }

    public double calcularComissao() {
        return totalVendas * percentualComissao / 100.0;
    }

    @Override
    public double calcularRemuneracao() {
        return getSalarioBase() + calcularComissao();
    }

    @Override
    public double calcularTributos() {
        return calcularRemuneracao() * ALIQUOTA_DESCONTO;
    }

    @Override
    protected String detalhesAdicionais() {
        return "Salário-base: " + FormatadorMoeda.formatar(getSalarioBase()) + "\n"
                + "Percentual de comissão: " + FormatadorMoeda.formatarPercentual(percentualComissao) + "\n"
                + "Total de vendas: " + FormatadorMoeda.formatar(totalVendas) + "\n";
    }
}
