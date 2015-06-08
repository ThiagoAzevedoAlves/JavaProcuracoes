/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procuracoes.db;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import procuracoes.frames.VisualizaProc;

/**
 *
 * @author Thiago
 */
public class Tabela{
    
    public String user;
    
    Tabela(String[][] dados, String[] colunas, String titulo, String usuario){
        
        JTable tabela = new JTable(dados,colunas);
        tabela.getColumn("Data Inicial").setMinWidth(80);
        tabela.getColumn("Data Final").setMinWidth(80);
        tabela.getColumn("Data Inicial").setMaxWidth(80);
        tabela.getColumn("Data Final").setMaxWidth(80);
        
        tabela.getColumn("Caminho").setMinWidth(120);
            JScrollPane scroll = new JScrollPane();
            scroll.setViewportView(tabela);
            
            JFrame f = new JFrame();
            tabela.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    String s = tabela.getModel().getValueAt(tabela.getSelectedRow(), colunas.length-1).toString();
                    if(s.substring(36,37).compareTo(".") == 0){
                        VisualizaProc v = new VisualizaProc(s, Integer.valueOf(String.valueOf(s.charAt(35))), usuario);
                        v.setVisible(true);
                    }else if(s.substring(37,38).compareTo(".") == 0){
                        String ss = s.substring(35,37);
                        VisualizaProc v = new VisualizaProc(s, Integer.valueOf(ss), usuario);
                        v.setVisible(true);
                    }else{
                        String ss = s.substring(35,38);
                        VisualizaProc v = new VisualizaProc(s, Integer.valueOf(ss), usuario);
                        v.setVisible(true);
                    }                    
                    
                    f.dispose();
                }
            });
            f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            
            f.add(scroll);
            f.setVisible(true);
            Point dot = new Point(GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint());
            dot.setLocation(dot.x-400, dot.y-200);
            f.setLocation(dot);
            f.setSize(800, 400);
            f.setResizable(false);
            f.setTitle(titulo);
    }
        
}
