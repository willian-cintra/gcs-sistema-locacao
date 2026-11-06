# Guia de Contribuição — Tchê Entulhos

Este projeto é mantido pela equipe no fluxo descrito abaixo.

## Fluxo de branches (GitHub Flow)

- A branch `main` é estável; não se faz commit direto nela.
- Para cada tarefa, crie uma branch a partir da `main`.
- Padrão de nome: `tipo/descricao-curta` ou `tipo/issue-NN-descricao`.
  - Exemplos: `feat/validacao-cpf`, `fix/issue-12-datas-locacao`, `docs/atualiza-readme`.
- Ao terminar, abra um Pull Request para a `main`.
- Pelo menos um outro membro revisa e aprova o PR antes do merge.

## Commits semânticos (Conventional Commits)

Formato: `tipo(escopo opcional): descrição no imperativo`

Tipos usados:

| Tipo | Quando usar |
|------|-------------|
| feat | Nova funcionalidade |
| fix | Correção de bug |
| docs | Documentação |
| style | Formatação, sem mudar lógica |
| refactor | Mudança que não altera comportamento |
| test | Testes |
| chore | Manutenção, configs, build |

Para fechar issues automaticamente, use o rodapé: `Closes #NN`.

## Classificação de issues

Toda issue recebe **uma label de severidade** e **uma de prioridade**,
conforme o modelo do Módulo 5.

### Severidade (impacto técnico no sistema)

| Label | Nível | Significado |
|-------|-------|-------------|
| `sev:critico` | Crítico | Trava ou inutiliza o sistema; impede o uso |
| `sev:major` | Major | Recurso principal não atende ao esperado |
| `sev:minor` | Minor | Defeito de impacto pequeno, com contorno |
| `sev:enhancement` | Enhancement | Cosmético: ortografia, alinhamento, texto |

### Prioridade (urgência de correção)

| Label | Nível | Significado |
|-------|-------|-------------|
| `prio:alta` | Alta | Resolver imediatamente; afeta entregas |
| `prio:media` | Média | Resolver na próxima sprint |
| `prio:baixa` | Baixa | Resolver quando houver tempo disponível |

> Severidade e prioridade são independentes: um texto errado na interface
> pode ter severidade baixa (enhancement) e prioridade alta (urgente).