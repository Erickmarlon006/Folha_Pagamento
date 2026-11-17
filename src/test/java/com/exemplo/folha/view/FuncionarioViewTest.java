package com.exemplo.folha.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.exemplo.folha.exception.DadoInvalidoException;
import com.exemplo.folha.repository.FuncionarioRepository;
import com.exemplo.folha.service.FuncionarioService;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FuncionarioViewTest {

    private FuncionarioService service;
    private ByteArrayOutputStream bytes;

    @BeforeEach
    void preparar() {
        service = new FuncionarioService(new FuncionarioRepository());
        bytes = new ByteArrayOutputStream();
    }

    /** Executa a view com as linhas informadas (uma por Enter) e devolve tudo o que foi impresso. */
    private String executar(String... linhas) {
        String entrada = String.join("\n", linhas) + "\n";
        Scanner scanner = new Scanner(entrada);
        PrintStream saida = new PrintStream(bytes, true, StandardCharsets.UTF_8);
        new FuncionarioView(service, scanner, saida).iniciar();
        return bytes.toString(StandardCharsets.UTF_8);
    }

    @Test
    void construtorExigeTodasAsDependencias() {
        PrintStream saida = new PrintStream(bytes);
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioView(null, new Scanner("x"), saida));
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioView(service, null, saida));
        assertThrows(DadoInvalidoException.class, () -> new FuncionarioView(service, new Scanner("x"), null));
    }

    @Test
    void sairEncerraOSistema() {
        String saida = executar("0");
        assertTrue(saida.contains("FOLHA DE PAGAMENTO"));
        assertTrue(saida.contains("Sistema encerrado."));
    }

    @Test
    void opcaoInvalidaEInformada() {
        String saida = executar("99", "0");
        assertTrue(saida.contains("Opção inválida."));
    }

    @Test
    void entradaQueAcabaEncerraSemQuebrar() {
        String saida = executar();
        assertTrue(saida.contains("Entrada encerrada."));
    }

    @Test
    void cadastraFuncionarioCLTEListaOsCadastrados() {
        String saida = executar(
                "1", "1", "1", "Ana Silva", "529.982.247-25", "3000", "500",
                "2",
                "0");
        assertTrue(saida.contains("Funcionário cadastrado com sucesso."));
        assertTrue(saida.contains("[1] CLT - Ana Silva"));
    }

    @Test
    void cadastraFuncionarioPJ() {
        String saida = executar(
                "1", "2", "2", "Bruno Costa", "111.444.777-35", "1500", "4",
                "7", "2",
                "0");
        assertTrue(saida.contains("Funcionário cadastrado com sucesso."));
        assertTrue(saida.contains("Remuneração: R$ 6.000,00"));
    }

    @Test
    void cadastraFuncionarioComissionadoAceitandoVirgulaDecimal() {
        String saida = executar(
                "1", "3", "3", "Carla Dias", "390.533.447-05", "2000", "10,5", "10000",
                "6", "3",
                "0");
        assertTrue(saida.contains("Funcionário cadastrado com sucesso."));
        assertTrue(saida.contains("Percentual de comissão: 10,50%"));
    }

    @Test
    void cpfInvalidoMostraMensagemDeErroENaoCadastra() {
        String saida = executar(
                "1", "1", "1", "Ana", "111.111.111-11", "3000", "0",
                "2",
                "0");
        assertTrue(saida.contains("Erro: CPF inválido"));
        assertTrue(saida.contains("Nenhum funcionário cadastrado."));
    }

    @Test
    void codigoDuplicadoMostraMensagemDeErro() {
        String saida = executar(
                "1", "1", "1", "Ana", "529.982.247-25", "3000", "0",
                "1", "1", "1", "Outra", "111.444.777-35", "1000", "0",
                "0");
        assertTrue(saida.contains("Erro: Já existe um funcionário com o código 1."));
    }

    @Test
    void valorNumericoInvalidoMostraMensagemDeErro() {
        String saida = executar(
                "1", "1", "1", "Ana", "529.982.247-25", "abc",
                "0");
        assertTrue(saida.contains("Erro: Valor numérico inválido"));
    }

    @Test
    void tipoDeFuncionarioInvalidoMostraMensagemDeErro() {
        String saida = executar("1", "9", "1", "Ana", "529.982.247-25", "0");
        assertTrue(saida.contains("Erro: Tipo de funcionário inválido"));
    }

    @Test
    void buscaPorCodigoCpfENome() {
        String saida = executar(
                "1", "1", "1", "Ana Silva", "529.982.247-25", "3000", "500",
                "3", "1",
                "4", "529.982.247-25",
                "5", "silva", "n",
                "0");
        int ocorrencias = saida.split("\\[1\\] CLT - Ana Silva", -1).length - 1;
        assertEquals(3, ocorrencias);
    }

    @Test
    void buscaDeFuncionarioInexistenteMostraMensagemDeErro() {
        String saida = executar("3", "99", "0");
        assertTrue(saida.contains("Erro: Funcionário não encontrado com o código 99."));
    }

    @Test
    void removeFuncionario() {
        String saida = executar(
                "1", "1", "1", "Ana Silva", "529.982.247-25", "3000", "500",
                "8", "1",
                "2",
                "0");
        assertTrue(saida.contains("Funcionário removido com sucesso."));
        assertTrue(saida.contains("Nenhum funcionário cadastrado."));
    }

    @Test
    void removerInexistenteMostraMensagemDeErro() {
        String saida = executar("8", "42", "0");
        assertTrue(saida.contains("Erro: Não é possível remover"));
    }

    @Test
    void geraRelatorioEMostraTotalDaFolha() {
        String saida = executar(
                "1", "1", "1", "Ana Silva", "529.982.247-25", "3000", "500",
                "9",
                "10",
                "0");
        assertTrue(saida.contains("RELATÓRIO DE FUNCIONÁRIOS"));
        assertTrue(saida.contains("Total da folha: R$ 3.500,00"));
    }
}
