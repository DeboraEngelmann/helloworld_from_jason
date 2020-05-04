# Jason + Chatbots

By [Débora Engelmann](https://github.com/DeboraEngelmann)

Você precisará ter instalado em seu computador:

[Git](https://git-scm.com/downloads), 
[Java SE 8](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html), 
[Eclipse](https://www.eclipse.org/downloads/) - Oxygen ou superior, 
[JaCaMo](http://jacamo.sourceforge.net/eclipseplugin/tutorial/),
[Gradle](https://gradle.org/install/),
[Postman](https://www.postman.com/downloads/),
[Docker](https://www.docker.com/get-started) e 
[SDK GCP](https://cloud.google.com/sdk/docs/quickstarts)

**Todos os comandos deste tutorial foram testados no Ubuntu 20.04, para outros sistemas operacionais e outras distribuições do Ubuntu podem haver variações**

**Use a seguinte lista para navegar para o tutorial correspondente a cada tarefa**

1. [Baixando e configurando projeto de integração](#baixando-e-configurando-projeto-de-integração)
2. [Testando projeto localmente com o Postman](#testando-projeto-localmente-com-o-postman)
3. [Criando imagem Docker](#criando-imagem-docker)
4. [Configurando projeto GCP e subindo aplicação](#configurando-projeto-gcp-e-subindo-aplicação)
5. [Criando intenção e configurando fulfillment no Dialogflow](#criando-intenção-e-configurando-fulfillment-no-dialogflow)

## Baixando e configurando projeto de integração

Abra o `Eclipse` e selecione o menu `File/Import`.

![](src/resources/img/menuImport.png)

Na Caixa de diálogo selecione `Git/Projects from Git` e clique em `Next >`

![](src/resources/img/importFromGit.png)

Selecione a opção `Clone URI` e clique em `Next >` novamente. Em `URI` coloque o link: https://github.com/DeboraEngelmann/helloworld_from_jason.git e clique em `Next >`.

![](src/resources/img/LinkGit.png)

Selecione `master` e clique em `Next >`. Escolha o diretório onde o projeto será salvo e clique em `Next >`.

![](src/resources/img/saveProjectGit.png)

Selecione a opção Import existing Eclipse projects e clique em `Next >`.

![](src/resources/img/saveProjectGit2.png)

Clique em `Finish`.

![](src/resources/img/finishImport.png)

O eclipse pode levar alguns instantes para conseguir sincronizar as dependencias do Gradle, enquanto estiver sincronizando você verá a informação na barra inferior conforme a imagem a seguir.

![](src/resources/img/sincronize.png)


## Testando projeto localmente com o Postman


Abra o projeto no terminal

![](src/resources/img/menuTerminal.png)

Caso prefira, pode abrir outro terminal e navegar até a pasta do projeto.

Rode o comando `gradle build` e logo após, `gradle run`. Isso pode levar algum tempo.

![](src/resources/img/comandos.png)

O `MAS Console` será aberto. Copie o endereço em que o `JaCaMo Rest API` está rodando (caso esteja aparecendo 0.0.0.0:8080 utilize o endereço http://localhost:8080/).

![](src/resources/img/masConsole.png)

Abra o `Postman` selecione o tipo `POST` e cole o endereço do `JaCaMo Rest API` na barra de endereço. 
Na aba `Body` selecione o tipo `raw` e arquivo do tipo `JSON`, e cole o seguinte código no corpo da requisição:

```
{
  "responseId": "d181b9c7-22ae-4bab-bab3-9dfa70310452-5fd6c646",
  "queryResult": {
    "queryText": "Diga oi ao agente Jason",
    "parameters": {
      
    },
    "allRequiredParamsPresent": true,
    "fulfillmentText": "Here will be called the Jason agent!",
    "fulfillmentMessages": [
      {
        "text": {
          "text": [
            "Here will be called the Jason agent!"
          ]
        }
      }
    ],
    "intent": {
      "name": "projects/integration-example-nymoic/agent/intents/ea529dec-e89f-48aa-bf43-527264379f06",
      "displayName": "Call Jason Agent"
    },
    "intentDetectionConfidence": 1,
    "diagnosticInfo": {
      "webhook_latency_ms": 405
    },
    "languageCode": "en"
  },
  "webhookStatus": {
    "message": "Webhook execution successful"
  }
}
```

Esse código é um exemplo de requisição feita pelo Dialogflow. Vamos verificar se nossa aplicação consegue responde-la.

![](src/resources/img/requisicaoPostman.png)

Verifique a resposta recebida do agente.

![](src/resources/img/responsePostman.png)


## Criando imagem Docker

Para instalar o docker rode os comandos a seguir.

**(Os comandos de instalação a seguir são adequados para o Ubuntu a partir da versão 19)**

```
sudo apt-get update
```

```
apt-cache policy docker-ce
```

```
sudo apt-get install -y docker-ce
```

```
sudo systemctl status docker
```

Rode o comando a seguir para configurar as permissões do Docker para o seu usuário.

```
sudo usermod -a -G docker <YourUser>
```

Navegue até o diretório do projeto e rode o comando a seguir para criar a imagem Docker (caso queira colocar outro nome na imagem altere o `chatbot-integration` no comando).

```
docker build . -t chatbot-integration
```
Para testar a imagem você pode rodar o comando a seguir. 

```
docker run -ti chatbot-integration
```

![](src/resources/img/testeDockerRun.png)

**O erro que aparece no terminal está relacionado á interface gráfica que o JaCaMo procura para iniciar mas não encontra. Apesar do erro, a aplicação permanece rodando e processando as requisições.**

Voce pode utilizar o endereço http://localhost:8080/ para fazer o teste com o Postman da mesma forma que foi feito anteriormente.

#### Links Úteis
- https://medium.com/@Grigorkh/how-to-install-docker-on-ubuntu-19-04-7ccfeda5935
- https://docs.docker.com/
 
## Configurando projeto GCP e subindo aplicação

Para instalar o SDK, adicione a URI da distribuição do Cloud SDK como um package source.  
```
echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] http://packages.cloud.google.com/apt cloud-sdk main" | sudo tee -a /etc/apt/sources.list.d/google-cloud-sdk.list
```
Importe a chave pública do Google Cloud Platform.  
```
curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key --keyring /usr/share/keyrings/cloud.google.gpg add -
```
Atualize o package list e instale o Cloud SDK.  
```
sudo apt-get update && sudo apt-get install google-cloud-sdk  
```
Para iniciar o SDK execute o seguinte comando.
```
gcloud init
```
![](src/resources/img/gcloudinit.png)

Siga as instruções para selecionar um projeto já criado ou criar um novo projeto.

Para configurar o Docker para usar o GCloud rode o comando a seguir.  
```
gcloud auth configure-docker
```
O comando abaixo serve para adicionar uma tag á imagem que acabamos de criar e definir a URL para onde vamos envia-la.  
```
docker tag chatbot-integration gcr.io/<YourProjectId>/chatbot-integration
```
Para subir a imagem para o GCP use o comando a seguir.  
```
docker push gcr.io/integration-example-nymoic/chatbot-integration
```

Acesse https://console.cloud.google.com/ (confira se o projeto está selecionado corretamente). No menú lateral selecione a opção `Container Registry`.

![](src/resources/img/containerRegistry.png)

Será listado o container que acabamos de subir.  
![](src/resources/img/containersList.png)

Agora, será necessário inicializar a imagem que acabamos de criar em um novo container. Para isso, acesse o mení `Cloud Run`.  
![](src/resources/img/menuCloudRun.png)

Clique em `Criar Serviço`.

![](src/resources/img/criarServico.png)

Dê um nome para o serviço, marque a opção `Permitir invocações não autenticadas` e clique em `Próxima`.

![](src/resources/img/servicoTela1.png)

Clique em `Selecionar` e procure a imagem que subimos.

![](src/resources/img/servicoTela2.png)

Clique em `Mostrar configurações Avançadas` e altere a quantidade de `Memória alocada` para 512MB e clique em `Criar`.

![](src/resources/img/servicoTela3.png)

Na tela seguinte quando a flag que indica o status estiver verde, a URL também estará disponível para ser copiada.

![](src/resources/img/servicoTela4.png)

Voce pode utilizar o endereço da URL para fazer o teste com o Postman da mesma forma que foi feito anteriormente.

#### Links Úteis:
- https://cloud.google.com/sdk/docs/quickstart-debian-ubuntu
- https://cloud.google.com/run/docs/using-gcp-services?hl=pt-BR
- https://cloud.google.com/run/docs/testing/local?hl=pt-BR

## Criando intenção e configurando fulfillment no Dialogflow

Crie uma nova intenção no seu chatbot Dialogflow, coloque o nome de `Call Jason Agent` crie algumas `Training phrases` como por exemplo:

- Diga oi ao agente Jason
- Chame o agente Jason
- Quero falar com o agente Jason

E no ítem `Fulfillment` habilite o webhook. Salve a intenção.

![](src/resources/img/callJasonAgenIntention.png)

Selecione a aba `Fulfillment` habilite a opção `Webhook`, cole a URL da aplicação do GCP e salve.

![](src/resources/img/fulfillment.png)

Agora podemos utilizar uma das frases de trinamento cadastradas na nossa intenção para verificar se estamos conseguindo contato com nosso agente Jason.

![](src/resources/img/chamandoAgenteJason.png)

#### Links Úteis:
- https://cloud.google.com/dialogflow/docs/quick/fulfillment
- https://cloud.google.com/dialogflow/docs/fulfillment-overview