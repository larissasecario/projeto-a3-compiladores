/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author laris
 */
public class LeitorArquivosTexto {
    
    private final static int TAMANHO_BUFFER = 20;
    int[] bufferLeitor;
    int ponteiro;
    int bufferAtual;
    int inicioLexema;
    private String lexema;
    
    InputStream is;
    
    public LeitorArquivosTexto(String arquivo){
        try {
            this.is = new FileInputStream(new File(arquivo));
            inicializarBuffler();
        } catch (FileNotFoundException ex) {
            System.getLogger(LeitorArquivosTexto.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    private void inicializarBuffler(){
        
        this.bufferAtual = 2;
        this.inicioLexema = 0;
        this.lexema = "";
        // Cada buffer Tamnho 5: Ao criar o vetor bufferLeitor vai ter então 2 Buffer de 5: onde será um buffer de 10
        // Ou seja, no vetor BufferLeitor, vai ser tamanho = 10 com dois buffer a esquerda e direita
        this.bufferLeitor = new int[this.TAMANHO_BUFFER * 2];
        this.ponteiro = 0; // Inicia na primeira posicao o Ponteiro
        
        // Buffer 1 : Esquerda 
        recarregarBuffer1();
    }
    
    private void incrementarPonteiro(){
        this.ponteiro++;
        // Logica Circular
        // Sempre que passar da metade do 1 recarrega o buffer da direira 
        if(this.ponteiro == this.TAMANHO_BUFFER){
            recarregarBuffer2();
            
        // Sempre que termina a metade dois precisa voltar o ponteiro para o começo e recarregar o buffer 1
        }else if(this.ponteiro == this.TAMANHO_BUFFER * 2){
            recarregarBuffer1();
            this.ponteiro = 0;
        }
        
    }
    
    // Ler do arquivo de entrada byte por byte e coloca neste array
    private void recarregarBuffer1(){
        
        if(this.bufferAtual == 2){
            this.bufferAtual = 1;        
            for(int i=0; i<this.TAMANHO_BUFFER; i++){
                try {
                    this.bufferLeitor[i] = this.is.read(); // Vai ler do arquivo e jogar no buffer de leitura

                    // Significa que chegou no fim de arquivo
                    if(this.bufferLeitor[i] == -1){
                        break;
                    }
                } catch (IOException ex) {
                    System.getLogger(LeitorArquivosTexto.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
        
            }
        }
    }
    
    private void recarregarBuffer2(){
        
        if(this.bufferAtual ==1){
            this.bufferAtual = 2;
            
            for(int i=this.TAMANHO_BUFFER; i<this.TAMANHO_BUFFER * 2; i++){
                try {
                    this.bufferLeitor[i] = this.is.read(); // Vai ler do arquivo e jogar no buffer de leitura

                    // Significa que chegou no fim de arquivo
                    if(this.bufferLeitor[i] == -1){
                        break;
                    }
                } catch (IOException ex) {
                    System.getLogger(LeitorArquivosTexto.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
        }
    }
    
    private int lerCaracterDoBuffer(){
        int ret = this.bufferLeitor[this.ponteiro];
        incrementarPonteiro();
        return ret;
    }
    
    
    // Um metodo para ler um caracter por vez, não é uma boa pratica - Vai retorna um inteiro que é o codigo em binario do caracter lido
    public int lerProximoCaractere(){
        int c = lerCaracterDoBuffer();
        this.lexema += (char)c;
        return c;
     }
    
    public void retroceder(){
        this.ponteiro--;
        this.lexema = this.lexema.substring(0, this.lexema.length() -1);
        if(this.ponteiro < 0){
            this.ponteiro = this.TAMANHO_BUFFER * 2 -1;
        }
    }
    
    public void zerar(){
        this.ponteiro = this.inicioLexema;
        this.lexema = "";
    }
    
    public void confirmar(){
        this.inicioLexema = this.ponteiro;
        this.lexema = "";
    }
    
    public String getLexema(){
        return this.lexema;
    }
    
    @Override
    public String toString(){
        String ret = "Buffer:[";
        
        for(int i: this.bufferLeitor){
            char c = (char) i;
            if(Character.isWhitespace(c)){
                ret+= ' ';
            }else{
                ret+= (char) i;
            }
        }
        ret +="]\n";
        ret +="        ";
        for(int i=0; i<this.TAMANHO_BUFFER * 2; i++){
            if( i == this.inicioLexema && i == this.ponteiro){
                ret+= "%";
            } else if (i== this.inicioLexema){
                ret+="^"; 
            } else if (i== this.ponteiro){
                ret+="*";
            }else{
                ret+=" ";
            }
        }
        return ret;
    }
    
}