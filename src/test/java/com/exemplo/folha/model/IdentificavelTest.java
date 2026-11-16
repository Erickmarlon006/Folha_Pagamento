package com.exemplo.folha.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class IdentificavelTest {

    @Test
    void funcionarioCLTRetornaOCodigoInformado() {
        Identificavel clt = new FuncionarioCLT(10, "Ana", "529.982.247-25", 1000.0, 0.0);
        assertEquals(10, clt.getCodigo());
    }

    @Test
    void funcionarioPJRetornaOCodigoInformado() {
        Identificavel pj = new FuncionarioPJ(20, "Bruno", "111.444.777-35", 100.0, 1);
        assertEquals(20, pj.getCodigo());
    }

    @Test
    void funcionarioComissionadoRetornaOCodigoInformado() {
        Identificavel com = new FuncionarioComissionado(30, "Carla", "390.533.447-05", 1000.0, 5.0, 0.0);
        assertEquals(30, com.getCodigo());
    }

    @Test
    void todosOsTiposDeFuncionarioSaoIdentificaveis() {
        assertTrue(new FuncionarioCLT(1, "Ana", "529.982.247-25", 1000.0, 0.0) instanceof Identificavel);
        assertTrue(new FuncionarioPJ(2, "Bruno", "111.444.777-35", 100.0, 1) instanceof Identificavel);
        assertTrue(new FuncionarioComissionado(3, "Carla", "390.533.447-05", 1000.0, 5.0, 0.0)
                instanceof Identificavel);
    }
}
