# 0002 - Campos obrigatórios para a CERC

## Context

O layout TAG define somente os campos de domicílio bancário como obrigátorios, porém, sem dados adicioneis o comportamento
de atualização de estabelecimentos não é possível pois não há como identificar o estabelecimento a ser atualizado.

## Decisão

Nesse caso, vamos definir os campos documentNumber e documentType como obrigatórios para a CERC.

## Justificativa

A decisão foi tomada para garantir a integridade dos dados e a possibilidade de atualização de estabelecimentos.

### Benefícios

Possibilidade de atualização de estabelecimentos
