/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package semantics;

import semantics.Simbolo;
import semantics.TipoDado;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author laris
 */
public class TabelaSimbolos {

    private Map<String, Simbolo> tabela = new HashMap<>();

    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }

    public void inserir(String nome, TipoDado tipo) {
        tabela.put(nome, new Simbolo(nome, tipo));
    }

    public Simbolo buscar(String nome) {
        return tabela.get(nome);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Tabela de Símbolos:\n");
        for (Simbolo s : tabela.values()) {
            sb.append("  ").append(s).append("\n");
        }
        return sb.toString();
    }
}