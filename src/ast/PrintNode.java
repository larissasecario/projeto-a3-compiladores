package ast;

/**
 *
 * @author laris
 */
public class PrintNode implements StmtNode {

    private final ExprNode expressao; // null se for string literal
    private final String literal;

    public PrintNode(ExprNode expressao) {
        this.expressao = expressao;
        this.literal = null;
    }

    public PrintNode(String literal) {
        this.literal = literal;
        this.expressao = null;
    }

    public ExprNode getExpressao() { return expressao; }
    public String getLiteral() { return literal; }
}