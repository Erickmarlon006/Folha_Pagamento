package com.exemplo.folha;

import com.exemplo.folha.repository.FuncionarioRepository;
import com.exemplo.folha.service.FuncionarioService;
import com.exemplo.folha.view.FuncionarioView;

import java.util.Scanner;

/**
 * Ponto de entrada: monta as camadas (repository -> service -> view) e inicia o menu.
 */
public class Main {

    public static void main(String[] args) {
        FuncionarioRepository repository = new FuncionarioRepository();
        FuncionarioService service = new FuncionarioService(repository);
        Scanner scanner = new Scanner(System.in);

        FuncionarioView view = new FuncionarioView(service, scanner, System.out);
        view.iniciar();

        scanner.close();
    }
}
