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

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import procuracoes.classes.Entidade;
import procuracoes.classes.Procuracao;
import procuracoes.classes.Procurador;
import procuracoes.frames.Inicial;
import procuracoes.frames.Visualiza;

public class Database implements WindowListener{
    public Connection conn = null;
    public Statement statment = null;
    public PreparedStatement preparedStatement = null;
    public ResultSet resultSet = null;
    

//realiza a coneexão no banco de dados----------------------------------------------------------------------------------------------------------------//
    public void connect(){
        try{
            Class.forName("com.mysql.jdbc.Driver"); //carrega o driver do mysql
            String url = "jdbc:mysql://192.168.2.170:3306/cartorioimoveis"; //acessa a tablea mysql "unimed_biom_teste" no localhost
            String usuario = "Thiago";
            String senha = "root";
            conn = DriverManager.getConnection(url, usuario, senha); //conecta no banco de dados MySql
        }catch (ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null, "CONNECT - " + e.getMessage());
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------//    

//Salva uma procuração, passando como atributo o objeto Procuração equivalente----------------------------------------------------------------------//    
    public void salva(Procuracao proc) throws ParseException{
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
        }
    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------//

//retorna o código equivalente à última entrada na tabela+1 -------------------------------------------------------------------------------------------//    
    public int getProcod(){
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
        }
        return 1;
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
    
//Retorna o índice da ultima Entidade na tabela--------------------------------------------------------------------------------------------------------//    
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
//-----------------------------------------------------------------------------------------------------------------------------------------------------//

//Retorna o caminho da Procuracao com o Id passado como paramentro-------------------------------------------------------------------------------------//    
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
//-----------------------------------------------------------------------------------------------------------------------------------------------------//
    
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

//Retorna a Data Inicial da procuracao passada como parametro------------------------------------------------------------------------------------------//
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
//-----------------------------------------------------------------------------------------------------------------------------------------------------//

//Retorna a Data Final da procuracao passada como parametro--------------------------------------------------------------------------------------------//    
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
//-----------------------------------------------------------------------------------------------------------------------------------------------------//
    
//Retorna o Conjunto da procuracao passada como parametro----------------------------------------------------------------------------------------------//    
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
            Inicial i = new Inicial();
            i.setVisible(true);
        }
        
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
                dados[i][0] = e.get(i).getNome();
                dados[i][1] = di[i];
                dados[i][2] = df[i];
                dados[i][3] = c[i];
                i++;
            }
            
            criaTabela(dados, colunas, "Busca de Entidades pelo Nome");
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "BUSCAENTIDADE\n" +ex.getMessage());
        }catch(NegativeArraySizeException ex){
            JOptionPane.showMessageDialog(null, "Nenhuma entidade encontrada com os dados inseridos.");
            Inicial i = new Inicial();
            i.setVisible(true);
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
            Inicial i = new Inicial();
            i.setVisible(true);
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
            Inicial i = new Inicial();
            i.setVisible(true);
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
            Inicial i = new Inicial();
            i.setVisible(true);
        }
        
    }
    
    
    
    
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
            Inicial i = new Inicial();
            i.setVisible(true);
        }
        
    }
    
    public void criaTabela(String[][] dados, String[] colunas, String titulo){
        JTable tabela = new JTable(dados,colunas);
            JScrollPane scroll = new JScrollPane();
            scroll.setViewportView(tabela);
            
            JFrame f = new JFrame();
            tabela.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    String s = tabela.getModel().getValueAt(tabela.getSelectedRow(), colunas.length-1).toString();
                    Visualiza v = new Visualiza(s, (s.charAt(15)-48));
                    v.setVisible(true);
                    f.dispose();
                }
            });
            f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            f.addWindowListener(this);
            
            f.add(scroll);
            f.setVisible(true);
            Point dot = new Point(GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint());
            dot.setLocation(dot.x-400, dot.y-200);
            f.setLocation(dot);
            f.setSize(800, 400);
            f.setResizable(false);
            f.setTitle(titulo);
    }
    
    @Override
    public void windowClosing(WindowEvent e) {
        Inicial i = new Inicial();
        i.setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        //DONOTHING
    }

    @Override
    public void windowClosed(WindowEvent e) {
        //DONOTHING
    }

    @Override
    public void windowIconified(WindowEvent e) {
        //DONOTHING
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        //DONOTHING
    }

    @Override
    public void windowActivated(WindowEvent e) {
        //DONOTHING
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        //DONOTHING.
    }
}
