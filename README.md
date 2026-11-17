# Sistema de Gerenciamento de Funcionários e Folha de Pagamento

Atividade avaliativa de Programação Orientada a Objetos em Java. O sistema cadastra funcionários de três categorias
(CLT, PJ e Comissionado), calcula a remuneração de cada um, gera relatório e totaliza a folha de pagamento.

## Tecnologias

- Java 17 ou superior
- Maven 3.8+
- JUnit 5 (testes automatizados)

## Estrutura do projeto

```
folha-pagamento/
├── pom.xml
└── src/
    ├── main/java/com/exemplo/folha/
    │   ├── Main.java
    │   ├── model/        Funcionario (abstrata), FuncionarioCLT, FuncionarioPJ,
    │   │                 FuncionarioComissionado, Tributavel, Identificavel
    │   ├── repository/   FuncionarioRepository (armazenamento em memória)
    │   ├── service/      FuncionarioService (regras de negócio)
    │   ├── exception/    FuncionarioNaoEncontradoException, FuncionarioJaCadastradoException,
    │   │                 DadoInvalidoException
    │   ├── view/         FuncionarioView (menu no console)
    │   └── util/         CpfValidador, FormatadorMoeda
    └── test/java/com/exemplo/folha/   (uma classe de teste para cada classe de produção)
```

## Como executar

Na pasta do projeto (onde está o `pom.xml`):

```
mvn clean compile
mvn clean package
java -jar target/folha-pagamento-1.0.0.jar
```

Se preferir não gerar o jar:

```
java -cp target/classes com.exemplo.folha.Main
```

## Como executar os testes

```
mvn test
```

O relatório de cada classe de teste fica em `target/surefire-reports/`.

## Regras de remuneração

| Categoria | Remuneração bruta | Desconto (Tributavel) |
|---|---|---|
| CLT | salário-base + benefícios | 11% da remuneração bruta |
| PJ | valor por projeto × quantidade de projetos | não se aplica (o PJ recolhe os próprios impostos) |
| Comissionado | salário-base + (total de vendas × percentual / 100) | 11% da remuneração bruta |

Observações:

- O percentual de comissão é informado de 0 a 100.
- O desconto de 11% é uma regra simplificada, definida apenas para fins didáticos.
- O **total da folha** é a soma das remunerações **brutas** de todos os funcionários.
- O relatório mostra também a remuneração líquida de cada funcionário.

## Conceitos de POO aplicados

- **Encapsulamento**: atributos privados, validações em construtores e setters.
- **Herança e abstração**: `Funcionario` é abstrata, com o método abstrato `calcularRemuneracao()`.
- **Interfaces**: `Identificavel` (todos os funcionários) e `Tributavel` (CLT e Comissionado).
- **Sobreposição (override)**: cada subclasse implementa a sua regra de cálculo.
- **Sobrecarga (overload)**: `FuncionarioService.buscarFuncionario(int)`, `buscarFuncionario(String cpf)` e
  `buscarFuncionario(String nome, boolean buscaExata)`.
- **Exceções personalizadas**: lançadas na model/service e tratadas na view.

## Menu do sistema

```
1  - Cadastrar funcionário      6  - Exibir dados completos
2  - Listar funcionários        7  - Calcular remuneração
3  - Buscar por código          8  - Remover funcionário
4  - Buscar por CPF             9  - Gerar relatório
5  - Buscar por nome            10 - Total da folha
0  - Sair
```

Ao digitar valores decimais, use vírgula ou ponto (ex.: `1500,50`), sem separador de milhar.

## Acentos no terminal

Se os acentos aparecerem trocados por `?` ou símbolos estranhos, execute com:

```
java -Dstdout.encoding=UTF-8 -jar target/folha-pagamento-1.0.0.jar
```

No Windows (cmd/PowerShell), rode antes `chcp 65001`.
