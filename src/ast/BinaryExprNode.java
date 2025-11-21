package ast;

/**
 *
 * @author laris
 */
public class BinaryExprNode implements ExprNode {

    private final ExprNode esquerda;
    private final String operador;
    private final ExprNode direita;

    public BinaryExprNode(ExprNode esquerda, String operador, ExprNode direita) {
        this.esquerda = esquerda;
        this.operador = operador;
        this.direita = direita;
    }

    public ExprNode getEsquerda() { return esquerda; }
    public ExprNode getDireita() { return direita; }
    public String getOperador() { return operador; }
}
