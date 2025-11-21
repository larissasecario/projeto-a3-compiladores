package errors;

/**
 *
 * @author laris
 */
public class SemanticError extends CompilerException {
    public SemanticError(String msg) {
        super("Erro semântico: " + msg);
    }
}