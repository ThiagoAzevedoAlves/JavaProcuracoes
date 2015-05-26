/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procuracoes.db;

import java.awt.HeadlessException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Thiago
 */
public class Datauser extends Database{
    
    public int Login(String login, String senha){
        try {
            PreparedStatement prepared;
            prepared = conn.prepareStatement("SELECT * FROM usuario");
            resultSet = prepared.executeQuery();
            String l;
            String s;
            int i = 0;
            while(resultSet.next()){
                l = resultSet.getString("login");
                s = resultSet.getString("senha");
                if(l.equals(login)){
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
        }
        JOptionPane.showMessageDialog(null, "Login/Usuário Inexistente");
        return -1;
    }

    public int getTipo(String login){
        try {
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
        }
        return -1;
    }
    
    public String getNome(String login){
        try {
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
        }
        return null;
    }
}
