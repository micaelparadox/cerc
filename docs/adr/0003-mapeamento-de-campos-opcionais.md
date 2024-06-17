# 0003 - Mapeamento de campos opcionais

## Context

O layout TAG permite alguns campos não obrigatórios. A decisão tomada aqui foi de como mapeá-los aos serviços que devem recebê-los.

## Decisão

No caso de não envio de campos opcionais, deveremos enviar como nulos para o bookkeping ao invés de realizar qualquer mapeamento.

## Justificativa

A decisão foi tomada para garantir a integridade dos dados e retrato fiel da entrada.

### Benefícios

Retrato fiel da entrada dos dados
