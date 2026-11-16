package com.exemplo.folha.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.exemplo.folha.exception.DadoInvalidoException;
import org.junit.jupiter.api.Test;

class FuncionarioPJTest {

    private static final String CPF = "111.444.777-35";

    private FuncionarioPJ criar() {
        return new FuncionarioPJ(2, "Bruno Costa", CPF, 1500.0, 4);
    }

    @Test
    void remuneracaoEhValorPorProjetoVezesQuantidade() {
        assertEquals(6000.0, criar().calcularRemuneracao(), 0.001);
    }

    @Test
    void semProjetosARemuneracaoEhZero() {
        FuncionarioPJ f = new FuncionarioPJ(2, "Bruno", CPF, 1500.0, 0);
        assertEquals(0.0, f.calcularRemuneracao(), 0.001);
    }

    @Test
    void salarioBaseEhSempreZero() {
        assertEquals(0.0, criar().getSalarioBase(), 0.001);
    }

    @Test
    void naoEhTributavelEALiquidaIgualABruta() {
        FuncionarioPJ f = criar();
        assertFalse(f instanceof Tributavel);
        assertEquals(f.calcularRemuneracao(), f.calcularRemuneracaoLiquida(), 0.001);
    }

    @Test
    void settersAtualizamACalculo() {
        FuncionarioPJ f = criar();
        f.setValorPorProjeto(2000.0);
        f.setQuantidadeProjetos(3);
        assertEquals(6000.0, f.calcularRemuneracao(), 0.001);
        assertEquals(2000.0, f.getValorPorProjeto(), 0.001);
        assertEquals(3, f.getQuantidadeProjetos());
    }

    @Test
    void valorPorProjetoNegativoLancaExcecao() {
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioPJ(2, "Bruno", CPF, -10.0, 1));
        FuncionarioPJ f = criar();
        assertThrows(DadoInvalidoException.class, () -> f.setValorPorProjeto(-1.0));
    }

    @Test
    void quantidadeDeProjetosNegativaLancaExcecao() {
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioPJ(2, "Bruno", CPF, 100.0, -1));
        FuncionarioPJ f = criar();
        assertThrows(DadoInvalidoException.class, () -> f.setQuantidadeProjetos(-2));
    }

    @Test
    void tipoEhPJ() {
        assertEquals("PJ", criar().getTipo());
    }

    @Test
    void exibirDadosContemDetalhesDeProjetos() {
        String dados = criar().exibirDados();
        assertTrue(dados.contains("Valor por projeto: R$ 1.500,00"));
        assertTrue(dados.contains("Projetos realizados: 4"));
        assertTrue(dados.contains("Remuneração bruta: R$ 6.000,00"));
        assertFalse(dados.contains("Tributos"));
    }
}
