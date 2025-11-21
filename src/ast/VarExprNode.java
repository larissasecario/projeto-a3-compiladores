package ast;

/**
 *
 * @author laris
 */
public class VarExprNode implements ExprNode {

    private final String nome;

    public VarExprNode(String nome) {
        this.nome = nome;
    }

    public String getNome() { return nome; }
}
