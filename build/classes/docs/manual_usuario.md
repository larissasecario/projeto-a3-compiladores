# MANUAL DO USUÁRIO — COMPILADOR SnipPy

## 1. Introdução

Este manual explica como instalar, configurar, executar e utilizar o Compilador SnipPy.

O SnipPy é um compilador acadêmico desenvolvida em Java para compilar uma linguagem própria chamada SnipPy, criada com base nas linguagens Python e C.

## 2. Requisitos do Sistema

Para executar o compilador, o usuário deve ter instalado:

* Java Development Kit (JDK) – versão 17 ou superior
* NetBeans (recomendado) ou outro IDE Java
* Compilador C (opcional, apenas se desejar executar o código C gerado na saída do compilador)

## 3. Instalando e Abrindo o Projeto

1. Acesse o repositório presente no GitHub:
   [https://github.com/larissasecario/projeto-a3-compiladores](https://github.com/larissasecario/projeto-a3-compiladores)

2. Baixar o projeto para o computador:
   Você pode fazer isso de duas formas:

   * Baixar ZIP:

     * Clique no botão Code e depois em Download ZIP.
     * Extraia o arquivo ZIP em uma pasta do seu computador.

   * Clonar com Git (se tiver Git instalado):

     * Abra o terminal ou Git Bash
     * Execute:

       ```bash
       git clone https://github.com/larissasecario/projeto-a3-compiladores.git
       ```
     * O projeto será baixado para uma pasta com esse nome.

3. Abra o NetBeans.

4. Clique em:
   File e depois em Open Project.

5. Selecione a pasta do projeto que você baixou/clonou e confirme.


## 4. Como preparar o ambiente para rodar o compilador

### 4.1 Criar um arquivo `.spy`

Crie um arquivo com extensão `.spy` e insira o código que você deseja compilar dentro desse arquivo.

Importante: para o programa compilar corretamente, o código deve seguir as regras da linguagem SnipPy.
Se houver algum erro que a linguagem não aceita, vai aparecer mensagem de erro durante a compilação.

Exemplo de arquivo:
`programa.spy`

Conteúdo:

```spy
int x;
x = 10;
print(x);
```

Você pode colocar esse arquivo dentro da pasta `data` presente no diretório do projeto, por exemplo: `src/data/programa.spy`.


## 5. Configurando o NetBeans para passar o arquivo como argumento

Para rodar o compilador, é necessário passar o arquivo com o código SnipPy através dos argumentos de execução.

Siga os passos abaixo para configurar isso dentro do NetBeans:

1. No NetBeans, clique com o botão direito no nome do projeto.
2. Clique em Properties.
3. Abra a aba Run.
4. No campo Arguments, escreva o caminho onde está o arquivo `.spy`. Exemplo:

   ```text
   src/data/programa.spy
   ```

5. Clique em OK.

No exemplo do projeto, o arquivo `programa.spy` contendo o código em SnipPy foi adicionado na pasta `data`, dentro do diretório do projeto.
Por isso, o caminho inserido no campo Arguments foi `src/data/programa.spy`.

Com isso configurado, toda vez que você rodar o projeto, o compilador vai ler automaticamente esse arquivo.


## 6. Executando o compilador

Depois de configurar o arquivo `.spy` nos argumentos:

1. Clique no botão Run no NetBeans.

Se tudo estiver correto, será exibido no terminal algo parecido com:

```text
 ==================================== COMPILADOR SnipPy ==================================== 
Nome do arquivo de entrada: src/data/programa.spy

 ------ Iniciando compilacao para linguagem SnipPy ------ 
 ------ ANALISE LEXICA + SINTATICA + SEMANTICA ------ 

[lEXICO]...
...
[lEXICO]...

------ RESULTADO DA ANALISE SEMANTICA ------ 
Tabela de S�mbolos:
...

------ GERACAO DE CODIGO C ------ 
...

==================================== COMPILACAO CONCLUIDA COM SUCESSO ==================================== 
BUILD SUCCESSFUL (total time: 0 seconds)

```

Ao final, o compilador exibirá:

* A tabela de símbolos, com as variáveis e seus tipos
* O código C gerado, equivalente ao programa escrito em SnipPy

Exemplo de saída:

```text
==== RESULTADO DA ANALISE SEMANTICA ====
<x : INT>
<y : REAL>

==== CODIGO C GERADO ====
#include <stdio.h>
int main() {
    int x;
    double y;
    ...
}
```


## 7. Salvando o código C gerado

O terminal do NetBeans vai mostrar um bloco de código em linguagem C.

Para testar esse código, você pode:

1. Copiar todo o código C exibido no console.
2. Abrir um editor de texto (Bloco de Notas, VS Code, etc.).
3. Colar o código.
4. Salvar o arquivo com extensão `.c`, por exemplo: `saida.c`.

Depois disso, você pode:

* Compilar usando um compilador C local.
* Colar o código em um site de compilador C online e executar por lá.


## 8. Estrutura básica de um programa SnipPy

Um programa SnipPy deve seguir algumas regras básicas:

* Declaração de variáveis (`int` ou `real`)
* Comandos terminados com ponto e vírgula `;`
* Blocos de código entre chaves `{ }` em estruturas como `if`, `else`, `while`, `for`
* Expressões válidas com tipos corretos (ex.: `int` com `int`, `real` com `real` ou mistura numérica permitida)

Exemplo completo:

```spy
int x;
real y;

x = 10;
y = x + 3.14;

if (y > 10) {
    print(y);
} else {
    print(0);
}
```

## 9. Como criar erros para teste

Nesta seção, será mostrado alguns exemplos de códigos que funcionam e outras que geram erros em cada etapa para teste.

### 9.1 Erros léxicos

Erros léxicos acontecem quando o código tem símbolos/caracteres que não fazem parte da linguagem.

#### Exemplo sem erro léxico:

```spy
int x;
x = 10;
print(x);
```

#### Exemplo com erro léxico:

```spy
int x;
x = 10;
$y = 5;
```

O símbolo `$` não existe na linguagem. O compilador deve indicar algo como:

```text
Erro léxico: caractere inválido '$'
```

### 9.2 Erros sintáticos

Erros sintáticos acontecem quando a estrutura do código não segue a gramática da linguagem (por exemplo, esquecer ponto e vírgula, parênteses, chaves, etc).

#### Exemplo sem erro sintático:

```spy
int x;
x = 10;
print(x);
```

#### Exemplo com erro sintático:

```spy
int x
x = 10;
print(x);
```

Depois de `int x` falta `;`. O compilador deve mostrar mensagem parecida com:

```text
Erro sintático: esperado PONTO_VIRGULA mas encontrado < KW_REAL, real >
```

### 9.3 Erros semânticos

Erros semânticos acontecem quando a estrutura está correta, mas o significado não faz sentido para a linguagem, por exemplo:

* variável usada sem ser declarada
* atribuição com tipos incompatíveis

#### Exemplo sem erro semânticos:

```spy
int a;
real b;

a = 10;
b = a + 2.5;
print(b);
```

#### Exemplo com erro semânticos:

```spy
int a;

a = 10;
b = a + 1; // 'b' não foi declarada
```

O compilador deve acusar algo do tipo:

```text
Erro semântico: Variável 'b' não foi declarada.
```


## 10. Conclusão

Este manual explicou o que fazer para baixar o projeto Github, abrir o projeto no Netbeans, configurar o arquivo `.spy` para ser comoilador, 
executar o compilador e testar o código final de saída. 

