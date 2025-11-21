package codegen;


import ast.*;
import semantics.TipoDado;

import java.util.List;

/**
 *
 * @author laris
 */
public class CodeGeneratorC implements CodeGenVisitor {

    private StringBuilder sb = new StringBuilder();
    private int indent = 0;

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
        sb.append("#include <stdio.h>\n\n");
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
    

    // =====================================
    // PROGRAM
    // =====================================

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

    // =====================================
    // DECLARAÇÃO
    // =====================================

    @Override
    public void visit(VarDeclNode node) {

        String tipoC =
            node.getTipo() == TipoDado.INT ? "int" :
            node.getTipo() == TipoDado.REAL ? "double" :
            node.getTipo() == TipoDado.BOOL ? "int" :  // bool → int
            "char*";                                    // string

        StringBuilder line = new StringBuilder(tipoC + " ");

        for (int i = 0; i < node.getNomes().size(); i++) {
            line.append(node.getNomes().get(i));
            if (i < node.getNomes().size() - 1)
                line.append(", ");
        }

        line.append(";");

        appendLine(line.toString());
    }

    // =====================================
    // ATRIBUIÇÃO
    // =====================================

    @Override
    public void visit(AssignNode node) {
        String exprC = visit(node.getExpressao());
        appendLine(node.getNome() + " = " + exprC + ";");
    }

    // =====================================
    // IF
    // =====================================

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

    // =====================================
    // WHILE
    // =====================================

    @Override
    public void visit(WhileNode node) {

        String condC = visit(node.getCondicao());

        openBlock("while (" + condC + ")");
        for (StmtNode s : node.getCorpo())
            visitStmt(s);
        closeBlock();
    }

    // =====================================
    // FOR
    // =====================================

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

    // =====================================
    // READ
    // =====================================

    @Override
    public void visit(ReadNode node) {

        String nome = node.getNome();
        String fmt;

        // Aqui usamos um truque: bool = int, string = char*
        // O semantic já garantiu tipos válidos
        // (Você pode estender isso)

        fmt = "%d"; // padrão

        appendLine("scanf(\"" + fmt + "\", &" + nome + ");");
    }

    // =====================================
    // PRINT
    // =====================================

    @Override
    public void visit(PrintNode node) {

        if (node.getLiteral() != null) {
            appendLine("printf(\"%s\\n\", \"" + node.getLiteral() + "\");");
        }
        else {
            String exprC = visit(node.getExpressao());
            appendLine("printf(\"%f\\n\", " + exprC + ");");
        }
    }

    // =====================================
    // EXPRESSÕES
    // =====================================

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
        return "(" + node.getOperador() + " " + expr + ")";
    }

    @Override
    public String visit(BinaryExprNode node) {

        String left = visit(node.getEsquerda());
        String right = visit(node.getDireita());
        String op = node.getOperador();

        // Lógica para operadores da linguagem → C
        switch (op) {
            case "and": op = "&&"; break;
            case "or": op = "||"; break;
        }

        return "(" + left + " " + op + " " + right + ")";
    }
}
