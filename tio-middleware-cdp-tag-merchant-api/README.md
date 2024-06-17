

# tio-middleware-cdp-tag-merchant-api

## Getting Started

### Rode a aplicação com seu profile correto (**local, k8s, default**)

**NOTE** 
*** caso precise migrar um projeto Maven para Gradle, seguir a documentação: https://cerc-portal.atlassian.net/wiki/spaces/AR/pages/366903629/Chassis+de+Servi+os#application-monitor ***

Use um profile do spring para iniciar a aplicação.

	mvn spring-boot:run -Dspring-boot.run.profiles=local

​ Como alternativa, você pode rodar seu serviço como um container docker

	mvn clean package spring-boot:repackage
    docker build -t examples/tio-middleware-cdp-tag-merchant-api .
	docker run --env spring.profiles.active=local,docker -p 8080:8080 examples/tio-middleware-cdp-tag-merchant-api

# Open Telemetry

Todos os serviços criados a partir do Chassis utilizam o agente do Open Telemetry que, ao ser executado, coleta métricas e exporta automaticamente para o New Relic da CERC.

O agente é um arquivo .jar localizado no diretório ***/open-telemetry/opentelemetry-javaagent.jar***

Para rodar a aplicação utilizando o agente do Open Telemetry, utilizamos o seguinte comando

     mvn spring-boot:run \
    -Dspring-boot.run.profiles=local \
    -Dspring-boot.run.jvmArguments=-javaagent:"./open-telemetry/opentelemetry-javaagent.jar \
    -Dotel.exporter.otlp.headers=api-key=$(NEW_RELIC_KEY) \
    -Dotel.exporter.otlp.endpoint=https://otlp.nr-data.net"

**IMPORTANT**

A propriede ***-Dotel.exporter.otlp.headers=api-key=$(NEW_RELIC_KEY)*** é onde especificamos a key do New Relic. O exemplo acima
é realizado com a chave do New Relic do ambiente de staging.

# Ambientes de Execução

As configurações de ambiente de execução são realizadas atravės de arquivos de configuração **.yml**, seguindo uma padronização de nomes que o Spring entende e executa automaticamente baseado na estrutura **application-[profile].yml**.
Esses arquivos **.yml** ficam localizados no diretório ***/src/main/resources***.

Por padrão, a aplicação criada a partir do chassis vem com o profile **"local"** como default, e suas variáveis de ambiente já inseridas, deixando o serviço pronto para executar.

Caso haja necessidade de rodarmos a aplicação apontando para um ambiente diferente do **"local"**, precisaremos antes de tudo, setar as variáveis de ambiente do projeto. Os valores dessas variáveis podem ser encontrados dentro do diretório **/charts/environments/{ABREVIAÇÃO_DO_PROFILE}/values.yml**
Com os valores setados corretamente, podemos rodar o comando de execução do projeto apontando para o ambiente desejado:

	mvn spring-boot:run -Dspring-boot.run.profiles={profile}

---
**IMPORTANT**

Sempre que precisar criar alguma nova propriedade nos arquivos de configuração (**/src/main/application-***), antes de realizar o deploy da aplicação, esta propriedade também deve ser criada nos arquivos **values.yaml** de cada ambiente.

---

### Teste a aplicação 

Por motivos de segurança, todos os nossos microsserviços usam **OAuth 2.0** (protocolo padrão para autorização). Isso significa que **toda requisição deve ser autorizada** e precisamos de um token de acesso.

Estamos utilizando o fluxo *_Client Credentials_*, no qual é um fluxo OAuth utlizado por aplicações clientes para obter um Access Token fora do contexto de um usuário.

Para obter o token em ambiente de staging execute a requisição:

    export ACCESS_TOKEN=$(curl -L -X POST 'https://api.stg.cerc.com/oauth2/token' \
    -H 'Content-Type: application/x-www-form-urlencoded' \
    -H 'Authorization: Basic hash64(user:password)' \
    -d 'grant_type=client_credentials' | jq ".access_token" | xargs)

---
**NOTE**

para executar o curl acima é necessário o processador JSON de linha de comando *jq*:

    brew install jq

---
O Basic Authorization token é uma combinação do *client_id* e *client_secret* do app cadastrado no Apigee X codificado em Base64.

Para testar o endpoint de exemplo HelloWorld:

    curl -L -X GET 'localhost:8080/' \
    -H "Authorization: Bearer $ACCESS_TOKEN"

Para testar o endpoint de exemplo GoodBye:

    curl -L -X GET 'localhost:8080/goodbye' \
    -H "Authorization: Bearer $ACCESS_TOKEN"

---

# Deploy da Aplicação

O processo de deploy está totalmente integrado com os padrões CERC. Ao realizar um push na branch main, a pipeline de Continuous Integration (CI)
presente no AzureDevOps será disparada e todos os passos rodados automaticamente.

Após todos os passos da esteira de Continuous Integration terem finalizado, é necessário criar uma nova release na pipeline de Continuous Delivery (CD), apontando para qual ambiente será
implantada a aplicação. 

O processo detalhado de como as esteiras de CI e CD funcionam se encontram disponíveis em nosso confluence: https://cerc-portal.atlassian.net/wiki/spaces/CINFRA/pages/380076035/Como+configurar+um+CI+CD+de+um+projeto+no+Azure+Devops

---

# Informativo da Aplicação

Utilizamos o Spring Actuator para monitoramento da aplicação. Ele nos disponibiliza alguns endpoints que podemos utilizar para coletar informaçōes sobre nossa aplicação.

Para entender como funciona o processo informativo desta aplicação, acesse o link: https://cerc-portal.atlassian.net/wiki/spaces/AR/pages/366903629/Chassis+de+Servi+os#application-monitor

---