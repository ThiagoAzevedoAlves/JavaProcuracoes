/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oficios.db;

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
import oficios.classes.Oficio;

public class Database{
    public Connection conn;
    public Statement statment = null;
    public PreparedStatement preparedStatement = null;
    public ResultSet resultSet = null;
    
    /**Realiza a conexão no banco de dados.*/
    public void connect(){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //carrega o driver do mysql
            String url = "jdbc:mysql://192.168.2.170:3306/cartorioimoveis?autoReconnect=true";
            String usuario = "Thiago";
            String senha = "root";
            conn = DriverManager.getConnection(url, usuario, senha); //conecta no banco de dados MySql
        }catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "CONNECT - " + e.getMessage());
        }
    }
    
    
    /**Salva um Oficio, passando como atributo o objeto Oficio equivalente
     * @param ofic Objeto da Classe Oficio
     * @throws ParseException
     * @throws SQLException
     */    
    public int salva(Oficio ofic) throws ParseException, SQLException{
              
        int ret = 0;
        java.sql.Date d = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if(ofic.data != null){ //se houver uma data inicial recebe o valor em uma variavel auxiliar
            java.util.Date d1 = sdf.parse(ofic.data);
            d = new Date(d1.getTime());
        }
        
        try{  //insere os valores finais na tabela procuracao enquanto houver procuradores e entidades nas listas passadas.  
            String sql;
            sql = "insert into oficio values(default, ?, ?, ?, ?)";
            preparedStatement = conn.prepareStatement(sql); //prepara os argumentos;
            
            preparedStatement.setInt(1, ofic.getNumero());
            preparedStatement.setInt(2, ofic.getAno());
            preparedStatement.setString(3, ofic.getCaminho());
            preparedStatement.setDate(4, d);
            preparedStatement.executeUpdate(); //executa o update na tabela
            ret = 1;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SALVAOFIC" + "\n" + e.getMessage());            
        }
        return ret;
    }
    
    
    /**Retorna o código equivalente à (última entrada na tabela)+1
     * @return int
     */
    public int getOficod(){
        try {
            if (conn.isClosed()){
                this.connect();
            }
            preparedStatement = conn.prepareStatement("SELECT MAX(numero) FROM oficio");
            resultSet = preparedStatement.executeQuery();
            preparedStatement = null;
            int n;
            while(resultSet.next()){
                n = resultSet.getInt(1);
                return n;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "GETPROCOD -"+ e.getMessage());
        }
        return 1;
    }
    
       
    /**Retorna o caminho do Oficio com o Id passado como paramentro
     * @param numero numero do Oficio
     * @param ano ano do Oficio
     * @return String - o caminho do Oficio
     */
    public String getCaminho(int numero, int ano){
        try {
            PreparedStatement prepared;
            
            prepared = conn.prepareStatement("SELECT caminho FROM oficio where (oficio.numero=? and oficio.ano=?)");
            prepared.setInt(1, numero);
            prepared.setInt(2, ano);
            resultSet = prepared.executeQuery();
            String aux;
            while(resultSet.next()){
                aux = resultSet.getString(1);
                return aux;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ofício Não Encontrado. Erro: " + e.getMessage());
        }
        return null;
    }    
    
    
    /**Retorna de inserção do Oficio passado como parametro.
     * @param numero numero do oficio.
     * @param ano ano do oficio
     * @return String - Data Inicial.
     */
    public String getData(int numero, int ano){
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT data FROM oficio where (oficio.numero=? and oficio.ano=?)");
            prepared.setInt(1, numero);
            prepared.setInt(2, ano);
            resultSet = prepared.executeQuery();
            Date n;
            while(resultSet.next()){
                n = resultSet.getDate(1);
                return String.valueOf(n);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "GETDATA - "+ e.getMessage());
        }
        return null;
    }
          
    /**Define a Data de inserção de um Oficio 
     * @param dt String representando a Data de Inserção/Atualização do Oficio
     * @param numero int representando o numero do Oficio
     * @param ano int representando o ano do Oficio
     * @return int - 1 se OK 0 caso algum erro
     */
    public int setData(int numero, int ano, String dt){
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("UPDATE oficio SET data=? where (oficio.numero=? and oficio.ano=?)");
            prepared.setString(1, dt);
            prepared.setInt(2, numero);
            prepared.setInt(3, ano);
            prepared.executeUpdate();
            return 1;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "SETDATA - "+ e.getMessage());
        }
        return 0;
    }
    
        
    /**Apaga o Ofcicio pelo numero/ano
     * @param numero int representando o numero do Oficio
     * @param ano int representando o ano do Oficio
     * @return int - 1 se OK 0 caso algum erro
     */
    public int apaga(int numero, int ano){
        int ret = 0;
        String caminho = this.getCaminho(numero, ano);
        if (caminho != null){
            try {
                String SQL = "DELETE from oficio where (oficio.numero=? and oficio.ano=?)";
                PreparedStatement prepared = conn.prepareStatement(SQL);
                prepared.setInt(1, numero);
                prepared.setInt(2, ano);
                prepared.executeUpdate();            
                ret=1;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.toString());
            }
        }
        return ret;
    }
    
}