package ast;

/**
 *
 * @author laris
 */
public class ReadNode implements StmtNode {

    private final String nome;

    public ReadNode(String nome) {
        this.nome = nome;
    }

    public String getNome() { return nome; }
}
