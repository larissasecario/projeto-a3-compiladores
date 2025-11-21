/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lexer;

/**
 *
 * @author laris
 */
public enum TipoToken {
    
     // --- Identificadores ---
    IDENTIFICADOR,
    
    // --- Numeros Literais ---
    INTEIRO,
    REAL,

    // --- Palavras-chave ---
    KW_INT,     // int
    KW_REAL,    // real
    KW_IF,      // if
    KW_ELSE,    // else
    KW_WHILE,   // while
    KW_FOR,     // for
    KW_READ,    // read
    KW_PRINT,   // print
    KW_TRUE,    // true
    KW_FALSE,   // false
    KW_BOOL,
    KW_STRING,

    // --- Operadores aritméticos ---
    OP_SOMA,      // +
    OP_SUB,       // -
    OP_MUL,       // *
    OP_DIV,       // /
    OP_MOD,       // %

    // --- Operadores relacionais ---
    OP_IGUAL,       // ==
    OP_DIFERENTE,   // !=
    OP_MENOR,       // <
    OP_MENOR_IGUAL, // <=
    OP_MAIOR,       // >
    OP_MAIOR_IGUAL, // >=

    // --- Operadores lógicos ---
    OP_AND,   // and
    OP_OR,    // or
    OP_NOT,   // not

    // --- Atribuição ---
    ATRIBUICAO, // =

    // --- Separadores ---
    PONTO_VIRGULA,  // ;
    VIRGULA,        // ,
    PAREN_ESQ,      // (
    PAREN_DIR,      // )
    CHAVE_ESQ,      // {
    CHAVE_DIR,      // }
    
    // --- Literais de texto ---
    STRING,
    
    // Fim do arquivo
    EOF,
    
}