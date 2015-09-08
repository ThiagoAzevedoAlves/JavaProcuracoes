/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procuracoes.classes2;

import procuracoes.classes.*;
import com.thoughtworks.xstream.XStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Thiago
 */
public class SisLog {
    public String form;
    public String usuario;
    public String acao;
    public Date data;
            
    public SisLog(String f, String u, String a){
        usuario = u;
        acao = a;
        form = f;
        data = new Date(Instant.now().toEpochMilli());
        XStream xstream = new XStream();
        xstream.alias(f, SisLog.class);
        String xml = xstream.toXML(this);
        try {
            FileWriter writer = new FileWriter("\\\\servidor\\Repositorio\\SistemaMezzari\\log", true);
            PrintWriter out = new PrintWriter(new BufferedWriter(writer));
            out.println(xml);
            out.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}