/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procuracoes.db;

/**
 *
 * @author Thiago
 */

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import procuracoes.classes.Procuracao;

public class Database{
    public Connection conn;
    public Statement statment = null;
    public PreparedStatement preparedStatement = null;
    public ResultSet resultSet = null;
    

//realiza a coneexão no banco de dados----------------------------------------------------------------------------------------------------------------//
    public void connect(){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //carrega o driver do mysql
            String url = "jdbc:mysql://192.168.2.170:3306/cartorioimoveis"; //acessa a tablea mysql "unimed_biom_teste" no localhost
            String usuario = "Thiago";
            String senha = "root";
            conn = DriverManager.getConnection(url, usuario, senha); //conecta no banco de dados MySql
        }catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "CONNECT - " + e.getMessage());
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------//    

//Salva uma procuração, passando como atributo o objeto Procuração equivalente----------------------------------------------------------------------//    
    public void salva(Procuracao proc) throws ParseException, SQLException{
        //salva as entidades
        try {
            int i = 0;
            while (i< proc.getQtent()){ //enquanto houver entidades elas são inseridas na tabela entidade--------------------------------------//
                preparedStatement = conn.prepareStatement("insert into entidade values(default, ?, ?, ?, ?)"); //prepara os argumentos;--------//
                preparedStatement.setString(1, proc.getEntidades().get(i).getNome());
                preparedStatement.setString(2, proc.getEntidades().get(i).getResponsavel());
                preparedStatement.setString(3, proc.getEntidades().get(i).getCpf());
                preparedStatement.setString(4, proc.getEntidades().get(i).getCnpj());
                preparedStatement.executeUpdate(); //executa o update na tabela 
                preparedStatement = null; 
                i++; 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "SALVAENT -" + e.getMessage());
        }finally{
            conn.close();
        }
        //salva os procuradores
        try {
            int i = 0;
            while (i< proc.getQtproc()){ //enquanto houver procuradores ele grava os procuradores na tabela equivalente
                preparedStatement = conn.prepareStatement("insert into procurador values(default, ?, ?, ?)"); //prepara os argumentos;
                preparedStatement.setString(1, proc.getProcuradores().get(i).getNome());
                preparedStatement.setString(2, proc.getProcuradores().get(i).getCpf());
                preparedStatement.setString(3, proc.getProcuradores().get(i).getPoderes());
                preparedStatement.executeUpdate(); //executa o update na tabela
                preparedStatement = null;
                i++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "SALVA -" + e.getMessage());
        }finally{
            conn.close();
        }
        java.sql.Date di = null;
        java.sql.Date df = null;
            
        //salva a procuracao
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if(proc.dtini != null){ //se houver uma data inicial recebe o valor em uma variavel auxiliar
            java.util.Date d1 = sdf.parse(proc.dtini);
            di = new Date(d1.getTime());
        }
        
        if(proc.dtfin    != null){//se houver uma data final recebe o valor em uma variavel auxiliar
            java.util.Date d2 = sdf.parse(proc.dtfin);
            df = new Date(d2.getTime());
        }
        
        try{  //insere os valores finais na tabela procuracao enquanto houver procuradores e entidades nas listas passadas.  
            int i = 0;
            int j;
            String sql;
            sql = "insert into procuracao values(default, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = conn.prepareStatement(sql); //prepara os argumentos;
            
            while( i < proc.getQtent()){
                j = 0;
                while (j < proc.getQtproc()){
                    preparedStatement.setInt(1, getLastProc()-j);
                    preparedStatement.setInt(2, getLastEnt()-i);
                    preparedStatement.setDate(3, di);
                    preparedStatement.setDate(4, df);
                    preparedStatement.setString(5, proc.getConjunto());
                    preparedStatement.setString(6, proc.getCaminho());
                    preparedStatement.setInt(7, proc.getId());
                    preparedStatement.executeUpdate(); //executa o update na tabela
                    j++;
                }
                i++;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SALVAPROC" + "\n" + preparedStatement.toString() + "\n" + e.getMessage() + " - " + e.getSQLState());
        }finally{
            conn.close();
        }
    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------//

//retorna o código equivalente à última entrada na tabela+1 -------------------------------------------------------------------------------------------//    
    public int getProcod() throws SQLException{
        try {
            preparedStatement = conn.prepareStatement("SELECT MAX(id) FROM procuracao");
            resultSet = preparedStatement.executeQuery();
            preparedStatement = null;
            int n;
            while(resultSet.next()){
                n = resultSet.getInt(1);
                preparedStatement = conn.prepareStatement("SELECT idgeral FROM procuracao where id= ?");
                preparedStatement.setInt(1, n);
                resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    n = resultSet.getInt(1);
                    return n+1;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "GETPROCOD -"+ e.getMessage());
        }finally{
            conn.close();
        }
        return 1;
    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------//
    
//Retorna o índice do último Procurador na tabela------------------------------------------------------------------------------------------------------//    
    public int getLastProc() throws SQLException{
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
        }finally{
            conn.close();
        }
        return -1;
    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------//
    
//Retorna o índice da ultima Entidade na tabela--------------------------------------------------------------------------------------------------------//    
    public int getLastEnt() throws SQLException{
        try {
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT MAX(id) FROM entidade");
            resultSet = prepared.executeQuery();
            int n;
            while(resultSet.next()){
                n = resultSet.getInt(1);
                return n;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "GETLASTENT - "+ e.getMessage());
        }finally{
            conn.close();
        }
        return -1;
    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------//

//Retorna o caminho da Procuracao com o Id passado como paramentro-------------------------------------------------------------------------------------//    
    public String getCaminho(int codProc) throws SQLException{
        try {
            PreparedStatement prepared;
            
            prepared = conn.prepareStatement("SELECT caminho FROM procuracao where procuracao.idgeral=?");
            prepared.setInt(1, codProc);
            resultSet = prepared.executeQuery();
            String aux;
            while(resultSet.next()){
                aux = resultSet.getString(1);
                return aux;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "GETCAMINHO - "+ e.getMessage());
        }finally{
            conn.close();
        }
        return null;
    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------//
    
//Retorna a Data Inicial da procuracao passada como parametro------------------------------------------------------------------------------------------//
    public String getDtini(int procod) throws SQLException{
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT dtinicial FROM procuracao where idgeral= ?");
            prepared.setInt(1, procod);
            resultSet = prepared.executeQuery();
            Date n;
            while(resultSet.next()){
                n = resultSet.getDate(1);
                return String.valueOf(n);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "GETDATAINICIAL - "+ e.getMessage());
        }finally{
            conn.close();
        }
        return null;
    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------//

//Retorna a Data Final da procuracao passada como parametro--------------------------------------------------------------------------------------------//    
    public String getDtfin(int procod) throws SQLException{
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT dtfinal FROM procuracao where idgeral= ?");
            prepared.setInt(1, procod);
            resultSet = prepared.executeQuery();
            Date n;
            while(resultSet.next()){
                n = resultSet.getDate(1);
                return String.valueOf(n);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "GETDATAFINAL - "+ e.getMessage());
        }finally{
            conn.close();
        }
        return null;
    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------//
    
//Retorna o Conjunto da procuracao passada como parametro----------------------------------------------------------------------------------------------//    
    public String getConjunto(int procod) throws SQLException{
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT conjunto FROM procuracao where idgeral= ?");
            prepared.setInt(1, procod);
            resultSet = prepared.executeQuery();
            String n;
            while(resultSet.next()){
                n = resultSet.getString(1);
                return String.valueOf(n);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "GETCONJUNTO - "+ e.getMessage());
        }finally{
            conn.close();
        }
        return null;
    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------//
    
    
    public void criaTabela(String[][] dados, String[] colunas, String titulo){
        Tabela t = new Tabela(dados, colunas, titulo);
    }
    
        
    public void setConjunto(String conjunto, int id) throws SQLException{
        try {
            PreparedStatement prepared = conn.prepareStatement("UPDATE procuracao SET procuracao.conjunto=? WHERE procuracao.idgeral=?");
            prepared.setString(1, conjunto);
            prepared.setInt(2, id);
            
            if (prepared.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Tipo de Conjunto alterado com sucesso!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }finally{
            conn.close();
        }
    }

    
    public int setDtini(int procod, String dt) throws SQLException{
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("UPDATE procuracao SET dtinicial=? where idgeral= ?");
            prepared.setString(1, dt);
            prepared.setInt(2, procod);
            prepared.executeUpdate();
            return 1;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "GETDATAINICIAL - "+ e.getMessage());
        }finally{
            conn.close();
        }
        return 0;
    }

    
    public int setDtfin(int procod, String dt) throws SQLException{
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("UPDATE procuracao SET dtfinal=? where idgeral= ?");
            prepared.setString(1, dt);
            prepared.setInt(2, procod);
            prepared.executeUpdate();
            return 1;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "GETDATAINICIAL - "+ e.getMessage());
        }finally{
            conn.close();
        }
        return 0;
    }

    
    public int apagaProc(int id) throws SQLException{
        int ret = 0;
        try {
            String SQL = "DELETE from procurador where procurador.id = any (SELECT procuracao.idprocurador from procuracao where procuracao.idgeral=?);";
            String SQL2 = "DELETE from entidade where entidade.id = any (SELECT procuracao.identidade from procuracao where procuracao.idgeral=?);";
            String SQL3 = "DELETE from procuracao where procuracao.idgeral=?";
            PreparedStatement prepared = conn.prepareStatement(SQL);
            prepared.setInt(1, id);
            prepared.executeUpdate();
            prepared = conn.prepareStatement(SQL2);
            prepared.setInt(1, id);
            prepared.executeUpdate();
            prepared = conn.prepareStatement(SQL3);
            prepared.setInt(1, id);
            prepared.executeUpdate();
            ret=1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }finally{
            conn.close();
        }
        return ret;
    }

}
