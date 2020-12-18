# Eobot Robot

Um aplicativo que monitora a cotação do site [Eobot](https://www.eobot.com) e decide qual a melhor criptomoeda para minerar no próprio site.


## Aviso

Este aplicativo foi descontinuado devido ao encerramento das atividades do Eobot, confira mais informações neste post [https://www.eobot.com/closing](https://www.eobot.com/closing).


## Configuração inicial

### Dados do Eobot
Abra o arquivo `src/Resources.java` e insira suas informações nos campos indicados.

```java
private static final String
    ID = ""
    API = ""
    email = ""
```
Onde _ID_ é o seu ID da conta no Eobot;
_API_ é a sua chave API do Eobot;
e _email_ é o seu email cadastrado no site.
Todas estas informações podem ser encontradas em [https://www.eobot.com/profile](https://www.eobot.com/profile).

### Dados do Telegram

Ainda no arquivo `src/Resources.java` insira as informações do seu bot.

```java
private static final String
    TelegramURL = ""
    chatID = ""
```

Onde _TelegramURL_ é a URL API fornecida pelo _BotFather_ na criação do bot; e _chatID_ é o código de identificação do seu chat com o bot, inicie uma conversa com o bot para obtê-lo. Mais informações em [https://core.telegram.org/bots/api#getting-updates](https://core.telegram.org/bots/api#getting-updates).

## Compilando o aplicativo

Após salvar as alterações é necessário criar o arquivo `.jar` para executar o aplicativo.
Caso esteja utilizando o [Eclipse IDE](https://www.eclipse.org/eclipseide/) clique com o botão direito na pasta do projeto e selecione a opção `Export`. Na janela que abrir selecione a opção `Java > Runnable JAR file` e clique em `Next`. Na opção `Launch configuration` selecione `Launcher - Eobot Robot`, em `Export destination` clique em `Browse` e selecione a pasta de destino e nomeie o arquivo. Em `Library handling` selecione a opção `Package required libraries into generated JAR` e clique em `Finish`.

## Executando o aplicativo

O aplicativo pode ser iniciado em qualquer plataforma que possua o Java JDK 11 ou superior instalado por meio do comando `java -jar <nome do arquivo>.jar`. Caso seja iniciado sem nenhum argumento, o aplicativo irá executar com o seu funcionamento padrão mas podem ser utilizados os parâmetros a seguir para modificar o seu funciomaneto.

#### -debug

Ativa o modo de depuração, onde todas as entradas e saídas de informações são exibidos no console.

#### -force-BTC ou -force-GHS6

Força o aplicativo a manter sempre a mineração no item selecionado, [BTC](https://www.eobot.com/btc) ou [GHS6](https://www.eobot.com/cloud).

#### -noTG

Desativa o envio de mensagens no Telegram.
