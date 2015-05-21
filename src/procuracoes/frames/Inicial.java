/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procuracoes.frames;

import SK.gnome.morena.MorenaException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import procuracoes.db.Database;

public class Inicial extends javax.swing.JFrame {

    public Inicial() {
        initComponents();
        //logo------------------------------------------------------------------------------------//
        BufferedImage resizedImg = new BufferedImage(400, 330, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/logo.jpg")).getImage(), 0, 0, 400, 330, null);
        g.dispose();

        jLabel1.setIcon(new javax.swing.ImageIcon(resizedImg));
        //-----------------------------------------------------------------------------------------//
        //botao adicionar--------------------------------------------------------------------------//
        resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        g = resizedImg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/botao.jpg")).getImage(), 0, 0, 100, 100, null);
        g.dispose();

        jLabel2.setIcon(new javax.swing.ImageIcon(resizedImg));
        //------------------------------------------------------------------------------------------//
        //botao pesquisar--------------------------------------------------------------------------//
        resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        g = resizedImg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/botao_pesquisa.png")).getImage(), 0, 0, 100, 100, null);
        g.dispose();

        jLabel3.setIcon(new javax.swing.ImageIcon(resizedImg));
        //------------------------------------------------------------------------------------------//
        //botao excluir--------------------------------------------------------------------------//
        resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        g = resizedImg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/botao_exclui2.jpg")).getImage(), 0, 0, 100, 100, null);
        g.dispose();

        jLabel4.setIcon(new javax.swing.ImageIcon(resizedImg));
        //------------------------------------------------------------------------------------------//
        //botao sobre--------------------------------------------------------------------------//
        resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        g = resizedImg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(new ImageIcon(getClass().getResource("/procuracoes/recursos/botao_sobre.jpg")).getImage(), 0, 0, 100, 100, null);
        g.dispose();

        jLabel5.setIcon(new javax.swing.ImageIcon(resizedImg));
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cartório Mezzari - 1º Registro de Imóveis de Pelotas");
        setIconImage(getIconImage());
        setIconImages(null);
        setUndecorated(true);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setToolTipText("Adicionar Procuração");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Liberation Serif", 0, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 124, 184));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Sistema de Procurações");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(149, 149, 149)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(61, 61, 61))
        );

        BufferedImage resizedImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(new javax.swing.ImageIcon(getClass().getResource("/procuracoes/recursos/logo.jpg")).getImage(), 0, 0, WIDTH, HEIGHT, null);
        g2.dispose();
        jLabel1.setIcon(new javax.swing.ImageIcon(resizedImg));
        jLabel1.getAccessibleContext().setAccessibleDescription("");

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 660, 370));

        setSize(new java.awt.Dimension(661, 370));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        createPopupIncluir().show(evt.getComponent(), evt.getX(), evt.getY());
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        createPopupExcluir().show(evt.getComponent(), evt.getX(), evt.getY());
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        createPopupBuscar().show(evt.getComponent(), evt.getX(), evt.getY());
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        createPopupAjuda().show(evt.getComponent(), evt.getX(), evt.getY());
    }//GEN-LAST:event_jLabel5MouseClicked

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        atalhos(evt);
    }//GEN-LAST:event_formKeyPressed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Inicial().setVisible(true);
            }
        });
    }

    public void apagaProc(int id) {
        Visualiza v = new Visualiza("D:/JavaImoveis/" + Integer.toString(id) + ".pdf", id);
        JButton b = new JButton("APAGAR!");
        v.add(b);
        b.setBounds(100, 520, 125, 25);
        b.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int resp;
                resp = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja EXCLUIR esta PROCURACAO?");
                if (resp == 0) {
                    Database db = new Database();
                    db.connect();
                    resp = db.apagaProc(id);
                    if (resp == 1) {
                        JOptionPane.showMessageDialog(null, "PROCURACAO Excluída com sucesso!");
                        v.dispose();
                        Inicial i = new Inicial();
                        i.setVisible(true);
                    }
                }
            }
        });
        v.pack();
        v.setVisible(true);
        this.dispose();

    }

    private void createPopupFechar() {

        JPopupMenu pmenu = new JPopupMenu();

        JMenuItem quitMi = new JMenuItem("Fechar - Esc");
        quitMi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        pmenu.add(quitMi);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    pmenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private JPopupMenu createPopupIncluir() {

        JPopupMenu pmenu = new JPopupMenu();

        JMenuItem digMi = new JMenuItem("Digitalizar Procuracao - Ctrl+D");
        digMi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Digitalizacao d = new Digitalizacao();
                try {
                    d.salva();
                } catch (MorenaException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });

        pmenu.add(digMi);

        JMenuItem manMi = new JMenuItem("Inserir Manualmente - Ctrl+N");
        manMi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Insere in;
                in = new Insere();
                in.setVisible(true);
            }
        });

        pmenu.add(manMi);
        return pmenu;
    }

    private JPopupMenu createPopupExcluir() {

        JPopupMenu pmenu = new JPopupMenu();

        JMenuItem Mi = new JMenuItem("Excluir Procuração - Ctrl+Del");
        Mi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuscaProcuracao b = new BuscaProcuracao(1);
                b.setVisible(true);
            }
        });

        pmenu.add(Mi);
        return pmenu;
    }

    private JPopupMenu createPopupBuscar() {

        JPopupMenu pmenu = new JPopupMenu();

        JMenuItem procMi = new JMenuItem("Buscar Procuração - Ctrl+F");
        procMi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuscaProcuracao b;
                b = new BuscaProcuracao(0);
                b.setVisible(true);
            }
        });
        pmenu.add(procMi);

        JMenuItem procuMi = new JMenuItem("Buscar Procurador - Ctrl+P");
        procuMi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuscaProcurador b;
                b = new BuscaProcurador();
                b.setVisible(true);
            }
        });
        pmenu.add(procuMi);

        JMenuItem entMi = new JMenuItem("Buscar Entidade - Ctrl+E");
        entMi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuscaEntidade b;
                b = new BuscaEntidade();
                b.setVisible(true);
            }
        });
        pmenu.add(entMi);

        return pmenu;
    }

    private JPopupMenu createPopupAjuda() {

        JPopupMenu pmenu = new JPopupMenu();

        JMenuItem Mi = new JMenuItem("Sobre o Sistema de Procurações Mezzari - Ctrl+S");
        Mi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ajuda a = new Ajuda();
                a.setVisible(true);
            }
        });

        pmenu.add(Mi);
        return pmenu;
    }

    private void atalhos(KeyEvent evt) {
        /*Mostra a key da tecla pressionada*/
        //System.out.println(evt.getKeyCode());  

        //Condição para CTRL + D 
        if ((evt.getKeyCode() == KeyEvent.VK_D) && (evt.isControlDown())) {
            Digitalizacao d = new Digitalizacao();
            try {
                d.salva();
            } catch (MorenaException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
            
            //Condição para CTRL + N  
        } else if ((evt.getKeyCode() == KeyEvent.VK_N) && (evt.isControlDown())) {
            Insere in;
            in = new Insere();
            in.setVisible(true);
            
            //Condição para CTRL + F  
        } else if ((evt.getKeyCode() == KeyEvent.VK_F) && (evt.isControlDown())) {
            BuscaProcuracao b;
            b = new BuscaProcuracao(0);
            b.setVisible(true);
            
            //Condição para CTRL + P
        } else if ((evt.getKeyCode() == KeyEvent.VK_P) && (evt.isControlDown())) {
            BuscaProcurador b;
            b = new BuscaProcurador();
            b.setVisible(true);
            
            //Condição para CTRL + E
        } else if ((evt.getKeyCode() == KeyEvent.VK_E) && (evt.isControlDown())) {
            BuscaEntidade b;
            b = new BuscaEntidade();
            b.setVisible(true);
            
            //Condição para CTRL + S
        } else if ((evt.getKeyCode() == KeyEvent.VK_S) && (evt.isControlDown())) {
            Ajuda a = new Ajuda();
            a.setVisible(true);
            
            //Condição para CTRL + Del 
        } else if ((evt.getKeyCode() == KeyEvent.VK_DELETE) && (evt.isControlDown())) {
            BuscaProcuracao b = new BuscaProcuracao(1);
            b.setVisible(true);
            
            //Condição para CTRL + Esc
        } else if ((evt.getKeyCode() == KeyEvent.VK_ESCAPE)) {
            this.dispose();
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

}
