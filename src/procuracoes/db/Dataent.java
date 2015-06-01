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
import procuracoes.classes.Entidade;

/**
 *
 * @author Thiago
 */
public class Dataent extends Database{
    
//Retorna uma lista com todos as ENTIDADES da procuracao passada como parametro------------------------------------------------------------------------//        
    public List<Entidade> getEntidades(int procod){
        
        List<Entidade> ret = new ArrayList<>();
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT * FROM entidade where entidade.id= any(SELECT identidade from procuracao where procuracao.idgeral = ?)");
            prepared.setInt(1, procod);
            resultSet = prepared.executeQuery();
            Entidade e;
            while(resultSet.next()){
                e = new Entidade(resultSet.getString("nome"), resultSet.getString("responsavel"), resultSet.getString("cpf"), resultSet.getString("cnpj"));
                e.setId(resultSet.getInt("id"));
                ret.add(e);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "GETENTIDADES - "+ e.getMessage());
        }
        return ret;
    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------//
    

    
    public void getEntidadesbyNome(String nome) {
        String[] colunas = new String[]{"Nome","Data Inicial", "Data Final", "Caminho"};
        List <Entidade> e = new ArrayList<>();
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT DISTINCT procuracao.identidade, procuracao.caminho, procuracao.dtinicial, procuracao.dtfinal from procuracao where procuracao.identidade = any(SELECT id FROM entidade where entidade.nome like ?);");
            prepared.setString(1, nome.concat("%"));
            resultSet = prepared.executeQuery();
            
            Entidade ent = new Entidade();
            int[] n = new int[500];
            String[] c = new String[500];
            String[] di = new String[500];
            String[] df = new String[500];
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
                prepared = conn.prepareStatement("SELECT * from entidade where entidade.id = ?");
                prepared.setInt(1, n[i]);
                resultSet = prepared.executeQuery();
                while(resultSet.next()){ //pega o os dados do procurados
                    ent = new Entidade(resultSet.getString("nome"), resultSet.getString("responsavel"), resultSet.getString("cpf"), resultSet.getString("cnpj"));
                    ent.setId(resultSet.getInt("id"));
                    e.add(ent);
                }
                i--;
            }
            i++;
            String dados[][] = new String[j][4];
            //desinverte a seleção do caminho - pesquisar uma maneira mais eficiente ------------------------//
            String temp[] = new String[200];
            int xx = 0;
            int xxx = e.size()-1;
            while(xx <=e.size()-1){
                temp[xx] = c[xxx];
                xx++;
                xxx--;
            }
            //------------------------------------------------------------------------------------------------//
            while(i < j){
                dados[i][0] = e.get(i).getNome();
                dados[i][1] = di[i];
                dados[i][2] = df[i];
                dados[i][3] = temp[i];
                i++;
            }
            
            criaTabela(dados, colunas, "Busca de Entidades pelo Nome");
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "BUSCAENTIDADE\n" +ex.getMessage());
        }catch(NegativeArraySizeException ex){
            JOptionPane.showMessageDialog(null, "Nenhuma entidade encontrada com os dados inseridos.");
        }
        
    }
    
    
    public void getEntidadesbyResponsavel(String resp) {
        String[] colunas = new String[]{"Responsavel","Data Inicial", "Data Final", "Caminho"};
        List <Entidade> e = new ArrayList<>();
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT DISTINCT procuracao.identidade, procuracao.caminho, procuracao.dtinicial, procuracao.dtfinal from procuracao where procuracao.identidade = any(SELECT id FROM entidade where entidade.responsavel like ?);");
            prepared.setString(1, resp.concat("%"));
            resultSet = prepared.executeQuery();
            
            Entidade ent = new Entidade();
            int[] n = new int[50];
            String[] c = new String[50];
            String[] di = new String[50];
            String[] df = new String[50];
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
                prepared = conn.prepareStatement("SELECT * from entidade where entidade.id = ?");
                prepared.setInt(1, n[i]);
                resultSet = prepared.executeQuery();
                while(resultSet.next()){ //pega o os dados do procurados
                    ent = new Entidade(resultSet.getString("nome"), resultSet.getString("responsavel"), resultSet.getString("cpf"), resultSet.getString("cnpj"));
                    ent.setId(resultSet.getInt("id"));
                    e.add(ent);
                }
                i--;
            }
            i++;
            String dados[][] = new String[j][4];
            while(i < j){
                dados[i][0] = e.get(i).getResponsavel();
                dados[i][1] = di[i];
                dados[i][2] = df[i];
                dados[i][3] = c[i];
                i++;
            }
            
            criaTabela(dados, colunas, "Busca de Entidades pelo Responsavel");
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "BUSCAENTIDADE\n" +ex.getMessage());
        }catch(NegativeArraySizeException ex){
            JOptionPane.showMessageDialog(null, "Nenhuma entidade encontrada com os dados inseridos.");
        }
        
    }
    
    
    public void getEntidadesbyCpf(String cpf) {
        String[] colunas = new String[]{"Responsavel", "Cpf","Data Inicial", "Data Final", "Caminho"};
        List <Entidade> e = new ArrayList<>();
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT DISTINCT procuracao.identidade, procuracao.caminho, procuracao.dtinicial, procuracao.dtfinal from procuracao where procuracao.identidade = any(SELECT id FROM entidade where entidade.cpf like ?);");
            prepared.setString(1, cpf);
            resultSet = prepared.executeQuery();
            
            Entidade ent = new Entidade();
            int[] n = new int[50];
            String[] c = new String[50];
            String[] di = new String[50];
            String[] df = new String[50];
            int i;
            i = 0;
            while(resultSet.next()){ //pega o código dos entidades com o nome parecido
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
                prepared = conn.prepareStatement("SELECT * from entidade where entidade.id = ?");
                prepared.setInt(1, n[i]);
                resultSet = prepared.executeQuery();
                while(resultSet.next()){ //pega o os dados do procurados
                    ent = new Entidade(resultSet.getString("nome"), resultSet.getString("responsavel"), resultSet.getString("cpf"), resultSet.getString("cnpj"));
                    ent.setId(resultSet.getInt("id"));
                    e.add(ent);
                }
                i--;
            }
            i++;
            String dados[][] = new String[j][5];
            while(i < j){
                dados[i][0] = e.get(i).getResponsavel();
                dados[i][1] = e.get(i).getCpf();
                dados[i][2] = di[i];
                dados[i][3] = df[i];
                dados[i][4] = c[i];
                i++;
            }
            
            criaTabela(dados, colunas, "Busca de Entidades pelo CPF do Responsável");
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "BUSCAENTIDADE\n" +ex.getMessage());
        }catch(NegativeArraySizeException ex){
            JOptionPane.showMessageDialog(null, "Nenhuma Entidade encontrado com os dados inseridos.");
        }
        
    }
    
    
    public void getEntidadesbyCnpj(String cnpj) {
        String[] colunas = new String[]{"Nome", "Cnpj","Data Inicial", "Data Final", "Caminho"};
        List <Entidade> e = new ArrayList<>();
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT DISTINCT procuracao.identidade, procuracao.caminho, procuracao.dtinicial, procuracao.dtfinal from procuracao where procuracao.identidade = any(SELECT id FROM entidade where entidade.cnpj like ?);");
            prepared.setString(1, cnpj);
            resultSet = prepared.executeQuery();
            
            Entidade ent = new Entidade();
            int[] n = new int[50];
            String[] c = new String[50];
            String[] di = new String[50];
            String[] df = new String[50];
            int i;
            i = 0;
            while(resultSet.next()){ //pega o código dos entidades com o nome parecido
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
                prepared = conn.prepareStatement("SELECT * from entidade where entidade.id = ?");
                prepared.setInt(1, n[i]);
                resultSet = prepared.executeQuery();
                while(resultSet.next()){ //pega o os dados do procurados
                    ent = new Entidade(resultSet.getString("nome"), resultSet.getString("responsavel"), resultSet.getString("cpf"), resultSet.getString("cnpj"));
                    ent.setId(resultSet.getInt("id"));
                    e.add(ent);
                }
                i--;
            }
            i++;
            String dados[][] = new String[j][5];
            while(i < j){
                dados[i][0] = e.get(i).getNome();
                dados[i][1] = e.get(i).getCnpj();
                dados[i][2] = di[i];
                dados[i][3] = df[i];
                dados[i][4] = c[i];
                i++;
            }
            
            criaTabela(dados, colunas, "Busca de Entidades pelo CNPJ");
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "BUSCAENTIDADE\n" +ex.getMessage());
        }catch(NegativeArraySizeException ex){
            JOptionPane.showMessageDialog(null, "Nenhuma Entidade encontrado com os dados inseridos.");
        }
        
    }
    
    
    public int getIdExatoEnt(int idgeral, String nome){
        int ret = 0;
        try {
            PreparedStatement prepared = conn.prepareStatement("SELECT entidade.id from entidade where (entidade.id = any(SELECT procuracao.identidade from procuracao where procuracao.idgeral= ?)) and (entidade.nome=?)");
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
    
        
    public void setNomeEntidade(String nome, int id){
        try {
            PreparedStatement prepared = conn.prepareStatement("UPDATE entidade SET entidade.nome =? WHERE entidade.id=?");
            prepared.setString(1, nome);
            prepared.setInt(2, id);
            
            if (prepared.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Nome da Entidade alterado com sucesso!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }        
    }
    
    
    public void setCnpjEntidade(String cpf, int id){
        try {
            PreparedStatement prepared = conn.prepareStatement("UPDATE entidade SET entidade.cnpj =? WHERE entidade.id=?");
            prepared.setString(1, cpf);
            prepared.setInt(2, id);
            
            if (prepared.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Cnpj da Entidade alterado com sucesso!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }        
    }

    
    public void setRespEntidade(String cpf, int id){
        try {
            PreparedStatement prepared = conn.prepareStatement("UPDATE entidade SET entidade.responsavel =? WHERE entidade.id=?");
            prepared.setString(1, cpf);
            prepared.setInt(2, id);
            
            if (prepared.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Nome do Responsavel alterado com sucesso!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }        
    }

    
    public void setCpfEntidade(String cpf, int id){
        try {
            PreparedStatement prepared = conn.prepareStatement("UPDATE entidade SET entidade.cpf =? WHERE entidade.id=?");
            prepared.setString(1, cpf);
            prepared.setInt(2, id);
            
            if (prepared.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Cpf do Responsavel alterado com sucesso!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }        
    }

    
}
