package ast;

/**
 *
 * @author laris
 */
public class AssignNode implements StmtNode {

    private final String nome;
    private final ExprNode expressao;

    public AssignNode(String nome, ExprNode expressao) {
        this.nome = nome;
        this.expressao = expressao;
    }

    public String getNome() { return nome; }
    public ExprNode getExpressao() { return expressao; }
}
