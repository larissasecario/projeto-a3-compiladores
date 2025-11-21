package ast;

import java.util.List;
import semantics.TipoDado;

/**
 *
 * @author laris
 */
public class VarDeclNode implements StmtNode {

    private final TipoDado tipo;
    private final List<String> nomes;

    public VarDeclNode(TipoDado tipo, List<String> nomes) {
        this.tipo = tipo;
        this.nomes = nomes;
    }

    public TipoDado getTipo() { return tipo; }
    public List<String> getNomes() { return nomes; }
}
