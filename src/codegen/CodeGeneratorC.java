package codegen;

import ast.*;
import semantics.TipoDado;
import semantics.TabelaSimbolos;
/**
 *
 * @author laris
 */
public class CodeGeneratorC implements CodeGenVisitor {

    private StringBuilder sb = new StringBuilder();
    private int indent = 0;
    private TabelaSimbolos tabela;

    public CodeGeneratorC(TabelaSimbolos tabela) {
        this.tabela = tabela;
    }

    private void appendLine(String line) {
        sb.append("    ".repeat(indent)).append(line).append("\n");
    }

    private void openBlock(String header) {
        appendLine(header + " {");
        indent++;
    }

    private void closeBlock() {
        indent--;
        appendLine("}");
    }

    public String gerarCodigo(ProgramNode node) {
        sb.append("#include <stdio.h>\n");
        sb.append("#include <string.h>\n\n");
        sb.append("int main() {\n");
        indent++;

        visit(node);

        appendLine("return 0;");
        indent--;
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public String visit(ExprNode node) {
        if (node instanceof VarExprNode) return visit((VarExprNode) node);
        if (node instanceof IntLiteralNode) return visit((IntLiteralNode) node);
        if (node instanceof RealLiteralNode) return visit((RealLiteralNode) node);
        if (node instanceof BoolLiteralNode) return visit((BoolLiteralNode) node);
        if (node instanceof StringLiteralExprNode) return visit((StringLiteralExprNode) node);
        if (node instanceof UnaryExprNode) return visit((UnaryExprNode) node);
        if (node instanceof BinaryExprNode) return visit((BinaryExprNode) node);

        throw new RuntimeException("Expressão desconhecida: " + node.getClass());
    }


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
        else throw new RuntimeException("Nó desconhecido no codegen: " + s.getClass());
    }

    private TipoDado tipoExpr(ExprNode node) {
        if (node instanceof IntLiteralNode) return TipoDado.INT;
        if (node instanceof RealLiteralNode) return TipoDado.REAL;
        if (node instanceof BoolLiteralNode) return TipoDado.BOOL;
        if (node instanceof StringLiteralExprNode) return TipoDado.STRING;

        if (node instanceof VarExprNode) {
            return this.tabela.buscar(((VarExprNode) node).getNome()).getTipo();
        }

        if (node instanceof UnaryExprNode) {
            UnaryExprNode u = (UnaryExprNode) node;
            if (u.getOperador().equals("-"))
                return tipoExpr(u.getExpr());
            if (u.getOperador().equals("not"))
                return TipoDado.BOOL;
        }

        if (node instanceof BinaryExprNode) {
            BinaryExprNode b = (BinaryExprNode) node;

            // Operadores lógicos
            if (b.getOperador().equals("and") || b.getOperador().equals("or"))
                return TipoDado.BOOL;

            // Operadores relacionais
            if (b.getOperador().matches("==|!=|<|<=|>|>="))
                return TipoDado.BOOL;

            // Operadores aritméticos
            TipoDado t1 = tipoExpr(b.getEsquerda());
            TipoDado t2 = tipoExpr(b.getDireita());
            if (t1 == TipoDado.REAL || t2 == TipoDado.REAL)
                return TipoDado.REAL;
            return TipoDado.INT;
        }

        return TipoDado.INVALIDO;
    }


    @Override
    public void visit(VarDeclNode node) {

        String tipoC = "";
        switch (node.getTipo()) {
            case INT: tipoC = "int"; break;
            case REAL: tipoC = "double"; break;
            case BOOL: tipoC = "int"; break;
        }

        if (node.getTipo() == TipoDado.STRING) {
            for (String nome : node.getNomes()) {
                appendLine("char " + nome + "[256];");
            }
            return;
        }

        StringBuilder line = new StringBuilder(tipoC + " ");
        for (int i = 0; i < node.getNomes().size(); i++) {
            line.append(node.getNomes().get(i));
            if (i < node.getNomes().size() - 1)
                line.append(", ");
        }

        line.append(";");
        appendLine(line.toString());
    }

    @Override
    public void visit(AssignNode node) {
        String exprC = visit(node.getExpressao());
        appendLine(node.getNome() + " = " + exprC + ";");
    }


    @Override
    public void visit(IfNode node) {
        String condC = visit(node.getCondicao());

        openBlock("if (" + condC + ")");
        for (StmtNode s : node.getBlocoThen())
            visitStmt(s);
        closeBlock();

        if (node.getBlocoElse() != null) {
            openBlock("else");
            for (StmtNode s : node.getBlocoElse())
                visitStmt(s);
            closeBlock();
        }
    }


    @Override
    public void visit(WhileNode node) {
        String condC = visit(node.getCondicao());

        openBlock("while (" + condC + ")");
        for (StmtNode s : node.getCorpo())
            visitStmt(s);
        closeBlock();
    }


    @Override
    public void visit(ForNode node) {

        String init = node.getInit().getNome() + " = " + visit(node.getInit().getExpressao());
        String cond = visit(node.getCondicao());
        String inc = node.getIncremento().getNome() + " = " + visit(node.getIncremento().getExpressao());

        openBlock("for (" + init + "; " + cond + "; " + inc + ")");
        for (StmtNode s : node.getCorpo())
            visitStmt(s);
        closeBlock();
    }

  
    @Override
    public void visit(ReadNode node) {
        TipoDado tipo = tabela.buscar(node.getNome()).getTipo();

        switch (tipo) {

            case STRING:
                appendLine("fgets(" + node.getNome() + ", 256, stdin);");
                appendLine(node.getNome() + "[strcspn(" + node.getNome() + ", \"\\n\")] = 0;");
                break;

            case INT:
                appendLine("scanf(\"%d\", &" + node.getNome() + ");");
                break;

            case REAL:
                appendLine("scanf(\"%lf\", &" + node.getNome() + ");");
                break;

            case BOOL:
                appendLine("scanf(\"%d\", &" + node.getNome() + ");");
                break;
        }
    }

  
    @Override
    public void visit(PrintNode node) {

        if (node.getLiteral() != null) {
            appendLine("printf(\"%s\\n\", \"" + node.getLiteral() + "\");");
            return;
        }

        ExprNode expr = node.getExpressao();
        String exprC = visit(expr);
        TipoDado tipo = tipoExpr(expr);

        switch (tipo) {
            case INT:
                appendLine("printf(\"%d\\n\", " + exprC + ");");
                break;

            case REAL:
                appendLine("printf(\"%f\\n\", " + exprC + ");");
                break;

            case BOOL:
                appendLine("printf(\"%s\\n\", (" + exprC + " ? \"true\" : \"false\"));");
                break;

            case STRING:
                appendLine("printf(\"%s\\n\", " + exprC + ");");
                break;
        }
    }

    
    @Override
    public String visit(VarExprNode node) {
        return node.getNome();
    }

    @Override
    public String visit(IntLiteralNode node) {
        return Integer.toString(node.getValor());
    }

    @Override
    public String visit(RealLiteralNode node) {
        return Double.toString(node.getValor());
    }

    @Override
    public String visit(BoolLiteralNode node) {
        return node.getValor() ? "1" : "0";
    }

    @Override
    public String visit(StringLiteralExprNode node) {
        return "\"" + node.getValor() + "\"";
    }

    @Override
    public String visit(UnaryExprNode node) {
        String expr = visit(node.getExpr());
        if (node.getOperador().equals("-"))
            return "(-" + expr + ")";
        return "(" + node.getOperador() + " " + expr + ")";
    }

    @Override
    public String visit(BinaryExprNode node) {
        String left = visit(node.getEsquerda());
        String right = visit(node.getDireita());
        String op = node.getOperador();

        switch (op) {
            case "and": op = "&&"; break;
            case "or": op = "||"; break;
        }

        return "(" + left + " " + op + " " + right + ")";
    }
}
