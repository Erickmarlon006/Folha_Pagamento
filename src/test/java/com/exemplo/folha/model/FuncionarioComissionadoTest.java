package com.exemplo.folha.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.exemplo.folha.exception.DadoInvalidoException;
import org.junit.jupiter.api.Test;

class FuncionarioComissionadoTest {

    private static final String CPF = "390.533.447-05";

    private FuncionarioComissionado criar() {
        return new FuncionarioComissionado(3, "Carla Dias", CPF, 2000.0, 10.0, 10000.0);
    }

    @Test
    void comissaoEhPercentualSobreAsVendas() {
        assertEquals(1000.0, criar().calcularComissao(), 0.001);
    }

    @Test
    void remuneracaoEhSalarioBaseMaisComissao() {
        assertEquals(3000.0, criar().calcularRemuneracao(), 0.001);
    }

    @Test
    void tributosSaoOnzePorCentoDaRemuneracao() {
        assertEquals(330.0, criar().calcularTributos(), 0.001);
        assertEquals(2670.0, criar().calcularRemuneracaoLiquida(), 0.001);
    }

    @Test
    void semVendasRecebeApenasSalarioBase() {
        FuncionarioComissionado f = new FuncionarioComissionado(3, "Carla", CPF, 2000.0, 10.0, 0.0);
        assertEquals(2000.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void aceitaPercentualNosLimites() {
        assertEquals(0.0, new FuncionarioComissionado(3, "Carla", CPF, 0.0, 0.0, 500.0).calcularRemuneracao(), 0.001);
        assertEquals(500.0, new FuncionarioComissionado(3, "Carla", CPF, 0.0, 100.0, 500.0).calcularRemuneracao(), 0.001);
    }

    @Test
    void percentualInvalidoLancaExcecao() {
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioComissionado(3, "Carla", CPF, 1000.0, -1.0, 0.0));
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioComissionado(3, "Carla", CPF, 1000.0, 100.01, 0.0));
        FuncionarioComissionado f = criar();
        assertThrows(DadoInvalidoException.class, () -> f.setPercentualComissao(150.0));
    }

    @Test
    void valoresNegativosLancamExcecao() {
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioComissionado(3, "Carla", CPF, -1.0, 10.0, 0.0));
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioComissionado(3, "Carla", CPF, 1000.0, 10.0, -1.0));
        FuncionarioComissionado f = criar();
        assertThrows(DadoInvalidoException.class, () -> f.setTotalVendas(-5.0));
    }

    @Test
    void settersAtualizamACalculo() {
        FuncionarioComissionado f = criar();
        f.setPercentualComissao(5.0);
        f.setTotalVendas(20000.0);
        assertEquals(5.0, f.getPercentualComissao(), 0.001);
        assertEquals(20000.0, f.getTotalVendas(), 0.001);
        assertEquals(3000.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void exibirDadosContemDetalhesDaComissao() {
        String dados = criar().exibirDados();
        assertTrue(dados.contains("Percentual de comissão: 10,00%"));
        assertTrue(dados.contains("Total de vendas: R$ 10.000,00"));
        assertTrue(dados.contains("Remuneração bruta: R$ 3.000,00"));
    }
}
