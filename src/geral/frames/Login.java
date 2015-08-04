/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geral.frames;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import procuracoes.classes.SisLog;
import geral.db.Datauser;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import oficios.frames.Oficios;
import procuracoes.frames.Procuracoes;

/**
 * JFrame de Confirmação de Usuário e Senha
 * @author Thiago Azevedi Alves
 */
public class Login extends javax.swing.JFrame {
    
    public Login() {
        initComponents();
        
        ImageIcon image = new ImageIcon(getClass().getResource("/recursos/icon.png"));
        this.setIconImage(image.getImage());
        
        //logo------------------------------------------------------------------------------------//
        BufferedImage resizedImg = new BufferedImage(320, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(new ImageIcon(getClass().getResource("/recursos/logo.jpg")).getImage(), 0, 0, 320, 80, null);
        g.dispose();

        jLproc.setIcon(new javax.swing.ImageIcon(resizedImg));        
        //-----------------------------------------------------------------------------------------//
                    
        KeyListener l = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    for(ActionListener a: jButton1.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(e, 1, null));
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    for(ActionListener a: jButton1.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(e, 1, null));
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    for(ActionListener a: jButton1.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(e, 1, null));
                    }
                }
            }
        };
        jPasswordField1.addKeyListener(l);
        jTextField1.addKeyListener(l);
        
        this.getContentPane().setBackground(Color.white);
        this.jPanel1.setBackground(Color.white);
    }
       
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPanel1 = new javax.swing.JPanel();
        jLproc = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.gray, java.awt.Color.lightGray));

        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("Ok");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLabel19.setText("Login:");

        jLabel20.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLabel20.setText("Senha:");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLproc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLproc, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                            .addComponent(jPasswordField1)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        
    }//GEN-LAST:event_jPanel1MouseClicked
    /**
     * Verifica Usuário e Senha; Após, Abre a Janela de Sistema Referente ao Tipo de Usuário.
     * @param evt Evento de Clique do Mouse
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Datauser db = new Datauser();
        try{
            db.connect();
            String login = jTextField1.getText();
            char[] ss = jPasswordField1.getPassword();
            String senha;
            senha = (String.valueOf(ss));
            if(db.Login(login,senha) == 1){ //confirma usuário e senha
                if(db.getTipo(login)==1){ //se estiverem correto verifica o tipo, se tipo==1 abre escolha de Sistema
                    servidor s = new servidor(db.getTipo(login), db.getNome(login));
                    s.setVisible(true);
                    this.dispose();
                    SisLog S = new SisLog("Login - Procuracoes",jTextField1.getText(), "Sucesso");
                }else if(db.getTipo(login)==0){ //se tipo==0 abre Sistema de Procuracoes
                    Procuracoes p = new Procuracoes(db.getTipo(login), db.getNome(login));
                    p.setVisible(true);
                    this.dispose();
                }else if(db.getTipo(login)==2){ //se tipo==2 abre Sistema de Oficios
                    Oficios o = new Oficios(db.getTipo(login), db.getNome(login));
                    o.setVisible(true);
                    this.dispose();
                }
            }else{
                SisLog S = new SisLog("Login - Procuracoes","Erro "+jTextField1.getText(), "Erro");
            }
        }finally{
            try {
                db.conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao Fechar conexão Mysql.");
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed
    
  
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLproc;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
