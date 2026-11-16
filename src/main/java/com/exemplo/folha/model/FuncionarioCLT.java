package com.exemplo.folha.model;

import com.exemplo.folha.util.FormatadorMoeda;

/**
 * Funcionário contratado em regime CLT: salário-base + adicional de benefícios.
 * Sobre a remuneração bruta incide um desconto simplificado de 11% (tipo INSS).
 */
public class FuncionarioCLT extends Funcionario implements Tributavel {

    /** Alíquota simplificada de desconto sobre a remuneração bruta. */
    public static final double ALIQUOTA_DESCONTO = 0.11;

    private double beneficios;

    public FuncionarioCLT(int codigo, String nome, String cpf, double salarioBase, double beneficios) {
        super(codigo, nome, cpf, salarioBase);
        setBeneficios(beneficios);
    }

    public final double getBeneficios() {
        return beneficios;
    }

    public final void setBeneficios(double beneficios) {
        this.beneficios = validarNaoNegativo(beneficios, "benefícios");
    }

    @Override
    public double calcularRemuneracao() {
        return getSalarioBase() + beneficios;
    }

    @Override
    public double calcularTributos() {
        return calcularRemuneracao() * ALIQUOTA_DESCONTO;
    }

    @Override
    protected String detalhesAdicionais() {
        return "Salário-base: " + FormatadorMoeda.formatar(getSalarioBase()) + "\n"
                + "Benefícios: " + FormatadorMoeda.formatar(beneficios) + "\n";
    }
}
