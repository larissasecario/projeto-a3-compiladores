# Guia da Linguagem SnipPy

## 1. Estrutura geral

SnipPy é uma linguagem criado com o intuito educacional com sintaxe inspirada em C e Python.

Cada comando deve ornigatoriamente terminar com ponto e vírgula (`;`) como a linguaguem , exceto blocos (`if`, `while`, `for`).

Exemplo de um arquivo SnipPy típico:

```spy
int x;
real y, z;

x = 10;
y = x + 3.14;

if (x > 5) {
    print(y);
}
```


## 2. Tipos de dados suportados

| Tipo SnipPy | Representação      |
| ------------ | ----------------- |
| `int`        | inteiros          |
| `real`       | números reais     |
| `bool`       | verdadeiro/falso  |
| `string`     | texto entre aspas |

### Literais aceitos

Inteiros:

```
10
0
-5
```

Reais:

```
3.14
0.01
12.0
```

Booleanos:

```
true
false
```

Strings:

```
"Ola"
"Teste 123"
```

Strings devem ser fechadas. Exemplo inválido:

```
"Ola
Teste 123"
```


## 3. Declaração de variáveis

Sempre começa com um tipo:

```spy
int x;
real y, z;
bool ativo;
string nome;
```

Inválido:

```
x int;
x;
```

É possivel declarar várias variáveis do mesmo tipo separadas por vírgula.


## 4. Atribuições

```spy
x = 10;
y = x + 2.5;
```

Regras semânticas:

- Não pode atribuir `real` → `int`:

```
int x;
x = 3.14;  // ERRO
```

- Pode int → real:

```
real y;
y = 10;   // OK
```

- Não pode usar variável não declarada:

```
a = 10;  // ERRO
print(a)
```

## 5. Operações permitidas

### Operadores Aritméticos

```
+   -   *   /   %
```

Só aceitam `int` ou `real`.

### Operadores Relacionais

```
==   !=   <   <=   >   >=
```

Só aceitam operandos numéricos e os resultados sempre é `bool`.

### Operadores Lógicos

```
and
or
not
```

Só aceitam `bool`.

## 6. Estruturas de Controle

### IF / ELSE

```spy
if (condicao) {
    comandos...
} else {
    comandos...
}
```

A condição deve ser bool. Exemplo:

```spy
if (x > 10 and y < 20) {
    print(x);
} else {
    print(0);
}
```


### WHILE

```spy
while (condicao) {
    comandos...
}
```

Condição **bool** obrigatória.

### FOR

Sintaxe:

```spy
for (i = 0; i < 5; i = i + 1) {
    comandos...
}
```

Cada parte funciona como:

* Inicialização: atribuição
* Condição: expressão que retorna bool
* Incremento: atribuição


## 7. Entrada e Saída

### print

Aceita string ou expressão:

```spy
print("Ola");
print(x);
print(x + y);
```

Requer ponto e vírgula no final:

```
print(x);   // correto
print(x)    // ERRO
```


### read

Lê um valor e atribui à variável:

```spy
read(x);
```

## 8. Resumo das Regras

| Situação                       | Permitido? | Resultado                     |
| ------------------------------ | ---------- | ----------------------------- |
| Usar variável sem declarar     | ❌         | Erro semântico                |
| Reatribuir tipos incompatíveis | ❌         | Erro semântico                |
| int → real                     | ✔          | Permitido                     |
| real → int                     | ❌         | Erro                          |
| int/real em if                 | ❌         | Erro (condição deve ser bool) |
| Operações com strings          | ❌         | Não suportado ainda           |
| Strings entre aspas apenas     | ✔          | OK                            |
| String sem fechar aspas        | ❌         | Erro léxico                   |


## 9. Exemplo completo válido SnipPy

```spy
int x;
real y, z;

x = 10;
y = x + 3.14;
z = y * 2;

if (x == 10 and y <= 20) {
    z = z / 2;
} else {
    z = 0;
}

while (x < 15) {
    x = x + 1;
}

for (x = 0; x < 5; x = x + 1) {
    print(x);
}

print("Fim!");
```

---

## 10. Exemplo com erros detectados

```spy
x = 10;               // ERRO: variável não declarada
real y;
y = "abc";            // ERRO: atribuição string → real
if (10) { }           // ERRO: condição precisa ser bool
while (x < 10)        // ERRO: falta chave e bloco
print(x)              // ERRO: falta ;
```

