package codegen;

import ast.*;
/**
 *
 * @author laris
 */


public interface CodeGenVisitor {

    // STATEMENTS
    void visit(ProgramNode node);
    void visit(VarDeclNode node);
    void visit(AssignNode node);
    void visit(IfNode node);
    void visit(WhileNode node);
    void visit(ForNode node);
    void visit(ReadNode node);
    void visit(PrintNode node);

    // EXPRESSÕES — TODOS DEVEM RETORNAR STRING
    String visit(ExprNode node);                // MÉTODO GENÉRICO
    String visit(VarExprNode node);
    String visit(IntLiteralNode node);
    String visit(RealLiteralNode node);
    String visit(BoolLiteralNode node);
    String visit(StringLiteralExprNode node);
    String visit(UnaryExprNode node);
    String visit(BinaryExprNode node);
}