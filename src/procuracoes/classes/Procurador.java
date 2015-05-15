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
public class Procurador {
    public int id;
    public String nome;
    public String cpf;
    public String poderes;

    public Procurador(String nome, String cpf, String poderes) {
        this.nome = nome;
        this.cpf = cpf;
        this.poderes = poderes;
    }

    public Procurador() {
        this.id = 0;
        this.nome = null;
        this.cpf = null;
        this.poderes = null;
    }

    public void setPoderes(String poderes) {
        this.poderes = poderes;
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

    public String getPoderes() {
        return poderes;
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
