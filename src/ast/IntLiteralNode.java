package ast;

/**
 *
 * @author laris
 */
public class IntLiteralNode implements ExprNode {

    private final int valor;

    public IntLiteralNode(int valor) {
        this.valor = valor;
    }

    public int getValor() { return valor; }
}
