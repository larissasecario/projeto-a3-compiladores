package ast;

/**
 *
 * @author laris
 */
public class StringLiteralExprNode implements ExprNode {

    private final String valor;

    public StringLiteralExprNode(String valor) {
        this.valor = valor;
    }

    public String getValor() { return valor; }
}
