package oficios.frames;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import javax.swing.JPanel;
import procuracoes.classes.SisLog;
import oficios.db.Database;
import geral.db.Datauser;

/**
 *
 * @author Thiago
 */

public class VisualizaOfic extends javax.swing.JFrame {
    
    public Database db;
    static String mensagem = null;//usado nos Dialogos de edição de data
    static JDialog dialog = null;//usado nos Dialogos de edição de data
    public String user;
    int tipo;
    
    /**
     * 
     * @param caminho STRING Representando o Caminho do Ofício
     * @param numero INT Representando o Numero do Oficio
     * @param ano INT Representando o Ano do Oficio
     * @param usuario STRING Representando o Usuario do Sistema
     */
    public VisualizaOfic(String caminho, int numero, int ano, String usuario) {
        
        Datauser du = new Datauser();
        du.connect();
        tipo = du.getTipo(usuario);
        this.user = usuario;
        try {
            db = new Database();
            db.connect();
            
            JPanel jPanel1; //painel de visualizacao
            ImageIcon image = new ImageIcon(getClass().getResource("/recursos/icon.png"));
            this.setIconImage(image.getImage());
            JInternalFrame PDFrame = new JInternalFrame("Visualizacao"); //Frame Interno responsável pela exibição do painel de visualização
            Document document = new Document(); //cria um objeto Documento
            try {
                document.setFile(caminho); //envia o caminho para o documento
            } catch(PDFException ex) {
                JOptionPane.showMessageDialog(null,"Erro ao analizar documento " +  ex.getMessage());
            } catch(PDFSecurityException ex) {
                JOptionPane.showMessageDialog(null,"Erro - encriptacao nao suportada " +  ex.getMessage());
            } catch(FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null,"Error - arquivo nao encontrado " +  ex.getMessage());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,"Error - IO -" +  ex.getMessage());
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null,"Documento não encontrado " +  ex.getMessage());
            }
            SwingController controller = new SwingController(); // inicia o controlador
            SwingViewBuilder factory = new SwingViewBuilder(controller); // Inicia um SwingViewFactory configurado com o controlador
            jPanel1 = factory.buildViewerPanel(); // Usa a fábrica para construir um jPanel pre-configurado com uma UI completa já pelo Viewer
            ComponentKeyBinding.install(controller, jPanel1); // adiciona o o controlador no painel
            PDFrame.setBounds(0 , 0, 100, 100); //define a localização e tamanho do frame
            PDFrame.add(jPanel1); //adiciona o painel de visualização ao frame interno
            //impede que o frame se mova mas nao impede de ser editado-----------------------------------------------------------------------------------------------------------//
            ComponentListener listener = new ComponentListener() { //impede que o frame interno seja movido implementando um listener com a substituição do método componentMoved
                @Override
                public void componentResized(ComponentEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    //donothing
                }
                
                @Override
                public void componentMoved(ComponentEvent e) {
                    PDFrame.setLocation(15, 15);
                }
                
                @Override
                public void componentShown(ComponentEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    //donothing
                }
                
                @Override
                public void componentHidden(ComponentEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    //donothing
                }
            };
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------//
            PDFrame.addComponentListener(listener); //adicioa o listener ao frame interno;
            PDFrame.setVisible(true);
            PDFrame.setFocusable(false);
            this.add(PDFrame);
            this.pack();
            this.setVisible(true);
            // Abre o PDF no view view
            controller.openDocument(document.getDocumentLocation());
            controller.getDocumentViewController().setFitMode(0);
            initComponents();
            carregaCampos(numero, ano);
            jPanel1.setBackground(Color.white);
            //PDFrame.getContentPane().setBackground(Color.white);
        jPanel1.paint(jPanel1.getGraphics());
        //PDFrame.paint(PDFrame.getGraphics());
        } catch(SQLException ex) {
            Logger.getLogger(VisualizaOfic.class.getName()).log(Level.SEVERE,null, ex);
        }       
        
    }
    
    /**
     * Método Responsável pela carga dos dados no formulario
     * @throws SQLException 
     */
    public void carregaCampos(int numero, int ano) throws SQLException{                
        this.jLcod.setText(numero + "/" + ano); //define o codigo
        this.jLdtini.setText(db.getData(numero, ano));//exibe a data inicial                      
    }
    
           
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator3 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        jL5 = new javax.swing.JLabel();
        jLdtini = new javax.swing.JLabel();
        jLcod4 = new javax.swing.JLabel();
        jLcod = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Visualizacao");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Aparajita", 0, 10)); // NOI18N
        setForeground(java.awt.Color.white);
        setIconImages(null);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jSeparator1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jL5.setBackground(new java.awt.Color(255, 255, 255));
        jL5.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jL5.setText("Data de Inserção:");

        jLdtini.setBackground(new java.awt.Color(255, 255, 255));
        jLdtini.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLdtini.setText("<00/00/0000>");

        jLcod4.setBackground(new java.awt.Color(255, 255, 255));
        jLcod4.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLcod4.setText("Cod:");

        jLcod.setBackground(new java.awt.Color(255, 255, 255));
        jLcod.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLcod.setText("<00>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLcod4)
                .addGap(6, 6, 6)
                .addComponent(jLcod)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 705, Short.MAX_VALUE)
                .addComponent(jL5)
                .addGap(6, 6, 6)
                .addComponent(jLdtini)
                .addContainerGap())
            .addComponent(jSeparator1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 521, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLcod4)
                    .addComponent(jLcod)
                    .addComponent(jL5)
                    .addComponent(jLdtini))
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(1065, 608));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        SisLog S = new SisLog("Visualiza", this.user, "Cancelado");
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jL5;
    private javax.swing.JLabel jLcod;
    private javax.swing.JLabel jLcod4;
    private javax.swing.JLabel jLdtini;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    // End of variables declaration//GEN-END:variables
}
