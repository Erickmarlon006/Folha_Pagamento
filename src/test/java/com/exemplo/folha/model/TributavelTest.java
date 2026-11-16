package com.exemplo.folha.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TributavelTest {

    @Test
    void cltEComissionadoSaoTributaveis() {
        Funcionario clt = new FuncionarioCLT(1, "Ana", "529.982.247-25", 1000.0, 0.0);
        Funcionario com = new FuncionarioComissionado(3, "Carla", "390.533.447-05", 1000.0, 10.0, 0.0);
        assertTrue(clt instanceof Tributavel);
        assertTrue(com instanceof Tributavel);
    }

    @Test
    void pjNaoEhTributavel() {
        Funcionario pj = new FuncionarioPJ(2, "Bruno", "111.444.777-35", 100.0, 1);
        assertFalse(pj instanceof Tributavel);
    }

    @Test
    void tributosDoCLTUsamAAliquotaDaClasse() {
        Tributavel clt = new FuncionarioCLT(1, "Ana", "529.982.247-25", 1000.0, 0.0);
        assertEquals(1000.0 * FuncionarioCLT.ALIQUOTA_DESCONTO, clt.calcularTributos(), 0.001);
    }

    @Test
    void tributosDoComissionadoIncidemSobreSalarioMaisComissao() {
        Tributavel com = new FuncionarioComissionado(3, "Carla", "390.533.447-05", 1000.0, 10.0, 5000.0);
        // remuneração = 1000 + 500 = 1500; tributos = 11% de 1500
        assertEquals(165.0, com.calcularTributos(), 0.001);
    }
}
