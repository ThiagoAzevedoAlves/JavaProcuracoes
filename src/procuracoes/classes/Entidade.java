/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procuracoes.classes;

/**
 *
 * @author Thiago
 */
public class Entidade {
    public int id;
    public String nome;
    public String responsavel;
    public String cpf;
    public String cnpj;

    public Entidade(String nome, String responsavel, String cpf, String cnpj) {
        this.nome = nome;
        this.responsavel = responsavel;
        this.cpf = cpf;
        this.cnpj = cnpj;
    }

    public Entidade() {
    }
    
    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }
}
