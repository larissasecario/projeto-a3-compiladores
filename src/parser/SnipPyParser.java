package parser;

import ast.*;
import errors.SyntaxError;
import lexer.Token;
import lexer.TipoToken;
import lexer.SnipPyLexico;
import semantics.TipoDado;

import java.util.ArrayList;
import java.util.List;

public class SnipPyParser {

    private final SnipPyLexico lexico;
    private Token tokenAtual;

    public SnipPyParser(SnipPyLexico lexico) {
        this.lexico = lexico;
        this.tokenAtual = lexico.proximoToken();
    }

    // ============================
    //  UTILITÁRIOS DE ERRO / MATCH
    // ============================

    private void erroSintatico(TipoToken esperado, Token encontrado) {
        throw new SyntaxError(
            "Esperado " + esperado +
            ", mas encontrado " + encontrado.getTipo() +
            " (lexema \"" + encontrado.getLexema() + "\")"
        );
    }

    private void erroSintatico(String msg) {
        throw new SyntaxError(msg);
    }

    private void casaToken(TipoToken esperado) {
        if (tokenAtual.getTipo() == esperado) {
            tokenAtual = lexico.proximoToken();
        } else {
            erroSintatico(esperado, tokenAtual);
        }
    }

    // ============================
    //  ENTRADA PÚBLICA
    // ============================

    /** Ponto de entrada: usado no main */
    public ProgramNode parseProgram() {
        List<StmtNode> comandos = listaComandos();

        if (tokenAtual.getTipo() != TipoToken.EOF) {
            throw new SyntaxError(
                "Código não terminou corretamente. EOF esperado, mas encontrado "
                    + tokenAtual.getTipo() + " (lexema \"" + tokenAtual.getLexema() + "\")."
            );
        }

        return new ProgramNode(comandos);
    }

    // ============================
    //  LISTA DE COMANDOS / COMANDO
    // ============================

    private List<StmtNode> listaComandos() {
        List<StmtNode> comandos = new ArrayList<>();

        while (tokenAtual.getTipo() != TipoToken.EOF &&
               tokenAtual.getTipo() != TipoToken.CHAVE_DIR) {

            StmtNode cmd = comando();
            comandos.add(cmd);
        }

        return comandos;
    }

    private StmtNode comando() {

        switch (tokenAtual.getTipo()) {

            case KW_INT:
            case KW_REAL:
            case KW_BOOL: {   // <-- ADICIONADO AQUI
                VarDeclNode decl = declaracaoVariavel();
                casaToken(TipoToken.PONTO_VIRGULA);
                return decl;
            }

            case IDENTIFICADOR: {
                AssignNode atrib = atribuicao();
                casaToken(TipoToken.PONTO_VIRGULA);
                return atrib;
            }

            case KW_IF:
                return condicional();

            case KW_WHILE:
            case KW_FOR:
                return repeticao();

            case KW_READ:
            case KW_PRINT: {
                StmtNode io = entradaSaida();
                casaToken(TipoToken.PONTO_VIRGULA);
                return io;
            }

            default:
                erroSintatico("Comando inválido iniciado em: " + tokenAtual.getTipo());
                return null; // nunca deve chegar aqui
        }
    }

    // ============================
    //  DECLARAÇÃO DE VARIÁVEIS
    // ============================

    private VarDeclNode declaracaoVariavel() {
        TipoDado tipoVar = tipo();
        List<String> nomes = listaIdentificadores();
        return new VarDeclNode(tipoVar, nomes);
    }

    private TipoDado tipo() {
        if (tokenAtual.getTipo() == TipoToken.KW_INT) {
            casaToken(TipoToken.KW_INT);
            return TipoDado.INT;
        } 
        else if (tokenAtual.getTipo() == TipoToken.KW_REAL) {
            casaToken(TipoToken.KW_REAL);
            return TipoDado.REAL;
        }
        else if (tokenAtual.getTipo() == TipoToken.KW_BOOL) {
            casaToken(TipoToken.KW_BOOL);
            return TipoDado.BOOL;
        }
        else {
            erroSintatico(TipoToken.KW_INT, tokenAtual);
            return TipoDado.INVALIDO;
        }
    }

    private List<String> listaIdentificadores() {
        List<String> nomes = new ArrayList<>();

        if (tokenAtual.getTipo() != TipoToken.IDENTIFICADOR) {
            erroSintatico(TipoToken.IDENTIFICADOR, tokenAtual);
        }

        nomes.add(tokenAtual.getLexema());
        casaToken(TipoToken.IDENTIFICADOR);

        while (tokenAtual.getTipo() == TipoToken.VIRGULA) {
            casaToken(TipoToken.VIRGULA);

            if (tokenAtual.getTipo() != TipoToken.IDENTIFICADOR) {
                erroSintatico(TipoToken.IDENTIFICADOR, tokenAtual);
            }

            nomes.add(tokenAtual.getLexema());
            casaToken(TipoToken.IDENTIFICADOR);
        }

        return nomes;
    }

    // ============================
    //  ATRIBUIÇÃO
    // ============================

    private AssignNode atribuicao() {
        String nome = tokenAtual.getLexema();
        casaToken(TipoToken.IDENTIFICADOR);
        casaToken(TipoToken.ATRIBUICAO);

        ExprNode expr = expressao();
        return new AssignNode(nome, expr);
    }

    // ============================
    //  EXPRESSÕES
    // ============================

    private ExprNode expressao() {
        return expressaoLogica();
    }

    private ExprNode expressaoLogica() {
        ExprNode left = expressaoLogicaAnd();

        while (tokenAtual.getTipo() == TipoToken.OP_OR) {
            casaToken(TipoToken.OP_OR);
            ExprNode right = expressaoLogicaAnd();
            left = new BinaryExprNode(left, "or", right);
        }

        return left;
    }

    private ExprNode expressaoLogicaAnd() {
        ExprNode left = expressaoRelacional();

        while (tokenAtual.getTipo() == TipoToken.OP_AND) {
            casaToken(TipoToken.OP_AND);
            ExprNode right = expressaoRelacional();
            left = new BinaryExprNode(left, "and", right);
        }

        return left;
    }

    private ExprNode expressaoRelacional() {
        ExprNode left = expressaoAritmetica();

        switch (tokenAtual.getTipo()) {
            case OP_IGUAL:
            case OP_DIFERENTE:
            case OP_MENOR:
            case OP_MENOR_IGUAL:
            case OP_MAIOR:
            case OP_MAIOR_IGUAL: {

                TipoToken opTok = tokenAtual.getTipo();
                String opStr;

                switch (opTok) {
                    case OP_IGUAL:       opStr = "=="; break;
                    case OP_DIFERENTE:   opStr = "!="; break;
                    case OP_MENOR:       opStr = "<";  break;
                    case OP_MENOR_IGUAL: opStr = "<="; break;
                    case OP_MAIOR:       opStr = ">";  break;
                    case OP_MAIOR_IGUAL: opStr = ">="; break;
                    default:             opStr = "";   break;
                }

                casaToken(opTok);
                ExprNode right = expressaoAritmetica();
                return new BinaryExprNode(left, opStr, right);
            }

            default:
                return left;
        }
    }

    private ExprNode expressaoAritmetica() {
        ExprNode left = termo();

        while (tokenAtual.getTipo() == TipoToken.OP_SOMA ||
               tokenAtual.getTipo() == TipoToken.OP_SUB) {

            TipoToken opTok = tokenAtual.getTipo();
            casaToken(opTok);

            ExprNode right = termo();

            String opStr = (opTok == TipoToken.OP_SOMA) ? "+" : "-";
            left = new BinaryExprNode(left, opStr, right);
        }

        return left;
    }

    private ExprNode termo() {
        ExprNode left = fator();

        while (tokenAtual.getTipo() == TipoToken.OP_MUL ||
               tokenAtual.getTipo() == TipoToken.OP_DIV ||
               tokenAtual.getTipo() == TipoToken.OP_MOD) {

            TipoToken opTok = tokenAtual.getTipo();
            casaToken(opTok);

            ExprNode right = fator();

            String opStr;
            switch (opTok) {
                case OP_MUL: opStr = "*"; break;
                case OP_DIV: opStr = "/"; break;
                case OP_MOD: opStr = "%"; break;
                default:     opStr = "";  break;
            }

            left = new BinaryExprNode(left, opStr, right);
        }

        return left;
    }

    private ExprNode fator() {

        switch (tokenAtual.getTipo()) {

            case OP_SUB: {  // <-- ADICIONADO AQUI
                casaToken(TipoToken.OP_SUB);
                ExprNode expr = fator();
                return new UnaryExprNode("-", expr);
            }

            case OP_NOT: {
                casaToken(TipoToken.OP_NOT);
                ExprNode expr = fator();
                return new UnaryExprNode("not", expr);
            }

            case PAREN_ESQ: {
                casaToken(TipoToken.PAREN_ESQ);
                ExprNode expr = expressao();
                casaToken(TipoToken.PAREN_DIR);
                return expr;
            }

            case IDENTIFICADOR:
                String nome = tokenAtual.getLexema();
                casaToken(TipoToken.IDENTIFICADOR);
                return new VarExprNode(nome);

            case INTEIRO:
                int valor = Integer.parseInt(tokenAtual.getLexema());
                casaToken(TipoToken.INTEIRO);
                return new IntLiteralNode(valor);

            case REAL:
                double valorR = Double.parseDouble(tokenAtual.getLexema());
                casaToken(TipoToken.REAL);
                return new RealLiteralNode(valorR);

            case STRING:
                String valorS = tokenAtual.getLexema();
                casaToken(TipoToken.STRING);
                return new StringLiteralExprNode(valorS);

            case KW_TRUE:
                casaToken(TipoToken.KW_TRUE);
                return new BoolLiteralNode(true);

            case KW_FALSE:
                casaToken(TipoToken.KW_FALSE);
                return new BoolLiteralNode(false);

            default:
                erroSintatico("Fator inesperado: " + tokenAtual.getTipo());
                return null;
        }
    }


    private IfNode condicional() {

        casaToken(TipoToken.KW_IF);
        casaToken(TipoToken.PAREN_ESQ);

        ExprNode cond = expressao();

        casaToken(TipoToken.PAREN_DIR);
        casaToken(TipoToken.CHAVE_ESQ);

        List<StmtNode> blocoThen = listaComandos();

        casaToken(TipoToken.CHAVE_DIR);

        List<StmtNode> blocoElse = null;

        if (tokenAtual.getTipo() == TipoToken.KW_ELSE) {
            casaToken(TipoToken.KW_ELSE);
            casaToken(TipoToken.CHAVE_ESQ);
            blocoElse = listaComandos();
            casaToken(TipoToken.CHAVE_DIR);
        }

        return new IfNode(cond, blocoThen, blocoElse);
    }


    private StmtNode repeticao() {

        if (tokenAtual.getTipo() == TipoToken.KW_WHILE) {
            return comandoWhile();
        } else if (tokenAtual.getTipo() == TipoToken.KW_FOR) {
            return comandoFor();
        } else {
            erroSintatico("Esperado 'while' ou 'for', mas encontrado " + tokenAtual.getTipo());
            return null;
        }
    }

    private WhileNode comandoWhile() {

        casaToken(TipoToken.KW_WHILE);
        casaToken(TipoToken.PAREN_ESQ);

        ExprNode cond = expressao();

        casaToken(TipoToken.PAREN_DIR);
        casaToken(TipoToken.CHAVE_ESQ);

        List<StmtNode> corpo = listaComandos();

        casaToken(TipoToken.CHAVE_DIR);

        return new WhileNode(cond, corpo);
    }

    private ForNode comandoFor() {

        casaToken(TipoToken.KW_FOR);
        casaToken(TipoToken.PAREN_ESQ);

        // init: IDENT = expr
        if (tokenAtual.getTipo() != TipoToken.IDENTIFICADOR) {
            erroSintatico(TipoToken.IDENTIFICADOR, tokenAtual);
        }
        String initNome = tokenAtual.getLexema();
        casaToken(TipoToken.IDENTIFICADOR);
        casaToken(TipoToken.ATRIBUICAO);
        ExprNode initExpr = expressao();
        AssignNode init = new AssignNode(initNome, initExpr);

        casaToken(TipoToken.PONTO_VIRGULA);

        // cond:
        ExprNode cond = expressao();

        casaToken(TipoToken.PONTO_VIRGULA);

        // inc: IDENT = expr
        if (tokenAtual.getTipo() != TipoToken.IDENTIFICADOR) {
            erroSintatico(TipoToken.IDENTIFICADOR, tokenAtual);
        }
        String incNome = tokenAtual.getLexema();
        casaToken(TipoToken.IDENTIFICADOR);
        casaToken(TipoToken.ATRIBUICAO);
        ExprNode incExpr = expressao();
        AssignNode inc = new AssignNode(incNome, incExpr);

        casaToken(TipoToken.PAREN_DIR);

        casaToken(TipoToken.CHAVE_ESQ);
        List<StmtNode> corpo = listaComandos();
        casaToken(TipoToken.CHAVE_DIR);

        return new ForNode(init, cond, inc, corpo);
    }


    private StmtNode entradaSaida() {

        if (tokenAtual.getTipo() == TipoToken.KW_READ) {
            casaToken(TipoToken.KW_READ);
            casaToken(TipoToken.PAREN_ESQ);

            if (tokenAtual.getTipo() != TipoToken.IDENTIFICADOR) {
                erroSintatico(TipoToken.IDENTIFICADOR, tokenAtual);
            }

            String nome = tokenAtual.getLexema();
            casaToken(TipoToken.IDENTIFICADOR);
            casaToken(TipoToken.PAREN_DIR);

            return new ReadNode(nome);
        }

        if (tokenAtual.getTipo() == TipoToken.KW_PRINT) {
            casaToken(TipoToken.KW_PRINT);
            casaToken(TipoToken.PAREN_ESQ);

            StmtNode stmt;

            if (tokenAtual.getTipo() == TipoToken.STRING) {
                String literal = tokenAtual.getLexema();
                casaToken(TipoToken.STRING);
                stmt = new PrintNode(literal);
            } else {
                ExprNode expr = expressao();
                stmt = new PrintNode(expr);
            }

            casaToken(TipoToken.PAREN_DIR);
            return stmt;
        }

        erroSintatico("Esperado 'read' ou 'print', mas encontrado " + tokenAtual.getTipo());
        return null;
    }

    public Token getTokenAtual() {
        return tokenAtual;
    }
}
