package ast;

/**
 *
 * @author laris
 */
public class BoolLiteralNode implements ExprNode {

    private final boolean valor;

    public BoolLiteralNode(boolean valor) {
        this.valor = valor;
    }

    public boolean getValor() { return valor; }
}
