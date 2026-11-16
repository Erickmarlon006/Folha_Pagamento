package com.exemplo.folha.model;

import com.exemplo.folha.exception.DadoInvalidoException;
import com.exemplo.folha.util.CpfValidador;
import com.exemplo.folha.util.FormatadorMoeda;

/**
 * Classe abstrata base de todos os funcionários.
 *
 * <p>Todos os atributos são privados; as validações ficam nos construtores e setters.
 * Cada subclasse define a sua própria regra em {@link #calcularRemuneracao()}.
 */
public abstract class Funcionario implements Identificavel {

    private final int codigo;
    private String nome;
    private String cpf;
    private double salarioBase;

    protected Funcionario(int codigo, String nome, String cpf, double salarioBase) {
        if (codigo <= 0) {
            throw new DadoInvalidoException("O código do funcionário deve ser um número positivo.");
        }
        this.codigo = codigo;
        setNome(nome);
        setCpf(cpf);
        setSalarioBase(salarioBase);
    }

    /** Calcula a remuneração bruta do funcionário conforme a sua categoria. */
    public abstract double calcularRemuneracao();

    /** Linhas (terminadas em '\n') com os dados específicos de cada categoria. */
    protected abstract String detalhesAdicionais();

    @Override
    public final int getCodigo() {
        return codigo;
    }

    public final String getNome() {
        return nome;
    }

    public final void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new DadoInvalidoException("O nome do funcionário é obrigatório.");
        }
        this.nome = nome.trim();
    }

    /** Retorna o CPF apenas com dígitos. */
    public final String getCpf() {
        return cpf;
    }

    public final String getCpfFormatado() {
        return CpfValidador.formatar(cpf);
    }

    public final void setCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new DadoInvalidoException("O CPF do funcionário é obrigatório.");
        }
        if (!CpfValidador.isValido(cpf)) {
            throw new DadoInvalidoException("CPF inválido: " + cpf);
        }
        this.cpf = CpfValidador.normalizar(cpf);
    }

    public final double getSalarioBase() {
        return salarioBase;
    }

    public final void setSalarioBase(double salarioBase) {
        this.salarioBase = validarNaoNegativo(salarioBase, "salário-base");
    }

    /** Nome da categoria (CLT, PJ, Comissionado), derivado do nome da classe. */
    public String getTipo() {
        return getClass().getSimpleName().replace("Funcionario", "");
    }

    /** Remuneração bruta menos os tributos (quando o funcionário é {@link Tributavel}). */
    public double calcularRemuneracaoLiquida() {
        double tributos = 0.0;
        if (this instanceof Tributavel) {
            Tributavel tributavel = (Tributavel) this;
            tributos = tributavel.calcularTributos();
        }
        return calcularRemuneracao() - tributos;
    }

    /** Texto com todos os dados do funcionário. */
    public String exibirDados() {
        StringBuilder sb = new StringBuilder();
        sb.append("Código: ").append(codigo).append('\n');
        sb.append("Tipo: ").append(getTipo()).append('\n');
        sb.append("Nome: ").append(nome).append('\n');
        sb.append("CPF: ").append(getCpfFormatado()).append('\n');
        sb.append(detalhesAdicionais());
        sb.append("Remuneração bruta: ").append(FormatadorMoeda.formatar(calcularRemuneracao())).append('\n');
        if (this instanceof Tributavel) {
            Tributavel tributavel = (Tributavel) this;
            sb.append("Tributos: ").append(FormatadorMoeda.formatar(tributavel.calcularTributos())).append('\n');
        }
        sb.append("Remuneração líquida: ").append(FormatadorMoeda.formatar(calcularRemuneracaoLiquida()));
        return sb.toString();
    }

    /** Valida valores monetários: não pode ser negativo, NaN ou infinito. */
    protected static double validarNaoNegativo(double valor, String campo) {
        if (Double.isNaN(valor) || Double.isInfinite(valor) || valor < 0) {
            throw new DadoInvalidoException("O campo '" + campo + "' deve ser um valor numérico não negativo.");
        }
        return valor;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s", codigo, getTipo(), nome);
    }
}
