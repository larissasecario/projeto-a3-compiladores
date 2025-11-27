/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import lexer.SnipPyLexico;
import parser.SnipPyParser;
import ast.ProgramNode;
import semantics.SemanticAnalyzer;
import codegen.CodeGeneratorC;
import errors.*;

/**
 *
 * @author laris
 */
public class CompiladorSnipPy {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        System.setErr(new java.io.PrintStream(System.err, true, java.nio.charset.StandardCharsets.UTF_8));

        System.out.println(" ==================================== COMPILADOR SnipPy ==================================== ");

        if (args.length == 0) {
            System.err.println("Erro: Nenhum arquivo informado.");
            return;
        }

        String arquivo = args[0];
        System.out.println("Nome do arquivo de entrada: " + arquivo);
        System.out.println();
        System.out.println(" ------ Iniciando compilacao para linguagem SnipPy ------ ");
        System.out.println();

        try {
            // LÉXICO
            SnipPyLexico lexico = new SnipPyLexico(arquivo);

            // PARSER → AST
            SnipPyParser parser = new SnipPyParser(lexico);
            ProgramNode ast = parser.parseProgram();

            // ANÁLISE SEMÂNTICA
            SemanticAnalyzer sem = new SemanticAnalyzer();
            sem.visit(ast);

            System.out.println();
            System.out.println(" ------ ANALISE SEMANTICA CONCLUIDA ------ ");
            System.out.println();

            // GERADOR DE C
            CodeGeneratorC gen = new CodeGeneratorC(sem.getTabela());
            String codigoC = gen.gerarCodigo(ast);

            System.out.println(" ------ CODIGO C GERADO ------ ");
            System.out.println();
            System.out.println(codigoC);

            System.out.println(" ==================================== COMPILACAO CONCLUIDA COM SUCESSO ==================================== ");

        } catch (LexicalError e) {
            System.err.println("Erro léxico: " + e.getMessage());
        } catch (SyntaxError e) {
            System.err.println("Erro sintático: " + e.getMessage());
        } catch (SemanticError e) {
            System.err.println("Erro semântico: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
