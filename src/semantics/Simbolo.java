/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package semantics;

import semantics.TipoDado;

/**
 *
 * @author laris
 */
public class Simbolo {
    private String nome;
    private TipoDado tipo;

    public Simbolo(String nome, TipoDado tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public TipoDado getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return String.format("<%s : %s>", nome, tipo);
    }
}
