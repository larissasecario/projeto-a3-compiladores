/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ast;

import ast.*;
import semantics.TipoDado;
/**
 *
 * @author laris
 */
public interface NodeVisitor {
    void visit(ProgramNode node);
    void visit(VarDeclNode node);
    void visit(AssignNode node);
    void visit(IfNode node);
    void visit(WhileNode node);
    void visit(ForNode node);
    void visit(ReadNode node);
    void visit(PrintNode node);

    // EXPRESSÕES
    TipoDado visit(VarExprNode node);
    TipoDado visit(IntLiteralNode node);
    TipoDado visit(RealLiteralNode node);
    TipoDado visit(BoolLiteralNode node);
    TipoDado visit(StringLiteralExprNode node);
    TipoDado visit(UnaryExprNode node);
    TipoDado visit(BinaryExprNode node);
}