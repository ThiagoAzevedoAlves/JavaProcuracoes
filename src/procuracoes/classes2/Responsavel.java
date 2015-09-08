/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procuracoes.classes2;

import procuracoes.classes.*;

/**
 * @author Thiago
 */

public class Responsavel {
    int id;
    public String nome;
    public String cpf;
    
    public Responsavel(String nome){
        this.nome = nome;
    }
    
    public void setNome(String responsavel) {
        this.nome = responsavel;
    }

    public String getNome() {
        return nome;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public String getCpf() {
        return cpf;
    }
    
}
