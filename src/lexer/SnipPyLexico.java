/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lexer;

import errors.LexicalError;

/**
 *
 * @author laris
 */
public class SnipPyLexico {

    private LeitorArquivosTexto leitor;

    public SnipPyLexico(String arquivo) {
        System.out.println(" ------ ANALISE LEXICA + SINTATICA + SEMANTICA ------ ");
        this.leitor = new LeitorArquivosTexto(arquivo);
    }
    
    
    public Token proximoToken() {
        Token t = proximoTokenInterno();

        // DEBUG: mostra cada token gerado
        System.out.println("[LEXICO] Token gerado: " + t);

        return t;
    }
    
    public Token proximoTokenInterno() {

           int codigo;
           char c;

           // IGNORA ESPAÇOS E QUEBRAS
           while (true) {
               codigo = leitor.lerProximoCaractere();
               c = (char) codigo;

               if (codigo == -1) {
                   return new Token(TipoToken.EOF, "");
               }

               if (!Character.isWhitespace(c)) {
                   break; // encontramos algo útil
               }

               leitor.confirmar(); // descarta lexema acumulado
           }
           
           //   COMENTÁRIOS DE LINHA
           if (c == '/') {
               int prox = leitor.lerProximoCaractere();

               if ((char)prox == '/') {
                   while (true) {
                       int k = leitor.lerProximoCaractere();
                       if (k == -1 || k == '\n' || k == '\r') {
                           break;
                       }
                   }
                   leitor.confirmar(); 
                   return proximoToken();
               } else {
                   leitor.retroceder();
               }
           }
           
            // STRINGS: "texto"
            if (c == '"') {
                return lerString();
            }


           // IDENTIFICADORES OU PALAVRAS-CHAVE
           if (Character.isLetter(c) || c == '_') {
               return lerIdentificador();
           }

           // NÚMEROS LITERAIS
           if (Character.isDigit(c)) {
               return lerNumeroLiterais();
           }

           // OPERADORES ARITMÉTICOS
           if ("+-*/%".indexOf(c) != -1) {
               return lerOperadoresAritmeticos();
           }

           // OPERADORES RELACIONAIS
           if ("=!<>".indexOf(c) != -1) {
               return lerOperadoresRelacionais();
           }

           // SEPARADORES
           if (";,(){}".indexOf(c) != -1) {
               return lerSeparador();
           }

           return erroLexico(c);
       }


    private Token erroLexico(char c) {
        throw new LexicalError("Caractere inválido: '" + c + "'");
    }
    
    private Token lerIdentificador() {
        int codigo;
        char c;

        while (true) {
            codigo = leitor.lerProximoCaractere();
            c = (char) codigo;

            if (!(Character.isLetterOrDigit(c) || c == '_')) {
                leitor.retroceder();
                break;
            }
        }

        String lexema = leitor.getLexema();
        leitor.confirmar();

        // Palavras-chave
        switch (lexema) {
            case "int": return new Token(TipoToken.KW_INT, lexema);
            case "real": return new Token(TipoToken.KW_REAL, lexema);
            case "if": return new Token(TipoToken.KW_IF, lexema);
            case "else": return new Token(TipoToken.KW_ELSE, lexema);
            case "while": return new Token(TipoToken.KW_WHILE, lexema);
            case "for": return new Token(TipoToken.KW_FOR, lexema);
            case "read": return new Token(TipoToken.KW_READ, lexema);
            case "print": return new Token(TipoToken.KW_PRINT, lexema);
            case "true": return new Token(TipoToken.KW_TRUE, lexema);
            case "false": return new Token(TipoToken.KW_FALSE, lexema);
            case "and": return new Token(TipoToken.OP_AND, lexema);
            case "or": return new Token(TipoToken.OP_OR, lexema);
            case "not": return new Token(TipoToken.OP_NOT, lexema);
        }

        return new Token(TipoToken.IDENTIFICADOR, lexema);
    }


    private Token lerNumeroLiterais() {

        boolean temPonto = false;
        int codigo;
        char c;

        while (true) {
            codigo = leitor.lerProximoCaractere();
            c = (char) codigo;

            if (Character.isDigit(c)) {
                continue;
            }

            if (c == '.' && !temPonto) {
                temPonto = true;
                continue;
            }

            leitor.retroceder();
            break;
        }

        String lexema = leitor.getLexema();
        leitor.confirmar();

        if (temPonto) {
            return new Token(TipoToken.REAL, lexema);
        }

        return new Token(TipoToken.INTEIRO, lexema);
    }


    private Token lerOperadoresAritmeticos() {
        char c = leitor.getLexema().charAt(0);
        leitor.confirmar();

        switch (c) {
            case '+': return new Token(TipoToken.OP_SOMA, "+");
            case '-': return new Token(TipoToken.OP_SUB, "-");
            case '*': return new Token(TipoToken.OP_MUL, "*");
            case '/': return new Token(TipoToken.OP_DIV, "/");
            case '%': return new Token(TipoToken.OP_MOD, "%");
        }

        return null;
    }


    private Token lerOperadoresRelacionais() {
        int codigo;
        char c1 = leitor.getLexema().charAt(0);

        codigo = leitor.lerProximoCaractere();
        char c2 = (char) codigo;

        // == e !=
        if ((c1 == '=' || c1 == '!') && c2 == '=') {
            leitor.confirmar();
            return (c1 == '=') ?
                    new Token(TipoToken.OP_IGUAL, "==") :
                    new Token(TipoToken.OP_DIFERENTE, "!=");
        }

        // <=  >=
        if ((c1 == '<' || c1 == '>') && c2 == '=') {
            leitor.confirmar();
            return (c1 == '<') ?
                    new Token(TipoToken.OP_MENOR_IGUAL, "<=") :
                    new Token(TipoToken.OP_MAIOR_IGUAL, ">=");
        }

        // retrocede se não for operador duplo
        leitor.retroceder();
        leitor.confirmar();

        switch (c1) {
            case '=': return new Token(TipoToken.ATRIBUICAO, "=");
            case '<': return new Token(TipoToken.OP_MENOR, "<");
            case '>': return new Token(TipoToken.OP_MAIOR, ">");
        }

        return null;
    }


    private Token lerSeparador() {
        char c = leitor.getLexema().charAt(0);
        leitor.confirmar();

        switch (c) {
            case ';': return new Token(TipoToken.PONTO_VIRGULA, ";");
            case ',': return new Token(TipoToken.VIRGULA, ",");
            case '(': return new Token(TipoToken.PAREN_ESQ, "(");
            case ')': return new Token(TipoToken.PAREN_DIR, ")");
            case '{': return new Token(TipoToken.CHAVE_ESQ, "{");
            case '}': return new Token(TipoToken.CHAVE_DIR, "}");
        }

        return null;
    }
    
    private Token lerString() {
        StringBuilder sb = new StringBuilder();
        int codigo;
        char c;

        while (true) {

            codigo = leitor.lerProximoCaractere();
            c = (char) codigo;

            if (codigo == -1) {
               throw new LexicalError("String não fechada antes do EOF");
            }

            // fim da string
            if (c == '"') {
                break;
            }

            // adiciona caractere à string
            sb.append(c);
        }

        leitor.confirmar();
        return new Token(TipoToken.STRING, sb.toString());
    }
    
}
