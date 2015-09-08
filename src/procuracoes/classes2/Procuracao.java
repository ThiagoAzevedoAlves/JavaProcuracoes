/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procuracoes.classes2;

import procuracoes.classes.*;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Thiago
 */
public class Procuracao {
    public int id;
    public int qtproc;
    public int qtent;
    public int qtresp;
    public List<Procurador> procuradores;
    public List<Entidade> entidades;
    public List<Responsavel> responsaveis;
    public String dtini;
    public String dtfin;
    public String conjunto;
    public String caminho;

    public Procuracao() {
    }

    public Procuracao(int id, int qtproc, int qtent, int qtresp, List<Procurador> procuradores, List<Entidade> entidades, List<Responsavel> responsaveis ,String dtini, String dtfin, String conjunto, String caminho) {
        this.id = id;
        this.qtproc = qtproc;
        this.qtent = qtent;
        this.qtresp = qtresp;
        this.procuradores = procuradores;
        this.entidades = entidades;
        this.responsaveis = responsaveis;
        this.dtini = dtini;
        this.dtfin = dtfin;
        this.conjunto = conjunto;
        this.caminho = caminho;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setResponsaveis(List<Responsavel> responsaveis) {
        this.responsaveis = responsaveis;
    }
    
    public void setEntidades(List<Entidade> entidades) {
        this.entidades = entidades;
    }

    public void setProcuradores(List<Procurador> procuradores) {
        this.procuradores = procuradores;
    }

    public void setQtproc(int qtproc) {
        this.qtproc = qtproc;
    }
    
    public void setQtent(int qtent) {
        this.qtent = qtent;
    }
    
    public void setQtresp(int qtresp) {
        this.qtresp = qtresp;
    }
    
    public int getQtresp() {
        return qtresp;
    }
    
    public int getQtent() {
        return qtent;
    }
    
    public void setDtini(String dtini) {
        this.dtini = dtini;
    }

    public void setDtfin(String dtfin) {
        this.dtfin = dtfin;
    }

    public void setConjunto(String conjunto) {
        this.conjunto = conjunto;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public int getId() {
        return id;
    }
    
    public List<Responsavel> getResponsaveis() {
        return responsaveis;
    }
    
    public List<Procurador> getProcuradores() {
        return procuradores;
    }

    public List<Entidade> getEntidades() {
        return entidades;
    }

    public int getQtproc() {
        return qtproc;
    }

    public String getDtini() {
        return dtini;
    }

    public String getDtfin() {
        return dtfin;
    }

    public String getConjunto() {
        return conjunto;
    }

    public String getCaminho() {
        return caminho;
    }
    
    public void addProcurador(Procurador p){
        this.setQtproc(qtproc+1);
        this.procuradores.add(p);
    }
    
    public void addResponsavel(Responsavel r){
        this.setQtresp(qtresp+1);
        this.responsaveis.add(r);
    }
}
