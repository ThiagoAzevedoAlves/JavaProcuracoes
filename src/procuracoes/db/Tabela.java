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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import procuracoes.frames.Visualiza;

/**
 *
 * @author Thiago
 */
public class Tabela implements WindowListener{
    
    Tabela(String[][] dados, String[] colunas, String titulo){
        
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
    public void windowOpened(WindowEvent e) {
        //DONOTHING
    }

    @Override
    public void windowClosing(WindowEvent e) {
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
        //DONOTHING
    }
    
}
