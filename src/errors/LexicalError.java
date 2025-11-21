package errors;

/**
 *
 * @author laris
 */
public class LexicalError extends CompilerException {
    public LexicalError(String msg) {
        super("Erro léxico: " + msg);
    }
}
