package ast;

import java.util.List;

/**
 *
 * @author laris
 */
public class ForNode implements StmtNode {

    private final AssignNode init;
    private final ExprNode condicao;
    private final AssignNode incremento;
    private final List<StmtNode> corpo;

    public ForNode(AssignNode init, ExprNode condicao, AssignNode incremento, List<StmtNode> corpo) {
        this.init = init;
        this.condicao = condicao;
        this.incremento = incremento;
        this.corpo = corpo;
    }

    public AssignNode getInit() { return init; }
    public ExprNode getCondicao() { return condicao; }
    public AssignNode getIncremento() { return incremento; }
    public List<StmtNode> getCorpo() { return corpo; }
}
