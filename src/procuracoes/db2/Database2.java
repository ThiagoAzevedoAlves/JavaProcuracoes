/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procuracoes.db2;

/**
 *
 * @author Thiago
 */

import procuracoes.db.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import procuracoes.classes2.Entidade;
import procuracoes.classes2.Procuracao;
import procuracoes.classes2.Procurador;
import procuracoes.classes2.Responsavel;

public class Database2{
    public Connection conn;
    public Statement statment = null;
    public PreparedStatement preparedStatement = null;
    public ResultSet resultSet = null;
    
    /**Realiza a conexão no banco de dados.*/
    public void connect(){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //carrega o driver do mysql
            String url = "jdbc:mysql://192.168.2.251:3306/test?autoReconnect=true"; //acessa a tablea mysql
            String usuario = "Thiago";
            String senha = "root";
            conn = DriverManager.getConnection(url, usuario, senha); //conecta no banco de dados MySql
        }catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "CONNECT - " + e.getMessage());
        }
    }
    
    
    /**Salva uma procuração, passando como atributo o objeto Procuração equivalente
     * @param proc Objeto da Classe Procuracao
     * @throws ParseException
     * @throws SQLException
     */    
    public void salva(Procuracao proc) throws ParseException, SQLException{
        try{
            //Insere a procuracao na tabela ---------------------------------------------------------------------//
            if (conn.isClosed()){
                this.connect();
            }
            preparedStatement = conn.prepareStatement("INSERT into procuracao values(default, ?, ?, ?, ?)");
            preparedStatement.setString(1, proc.dtini);
            preparedStatement.setString(2, proc.dtfin);
            preparedStatement.setString(3, proc.conjunto);
            preparedStatement.setString(4, proc.caminho);
            preparedStatement.executeUpdate(); //executa o update na tabela 
            preparedStatement = null; 
            //--------------------------------------------------------------------------------------------------//
            
            this.salvaProcuradores(proc.getProcuradores(), proc.getId());
            
            this.salvaEntidades(proc.getEntidades(), proc.getId());
            
            this.salvaResponsaveis(proc.getResponsaveis(), proc.getId());
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void salvaProcuradores(List<Procurador> p, int idProc){
        //Verifica se os Procuradores já existem no sistema; Caso não exsitam realiza a inserção------//
            int[] relProcu = new int[1000];
            int i = 0;
            int j = 0;
            int id;
            while(i < p.size()){
                id = getIdbyNomeProc(p.get(i).getNome());
                //Se já existe armazena o id----------------------------------------------------------------//
                if(id != 0){
                    relProcu[j] = id;
                    j++;
                //Se nao existe ainda Insere na tabela     ------------------------------------------------//
                }else{
                    try {
                        preparedStatement = conn.prepareStatement("INSERT into pessoas values(default, ?, ?)");
                        preparedStatement.setString(1, p.get(i).getNome());
                        preparedStatement.setString(2, p.get(i).getCpf());
                        preparedStatement.executeUpdate(); //executa o update na tabela
                        preparedStatement = null;
                        relProcu[j] = getIdbyNomeProc(p.get(i).getNome());
                        j++;
                    } catch (SQLException ex) {
                        Logger.getLogger(Database2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                i++;
            }
            //--------------------------------------------------------------------------------------------//
            
            //Constroi a tabela de Relacionamento PROC-PROCU---------//
            i = 0;
            while(i < relProcu.length){
                try {
                    preparedStatement = conn.prepareStatement("INSERT into relac_proc_procu values(default, ?, ?, ?)");
                    preparedStatement.setInt(1, relProcu[i]);
                    preparedStatement.setInt(2, idProc);
                    preparedStatement.setString(3, p.get(0).poderes);
                    preparedStatement.executeUpdate(); //executa o update na tabela
                    preparedStatement = null;
                    i++;
                } catch (SQLException ex) {
                    Logger.getLogger(Database2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            //-------------------------------------------------------//
    }    
    
    public void salvaEntidades(List<Entidade> p, int idProc){
        try{
        //Verifica se as Entidades já existem no sistema; Caso não existam realiza a Inserção ------//
            int[] relEnt = new int[1000];
            int i = 0;
            int j = 0;
            int id = 0;
            while(i < p.size()){
                id = getIdbyNomeEnt(p.get(i).getNome());
                //Se já existe armazena o id
                if(id != 0){
                    relEnt[j] = id;
                    j++;
                //Se nao existe ainda Insere na tabela     
                }else{
                    preparedStatement = conn.prepareStatement("INSERT into entidades values(default, ?, ?)");
                    preparedStatement.setString(1, p.get(i).getNome());
                    preparedStatement.setString(2, p.get(i).getCnpj());
                    preparedStatement.executeUpdate(); //executa o update na tabela 
                    preparedStatement = null;
                    relEnt[j] = getIdbyNomeEnt(p.get(i).getNome());
                    j++;
                }
                i++;
            }
            //----------------------------------------------------------------------------------------//
            
            //Constroi a tabela de Relacionamento PROC-ENT---------//
            i = 0;
            while(i < relEnt.length){
                preparedStatement = conn.prepareStatement("INSERT into relac_proc_ent values(default, ?, ?)");
                preparedStatement.setInt(1, relEnt[i]);
                preparedStatement.setInt(2, idProc);
                preparedStatement.executeUpdate(); //executa o update na tabela 
                preparedStatement = null;
                i++;
            }
            //-------------------------------------------------------//
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void salvaResponsaveis(List<Responsavel> p, int idProc){
        try{
            //Verifica se os Procuradores já existem no sistema; Caso não exsitam realiza a inserção------//
            int[] relProcu = new int[1000];
            int i = 0;
            int j = 0;
            int id;
            while(i < p.size()){
                id = getIdbyNomeResp(p.get(i).getNome());
                //Se já existe armazena o id----------------------------------------------------------------//
                if(id != 0){
                    relProcu[j] = id;
                    j++;
                //Se nao existe ainda Insere na tabela     ------------------------------------------------//
                }else{
                    preparedStatement = conn.prepareStatement("INSERT into responsaveis values(default, ?, ?)");
                    preparedStatement.setString(1, p.get(i).getNome());
                    preparedStatement.setString(2, p.get(i).getCpf());
                    preparedStatement.executeUpdate(); //executa o update na tabela
                    preparedStatement = null;
                    relProcu[j] = getIdbyNomeResp(p.get(i).getNome());
                    j++;
                }
                i++;
            }
            //--------------------------------------------------------------------------------------------//
            
            //Constroi a tabela de Relacionamento PROC-PROCU---------//
            i = 0;
            while(i < relProcu.length){
                preparedStatement = conn.prepareStatement("INSERT into relac_proc_resp values(default, ?, ?)");
                preparedStatement.setInt(1, relProcu[i]);
                preparedStatement.setInt(2, idProc);
                preparedStatement.executeUpdate(); //executa o update na tabela
                preparedStatement = null;
                i++;                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database2.class.getName()).log(Level.SEVERE, null, ex);
        }
            //-------------------------------------------------------//
    }
    
    
    //Retorna o ID de um porcurador pelo Nome--------------------------------------------------------------------------------------------------------------//
    public int getIdbyNomeProc(String procurador){
        int ret = 0;
        try {
            PreparedStatement prepared = conn.prepareStatement("SELECT id from pessoas WHERE nome=?");
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
    
    //Retorna o ID de um porcurador pelo Nome--------------------------------------------------------------------------------------------------------------//
    public int getIdbyNomeEnt(String entidade){
        int ret = 0;
        try {
            PreparedStatement prepared = conn.prepareStatement("SELECT id from entidades WHERE nome=?");
            prepared.setString(1, entidade);
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
    
    //Retorna o ID de um porcurador pelo Nome--------------------------------------------------------------------------------------------------------------//
    public int getIdbyNomeResp(String resp){
        int ret = 0;
        try {
            PreparedStatement prepared = conn.prepareStatement("SELECT id from responsaveis WHERE nome=?");
            prepared.setString(1, resp);
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
    
    /**Retorna o código equivalente à (última entrada na tabela)+1
     * @return int
     */
    public int getProc(){
        try {
            if (conn.isClosed()){
                this.connect();
            }
            preparedStatement = conn.prepareStatement("SELECT MAX(id) FROM procuracao");
            resultSet = preparedStatement.executeQuery();
            preparedStatement = null;
            int n;
            while(resultSet.next()){
                n = resultSet.getInt(1);
                return n+1;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "GETPROCOD -"+ e.getMessage());
        }
        return 1;
    }
    
    /**Retorna o índice do último Procurador na tabela
     * @return int - indice do ultimo procurador na tabela
     */    
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
    
    
    /**Retorna o índice da ultima Entidade na tabela
     * @return int - indice da ultima entidade na tabela
     */    
    public int getLastEnt(){
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
        }
        return -1;
    }    
    
    
    /**Retorna o caminho da Procuracao com o Id passado como paramentro
     * @param codProc id da Procuração
     * @return String - o caminho da Procuracao
     */
    public String getCaminho(int codProc){
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
        }
        return null;
    }    
    
    
    /**Retorna a Data Inicial da procuracao passada como parametro.
     * @param procod codigo da procuracao.
     * @return String - Data Inicial.
     */
    public String getDtini(int procod){
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
        }
        return null;
    }
        
    
    /**Retorna a Data Final da procuracao passada como parametro
     * @param procod codigo da Procuracao.
     * @return String - Data Final.
     */    
    public String getDtfin(int procod){
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
        }
        return null;
    }
    
    
    /**Retorna o Conjunto da procuracao passada como parametro.
     * @param procod codigo da Procuracao.
     * @return String - Tipo de Conjunto da Procuracao.
     */    
    public String getConjunto(int procod){
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
        }
        return null;
    }
    
    
    /**Cria uma tabela a partir dos dados passados.
     * @param dados String[][] representando os dados que serão exibidos.
     * @param colunas String[] representando o titulo de cada uma das colunas da tbela.
     * @param titulo String representando o titulo da janela.
     * @param usuario String representando o usuario que está efetivando a operacao
     */
    public void criaTabela(String[][] dados, String[] colunas, String titulo, String usuario){
        Tabela t = new Tabela(dados, colunas, titulo, usuario);
    }
    
    
    /**Define o Tipo de Conjunto da Procuracao
     * @param conjunto String representando Tipo de Conjunto
     * @param id int representando o id da Procuracao
     */    
    public void setConjunto(String conjunto, int id){
        try {
            PreparedStatement prepared = conn.prepareStatement("UPDATE procuracao SET procuracao.conjunto=? WHERE procuracao.idgeral=?");
            prepared.setString(1, conjunto);
            prepared.setInt(2, id);
            
            if (prepared.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Tipo de Conjunto alterado com sucesso!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
    
    
    /**Define a Data Inicial da Procuracao
     * @param dt String representando a Data Inicial
     * @param procod int representando o id da Procuracao
     * @return int - 1 se OK 0 caso algum erro
     */
    public int setDtini(int procod, String dt){
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("UPDATE procuracao SET dtinicial=? where idgeral= ?");
            prepared.setString(1, dt);
            prepared.setInt(2, procod);
            prepared.executeUpdate();
            return 1;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "GETDATAINICIAL - "+ e.getMessage());
        }
        return 0;
    }
    
    
    /**Define a Data Final da Procuracao
     * @param dt String representando a Data Final
     * @param procod int representando o id da Procuracao
     * @return int - 1 se OK 0 caso algum erro
     */
    public int setDtfin(int procod, String dt){
        try{
            PreparedStatement prepared;
            prepared = conn.prepareStatement("UPDATE procuracao SET dtfinal=? where idgeral= ?");
            prepared.setString(1, dt);
            prepared.setInt(2, procod);
            prepared.executeUpdate();
            return 1;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "GETDATAINICIAL - "+ e.getMessage());
        }
        return 0;
    }
    
    
    /**Apaga a Procuracao pelo ID
     * @param id int representando o ID da Procuracao
     * @return int - 1 se OK 0 caso algum erro
     */
    public int apagaProc(int id){
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
        }
        return ret;
    }
}