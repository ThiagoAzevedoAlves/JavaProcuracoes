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
import procuracoes.classes.Procurador;

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
    
    //Constroi uma tabela com todas as entidades começando com o Nome passado como parametro---------------------------------------------------------------//      
    public void getEntidadesbyNome(String nome, String usuario) {
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
            i--;
            int j = i;
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
                dados[i][0] = e.get(i).getNome();
                dados[i][1] = di[j-i];
                dados[i][2] = df[j-i];
                dados[i][3] = c[j-i];
                i++;
            }
            
            criaTabela(dados, colunas, "Busca de Entidades pelo Nome", usuario);
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "BUSCAENTIDADE\n" +ex.getMessage());
        }catch(NegativeArraySizeException ex){
            JOptionPane.showMessageDialog(null, "Nenhuma entidade encontrada com os dados inseridos.");
        }
        
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------// 
    
    //Constroi uma tabela com todas as entidades começando com o Nome do Responsavel passado como parametro------------------------------------------------//     
    public void getEntidadesbyResponsavel(String resp, String usuario) {
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
            i--;
            int j = i;
            
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
                dados[i][1] = di[j-i];
                dados[i][2] = df[j-i];
                dados[i][3] = c[j-i];
                i++;
            }
            
            criaTabela(dados, colunas, "Busca de Entidades pelo Responsavel", usuario);
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "BUSCAENTIDADE\n" +ex.getMessage());
        }catch(NegativeArraySizeException ex){
            JOptionPane.showMessageDialog(null, "Nenhuma entidade encontrada com os dados inseridos.");
        }
        
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//     
 
    //Constroi uma tabela com todas as entidades com o Cpf do Responsavel passado como parametro-----------------------------------------------------------//     
    public void getEntidadesbyCpf(String cpf, String usuario) {
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
            i--;
            int j = i;
            
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
                dados[i][2] = di[j-i];
                dados[i][3] = df[j-i];
                dados[i][4] = c[j-i];
                i++;
            }
            
            criaTabela(dados, colunas, "Busca de Entidades pelo CPF do Responsável", usuario);
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "BUSCAENTIDADE\n" +ex.getMessage());
        }catch(NegativeArraySizeException ex){
            JOptionPane.showMessageDialog(null, "Nenhuma Entidade encontrado com os dados inseridos.");
        }
        
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//     

    //Constroi uma tabela com todas as entidades com o Cnpj passado como parametro-------------------------------------------------------------------------//     
    public void getEntidadesbyCnpj(String cnpj, String usuario) {
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
            i--;
            int j = i;
            
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
                dados[i][2] = di[j-i];
                dados[i][3] = df[j-i];
                dados[i][4] = c[j-i];
                i++;
            }
            
            criaTabela(dados, colunas, "Busca de Entidades pelo CNPJ", usuario);
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "BUSCAENTIDADE\n" +ex.getMessage());
        }catch(NegativeArraySizeException ex){
            JOptionPane.showMessageDialog(null, "Nenhuma Entidade encontrado com os dados inseridos.");
        }
        
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//     

    //Retorna o ID da entidade na Procuracao(idgeral) com o Nome passado como parametro--------------------------------------------------------------------//     
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
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//    

    //Define o Nome de Entidade com o ID passado como paramentro-------------------------------------------------------------------------------------------//    
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
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//        

    //Define o Cnpj de Entidade com o ID passado como paramentro-------------------------------------------------------------------------------------------//        
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
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//        
    
    //Define o Nome do Responsavem pela Entidade com o ID passado como paramentro--------------------------------------------------------------------------//    
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
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//    

    //Define o Cpf do Responsavel da Entidade com o ID passado como paramentro-----------------------------------------------------------------------------//
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
    //-----------------------------------------------------------------------------------------------------------------------------------------------------//

    public int apaga(String nome, int idgeral){
        int ret = 0;
        try {
            String SQLset = "SET SQL_SAFE_UPDATES = 0;";
            String SQL = "DELETE from entidade where (entidade.id= any(SELECT procuracao.identidade from procuracao where procuracao.idgeral=?)) and (entidade.nome=?);";
            PreparedStatement prepared = conn.prepareStatement(SQLset);
            prepared.executeUpdate();
            prepared = conn.prepareStatement(SQL);
            prepared.setInt(1, idgeral);
            prepared.setString(2, nome);
            prepared.executeUpdate();
            ret = 1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null," APAGA ENTIDADE - Erro SQL - " + ex);
        }
        return ret;
    }

    public int add(String nome, String cnpj, String responsavel, String cpf, int idgeral) {
        int ret = 0;
        String SQL = "INSERT into entidade(id, nome, responsavel, cpf, cnpj) VALUES(default, ?, ?, ?, ?)";
        try {            
            PreparedStatement prepared = conn.prepareStatement(SQL);
            prepared.setString(1, nome);
            prepared.setString(2,responsavel);
            prepared.setString(3, cpf);
            prepared.setString(4, cnpj);
            prepared.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null," ADD PROCURADOR - Erro SQL - " + ex);
        }
        Dataproc dp = new Dataproc();
        dp.connect();
        List<Procurador> lp = new ArrayList<>();
        lp.addAll(dp.getProcuradores(idgeral));
        int i = 0;
        SQL = "INSERT into procuracao(id, idprocurador, identidade, dtinicial, dtfinal, conjunto, caminho, idgeral) VALUES(default, ?, ?, ?, ?, ?, ?, ?);";
        while(i<lp.size()){
            try{
                PreparedStatement prepared = conn.prepareStatement(SQL);
                prepared.setInt(1, lp.get(i).getId());
                prepared.setInt(2, this.getLastEnt());
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
