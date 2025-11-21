package ast;


import java.util.List;
/**
 *
 * @author laris
 */
public class WhileNode implements StmtNode {

    private final ExprNode condicao;
    private final List<StmtNode> corpo;

    public WhileNode(ExprNode condicao, List<StmtNode> corpo) {
        this.condicao = condicao;
        this.corpo = corpo;
    }

    public ExprNode getCondicao() { return condicao; }
    public List<StmtNode> getCorpo() { return corpo; }
}
