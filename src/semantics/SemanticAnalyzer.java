package semantics;

import ast.NodeVisitor;
import ast.*;
import errors.SemanticError;

import java.util.List;

public class SemanticAnalyzer implements NodeVisitor {

    private final TabelaSimbolos tabela = new TabelaSimbolos();

    private void erro(String msg) {
        throw new SemanticError(msg);
    }

    //  PROGRAMA
    @Override
    public void visit(ProgramNode node) {
        for (StmtNode s : node.getStatements()) {
            visitStmt(s);
        }
    }

    private void visitStmt(StmtNode s) {
        if (s instanceof VarDeclNode) visit((VarDeclNode) s);
        else if (s instanceof AssignNode) visit((AssignNode) s);
        else if (s instanceof IfNode) visit((IfNode) s);
        else if (s instanceof WhileNode) visit((WhileNode) s);
        else if (s instanceof ForNode) visit((ForNode) s);
        else if (s instanceof ReadNode) visit((ReadNode) s);
        else if (s instanceof PrintNode) visit((PrintNode) s);
        else erro("Tipo de comando desconhecido: " + s.getClass());
    }
    
    
    private TipoDado visit(ExprNode expr) {
        if (expr instanceof VarExprNode)
            return visit((VarExprNode) expr);

        if (expr instanceof IntLiteralNode)
            return visit((IntLiteralNode) expr);

        if (expr instanceof RealLiteralNode)
            return visit((RealLiteralNode) expr);

        if (expr instanceof BoolLiteralNode)
            return visit((BoolLiteralNode) expr);

        if (expr instanceof StringLiteralExprNode)
            return visit((StringLiteralExprNode) expr);

        if (expr instanceof UnaryExprNode)
            return visit((UnaryExprNode) expr);

        if (expr instanceof BinaryExprNode)
            return visit((BinaryExprNode) expr);

        throw new SemanticError("Expressão desconhecida: " + expr.getClass());
    }


    //  DECLARAÇÃO
    @Override
    public void visit(VarDeclNode node) {
        for (String nome : node.getNomes()) {

            if (tabela.existe(nome))
                erro("Variável '" + nome + "' já foi declarada.");

            tabela.inserir(nome, node.getTipo());
        }
    }

    //  ATRIBUIÇÃO
    @Override
    public void visit(AssignNode node) {
        String nome = node.getNome();

        if (!tabela.existe(nome))
            erro("Variável '" + nome + "' não foi declarada.");

        TipoDado tipoVar = tabela.buscar(nome).getTipo();
        TipoDado tipoExpr = visit(node.getExpressao());

        if (tipoVar == TipoDado.INT && tipoExpr == TipoDado.REAL)
            erro("Não é permitido atribuir REAL a uma variável INT: " + nome);

        if (tipoVar != tipoExpr &&
           !(tipoVar == TipoDado.REAL && tipoExpr == TipoDado.INT))
            erro("Tipos incompatíveis na atribuição: " + tipoVar + " = " + tipoExpr);
    }

    //  IF
    @Override
    public void visit(IfNode node) {
        TipoDado tipoCond = visit(node.getCondicao());

        if (tipoCond != TipoDado.BOOL)
            erro("Condição do IF deve ser BOOL.");

        for (StmtNode s : node.getBlocoThen())
            visitStmt(s);

        if (node.getBlocoElse() != null)
            for (StmtNode s : node.getBlocoElse())
                visitStmt(s);
    }

    //  WHILE
    @Override
    public void visit(WhileNode node) {
        TipoDado tipoCond = visit(node.getCondicao());

        if (tipoCond != TipoDado.BOOL)
            erro("Condição do WHILE deve ser BOOL.");

        for (StmtNode s : node.getCorpo())
            visitStmt(s);
    }

    //  FOR
    @Override
    public void visit(ForNode node) {
        // init
        visit(node.getInit());

        // cond
        TipoDado cond = visit(node.getCondicao());
        if (cond != TipoDado.BOOL)
            erro("Condição do FOR deve ser BOOL.");

        // incremento
        visit(node.getIncremento());

        for (StmtNode s : node.getCorpo())
            visitStmt(s);
    }

    //  READ
    @Override
    public void visit(ReadNode node) {
        if (!tabela.existe(node.getNome()))
            erro("Variável '" + node.getNome() + "' não foi declarada.");
    }

    //  PRINT
    @Override
    public void visit(PrintNode node) {
        if (node.getExpressao() != null)
            visit(node.getExpressao());
    }

    // EXPRESSÕES
    @Override
    public TipoDado visit(VarExprNode node) {
        if (!tabela.existe(node.getNome()))
            erro("Variável '" + node.getNome() + "' não foi declarada.");

        return tabela.buscar(node.getNome()).getTipo();
    }

    @Override
    public TipoDado visit(IntLiteralNode node) {
        return TipoDado.INT;
    }

    @Override
    public TipoDado visit(RealLiteralNode node) {
        return TipoDado.REAL;
    }

    @Override
    public TipoDado visit(BoolLiteralNode node) {
        return TipoDado.BOOL;
    }

    @Override
    public TipoDado visit(StringLiteralExprNode node) {
        return TipoDado.STRING;
    }

    @Override
    public TipoDado visit(UnaryExprNode node) {
        TipoDado tipoExpr = visit(node.getExpr());

        switch (node.getOperador()) {
            case "not":
                if (tipoExpr != TipoDado.BOOL)
                    erro("Operador 'not' requer BOOL.");
                return TipoDado.BOOL;

            default:
                erro("Operador unário inválido: " + node.getOperador());
                return TipoDado.INVALIDO;
        }
    }

    @Override
    public TipoDado visit(BinaryExprNode node) {
        TipoDado left = visit(node.getEsquerda());
        TipoDado right = visit(node.getDireita());

        String op = node.getOperador();

        // OPERADORES LÓGICOS
        if (op.equals("and") || op.equals("or")) {
            if (left != TipoDado.BOOL || right != TipoDado.BOOL)
                erro("Operador '" + op + "' requer operandos BOOL.");
            return TipoDado.BOOL;
        }

        // OPERADORES RELACIONAIS
        if (op.matches("==|!=|<|<=|>|>=")) {
            if (!isNum(left) || !isNum(right))
                erro("Operador relacional '" + op + "' requer operandos numéricos.");
            return TipoDado.BOOL;
        }

        // OPERADORES ARITMÉTICOS
        if (op.matches("\\+|-|\\*|/|%")) {
            if (!isNum(left) || !isNum(right))
                erro("Operação '" + op + "' requer INT ou REAL.");

            if (left == TipoDado.REAL || right == TipoDado.REAL)
                return TipoDado.REAL;

            return TipoDado.INT;
        }

        erro("Operador inválido: " + op);
        return TipoDado.INVALIDO;
    }

    private boolean isNum(TipoDado t) {
        return t == TipoDado.INT || t == TipoDado.REAL;
    }
}
