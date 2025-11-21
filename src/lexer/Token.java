/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lexer;

import lexer.TipoToken;

/**
 *
 * @author laris
 */
public class Token {
    
    // Estrutura de dados do token: <nome, lexema>
    private TipoToken tipo; //Um dos tipos criados na Classe TipoToken
    private String lexema; // O lexema desse tipo
    
    
    // Construtor
    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
    }
    
    //Metodos get
    public TipoToken getTipo() {
        return tipo;
    }

    public String getLexema() {
        return lexema;
    }
    
    
    @Override
    public String toString() {
        return String.format("< %s, %s >", this.tipo, this.lexema);
    }
    
}
