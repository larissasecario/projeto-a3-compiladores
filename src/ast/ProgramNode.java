package ast;


import java.util.List;

/**
 *
 * @author laris
 */
public class ProgramNode implements Node {

    private final List<StmtNode> statements;

    public ProgramNode(List<StmtNode> statements) {
        this.statements = statements;
    }

    public List<StmtNode> getStatements() {
        return statements;
    }
}