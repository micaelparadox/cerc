# 0001 - Response de Sucesso Parcial

## Context

Endpoint da TAG suporta envio de lista de estabelecimentos, mas não suporta status code de retorno “207: Multi-Status”, como a CERC. Apenas 400 e 200.

## Decisão

Nesse caso, caso o sucesso seja parcial nós retornaremos 200, com o corpo de resposta contendo apenas os estabelecimentos que tiveram sucesso.
Retornaremos status 400 com os erros somente em caso de erros presentes em todos os itens do batch

## Justificativa

Adequar o formato de processamento parcial com o layout fornecido pela TAG

### Benefícios

Possibilidade de processar o batch parcialmente, sem necessidade de rollback da transação caso algum dos itens do batch falhe
