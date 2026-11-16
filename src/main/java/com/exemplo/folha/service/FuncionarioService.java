package com.exemplo.folha.service;

import com.exemplo.folha.exception.DadoInvalidoException;
import com.exemplo.folha.exception.FuncionarioJaCadastradoException;
import com.exemplo.folha.exception.FuncionarioNaoEncontradoException;
import com.exemplo.folha.model.Funcionario;
import com.exemplo.folha.repository.FuncionarioRepository;
import com.exemplo.folha.util.CpfValidador;
import com.exemplo.folha.util.FormatadorMoeda;

import java.util.List;

/**
 * Regras de negócio do sistema: cadastro, consultas, remoção, remuneração e folha.
 */
public class FuncionarioService {

    private final FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        if (repository == null) {
            throw new DadoInvalidoException("O repositório é obrigatório.");
        }
        this.repository = repository;
    }

    /**
     * Cadastra o funcionário.
     *
     * @throws FuncionarioJaCadastradoException se já existir o mesmo código ou o mesmo CPF
     */
    public void cadastrar(Funcionario funcionario) throws FuncionarioJaCadastradoException {
        if (funcionario == null) {
            throw new DadoInvalidoException("O funcionário é obrigatório.");
        }
        if (repository.existePorCodigo(funcionario.getCodigo())) {
            throw new FuncionarioJaCadastradoException(
                    "Já existe um funcionário com o código " + funcionario.getCodigo() + ".");
        }
        if (repository.buscarPorCpf(funcionario.getCpf()) != null) {
            throw new FuncionarioJaCadastradoException(
                    "Já existe um funcionário com o CPF " + funcionario.getCpfFormatado() + ".");
        }
        repository.salvar(funcionario);
    }

    public List<Funcionario> listar() {
        return repository.listarTodos();
    }

    // ----- Sobrecarga (overload) de buscarFuncionario -----

    public Funcionario buscarFuncionario(int codigo) throws FuncionarioNaoEncontradoException {
        Funcionario funcionario = repository.buscarPorCodigo(codigo);
        if (funcionario == null) {
            throw new FuncionarioNaoEncontradoException(
                    "Funcionário não encontrado com o código " + codigo + ".");
        }
        return funcionario;
    }

    public Funcionario buscarFuncionario(String cpf) throws FuncionarioNaoEncontradoException {
        if (cpf == null || cpf.isBlank()) {
            throw new DadoInvalidoException("O CPF para busca é obrigatório.");
        }
        Funcionario funcionario = repository.buscarPorCpf(cpf);
        if (funcionario == null) {
            throw new FuncionarioNaoEncontradoException(
                    "Funcionário não encontrado com o CPF " + CpfValidador.formatar(cpf) + ".");
        }
        return funcionario;
    }

    public List<Funcionario> buscarFuncionario(String nome, boolean buscaExata)
            throws FuncionarioNaoEncontradoException {
        if (nome == null || nome.isBlank()) {
            throw new DadoInvalidoException("O nome para busca é obrigatório.");
        }
        List<Funcionario> encontrados = repository.buscarPorNome(nome, buscaExata);
        if (encontrados.isEmpty()) {
            throw new FuncionarioNaoEncontradoException(
                    "Nenhum funcionário encontrado com o nome \"" + nome + "\".");
        }
        return encontrados;
    }

    public void remover(int codigo) throws FuncionarioNaoEncontradoException {
        boolean removido = repository.remover(codigo);
        if (!removido) {
            throw new FuncionarioNaoEncontradoException(
                    "Não é possível remover: funcionário com o código " + codigo + " não existe.");
        }
    }

    public double calcularRemuneracao(int codigo) throws FuncionarioNaoEncontradoException {
        return buscarFuncionario(codigo).calcularRemuneracao();
    }

    public String exibirDados(int codigo) throws FuncionarioNaoEncontradoException {
        return buscarFuncionario(codigo).exibirDados();
    }

    /** Total da folha = soma das remunerações brutas de todos os funcionários. */
    public double calcularTotalFolha() {
        double total = 0.0;
        for (Funcionario f : repository.listarTodos()) {
            total += f.calcularRemuneracao();
        }
        return total;
    }

    /** Relatório com todos os funcionários, suas remunerações e o total da folha. */
    public String gerarRelatorio() {
        List<Funcionario> lista = repository.listarTodos();
        StringBuilder sb = new StringBuilder();
        sb.append("===== RELATÓRIO DE FUNCIONÁRIOS =====\n");
        if (lista.isEmpty()) {
            sb.append("Nenhum funcionário cadastrado.\n");
        } else {
            sb.append(String.format("%-8s %-14s %-25s %16s %16s\n",
                    "Código", "Tipo", "Nome", "Bruta", "Líquida"));
            for (Funcionario f : lista) {
                sb.append(String.format("%-8d %-14s %-25.25s %16s %16s\n",
                        f.getCodigo(), f.getTipo(), f.getNome(),
                        FormatadorMoeda.formatar(f.calcularRemuneracao()),
                        FormatadorMoeda.formatar(f.calcularRemuneracaoLiquida())));
            }
        }
        sb.append("TOTAL DA FOLHA (bruto): ").append(FormatadorMoeda.formatar(calcularTotalFolha()));
        return sb.toString();
    }
}
