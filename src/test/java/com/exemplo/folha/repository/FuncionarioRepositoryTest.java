package com.exemplo.folha.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.exemplo.folha.exception.DadoInvalidoException;
import com.exemplo.folha.model.Funcionario;
import com.exemplo.folha.model.FuncionarioCLT;
import com.exemplo.folha.model.FuncionarioPJ;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FuncionarioRepositoryTest {

    private FuncionarioRepository repository;
    private Funcionario ana;
    private Funcionario bruno;

    @BeforeEach
    void preparar() {
        repository = new FuncionarioRepository();
        ana = new FuncionarioCLT(1, "Ana Silva", "529.982.247-25", 3000.0, 500.0);
        bruno = new FuncionarioPJ(2, "Bruno Costa", "111.444.777-35", 1500.0, 4);
    }

    @Test
    void comecaVazio() {
        assertEquals(0, repository.quantidade());
        assertTrue(repository.listarTodos().isEmpty());
    }

    @Test
    void salvaERecuperaPorCodigo() {
        repository.salvar(ana);
        assertSame(ana, repository.buscarPorCodigo(1));
        assertTrue(repository.existePorCodigo(1));
    }

    @Test
    void buscaPorCodigoInexistenteRetornaVazio() {
        assertNull(repository.buscarPorCodigo(99));
        assertFalse(repository.existePorCodigo(99));
    }

    @Test
    void buscaPorCpfComOuSemMascara() {
        repository.salvar(ana);
        assertSame(ana, repository.buscarPorCpf("529.982.247-25"));
        assertSame(ana, repository.buscarPorCpf("52998224725"));
        assertNull(repository.buscarPorCpf("111.444.777-35"));
    }

    @Test
    void listarTodosMantemAOrdemDeCadastro() {
        repository.salvar(bruno);
        repository.salvar(ana);
        List<Funcionario> lista = repository.listarTodos();
        assertEquals(2, lista.size());
        assertSame(bruno, lista.get(0));
        assertSame(ana, lista.get(1));
    }

    @Test
    void listaRetornadaEUmaCopia() {
        repository.salvar(ana);
        repository.listarTodos().clear();
        assertEquals(1, repository.quantidade());
    }

    @Test
    void removeFuncionarioExistente() {
        repository.salvar(ana);
        assertTrue(repository.remover(1));
        assertEquals(0, repository.quantidade());
    }

    @Test
    void removerInexistenteRetornaFalso() {
        assertFalse(repository.remover(99));
    }

    @Test
    void buscaPorNomeExataIgnoraMaiusculas() {
        repository.salvar(ana);
        repository.salvar(bruno);
        assertEquals(1, repository.buscarPorNome("ana silva", true).size());
        assertTrue(repository.buscarPorNome("Ana", true).isEmpty());
    }

    @Test
    void buscaPorNomeParcialEncontraPorTrecho() {
        repository.salvar(ana);
        repository.salvar(bruno);
        assertEquals(1, repository.buscarPorNome("sil", false).size());
        assertEquals(2, repository.buscarPorNome("a", false).size());
    }

    @Test
    void buscaPorNomeNuloRetornaListaVazia() {
        assertTrue(repository.buscarPorNome(null, false).isEmpty());
    }

    @Test
    void salvarNuloLancaExcecao() {
        assertThrows(DadoInvalidoException.class, () -> repository.salvar(null));
    }

    @Test
    void salvarComMesmoCodigoSubstituiOAnterior() {
        repository.salvar(ana);
        Funcionario outra = new FuncionarioCLT(1, "Outra Pessoa", "390.533.447-05", 1000.0, 0.0);
        repository.salvar(outra);
        assertEquals(1, repository.quantidade());
        assertSame(outra, repository.buscarPorCodigo(1));
    }
}
