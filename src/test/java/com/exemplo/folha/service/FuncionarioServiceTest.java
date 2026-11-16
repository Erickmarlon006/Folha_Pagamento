package com.exemplo.folha.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.exemplo.folha.exception.DadoInvalidoException;
import com.exemplo.folha.exception.FuncionarioJaCadastradoException;
import com.exemplo.folha.exception.FuncionarioNaoEncontradoException;
import com.exemplo.folha.model.Funcionario;
import com.exemplo.folha.model.FuncionarioCLT;
import com.exemplo.folha.model.FuncionarioComissionado;
import com.exemplo.folha.model.FuncionarioPJ;
import com.exemplo.folha.repository.FuncionarioRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FuncionarioServiceTest {

    private FuncionarioService service;
    private Funcionario ana;
    private Funcionario bruno;
    private Funcionario carla;

    @BeforeEach
    void preparar() {
        service = new FuncionarioService(new FuncionarioRepository());
        ana = new FuncionarioCLT(1, "Ana Silva", "529.982.247-25", 3000.0, 500.0);
        bruno = new FuncionarioPJ(2, "Bruno Costa", "111.444.777-35", 1500.0, 4);
        carla = new FuncionarioComissionado(3, "Carla Dias", "390.533.447-05", 2000.0, 10.0, 10000.0);
    }

    private void cadastrarTodos() throws Exception {
        service.cadastrar(ana);
        service.cadastrar(bruno);
        service.cadastrar(carla);
    }

    // ----- construtor -----

    @Test
    void repositorioNuloLancaExcecao() {
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioService(null));
    }

    // ----- cadastro -----

    @Test
    void cadastraEListaFuncionarios() throws Exception {
        cadastrarTodos();
        List<Funcionario> lista = service.listar();
        assertEquals(3, lista.size());
        assertSame(ana, lista.get(0));
    }

    @Test
    void cadastroComCodigoDuplicadoLancaExcecao() throws Exception {
        service.cadastrar(ana);
        Funcionario mesmoCodigo = new FuncionarioPJ(1, "Outro", "111.444.777-35", 100.0, 1);
        FuncionarioJaCadastradoException e = assertThrows(FuncionarioJaCadastradoException.class,
                () -> service.cadastrar(mesmoCodigo));
        assertTrue(e.getMessage().contains("código 1"));
        assertEquals(1, service.listar().size());
    }

    @Test
    void cadastroComCpfDuplicadoLancaExcecao() throws Exception {
        service.cadastrar(ana);
        Funcionario mesmoCpf = new FuncionarioPJ(9, "Outro", "529.982.247-25", 100.0, 1);
        assertThrows(FuncionarioJaCadastradoException.class, () -> service.cadastrar(mesmoCpf));
    }

    @Test
    void cadastroDeFuncionarioNuloLancaExcecao() {
        assertThrows(DadoInvalidoException.class, () -> service.cadastrar(null));
    }

    // ----- busca (overload) -----

    @Test
    void buscaPorCodigo() throws Exception {
        cadastrarTodos();
        assertSame(bruno, service.buscarFuncionario(2));
    }

    @Test
    void buscaPorCodigoInexistenteLancaExcecao() {
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.buscarFuncionario(99));
    }

    @Test
    void buscaPorCpf() throws Exception {
        cadastrarTodos();
        assertSame(carla, service.buscarFuncionario("390.533.447-05"));
        assertSame(carla, service.buscarFuncionario("39053344705"));
    }

    @Test
    void buscaPorCpfInexistenteLancaExcecao() {
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.buscarFuncionario("168.995.350-09"));
    }

    @Test
    void buscaPorCpfVazioLancaDadoInvalido() {
        assertThrows(DadoInvalidoException.class, () -> service.buscarFuncionario(" "));
        assertThrows(DadoInvalidoException.class, () -> service.buscarFuncionario((String) null));
    }

    @Test
    void buscaPorNomeExata() throws Exception {
        cadastrarTodos();
        List<Funcionario> encontrados = service.buscarFuncionario("ana silva", true);
        assertEquals(1, encontrados.size());
        assertSame(ana, encontrados.get(0));
    }

    @Test
    void buscaPorNomeParcial() throws Exception {
        cadastrarTodos();
        assertEquals(1, service.buscarFuncionario("costa", false).size());
    }

    @Test
    void buscaPorNomeExataNaoEncontraTrecho() throws Exception {
        cadastrarTodos();
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.buscarFuncionario("Ana", true));
    }

    @Test
    void buscaPorNomeSemResultadoLancaExcecao() {
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.buscarFuncionario("Zé", false));
    }

    @Test
    void buscaPorNomeVazioLancaDadoInvalido() {
        assertThrows(DadoInvalidoException.class, () -> service.buscarFuncionario("", false));
    }

    // ----- remoção -----

    @Test
    void removeFuncionarioExistente() throws Exception {
        cadastrarTodos();
        service.remover(2);
        assertEquals(2, service.listar().size());
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.buscarFuncionario(2));
    }

    @Test
    void removerInexistenteLancaExcecao() {
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.remover(99));
    }

    // ----- remuneração, dados, relatório -----

    @Test
    void calculaRemuneracaoDeCadaCategoria() throws Exception {
        cadastrarTodos();
        assertEquals(3500.0, service.calcularRemuneracao(1), 0.001);
        assertEquals(6000.0, service.calcularRemuneracao(2), 0.001);
        assertEquals(3000.0, service.calcularRemuneracao(3), 0.001);
    }

    @Test
    void calcularRemuneracaoDeInexistenteLancaExcecao() {
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.calcularRemuneracao(99));
    }

    @Test
    void exibeDadosCompletosDoFuncionario() throws Exception {
        cadastrarTodos();
        String dados = service.exibirDados(1);
        assertTrue(dados.contains("Ana Silva"));
        assertTrue(dados.contains("Remuneração bruta: R$ 3.500,00"));
    }

    @Test
    void exibirDadosDeInexistenteLancaExcecao() {
        assertThrows(FuncionarioNaoEncontradoException.class, () -> service.exibirDados(99));
    }

    @Test
    void totalDaFolhaEhASomaDasRemuneracoesBrutas() throws Exception {
        cadastrarTodos();
        assertEquals(12500.0, service.calcularTotalFolha(), 0.001);
    }

    @Test
    void totalDaFolhaVaziaEhZero() {
        assertEquals(0.0, service.calcularTotalFolha(), 0.001);
    }

    @Test
    void totalDaFolhaAtualizaAposRemocao() throws Exception {
        cadastrarTodos();
        service.remover(2);
        assertEquals(6500.0, service.calcularTotalFolha(), 0.001);
    }

    @Test
    void relatorioListaTodosOsFuncionariosETotal() throws Exception {
        cadastrarTodos();
        String relatorio = service.gerarRelatorio();
        assertTrue(relatorio.contains("Ana Silva"));
        assertTrue(relatorio.contains("Bruno Costa"));
        assertTrue(relatorio.contains("Carla Dias"));
        assertTrue(relatorio.contains("R$ 3.500,00"));
        assertTrue(relatorio.contains("R$ 6.000,00"));
        assertTrue(relatorio.contains("TOTAL DA FOLHA (bruto): R$ 12.500,00"));
    }

    @Test
    void relatorioSemFuncionariosInformaQueEstaVazio() {
        String relatorio = service.gerarRelatorio();
        assertTrue(relatorio.contains("Nenhum funcionário cadastrado."));
        assertTrue(relatorio.contains("R$ 0,00"));
    }
}
