package com.exemplo.folha.repository;

import com.exemplo.folha.exception.DadoInvalidoException;
import com.exemplo.folha.model.Funcionario;
import com.exemplo.folha.util.CpfValidador;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Armazena os funcionários em memória, mantendo a ordem de cadastro.
 * Não aplica regras de negócio: duplicidades são verificadas na camada de serviço.
 */
public class FuncionarioRepository {

    private final Map<Integer, Funcionario> funcionarios = new LinkedHashMap<>();

    /** Salva (ou substitui, se o código já existir) o funcionário. */
    public void salvar(Funcionario funcionario) {
        if (funcionario == null) {
            throw new DadoInvalidoException("O funcionário é obrigatório.");
        }
        funcionarios.put(funcionario.getCodigo(), funcionario);
    }

    /** Retorna o funcionário ou null se o código não existir. */
    public Funcionario buscarPorCodigo(int codigo) {
        return funcionarios.get(codigo);
    }

    /** Busca pelo CPF, com ou sem máscara. Retorna null se não encontrar. */
    public Funcionario buscarPorCpf(String cpf) {
        String procurado = CpfValidador.normalizar(cpf);
        for (Funcionario f : funcionarios.values()) {
            if (f.getCpf().equals(procurado)) {
                return f;
            }
        }
        return null;
    }

    /**
     * Busca por nome, ignorando maiúsculas/minúsculas.
     *
     * @param exata se true, o nome deve ser igual; se false, basta conter o texto
     */
    public List<Funcionario> buscarPorNome(String nome, boolean exata) {
        List<Funcionario> encontrados = new ArrayList<>();
        if (nome == null) {
            return encontrados;
        }
        String procurado = nome.trim().toLowerCase();
        for (Funcionario f : funcionarios.values()) {
            String atual = f.getNome().toLowerCase();
            boolean combina;
            if (exata) {
                combina = atual.equals(procurado);
            } else {
                combina = atual.contains(procurado);
            }
            if (combina) {
                encontrados.add(f);
            }
        }
        return encontrados;
    }

    /** Retorna uma cópia da lista, para que o chamador não altere o armazenamento. */
    public List<Funcionario> listarTodos() {
        return new ArrayList<>(funcionarios.values());
    }

    /** @return true se havia um funcionário com o código e ele foi removido */
    public boolean remover(int codigo) {
        return funcionarios.remove(codigo) != null;
    }

    public boolean existePorCodigo(int codigo) {
        return funcionarios.containsKey(codigo);
    }

    public int quantidade() {
        return funcionarios.size();
    }
}
