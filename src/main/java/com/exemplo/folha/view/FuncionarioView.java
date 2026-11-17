package com.exemplo.folha.view;

import com.exemplo.folha.exception.DadoInvalidoException;
import com.exemplo.folha.exception.FuncionarioJaCadastradoException;
import com.exemplo.folha.exception.FuncionarioNaoEncontradoException;
import com.exemplo.folha.model.Funcionario;
import com.exemplo.folha.model.FuncionarioCLT;
import com.exemplo.folha.model.FuncionarioComissionado;
import com.exemplo.folha.model.FuncionarioPJ;
import com.exemplo.folha.service.FuncionarioService;
import com.exemplo.folha.util.FormatadorMoeda;

import java.io.PrintStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Interface de texto (menu no console). Recebe o Scanner e o PrintStream
 * por construtor, o que permite testá-la sem teclado e sem tela.
 */
public class FuncionarioView {

    private final FuncionarioService service;
    private final Scanner entrada;
    private final PrintStream saida;

    public FuncionarioView(FuncionarioService service, Scanner entrada, PrintStream saida) {
        if (service == null || entrada == null || saida == null) {
            throw new DadoInvalidoException("Serviço, entrada e saída são obrigatórios.");
        }
        this.service = service;
        this.entrada = entrada;
        this.saida = saida;
    }

    /** Executa o menu até o usuário escolher "0 - Sair" ou a entrada acabar. */
    public void iniciar() {
        boolean continuar = true;
        try {
            while (continuar) {
                exibirMenu();
                String opcao = lerTexto("Escolha uma opção");
                try {
                    continuar = processar(opcao);
                } catch (FuncionarioNaoEncontradoException e) {
                    saida.println("Erro: " + e.getMessage());
                } catch (FuncionarioJaCadastradoException e) {
                    saida.println("Erro: " + e.getMessage());
                } catch (DadoInvalidoException e) {
                    saida.println("Erro: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            saida.println("Entrada encerrada.");
        }
        saida.println("Sistema encerrado.");
    }

    private void exibirMenu() {
        saida.println();
        saida.println("===== FOLHA DE PAGAMENTO =====");
        saida.println("1  - Cadastrar funcionário");
        saida.println("2  - Listar funcionários");
        saida.println("3  - Buscar por código");
        saida.println("4  - Buscar por CPF");
        saida.println("5  - Buscar por nome");
        saida.println("6  - Exibir dados completos");
        saida.println("7  - Calcular remuneração");
        saida.println("8  - Remover funcionário");
        saida.println("9  - Gerar relatório");
        saida.println("10 - Total da folha");
        saida.println("0  - Sair");
    }

    /** @return false quando o usuário pediu para sair */
    private boolean processar(String opcao) throws FuncionarioNaoEncontradoException,
            FuncionarioJaCadastradoException {
        switch (opcao) {
            case "1":
                cadastrar();
                break;
            case "2":
                listar();
                break;
            case "3":
                saida.println(service.buscarFuncionario(lerInt("Código")));
                break;
            case "4":
                saida.println(service.buscarFuncionario(lerTexto("CPF")));
                break;
            case "5":
                buscarPorNome();
                break;
            case "6":
                saida.println(service.exibirDados(lerInt("Código")));
                break;
            case "7":
                double remuneracao = service.calcularRemuneracao(lerInt("Código"));
                saida.println("Remuneração: " + FormatadorMoeda.formatar(remuneracao));
                break;
            case "8":
                service.remover(lerInt("Código"));
                saida.println("Funcionário removido com sucesso.");
                break;
            case "9":
                saida.println(service.gerarRelatorio());
                break;
            case "10":
                saida.println("Total da folha: " + FormatadorMoeda.formatar(service.calcularTotalFolha()));
                break;
            case "0":
                return false;
            default:
                saida.println("Opção inválida.");
        }
        return true;
    }

    private void cadastrar() throws FuncionarioJaCadastradoException {
        int tipo = lerInt("Tipo (1-CLT, 2-PJ, 3-Comissionado)");
        int codigo = lerInt("Código");
        String nome = lerTexto("Nome");
        String cpf = lerTexto("CPF");

        Funcionario funcionario;
        if (tipo == 1) {
            double salario = lerDouble("Salário-base");
            double beneficios = lerDouble("Benefícios");
            funcionario = new FuncionarioCLT(codigo, nome, cpf, salario, beneficios);
        } else if (tipo == 2) {
            double valor = lerDouble("Valor por projeto");
            int quantidade = lerInt("Quantidade de projetos");
            funcionario = new FuncionarioPJ(codigo, nome, cpf, valor, quantidade);
        } else if (tipo == 3) {
            double salario = lerDouble("Salário-base");
            double percentual = lerDouble("Percentual de comissão (0 a 100)");
            double vendas = lerDouble("Total de vendas");
            funcionario = new FuncionarioComissionado(codigo, nome, cpf, salario, percentual, vendas);
        } else {
            throw new DadoInvalidoException("Tipo de funcionário inválido: " + tipo);
        }
        service.cadastrar(funcionario);
        saida.println("Funcionário cadastrado com sucesso.");
    }

    private void listar() {
        List<Funcionario> lista = service.listar();
        if (lista.isEmpty()) {
            saida.println("Nenhum funcionário cadastrado.");
            return;
        }
        for (Funcionario f : lista) {
            saida.println(f);
        }
    }

    private void buscarPorNome() throws FuncionarioNaoEncontradoException {
        String nome = lerTexto("Nome");
        boolean exata = lerTexto("Busca exata? (s/n)").equalsIgnoreCase("s");
        List<Funcionario> encontrados = service.buscarFuncionario(nome, exata);
        for (Funcionario f : encontrados) {
            saida.println(f);
        }
    }

    // ----- leitura de dados -----

    private String lerTexto(String rotulo) {
        saida.print(rotulo + ": ");
        return entrada.nextLine().trim();
    }

    private int lerInt(String rotulo) {
        String texto = lerTexto(rotulo);
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new DadoInvalidoException("Valor inteiro inválido: \"" + texto + "\".");
        }
    }

    /** Aceita vírgula ou ponto como separador decimal (sem separador de milhar). */
    private double lerDouble(String rotulo) {
        String texto = lerTexto(rotulo);
        try {
            return Double.parseDouble(texto.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new DadoInvalidoException("Valor numérico inválido: \"" + texto + "\".");
        }
    }
}
