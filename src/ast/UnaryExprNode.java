package ast;

/**
 *
 * @author laris
 */
public class UnaryExprNode implements ExprNode {

    private final String operador;
    private final ExprNode expr;

    public UnaryExprNode(String operador, ExprNode expr) {
        this.operador = operador;
        this.expr = expr;
    }

    public String getOperador() { return operador; }
    public ExprNode getExpr() { return expr; }
}
