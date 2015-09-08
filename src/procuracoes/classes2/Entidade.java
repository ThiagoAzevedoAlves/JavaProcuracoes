/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procuracoes.classes2;

/**
 *
 * @author Thiago
 */
public class Entidade {
    public int id;
    public String nome;
    public String cnpj;

    public Entidade(String nome, String cnpj) {
        this.nome = nome;
        this.cnpj = cnpj;
    }

    public Entidade() {
        
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }
    
    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }
}
