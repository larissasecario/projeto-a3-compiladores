package ast;

import java.util.List;
/**
 *
 * @author laris
 */
public class IfNode implements StmtNode {

    private final ExprNode condicao;
    private final List<StmtNode> blocoThen;
    private final List<StmtNode> blocoElse;

    public IfNode(ExprNode condicao, List<StmtNode> blocoThen, List<StmtNode> blocoElse) {
        this.condicao = condicao;
        this.blocoThen = blocoThen;
        this.blocoElse = blocoElse;
    }

    public ExprNode getCondicao() { return condicao; }
    public List<StmtNode> getBlocoThen() { return blocoThen; }
    public List<StmtNode> getBlocoElse() { return blocoElse; }
}
