/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oficios.frames;

import procuracoes.frames.*;
import SK.gnome.morena.MorenaException;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import procuracoes.classes.SisLog;
import oficios.db.Database;

public class Oficios extends javax.swing.JFrame {
    
    static JDialog dialog;
    boolean cPesq, cIncl, cExcl, cSobr;
    public String user;
    
    public Oficios(int tipo, String usuario){
        initComponents();
        user = usuario;
        cPesq = false;
        cIncl = false;
        cExcl = false;
        cSobr = false;
        JPAjuda.setBorder(null);
               
        jLbv.setText("Bem Vindo "+ usuario+" !");
        
        //logo------------------------------------------------------------------------------------//
        BufferedImage resizedImg = new BufferedImage(1000, 350, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/logo2.jpg")).getImage(), 0, 0, 1000, 350, null);
        g.dispose();
        
        jLabel1.setIcon(new javax.swing.ImageIcon(resizedImg));
        //-----------------------------------------------------------------------------------------//    
        
        JPExcluir.setBorder(null);
        jPBuscar.setBorder(null);
        jPIncluir.setBorder(null);
        
        if(tipo==1){            
            
        
            //botao adicionar--------------------------------------------------------------------------//
        
            resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            g = resizedImg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/docma.png")).getImage(), 0, 0, 100, 100, null);
            g.dispose();
    
            jLIncluir.setIcon(new javax.swing.ImageIcon(resizedImg));
            jLIncluir.setVisible(true);
            
            //Adicionar manualmente
            resizedImg = new BufferedImage(75, 75, BufferedImage.TYPE_INT_ARGB);
            g = resizedImg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/DIGITALMENTE.png")).getImage(), 0, 0, 75, 75, null);
            g.dispose();
            
            jLIman.setIcon(new javax.swing.ImageIcon(resizedImg));
            jLIman.setVisible(false);
            
            //Digitalizar Oficios
            resizedImg = new BufferedImage(75, 75, BufferedImage.TYPE_INT_ARGB);
            g = resizedImg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/scannere2.png")).getImage(), 0, 0, 75, 75, null);
            g.dispose();
            
            jPIncluir.add(jLIdig);
            jLIdig.setIcon(new javax.swing.ImageIcon(resizedImg));
            jLIdig.setVisible(false);
            //------------------------------------------------------------------------------------------//

            //botao excluir--------------------------------------------------------------------------//
            resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            g = resizedImg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/exc2.png")).getImage(), 0, 0, 100, 100, null);
            g.dispose();

            jLApagar.setIcon(new javax.swing.ImageIcon(resizedImg));
            //------------------------------------------------------------------------------------------// 
        
        }else{
            jLApagar.setVisible(false);
            jLIncluir.setVisible(false);
        }
        //botao sobre--------------------------------------------------------------------------//
            resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            g = resizedImg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/ajuda.png")).getImage(), 0, 0, 100, 100, null);
            g.dispose();

            jLSobre.setIcon(new javax.swing.ImageIcon(resizedImg));        
            //------------------------------------------------------------------------------------------//
            
            //botao pesquisar--------------------------------------------------------------------------//
            resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            g = resizedImg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/pesquisar.png")).getImage(), 0, 0, 100, 100, null);
            g.dispose();
        
            jLBuscar.setIcon(new javax.swing.ImageIcon(resizedImg));
            //------------------------------------------------------------------------------------------//
            
            //botao trocar usuario--------------------------------------------------------------------------//
            resizedImg = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
            g = resizedImg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/log.png")).getImage(), 0, 0, 40, 40, null);
            g.dispose();

            jLLogin.setIcon(new javax.swing.ImageIcon(resizedImg));        
            //------------------------------------------------------------------------------------------//    
            
        
            //botao fechar--------------------------------------------------------------------------//
            resizedImg = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
            g = resizedImg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/fechar.png")).getImage(), 0, 0, 40, 40, null);
            g.dispose();

            jLFechar.setIcon(new javax.swing.ImageIcon(resizedImg));        
            //------------------------------------------------------------------------------------------//    
            
            this.getContentPane().setBackground(Color.white);
            this.jPanel1.setBackground(Color.white);
            ImageIcon image = new ImageIcon(getClass().getResource("/procuracoes/recursos/icon.png"));
            this.setIconImage(image.getImage());

            this.createPopupFechar();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPIncluir = new javax.swing.JPanel();
        jLIncluir = new javax.swing.JLabel();
        jLIman = new javax.swing.JLabel();
        jLIdig = new javax.swing.JLabel();
        jPBuscar = new javax.swing.JPanel();
        jLBuscar = new javax.swing.JLabel();
        JPExcluir = new javax.swing.JPanel();
        jLApagar = new javax.swing.JLabel();
        JPAjuda = new javax.swing.JPanel();
        jLSobre = new javax.swing.JLabel();
        jLLogin = new javax.swing.JLabel();
        jLFechar = new javax.swing.JLabel();
        jLbv = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cartório Mezzari - 1º Registro de Imóveis de Pelotas v0.1.0");
        setIconImage(getIconImage());
        setIconImages(null);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 124, 184), 3, true));
        jPanel1.setForeground(new java.awt.Color(102, 124, 184));

        jLabel6.setFont(new java.awt.Font("Arial Black", 0, 44)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 124, 184));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Sistema de Ofícios");
        jLabel6.setToolTipText("");
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jPIncluir.setBackground(new java.awt.Color(255, 255, 255));
        jPIncluir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPIncluir.setPreferredSize(new java.awt.Dimension(250, 250));

        jLIncluir.setToolTipText("Incluir Procuração");
        jLIncluir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLIncluirMouseClicked(evt);
            }
        });

        jLIman.setToolTipText("Incluir Manualmente");
        jLIman.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLImanMouseClicked(evt);
            }
        });

        jLIdig.setToolTipText("Digitalizar Procuração");
        jLIdig.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLIdigMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPIncluirLayout = new javax.swing.GroupLayout(jPIncluir);
        jPIncluir.setLayout(jPIncluirLayout);
        jPIncluirLayout.setHorizontalGroup(
            jPIncluirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPIncluirLayout.createSequentialGroup()
                .addGroup(jPIncluirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPIncluirLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLIman, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLIdig, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPIncluirLayout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jLIncluir, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jPIncluirLayout.setVerticalGroup(
            jPIncluirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPIncluirLayout.createSequentialGroup()
                .addComponent(jLIncluir, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPIncluirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLIman, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLIdig, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jPBuscar.setBackground(new java.awt.Color(255, 255, 255));
        jPBuscar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPBuscar.setPreferredSize(new java.awt.Dimension(250, 250));

        jLBuscar.setToolTipText("Pesquisar");
        jLBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLBuscarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPBuscarLayout = new javax.swing.GroupLayout(jPBuscar);
        jPBuscar.setLayout(jPBuscarLayout);
        jPBuscarLayout.setHorizontalGroup(
            jPBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPBuscarLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(jLBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(76, Short.MAX_VALUE))
        );
        jPBuscarLayout.setVerticalGroup(
            jPBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPBuscarLayout.createSequentialGroup()
                .addComponent(jLBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        JPExcluir.setBackground(new java.awt.Color(255, 255, 255));
        JPExcluir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JPExcluir.setPreferredSize(new java.awt.Dimension(250, 250));

        jLApagar.setToolTipText("Excluir Procuração");
        jLApagar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLApagarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout JPExcluirLayout = new javax.swing.GroupLayout(JPExcluir);
        JPExcluir.setLayout(JPExcluirLayout);
        JPExcluirLayout.setHorizontalGroup(
            JPExcluirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPExcluirLayout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(jLApagar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );
        JPExcluirLayout.setVerticalGroup(
            JPExcluirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPExcluirLayout.createSequentialGroup()
                .addComponent(jLApagar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        JPAjuda.setBackground(new java.awt.Color(255, 255, 255));
        JPAjuda.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JPAjuda.setPreferredSize(new java.awt.Dimension(250, 250));

        jLSobre.setToolTipText("Sobre o Sistema de Procurações Mezzari");
        jLSobre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLSobreMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout JPAjudaLayout = new javax.swing.GroupLayout(JPAjuda);
        JPAjuda.setLayout(JPAjudaLayout);
        JPAjudaLayout.setHorizontalGroup(
            JPAjudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPAjudaLayout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addComponent(jLSobre, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );
        JPAjudaLayout.setVerticalGroup(
            JPAjudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPAjudaLayout.createSequentialGroup()
                .addComponent(jLSobre, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLLoginMouseClicked(evt);
            }
        });

        jLFechar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLFecharMouseClicked(evt);
            }
        });

        jLbv.setFont(new java.awt.Font("Arial Black", 0, 36)); // NOI18N
        jLbv.setForeground(new java.awt.Color(102, 124, 184));
        jLbv.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbv.setText("Bem Vindo Thiago!");
        jLbv.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(40, 40, 40)
                        .addComponent(jLFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPIncluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JPExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JPAjuda, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLbv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLbv, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPIncluir, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                    .addComponent(JPExcluir, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                    .addComponent(JPAjuda, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                    .addComponent(jPBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );

        BufferedImage resizedImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(new javax.swing.ImageIcon(getClass().getResource("/procuracoes/recursos/logo.jpg")).getImage(), 0, 0, WIDTH, HEIGHT, null);
        g2.dispose();
        jLabel1.setIcon(new javax.swing.ImageIcon(resizedImg));
        jLabel1.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1037, 670));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        try {
            atalhos(evt);
        } catch (SQLException ex) {
            Logger.getLogger(Oficios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formKeyPressed

    private void jLSobreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLSobreMouseClicked
        BufferedImage resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/ajuda2.png")).getImage(), 0, 0, 100, 100, null);
        jLSobre.setIcon(new javax.swing.ImageIcon(resizedImg));
        //excluiProcuracao();
        Ajuda a = new Ajuda();
        a.setVisible(true);
        a.addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent w){
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/ajuda.png")).getImage(), 0, 0, 100, 100, null);
                g.dispose();
                jLSobre.setIcon(new javax.swing.ImageIcon(resizedImg));
            }
        });
        
    }//GEN-LAST:event_jLSobreMouseClicked

    private void jLLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLLoginMouseClicked
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja Trocar de Usuário? ", null, JOptionPane.YES_NO_OPTION);
        if(resposta == JOptionPane.YES_OPTION){
            Login l = new Login();
            l.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_jLLoginMouseClicked

    private void jLFecharMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLFecharMouseClicked
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.dispose();
    }//GEN-LAST:event_jLFecharMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        SisLog S = new SisLog("Logout", this.user, "Sucesso");
    }//GEN-LAST:event_formWindowClosed

    private void jLApagarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLApagarMouseClicked
        BufferedImage resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImg.createGraphics();
        
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/exc.png")).getImage(), 0, 0, 100, 100, null);
        jLApagar.setIcon(new javax.swing.ImageIcon(resizedImg));

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/exc2.png")).getImage(), 0, 0, 100, 100, null);
        g.dispose();
        jLApagar.setIcon(new javax.swing.ImageIcon(resizedImg));

    }//GEN-LAST:event_jLApagarMouseClicked

    private void jLBuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLBuscarMouseClicked
        BufferedImage resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImg.createGraphics();
        
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/pesquisar2.png")).getImage(), 0, 0, 100, 100, null);
        jLBuscar.setIcon(new javax.swing.ImageIcon(resizedImg));
            
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/pesquisar.png")).getImage(), 0, 0, 100, 100, null);
        g.dispose();        
        jLBuscar.setIcon(new javax.swing.ImageIcon(resizedImg));
        
        this.buscaOficios();
    }//GEN-LAST:event_jLBuscarMouseClicked

    private void jLIncluirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLIncluirMouseClicked
        BufferedImage resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImg.createGraphics();
        if(!cIncl){
            jLIdig.setVisible(true);
            jLIman.setVisible(true);
            cIncl = true;
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/docma2.png")).getImage(), 0, 0, 100, 100, null);
            g.dispose();
        }else{
            jLIdig.setVisible(false);
            jLIman.setVisible(false);
            cIncl = false;
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/docma.png")).getImage(), 0, 0, 100, 100, null);
            g.dispose();
        }
        jLIncluir.setIcon(new javax.swing.ImageIcon(resizedImg));
    }//GEN-LAST:event_jLIncluirMouseClicked

    private void jLImanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLImanMouseClicked
        InsereProc in;
        try {
            in = new InsereProc(this.user);
            in.setVisible(true);
            SisLog S = new SisLog("InsereProcuracao", this.user, "Manualmente");
        } catch (SQLException ex) {
            Logger.getLogger(Oficios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLImanMouseClicked

    private void jLIdigMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLIdigMouseClicked
        Digitalizacao d = new Digitalizacao();
        try {
            d.salva(this.user);
            SisLog S = new SisLog("InsereProcuracao", this.user, "Digitalizada");
        } catch (MorenaException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_jLIdigMouseClicked
    
    public class MoveJanela implements MouseListener, MouseMotionListener{
        
        JComponent target;
        Point start_drag;
        Point start_loc;
        
        public MoveJanela(JComponent target) {
            this.target = target;
        }
        
        public JFrame getFrame(Container target) {
            if (target instanceof JFrame) {
                return (JFrame) target;
            }
            return getFrame(target.getParent());
        }

        Point getScreenLocation(MouseEvent e) {
            Point cursor = e.getPoint();
            Point target_location = this.target.getLocationOnScreen();
            return new Point((int) (target_location.getX() + cursor.getX()), (int) (target_location.getY() + cursor.getY()));
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            this.start_drag = this.getScreenLocation(e);
            this.start_loc = this.getFrame(this.target).getLocation();
        }

        @Override
        public void mouseReleased(MouseEvent e) {            
        }

        @Override
        public void mouseEntered(MouseEvent e) {            
        }

        @Override
        public void mouseExited(MouseEvent e) {            
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point current = this.getScreenLocation(e);
            Point offset = new Point((int) current.getX() - (int) start_drag.getX(),(int) current.getY() - (int) start_drag.getY());
            JFrame frame = this.getFrame(target);
            Point new_location = new Point((int) (this.start_loc.getX() + offset.getX()), (int) (this.start_loc.getY() + offset.getY()));
            frame.setLocation(new_location);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            
        }
               
        
    }
    
        
    private void createPopupFechar() {

        JPopupMenu pmenu = new JPopupMenu();

        JMenuItem quitMi = new JMenuItem("Fechar - Esc");
        quitMi.addActionListener((ActionEvent e) -> {
            SisLog S = new SisLog("Logout", this.user, "Sucesso");
            System.exit(0);
        });

        pmenu.add(quitMi);
        MoveJanela mj = new MoveJanela(this.jPanel1);
        jPanel1.addMouseListener(mj);
        jPanel1.addMouseMotionListener(mj);
        jPanel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    pmenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        
    }

    
    private void atalhos(KeyEvent evt) throws SQLException {
        /*Mostra a key da tecla pressionada*/
        //System.out.println(evt.getKeyCode());  

        //Condição para CTRL + D 
        if ((evt.getKeyCode() == KeyEvent.VK_D) && (evt.isControlDown())) {
            Digitalizacao d = new Digitalizacao();
            try {
                d.salva(this.user);
            } catch (MorenaException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
            
            //Condição para CTRL + N  
        } else if ((evt.getKeyCode() == KeyEvent.VK_N) && (evt.isControlDown())) {
            InsereProc in;
            in = new InsereProc(this.user);
            in.setVisible(true);
            
            //Condição para CTRL + F  
        } else if ((evt.getKeyCode() == KeyEvent.VK_F) && (evt.isControlDown())) {
            buscaOficios();            
            //Condição para CTRL + P
        
        } else if ((evt.getKeyCode() == KeyEvent.VK_S) && (evt.isControlDown())) {
            Ajuda a = new Ajuda();
            a.setVisible(true);
            
            //Condição para CTRL + Del 
        } else if ((evt.getKeyCode() == KeyEvent.VK_DELETE) && (evt.isControlDown())) {
            //excluiOficios();
            
            //Condição para Esc
        } else if ((evt.getKeyCode() == KeyEvent.VK_ESCAPE)) {
            this.dispose();
        }
    }
   
    
    public void buscaOficios(){
        Database db;
        db = new Database();
        db.connect();
        
        JButton jb = new JButton("Ok!");
                
        JTextField jt = new JTextField("0001/2015");
        KeyListener l = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    for(ActionListener a: jb.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(e, 1, null));
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    for(ActionListener a: jb.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(e, 1, null));
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    for(ActionListener a: jb.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(e, 1, null));
                    }
                }
            }
        };
        jt.addKeyListener(l);
        jt.selectAll();
        
        jb.addActionListener((ActionEvent e) ->{
            VisualizaOfic v;
            String numero, ano;
            numero = jt.getText().substring(0, 4);
            ano = jt.getText().substring(5);
            v = new VisualizaOfic(db.getCaminho(Integer.valueOf(numero), Integer.valueOf(ano)), Integer.valueOf(numero), Integer.valueOf(ano), this.user);
            SisLog S = new SisLog("BuscaProcuracao", this.user, "Procuracao - " + jt.getText());
            v.setVisible(true);
            v.getContentPane().setBackground(Color.WHITE);
            dialog.setVisible(false);
            this.toBack();
        });
        Object data[] = {"Digite o número/ano do Ofício:", jt, jb};
        JOptionPane option = new JOptionPane();
        option.setMessage(data);
        option.setMessageType(JOptionPane.QUESTION_MESSAGE);
        option.remove(1);
        dialog = option.createDialog(null, "Busca por Oficio");
        dialog.setVisible(true);
        dialog.setName(user);
                
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPAjuda;
    private javax.swing.JPanel JPExcluir;
    private javax.swing.JLabel jLApagar;
    private javax.swing.JLabel jLBuscar;
    private javax.swing.JLabel jLFechar;
    private javax.swing.JLabel jLIdig;
    private javax.swing.JLabel jLIman;
    private javax.swing.JLabel jLIncluir;
    private javax.swing.JLabel jLLogin;
    private javax.swing.JLabel jLSobre;
    public javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLbv;
    private javax.swing.JPanel jPBuscar;
    private javax.swing.JPanel jPIncluir;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

}
