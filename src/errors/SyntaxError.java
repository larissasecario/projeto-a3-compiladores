package errors;

/**
 *
 * @author laris
 */
public class SyntaxError extends CompilerException {
    public SyntaxError(String msg) {
        super("Erro sintático: " + msg);
    }
}
