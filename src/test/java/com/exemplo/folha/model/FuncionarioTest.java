package com.exemplo.folha.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.exemplo.folha.exception.DadoInvalidoException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Testa o comportamento comum da classe abstrata e o polimorfismo por sobreposição. */
class FuncionarioTest {

    @Test
    void cpfEhArmazenadoSoComDigitosEPodeSerFormatado() {
        Funcionario f = new FuncionarioCLT(1, "Ana", "529.982.247-25", 1000.0, 0.0);
        assertEquals("52998224725", f.getCpf());
        assertEquals("529.982.247-25", f.getCpfFormatado());
    }

    @Test
    void nomeEhAparado() {
        Funcionario f = new FuncionarioCLT(1, "  Ana  ", "52998224725", 1000.0, 0.0);
        assertEquals("Ana", f.getNome());
    }

    @Test
    void settersValidamOsDados() {
        Funcionario f = new FuncionarioCLT(1, "Ana", "52998224725", 1000.0, 0.0);
        assertThrows(DadoInvalidoException.class, () -> f.setNome(""));
        assertThrows(DadoInvalidoException.class, () -> f.setCpf("123"));
        assertThrows(DadoInvalidoException.class, () -> f.setCpf(null));
        assertThrows(DadoInvalidoException.class, () -> f.setSalarioBase(-1.0));
        assertThrows(DadoInvalidoException.class, () -> f.setSalarioBase(Double.NaN));
    }

    @Test
    void settersValidosAlteramOsDados() {
        Funcionario f = new FuncionarioCLT(1, "Ana", "52998224725", 1000.0, 0.0);
        f.setNome("Ana Maria");
        f.setCpf("111.444.777-35");
        f.setSalarioBase(1500.0);
        assertEquals("Ana Maria", f.getNome());
        assertEquals("11144477735", f.getCpf());
        assertEquals(1500.0, f.getSalarioBase(), 0.001);
    }

    @Test
    void toStringTemCodigoTipoENome() {
        Funcionario f = new FuncionarioPJ(7, "Bruno", "111.444.777-35", 100.0, 1);
        assertEquals("[7] PJ - Bruno", f.toString());
    }

    @Test
    void polimorfismoCadaTipoExecutaASuaPropriaRegra() {
        Funcionario clt = new FuncionarioCLT(1, "Ana", "529.982.247-25", 3000.0, 500.0);
        Funcionario pj = new FuncionarioPJ(2, "Bruno", "111.444.777-35", 1500.0, 4);
        Funcionario com = new FuncionarioComissionado(3, "Carla", "390.533.447-05", 2000.0, 10.0, 10000.0);

        assertEquals(3500.0, clt.calcularRemuneracao(), 0.001);
        assertEquals(6000.0, pj.calcularRemuneracao(), 0.001);
        assertEquals(3000.0, com.calcularRemuneracao(), 0.001);
    }

    @Test
    void polimorfismoEmUmaListaDeFuncionarios() {
        List<Funcionario> lista = new ArrayList<>();
        lista.add(new FuncionarioCLT(1, "Ana", "529.982.247-25", 3000.0, 500.0));
        lista.add(new FuncionarioPJ(2, "Bruno", "111.444.777-35", 1500.0, 4));
        lista.add(new FuncionarioComissionado(3, "Carla", "390.533.447-05", 2000.0, 10.0, 10000.0));

        double total = 0;
        for (Funcionario f : lista) {
            total += f.calcularRemuneracao();
        }
        assertEquals(12500.0, total, 0.001);
    }

    @Test
    void todasAsSubclassesSaoIdentificaveis() {
        Funcionario clt = new FuncionarioCLT(1, "Ana", "529.982.247-25", 3000.0, 0.0);
        Funcionario pj = new FuncionarioPJ(2, "Bruno", "111.444.777-35", 1500.0, 1);
        Funcionario com = new FuncionarioComissionado(3, "Carla", "390.533.447-05", 2000.0, 10.0, 0.0);
        assertTrue(clt instanceof Identificavel);
        assertTrue(pj instanceof Identificavel);
        assertTrue(com instanceof Identificavel);
    }
}
