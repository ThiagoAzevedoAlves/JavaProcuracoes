/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procuracoes.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import procuracoes.classes.Entidade;
import procuracoes.classes.Procurador;

/**
 *
 * @author Thiago
 */
public class Dataproc extends Database{
    

    //Retorna uma lista com todos os PROCURADORES da procuracao passada como parametro---------------------------------------------------------------------//    
    public List<Procurador> getProcuradores(int procod){
        
        List<Procurador> ret = new ArrayList<>();
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT * FROM procurador where procurador.id= any(SELECT idprocurador from procuracao where procuracao.idgeral = ?)");
            prepared.setInt(1, procod);
            resultSet = prepared.executeQuery();
            Procurador p;
            while(resultSet.next()){
                p = new Procurador(resultSet.getString("nome"), resultSet.getString("cpf"), resultSet.getString("poderes"));
                p.setId(resultSet.getInt("id"));
                ret.add(p);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "GETPROCURADORES - "+ e.getMessage());
        }
        return ret;
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//

    //Abre uma tabela com o resultado da pesquisa de Procuradores por nome---------------------------------------------------------------------------------//        
    public void getProcuradoresbyNome(String nome, String usuario) {
        String[] colunas = new String[]{"Nome","Data Inicial", "Data Final", "Caminho"}; //seta o cabeçalho
        List <Procurador> p = new ArrayList<>();
        try{ //seleciona atributos das procuraçoes que contenham um procurador com o nome parecido com o digitado
            
            PreparedStatement prepared; //seleciona dados do procurador pelo nome, preparando o conexao no proximo comando
            prepared = conn.prepareStatement("SELECT DISTINCT procuracao.idprocurador, procuracao.caminho, procuracao.dtinicial, procuracao.dtfinal from procuracao where procuracao.idprocurador = any(SELECT id FROM procurador where procurador.nome like ?);");
            prepared.setString(1, nome.concat("%"));
            resultSet = prepared.executeQuery();
            
            Procurador proc = new Procurador();
            int[] n = new int[200];
            String[] c = new String[200]; //variavel responsavel pelos caminhos da procuracao
            String[] di = new String[200];//variavel responsavel pelas datas iniciais da procuração
            String[] df = new String[200];//variavel responsavel pelas datas finais da procuração
            int i;
            i = 0;
            while(resultSet.next()){ //pega o código dos procuradores com o nome parecido
                n[i] = resultSet.getInt(1);
                c[i] = resultSet.getString(2);
                if (resultSet.getDate(3) != null){ //se houver uma data inicial
                    di[i] = resultSet.getDate(3).toString(); //di[i] = data inicial
                }else{ //senão
                    di[i] = ""; //deixa di[i] em branco
                }
                if (resultSet.getDate(4) != null){ //se houver uma data final
                    df[i] = resultSet.getDate(4).toString(); //df[i] = data final
                }else{ //senao
                    df[i] = ""; // deixa df[i] em branco
                }
                i++;
            }
            int j = i;
            i--;
            while (i>=0){
                prepared = conn.prepareStatement("SELECT * from procurador where procurador.id = ?");
                prepared.setInt(1, n[i]);
                resultSet = prepared.executeQuery();
                while(resultSet.next()){ //pega o os dados do procurados
                    proc = new Procurador(resultSet.getString("nome"), resultSet.getString("cpf"), resultSet.getString("poderes"));
                    proc.setId(resultSet.getInt("id"));
                    p.add(proc);
                }
                i--;
            }
            i++;
            String dados[][] = new String[j][4];
        //desinverte a seleção de procuradores----------------------------------------------------------------------//
            List<Procurador> procaux = new ArrayList<>();            
            while(i < j){
                procaux.add(new Procurador(p.get(i).getNome(), p.get(i).getCpf(), p.get(i).getPoderes()));
                i++;
            }
            i = 0;
        //---------------------------------------------------------------------------------------------------------//
            while(i<j){
                dados[i][0] = procaux.get(i).getNome();
                dados[i][1] = di[i];
                dados[i][2] = df[i];
                dados[i][3] = c[i];
                i++;
            }
            
            criaTabela(dados, colunas, "Busca de Procuradores pelo Nome", usuario);
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "BUSCAPROCURADOR\n" +e.getMessage());
        }catch(NegativeArraySizeException e){
            JOptionPane.showMessageDialog(null, "Nenhum Procurador encontrado com os dados inseridos.");
        }
        
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//    
    //Abre uma tabela com o resultado da pesquisa de Procuradores por cpf----------------------------------------------------------------------------------//
    public void getProcuradoresbyCpf(String cpf, String usuario) {
        String[] colunas = new String[]{"Nome", "Cpf","Data Inicial", "Data Final", "Caminho"};
        List <Procurador> p = new ArrayList<>();
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT DISTINCT procuracao.idprocurador, procuracao.caminho, procuracao.dtinicial, procuracao.dtfinal from procuracao where procuracao.idprocurador = any(SELECT id FROM procurador where procurador.cpf like ?);");
            prepared.setString(1, cpf);
            resultSet = prepared.executeQuery();
            
            Procurador proc = new Procurador();
            int[] n = new int[200];
            String[] c = new String[200];
            String[] di = new String[200];
            String[] df = new String[200];
            int i;
            i = 0;
            while(resultSet.next()){ //pega o código dos procuradores com o nome parecido
                n[i] = resultSet.getInt(1);
                c[i] = resultSet.getString(2);
                if (resultSet.getDate(3) != null){
                    di[i] = resultSet.getDate(3).toString();
                }else{
                    di[i] = "";
                }
                if (resultSet.getDate(4) != null){
                    df[i] = resultSet.getDate(4).toString();
                }else{
                    df[i] = "";
                }
                i++;
            }
            int j = i;
            i--;
            while (i>=0){
                prepared = conn.prepareStatement("SELECT * from procurador where procurador.id = ?");
                prepared.setInt(1, n[i]);
                resultSet = prepared.executeQuery();
                while(resultSet.next()){ //pega o os dados do procurados
                    proc = new Procurador(resultSet.getString("nome"), resultSet.getString("cpf"), resultSet.getString("poderes"));
                    proc.setId(resultSet.getInt("id"));
                    p.add(proc);
                }
                i--;
            }
            i++;
            String dados[][] = new String[j][5];
        //desinverte a seleção de procuradores----------------------------------------------------------------------//
            List<Procurador> procaux = new ArrayList<>();            
            while(i < j){
                procaux.add(new Procurador(p.get(i).getNome(), p.get(i).getCpf(), p.get(i).getPoderes()));
                i++;
            }
            i = 0;
        //---------------------------------------------------------------------------------------------------------//
            while(i < j){
                dados[i][0] = p.get(i).getNome();
                dados[i][1] = p.get(i).getCpf();
                dados[i][2] = di[i];
                dados[i][3] = df[i];
                dados[i][4] = c[i];
                i++;
            }
            
            criaTabela(dados, colunas, "Busca de Procuradores por CPF", usuario);
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "BUSCAPROCURADOR\n" +e.getMessage());
        }catch(NegativeArraySizeException e){
            JOptionPane.showMessageDialog(null, "Nenhum Procurador encontrado com os dados inseridos.");
        }
        
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//
    //Retorna o ID de um porcurador pelo Nome--------------------------------------------------------------------------------------------------------------//
    public int getIdbyNomeProc(String procurador){
        int ret = 0;
        try {
            PreparedStatement prepared = conn.prepareStatement("SELECT id from procurador WHERE nome=?");
            prepared.setString(1, procurador);
            resultSet =  prepared.executeQuery();
            while(resultSet.next()){
                ret = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
        return ret;
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//
    //Retorna o ID de um porcurador pelo Cpf---------------------------------------------------------------------------------------------------------------//
    public int getIdbyCpfProc(String cpf){
        int ret = 0;
        try {
            PreparedStatement prepared = conn.prepareStatement("SELECT id from procurador WHERE cpf=?");
            prepared.setString(1, cpf);
            resultSet =  prepared.executeQuery();
            while(resultSet.next()){
                ret = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
        return ret;
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//
    //Retorna o ID de um porcuradorna Procuracao(idgeral) pelo Nome----------------------------------------------------------------------------------------//
    public int getIdExatoProc(int idgeral, String nome){
        int ret = 0;
        try {
            PreparedStatement prepared = conn.prepareStatement("SELECT procurador.id from procurador where (procurador.id = any (SELECT procuracao.idprocurador from procuracao where procuracao.idgeral= ?)) and (procurador.nome=?)");
            prepared.setInt(1, idgeral);
            prepared.setString(2, nome);
            resultSet =  prepared.executeQuery();
            while(resultSet.next()){
                ret = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
        return ret;
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//
    //Define o Cpf do Procurador(id)-----------------------------------------------------------------------------------------------------------------------//
    public void setCpfProcurador(String cpf, int id){
        try {
            PreparedStatement prepared = conn.prepareStatement("UPDATE procurador SET procurador.cpf =? WHERE procurador.id=?");
            prepared.setString(1, cpf);
            prepared.setInt(2, id);
            
            if (prepared.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Cpf do Procurador alterado com sucesso!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }        
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//
    //Define o Nome do Procurador(id)----------------------------------------------------------------------------------------------------------------------//
    public void setNomeProcurador(String nome, int id){
        try {
            PreparedStatement prepared = conn.prepareStatement("UPDATE procurador SET procurador.nome =? WHERE procurador.id=?");
            prepared.setString(1, nome);
            prepared.setInt(2, id);
            
            if (prepared.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Nome do Procurador alterado com sucesso!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }        
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//
    //Define os Poderes do Procurador(id)------------------------------------------------------------------------------------------------------------------//
    public void setPoderesProcurador(String poderes, int id){
        try {
            PreparedStatement prepared = conn.prepareStatement("UPDATE procurador SET procurador.poderes =? WHERE procurador.id=?");
            prepared.setString(1, poderes);
            prepared.setInt(2, id);
            
            if (prepared.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Poderes do Procurador alterado com sucesso!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }        
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//
    
    
    public int apaga(String nome, int idgeral){
        int ret = 0;
        try {
            String SQLset = "SET SQL_SAFE_UPDATES = 0;";
            String SQL = "DELETE from procurador where (procurador.id= any(SELECT procuracao.idprocurador from procuracao where procuracao.idgeral=?)) and (procurador.nome=?);";
            PreparedStatement prepared = conn.prepareStatement(SQLset);
            prepared.executeUpdate();
            prepared = conn.prepareStatement(SQL);
            prepared.setInt(1, idgeral);
            prepared.setString(2, nome);
            prepared.executeUpdate();
            ret = 1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null," APAGA PROCURADOR - Erro SQL - " + ex);
        }
        return ret;
    }

    public int add(String nome, String cpf, String poderes, int idgeral) {
        int ret = 0;
        String SQL = "INSERT into procurador(id, nome, cpf, poderes) VALUES(default, ?, ?, ?)";
        try {            
            PreparedStatement prepared = conn.prepareStatement(SQL);
            prepared.setString(1, nome);
            prepared.setString(2, cpf);
            prepared.setString(3, poderes);
            prepared.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null," ADD PROCURADOR - Erro SQL - " + ex);
        }
        Dataent de = new Dataent();
        de.connect();
        List<Entidade> le = new ArrayList<>();
        le.addAll(de.getEntidades(idgeral));
        int i = 0;
        SQL = "INSERT into procuracao(id, idprocurador, identidade, dtinicial, dtfinal, conjunto, caminho, idgeral) VALUES(default, ?, ?, ?, ?, ?, ?, ?);";
        while(i<le.size()){
            try{
                PreparedStatement prepared = conn.prepareStatement(SQL);
                prepared.setInt(1, this.getLastProc());
                prepared.setInt(2, le.get(i).getId());
                prepared.setString(3, this.getDtini(idgeral));
                prepared.setString(4, this.getDtfin(idgeral));
                prepared.setString(5, this.getConjunto(idgeral));
                prepared.setString(6, this.getCaminho(idgeral));
                prepared.setInt(7, idgeral);
                prepared.executeUpdate();
                ret = 1;
                JOptionPane.showMessageDialog(null,"Procurador Adicionado com Sucesso!");
            }catch(SQLException ex){
                JOptionPane.showMessageDialog(null," ADD PROCURADOR - Erro SQL - " + ex);
            }
            i++;
        }
        return ret;
    }
    
    public int add(String nome, String poderes, int idgeral) {
        int ret = 0;
        String SQL = "INSERT into procurador(id, nome, cpf, poderes) VALUES(default, ?, null, ?)";
        try {            
            PreparedStatement prepared = conn.prepareStatement(SQL);
            prepared.setString(1, nome);
            prepared.setString(2, poderes);
            prepared.executeUpdate();
            ret = 1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null," ADD PROCURADOR - Erro SQL - " + ex);
        }
        Dataent de = new Dataent();
        de.connect();
        List<Entidade> le = new ArrayList<>();
        le.addAll(de.getEntidades(idgeral));
        int i = 0;
        SQL = "INSERT into procuracao(id, idprocurador, identidade, dtinicial, dtfinal, conjunto, caminho, idgeral) VALUES(default, ?, ?, ?, ?, ?, ?, ?);";
        while(i<le.size()){
            try{
                PreparedStatement prepared = conn.prepareStatement(SQL);
                prepared.setInt(1, this.getLastProc());
                prepared.setInt(2, le.get(i).getId());
                prepared.setString(3, this.getDtini(idgeral));
                prepared.setString(4, this.getDtfin(idgeral));
                prepared.setString(5, this.getConjunto(idgeral));
                prepared.setString(6, this.getCaminho(idgeral));
                prepared.setInt(7, idgeral);
                prepared.executeUpdate();
                ret = 1;
                JOptionPane.showMessageDialog(null,"Procurador Adicionado com Sucesso!");
            }catch(SQLException ex){
                JOptionPane.showMessageDialog(null," ADD PROCURADOR - Erro SQL - " + ex);
            }
            i++;
        }
        return ret;
    }
    
}
