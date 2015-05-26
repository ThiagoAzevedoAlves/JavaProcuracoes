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
import javax.swing.JOptionPane;
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
    
    //Retorna o índice do último Procurador na tabela------------------------------------------------------------------------------------------------------//    
    public int getLastProc(){
        try {
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT MAX(id) FROM procurador");
            resultSet = prepared.executeQuery();
            int n;
            while(resultSet.next()){
                n = resultSet.getInt(1);
                return n;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "GETLASTPROC - "+ e.getMessage());
        }
        return -1;
    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------//

//Abre uma tabela com o resultado da pesquisa de Procuradores por nome---------------------------------------------------------------------------------//        
    public void getProcuradoresbyNome(String nome) {
        String[] colunas = new String[]{"Nome","Data Inicial", "Data Final", "Caminho"}; //seta o cabeçalho
        List <Procurador> p = new ArrayList<>();
        try{ //seleciona atributos das procuraçoes que contenham um procurador com o nome parecido com o digitado
            
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT DISTINCT procuracao.idprocurador, procuracao.caminho, procuracao.dtinicial, procuracao.dtfinal from procuracao where procuracao.idprocurador = any(SELECT id FROM procurador where procurador.nome like ?);");
            prepared.setString(1, nome.concat("%"));
            resultSet = prepared.executeQuery();
            
            Procurador proc = new Procurador();
            int[] n = new int[20];
            String[] c = new String[20];
            String[] di = new String[20];
            String[] df = new String[20];
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
            String dados[][] = new String[j][4];
            while(i < j){
                dados[i][0] = p.get(i).getNome();
                dados[i][1] = di[i];
                dados[i][2] = df[i];
                dados[i][3] = c[i];
                i++;
            }
            
            criaTabela(dados, colunas, "Busca de Procuradores pelo Nome");
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "BUSCAPROCURADOR\n" +e.getMessage());
        }catch(NegativeArraySizeException e){
            JOptionPane.showMessageDialog(null, "Nenhum Procurador encontrado com os dados inseridos.");
        }
        
    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------//    
    
    
    
    public void getProcuradoresbyCpf(String cpf) {
        String[] colunas = new String[]{"Nome", "Cpf","Data Inicial", "Data Final", "Caminho"};
        List <Procurador> p = new ArrayList<>();
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT DISTINCT procuracao.idprocurador, procuracao.caminho, procuracao.dtinicial, procuracao.dtfinal from procuracao where procuracao.idprocurador = any(SELECT id FROM procurador where procurador.cpf like ?);");
            prepared.setString(1, cpf);
            resultSet = prepared.executeQuery();
            
            Procurador proc = new Procurador();
            int[] n = new int[20];
            String[] c = new String[20];
            String[] di = new String[20];
            String[] df = new String[20];
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
            while(i < j){
                dados[i][0] = p.get(i).getNome();
                dados[i][1] = p.get(i).getCpf();
                dados[i][2] = di[i];
                dados[i][3] = df[i];
                dados[i][4] = c[i];
                i++;
            }
            
            criaTabela(dados, colunas, "Busca de Procuradores por CPF");
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "BUSCAPROCURADOR\n" +e.getMessage());
        }catch(NegativeArraySizeException e){
            JOptionPane.showMessageDialog(null, "Nenhum Procurador encontrado com os dados inseridos.");
        }
        
    }
    
    
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

    
}
