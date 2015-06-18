/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geral.db;

import java.awt.HeadlessException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import procuracoes.db.Database;

/**
 * Classe Responsável pelo Login do Usuário.
 * @author Thiago Azevedo Alves
 */
public class Datauser extends Database{
    
    /**
     * @param login String representando o LOGIN do usuário
     * @param senha String representando a SENHA do usuário
     * @return -1 caso usuário não exista, 0 se a senha estiver errada, 1 caso Login e Senha corretos.
     */
    public int Login(String login, String senha){
        try {
            this.connect();
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT * FROM usuario");
            resultSet = prepared.executeQuery();
            String l;
            String s;
            int i = 0;
            while(resultSet.next()){
                l = resultSet.getString("login");
                s = resultSet.getString("senha");
                if(l.equals(login.toLowerCase())){
                    if(s==null ||senha==null) return 1;
                    if(s.equals(senha)){
                        return 1;
                    }else{
                        JOptionPane.showMessageDialog(null, "Senha Incorreta, por favor, digite novamente.");
                        return 0;
                    }
                }
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "LOGIN - "+ e.getMessage());
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao encerrar conexão Mysql.");
            }
        }
        JOptionPane.showMessageDialog(null, "Login/Usuário Inexistente");
        return -1;
    }
    
    /**
     * @param login String Representando LOGIN do Usuário
     * @return int Representando o Tipo de Usuário ou -1 em caso de Erro.
     */
    public int getTipo(String login){
        try {
            this.connect();
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT * FROM usuario WHERE login=?");
            prepared.setString(1, login);
            resultSet = prepared.executeQuery();
            int n;
            while(resultSet.next()){
                n = resultSet.getInt("tipo");
                return n;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "GETTIPO - "+ e.getMessage());
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao encerrar conexão Mysql.");
            }
        }
        return -1;
    }
    /**
     * 
     * @param login String representando o LOGIN do Usuário
     * @return String representado o Nome do Usuário, null em caso de Erro.
     */
    public String getNome(String login){
        try {
            this.connect();
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT * FROM usuario WHERE login=?");
            prepared.setString(1, login);
            resultSet = prepared.executeQuery();
            String ret;
            while(resultSet.next()){
                ret = resultSet.getString("nome");
                return ret;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "GETNOME - "+ e.getMessage());
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao encerrar conexão Mysql.");
            }
        }
        return null;
    }
}
