package ast;

/**
 *
 * @author laris
 */
public class RealLiteralNode implements ExprNode {

    private final double valor;

    public RealLiteralNode(double valor) {
        this.valor = valor;
    }

    public double getValor() { return valor; }
}
