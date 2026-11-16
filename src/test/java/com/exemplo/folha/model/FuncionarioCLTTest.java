package com.exemplo.folha.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.exemplo.folha.exception.DadoInvalidoException;
import org.junit.jupiter.api.Test;

class FuncionarioCLTTest {

    private static final String CPF = "529.982.247-25";

    private FuncionarioCLT criar() {
        return new FuncionarioCLT(1, "Ana Silva", CPF, 3000.0, 500.0);
    }

    @Test
    void remuneracaoEhSalarioBaseMaisBeneficios() {
        assertEquals(3500.0, criar().calcularRemuneracao(), 0.001);
    }

    @Test
    void tributosSaoOnzePorCentoDaRemuneracao() {
        assertEquals(385.0, criar().calcularTributos(), 0.001);
    }

    @Test
    void remuneracaoLiquidaDescontaTributos() {
        assertEquals(3115.0, criar().calcularRemuneracaoLiquida(), 0.001);
    }

    @Test
    void aceitaBeneficiosZero() {
        FuncionarioCLT f = new FuncionarioCLT(1, "Ana", CPF, 2000.0, 0.0);
        assertEquals(2000.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void gettersRetornamOsDadosInformados() {
        FuncionarioCLT f = criar();
        assertEquals(1, f.getCodigo());
        assertEquals("Ana Silva", f.getNome());
        assertEquals("52998224725", f.getCpf());
        assertEquals(3000.0, f.getSalarioBase(), 0.001);
        assertEquals(500.0, f.getBeneficios(), 0.001);
        assertEquals("CLT", f.getTipo());
    }

    @Test
    void setBeneficiosAlteraARemuneracao() {
        FuncionarioCLT f = criar();
        f.setBeneficios(1000.0);
        assertEquals(4000.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void salarioNegativoLancaExcecao() {
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioCLT(1, "Ana", CPF, -1.0, 0.0));
    }

    @Test
    void beneficiosNegativosLancamExcecao() {
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioCLT(1, "Ana", CPF, 1000.0, -5.0));
        FuncionarioCLT f = criar();
        assertThrows(DadoInvalidoException.class, () -> f.setBeneficios(-0.01));
    }

    @Test
    void cpfInvalidoLancaExcecao() {
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioCLT(1, "Ana", "111.111.111-11", 1000.0, 0.0));
    }

    @Test
    void nomeObrigatorio() {
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioCLT(1, "  ", CPF, 1000.0, 0.0));
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioCLT(1, null, CPF, 1000.0, 0.0));
    }

    @Test
    void codigoDeveSerPositivo() {
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioCLT(0, "Ana", CPF, 1000.0, 0.0));
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioCLT(-3, "Ana", CPF, 1000.0, 0.0));
    }

    @Test
    void exibirDadosContemInformacoesPrincipais() {
        String dados = criar().exibirDados();
        assertTrue(dados.contains("Ana Silva"));
        assertTrue(dados.contains("529.982.247-25"));
        assertTrue(dados.contains("Benefícios: R$ 500,00"));
        assertTrue(dados.contains("Remuneração bruta: R$ 3.500,00"));
        assertTrue(dados.contains("Tributos: R$ 385,00"));
        assertTrue(dados.contains("Remuneração líquida: R$ 3.115,00"));
    }
}
