# Tchê Entulhos

Sistema de locação de caçambas desenvolvido como trabalho prático da disciplina.
Arquitetura em 4 camadas: interface (Swing), controladora, negócio e persistência (DAO + JDBC).

## Pré-requisitos
- **JDK 17** (ou superior)
- **Maven 3.8+** (ou use o Maven embutido da IDE)

## Como rodar
```bash
mvn exec:java
```
Na primeira execução o banco `tche_entulhos.db` é criado e populado com dados de exemplo.

## Comandos úteis
```bash
mvn compile      # compila
mvn test         # roda os testes
mvn package      # gera o .jar em target/
```

## Abrir na IDE
- **IntelliJ**: `File → Open` e selecione a pasta (ou o `pom.xml`).
- **Eclipse**: `File → Import → Existing Maven Projects`.
- **VS Code**: abra a pasta (extensão "Extension Pack for Java").

As dependências (FlatLaf, sqlite-jdbc, JUnit) são baixadas automaticamente pelo Maven.

## Estrutura
```
src/main/java/
  Main.java              ponto de entrada (FlatLaf + banco + menu)
  model/                 camada de negócio
  controle/              camada controladora
  dao/                   camada de persistência (Conexao + DAOs)
  view/                  camada de interface (Swing)
src/main/resources/
  schema.sql             criação das tabelas
  seed.sql               dados de exemplo
```

Ver `CLAUDE.md` para o detalhamento do design (classes, métodos, mapeamento O-R, decisões).
