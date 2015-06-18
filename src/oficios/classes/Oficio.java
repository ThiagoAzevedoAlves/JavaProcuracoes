/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oficios.classes;

/**
 * Classe que mapeia o Objeto Oficio com o Banco de Dados.
 * @author Thiago Azevedo Alves
 */
public class Oficio {
    public int id;
    public int numero;
    public int ano;
    public String caminho;
    public String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    /**
     * 
     * @param i INT Representando o ID do Ofício;
     * @param n INT Representando o NUMERO do Oficio;
     * @param a INT Reoresentando o ANO do Oficio;
     * @param c STRING Representando o CAMINHO do Oficio;
     * @param d STRING Representando a DATA do Oficio;
     */
    public Oficio(int i, int n, int a, String c, String d){
        this.id = i;
        this.numero = n;
        this.ano = a;
        this.caminho = c;
        this.data = d;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }
    
}


