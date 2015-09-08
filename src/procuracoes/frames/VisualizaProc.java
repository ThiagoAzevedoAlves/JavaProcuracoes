package procuracoes.frames;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import javax.swing.JPanel;
import javax.swing.JTextField;
import procuracoes.classes.ComboData;
import procuracoes.classes.Entidade;
import procuracoes.classes.Procurador;
import procuracoes.classes.SisLog;
import procuracoes.db.Database;
import procuracoes.db.Dataent;
import procuracoes.db.Dataproc;
import geral.db.Datauser;

/**
 * Classe Responsável pelo Frame de Visualização de Ofícios;
 * @author Thiago Azevedo Alves
 */

public class VisualizaProc extends javax.swing.JFrame {
    
    public Database db;
    public Dataproc dp;
    public Dataent de;
    public List<Procurador> procuradores;
    public List<Entidade> entidades;
    public int cod;
    static String mensagem = null;//usado nos Dialogos de edição de data
    static JDialog dialog = null;//usado nos Dialogos de edição de data
    public String user;
    int tipo;
    
    /**
     * @param caminho STRING representando o Caminho da Procuração
     * @param cod INT representando o código da Procuração
     * @param usuario STRING representando o Usuário do Sistema
     */
    public VisualizaProc(String caminho, int cod, String usuario) {
        
        Datauser du = new Datauser();
        du.connect();
        tipo = du.getTipo(usuario);
        this.user = usuario;
        try {
            this.cod = cod;
            db = new Database();
            db.connect();
            dp = new Dataproc();
            dp.connect();
            de = new Dataent();
            de.connect();
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
                    PDFrame.setLocation(15, 10);
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
            
            if(tipo == 1){
            
                //botao excluir procurador --------------------------------------------------------------------------//

                BufferedImage resizedImg = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = resizedImg.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.drawImage(new ImageIcon(getClass().getResource("/recursos/exc2.png")).getImage(), 0, 0, 40, 40, null);
                g.dispose();

                jLremproc.setIcon(new javax.swing.ImageIcon(resizedImg));
                //------------------------------------------------------------------------------------------//

                //botao adicionar prourador--------------------------------------------------------------------------//

                resizedImg = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
                g = resizedImg.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.drawImage(new ImageIcon(getClass().getResource("/recursos/add.png")).getImage(), 0, 0, 40, 40, null);
                g.dispose();

                jLaddproc.setIcon(new javax.swing.ImageIcon(resizedImg));
                //------------------------------------------------------------------------------------------//

                //botao excluir entidade --------------------------------------------------------------------------//

                resizedImg = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
                g = resizedImg.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.drawImage(new ImageIcon(getClass().getResource("/recursos/exc2.png")).getImage(), 0, 0, 40, 40, null);
                g.dispose();

                jLrement.setIcon(new javax.swing.ImageIcon(resizedImg));
                //------------------------------------------------------------------------------------------//

                //botao adicionar entidade--------------------------------------------------------------------------//

                resizedImg = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
                g = resizedImg.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.drawImage(new ImageIcon(getClass().getResource("/recursos/add2.png")).getImage(), 0, 0, 40, 40, null);
                g.dispose();

                jLaddent.setIcon(new javax.swing.ImageIcon(resizedImg));
                //------------------------------------------------------------------------------------------//
            }
            carregaCampos();
            jPanel1.setBackground(Color.white);
            //PDFrame.getContentPane().setBackground(Color.white);
        jPanel1.paint(jPanel1.getGraphics());
        //PDFrame.paint(PDFrame.getGraphics());
        } catch(SQLException ex) {
            Logger.getLogger(VisualizaProc.class.getName()).log(Level.SEVERE,null, ex);
        }       
        
    }
    
    /**
     * Método Responsável pela carga dos dados no formulario
     * @throws SQLException 
     */
    public void carregaCampos() throws SQLException{
        procuradores = new ArrayList<>();                              //Lista auxiliar para carregar os procuradores
        procuradores.addAll(dp.getProcuradores(this.cod));             //popula a lista de procuradores
        entidades = new ArrayList<>();                                 //Lista auxiliar para carregar as entidades
        entidades.addAll(de.getEntidades(this.cod));                   //popula a lista de entidades
        
        this.jLcod.setText(String.valueOf(cod));                       //define o codigo
        this.jLcod.setForeground(Color.red);                           //destaca o código como vermelho
        this.jLcod4.setForeground(Color.red);
        
        this.jLproc.setText("/"+Integer.toString(procuradores.size()));//define o numero de procuradores
        setProcuradores();                                             //popula a lista do comboBox com o numero de procuradores
        this.jLprocnome.setText(procuradores.get(0).getNome());        //exibe o nome do procurador
        this.jLproccpf.setText(procuradores.get(0).getCpf());          //exibe o cpf do procurador
        this.jLprocpod.setText(procuradores.get(0).getPoderes());      //exibe os poderes do procurador
        
        this.jLdtini.setText(db.getDtini(cod));                        //exibe a data inicial
        this.jLdtfin.setText(db.getDtfin(cod));                        //exibe a data final
        this.jLconjunto.setText(db.getConjunto(cod));                  //exibe o tipo de conjunto
        
        this.jLentidade.setText("/"+Integer.toString(entidades.size()));//define o numero de procuradores
        setEntidades();
        this.jLentnome.setText(entidades.get(0).getNome());             //exibe o nome da entidade
        this.jLentresp.setText(entidades.get(0).getResponsavel());      //exibe o nome do responsavel pela entidade
        this.jLentcnpj.setText(entidades.get(0).getCnpj());             //exibe o cnpj da entidade 
        this.jLentcpf.setText(entidades.get(0).getCpf());               //exibe o cpf do responsavel pela entidade
        
        
    }
    
    /**
     * Método Responsável por atualizar o ComboBox de Procuradores com a quantidade correta.
     */
    public void setProcuradores(){
        int i = procuradores.size();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        String s;
        int n = 1;
        while(n <= i){
            s = "Procurador " + String.valueOf(n);
            model.addElement(s);
            n++;
        }
        this.jCproc.setModel(model);
    }
    
    public void setEntidades(){
        int i = entidades.size();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        String s;
        int n = 1;
        while(n <= i){
            s = "Entidade " + String.valueOf(n);
            model.addElement(s);
            n++;
        }
        this.jCentidade.setModel(model);
    }
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator3 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        jL5 = new javax.swing.JLabel();
        jLdtini = new javax.swing.JLabel();
        jLcod2 = new javax.swing.JLabel();
        jLdtfin = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jLcod4 = new javax.swing.JLabel();
        jLcod = new javax.swing.JLabel();
        jLcod6 = new javax.swing.JLabel();
        jCproc = new javax.swing.JComboBox();
        jLproc = new javax.swing.JLabel();
        jLprocnome2 = new javax.swing.JLabel();
        jLproccpf2 = new javax.swing.JLabel();
        JLprocpod2 = new javax.swing.JLabel();
        jLcod11 = new javax.swing.JLabel();
        jCentidade = new javax.swing.JComboBox();
        jLentidade = new javax.swing.JLabel();
        jLentnome2 = new javax.swing.JLabel();
        JLentcnpj2 = new javax.swing.JLabel();
        jLcod15 = new javax.swing.JLabel();
        jLentresp2 = new javax.swing.JLabel();
        jLentcpf2 = new javax.swing.JLabel();
        jLconjunto = new javax.swing.JLabel();
        jLprocpod = new javax.swing.JLabel();
        jLprocnome = new javax.swing.JLabel();
        jLproccpf = new javax.swing.JLabel();
        jLentnome = new javax.swing.JLabel();
        jLentcnpj = new javax.swing.JLabel();
        jLentresp = new javax.swing.JLabel();
        jLentcpf = new javax.swing.JLabel();
        jLremproc = new javax.swing.JLabel();
        jLaddproc = new javax.swing.JLabel();
        jLaddent = new javax.swing.JLabel();
        jLrement = new javax.swing.JLabel();

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
        jL5.setText("Data Inicial:");

        jLdtini.setBackground(new java.awt.Color(255, 255, 255));
        jLdtini.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLdtini.setText("<00/00/0000>");
        jLdtini.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLdtiniMouseClicked(evt);
            }
        });

        jLcod2.setBackground(new java.awt.Color(255, 255, 255));
        jLcod2.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLcod2.setText("Data Final:");

        jLdtfin.setBackground(new java.awt.Color(255, 255, 255));
        jLdtfin.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLdtfin.setText("<00/00/0000>");
        jLdtfin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLdtfinMouseClicked(evt);
            }
        });

        jSeparator2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLcod4.setBackground(new java.awt.Color(255, 255, 255));
        jLcod4.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLcod4.setText("Cod:");

        jLcod.setBackground(new java.awt.Color(255, 255, 255));
        jLcod.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLcod.setText("<00>");

        jLcod6.setBackground(new java.awt.Color(255, 255, 255));
        jLcod6.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLcod6.setText("Procurador:");

        jCproc.setFont(new java.awt.Font("Square721 BT", 0, 11)); // NOI18N
        jCproc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Procurador 1" }));
        jCproc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCprocItemStateChanged(evt);
            }
        });

        jLproc.setBackground(new java.awt.Color(255, 255, 255));
        jLproc.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLproc.setText("</1>");

        jLprocnome2.setBackground(new java.awt.Color(255, 255, 255));
        jLprocnome2.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLprocnome2.setText("Nome:");
        jLprocnome2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLprocnome2MouseClicked(evt);
            }
        });

        jLproccpf2.setBackground(new java.awt.Color(255, 255, 255));
        jLproccpf2.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLproccpf2.setText("Cpf:");
        jLproccpf2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLproccpf2MouseClicked(evt);
            }
        });

        JLprocpod2.setBackground(new java.awt.Color(255, 255, 255));
        JLprocpod2.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        JLprocpod2.setText("Poderes:");
        JLprocpod2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLprocpod2MouseClicked(evt);
            }
        });

        jLcod11.setBackground(new java.awt.Color(255, 255, 255));
        jLcod11.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLcod11.setText("Entidade:");

        jCentidade.setFont(new java.awt.Font("Square721 BT", 0, 11)); // NOI18N
        jCentidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Entidade 1" }));
        jCentidade.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCentidadeItemStateChanged(evt);
            }
        });

        jLentidade.setBackground(new java.awt.Color(255, 255, 255));
        jLentidade.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLentidade.setText("</1>");

        jLentnome2.setBackground(new java.awt.Color(255, 255, 255));
        jLentnome2.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLentnome2.setText("Nome:");
        jLentnome2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLentnome2MouseClicked(evt);
            }
        });

        JLentcnpj2.setBackground(new java.awt.Color(255, 255, 255));
        JLentcnpj2.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        JLentcnpj2.setText("Cnpj:");
        JLentcnpj2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLentcnpj2MouseClicked(evt);
            }
        });

        jLcod15.setBackground(new java.awt.Color(255, 255, 255));
        jLcod15.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLcod15.setText("Conjunto:");

        jLentresp2.setBackground(new java.awt.Color(255, 255, 255));
        jLentresp2.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLentresp2.setText("Responsável:");
        jLentresp2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLentresp2MouseClicked(evt);
            }
        });

        jLentcpf2.setBackground(new java.awt.Color(255, 255, 255));
        jLentcpf2.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLentcpf2.setText("Cpf:");
        jLentcpf2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLentcpf2MouseClicked(evt);
            }
        });

        jLconjunto.setBackground(new java.awt.Color(255, 255, 255));
        jLconjunto.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLconjunto.setText("<Em conjunto>");
        jLconjunto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLconjuntoMouseClicked(evt);
            }
        });

        jLprocpod.setBackground(new java.awt.Color(255, 255, 255));
        jLprocpod.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLprocpod.setText("<Poderes>");
        jLprocpod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLprocpodMouseClicked(evt);
            }
        });

        jLprocnome.setBackground(new java.awt.Color(255, 255, 255));
        jLprocnome.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLprocnome.setText("<Nome>");
        jLprocnome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLprocnomeMouseClicked(evt);
            }
        });

        jLproccpf.setBackground(new java.awt.Color(255, 255, 255));
        jLproccpf.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLproccpf.setText("<Cpf>");
        jLproccpf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLproccpfMouseClicked(evt);
            }
        });

        jLentnome.setBackground(new java.awt.Color(255, 255, 255));
        jLentnome.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLentnome.setText("<Nome>");
        jLentnome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLentnomeMouseClicked(evt);
            }
        });

        jLentcnpj.setBackground(new java.awt.Color(255, 255, 255));
        jLentcnpj.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLentcnpj.setText("<Cnpj>");
        jLentcnpj.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLentcnpjMouseClicked(evt);
            }
        });

        jLentresp.setBackground(new java.awt.Color(255, 255, 255));
        jLentresp.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLentresp.setText("<Responsavel>");
        jLentresp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLentrespMouseClicked(evt);
            }
        });

        jLentcpf.setBackground(new java.awt.Color(255, 255, 255));
        jLentcpf.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLentcpf.setText("<Cpf>");
        jLentcpf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLentcpfMouseClicked(evt);
            }
        });

        jLremproc.setBackground(new java.awt.Color(255, 255, 255));
        jLremproc.setFont(new java.awt.Font("Liberation Serif", 1, 14)); // NOI18N
        jLremproc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLremprocMouseClicked(evt);
            }
        });

        jLaddproc.setBackground(new java.awt.Color(255, 255, 255));
        jLaddproc.setFont(new java.awt.Font("Liberation Serif", 1, 14)); // NOI18N
        jLaddproc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLaddprocMouseClicked(evt);
            }
        });

        jLaddent.setBackground(new java.awt.Color(255, 255, 255));
        jLaddent.setFont(new java.awt.Font("Liberation Serif", 1, 14)); // NOI18N
        jLaddent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLaddentMouseClicked(evt);
            }
        });

        jLrement.setBackground(new java.awt.Color(255, 255, 255));
        jLrement.setFont(new java.awt.Font("Liberation Serif", 1, 14)); // NOI18N
        jLrement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLrementMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLcod4)
                        .addGap(6, 6, 6)
                        .addComponent(jLcod)
                        .addGap(278, 278, 278)
                        .addComponent(jLcod15)
                        .addGap(6, 6, 6)
                        .addComponent(jLconjunto)
                        .addGap(18, 18, 18)
                        .addComponent(jL5)
                        .addGap(6, 6, 6)
                        .addComponent(jLdtini)
                        .addGap(45, 45, 45)
                        .addComponent(jLcod2)
                        .addGap(6, 6, 6)
                        .addComponent(jLdtfin)
                        .addGap(0, 98, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLcod6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jCproc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLproc)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLremproc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLaddproc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLproccpf2)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLproccpf, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLprocnome2)
                                            .addGap(10, 10, 10)
                                            .addComponent(jLprocnome, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(31, 31, 31))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(JLprocpod2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLprocpod, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLentnome2)
                                    .addComponent(JLentcnpj2))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLentnome, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
                                    .addComponent(jLentcnpj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLcod11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCentidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLentidade)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLrement, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLaddent, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLentresp2)
                                    .addComponent(jLentcpf2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLentcpf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLentresp, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE))))
                        .addGap(25, 25, 25)))
                .addContainerGap())
            .addComponent(jSeparator2)
            .addComponent(jSeparator1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 505, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLcod4)
                    .addComponent(jLcod)
                    .addComponent(jLcod15)
                    .addComponent(jLconjunto)
                    .addComponent(jL5)
                    .addComponent(jLdtini)
                    .addComponent(jLcod2)
                    .addComponent(jLdtfin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLremproc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLaddproc, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(12, 12, 12)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLcod6)
                                        .addComponent(jCproc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLproc))))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLprocnome)
                                .addComponent(jLprocnome2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLproccpf)
                                .addComponent(jLproccpf2))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(JLprocpod2)
                                .addComponent(jLprocpod)))
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLrement, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLaddent, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLcod11)
                                    .addComponent(jCentidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLentidade))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLentnome2)
                            .addComponent(jLentnome))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JLentcnpj2)
                            .addComponent(jLentcnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLentresp2)
                            .addComponent(jLentresp))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLentcpf2)
                            .addComponent(jLentcpf))))
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(1121, 743));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    private void jCprocItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCprocItemStateChanged
        this.jLprocnome.setText(procuradores.get(jCproc.getSelectedIndex()).getNome());
        this.jLproccpf.setText(procuradores.get(jCproc.getSelectedIndex()).getCpf());
        this.jLprocpod.setText(procuradores.get(jCproc.getSelectedIndex()).getPoderes());
    }//GEN-LAST:event_jCprocItemStateChanged

    
    private void jCentidadeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCentidadeItemStateChanged
        this.jLentnome.setText(entidades.get(jCentidade.getSelectedIndex()).getNome());
        this.jLentcnpj.setText(entidades.get(jCentidade.getSelectedIndex()).getCnpj());
        this.jLentresp.setText(entidades.get(jCentidade.getSelectedIndex()).getResponsavel());
        this.jLentcpf.setText(entidades.get(jCentidade.getSelectedIndex()).getCpf());
    }//GEN-LAST:event_jCentidadeItemStateChanged

    
    private void jLprocnomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLprocnomeMouseClicked
        String aux = null;
        String nomeaux = jLprocnome.getText();
        int resp;
        int nproc =  dp.getIdExatoProc(Integer.valueOf(jLcod.getText()), nomeaux);
        int naux = nproc;
        int naux2 = 0;
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar o NOME do PROCURADOR?");
            if (resp == 0){
                    aux = JOptionPane.showInputDialog(null, "Digite o NOME do PROCURADOR:", "Editar Procurador", JOptionPane.QUESTION_MESSAGE, null, null, this.jLprocnome.getText()).toString();
            }
            if (aux != null){
                SisLog S = new SisLog("Visualiza", this.user, "Editar Nome do Procurador - " + aux);
                jLprocnome.setText(aux);
                procuradores.get(jCproc.getSelectedIndex()).setNome(aux);
                while((nproc != 0)&&(nproc!=naux2)){
                    dp.setNomeProcurador(aux, nproc);
                    nproc = dp.getIdExatoProc(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2=naux;
                }                
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar Nome do Procurador, Cancelado");
            }
        }
    }//GEN-LAST:event_jLprocnomeMouseClicked

    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        SisLog S = new SisLog("Visualiza", this.user, "Cancelado");
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    
    private void jLproccpfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLproccpfMouseClicked
        String aux = null;
        String nomeaux = jLprocnome.getText();
        int resp;
        int nproc =  dp.getIdExatoProc(Integer.valueOf(jLcod.getText()), nomeaux);
        int naux = nproc;
        int naux2 = 0;
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar o CPF do PROCURADOR?");
            if (resp == 0){
                aux = JOptionPane.showInputDialog(null, "Digite o CPF do PROCURADOR:", "Editar Procurador", JOptionPane.QUESTION_MESSAGE, null, null, this.jLproccpf.getText()).toString();
            }
            if (aux != null){
                jLproccpf.setText(aux);
                procuradores.get(jCproc.getSelectedIndex()).setNome(aux);
                while((nproc != 0)&&(nproc!=naux2)){
                    dp.setCpfProcurador(aux, nproc);
                    nproc = dp.getIdExatoProc(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2=naux;
                }
                SisLog S = new SisLog("Visualiza", this.user, "Editar Cpf do Procurador - " + aux);
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar Cpf do Procurador, Cancelado");
            }
        }
    }//GEN-LAST:event_jLproccpfMouseClicked

    
    private void jLprocpodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLprocpodMouseClicked
        String aux = null;
        String nomeaux = jLprocnome.getText();
        int resp;
        int nproc = dp.getIdExatoProc(Integer.valueOf(jLcod.getText()), nomeaux);
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar os PODERES do PROCURADOR?");
            if (resp == 0){
                aux = JOptionPane.showInputDialog(null, "Digite os PODERES do PROCURADOR:", "Editar Procurador", JOptionPane.QUESTION_MESSAGE, null, null, this.jLprocpod.getText()).toString();
            }
            if (aux != null){
                jLprocpod.setText(aux);
                procuradores.get(jCproc.getSelectedIndex()).setPoderes(aux);
                dp.setPoderesProcurador(aux, nproc);
                SisLog S = new SisLog("Visualiza", this.user, "Editar Poderes do Procurador - " + aux);
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar Poderes do Procurador, Cancelado");
            }
        }
    }//GEN-LAST:event_jLprocpodMouseClicked

    
    private void jLentnomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLentnomeMouseClicked
        String aux = null;
        String nomeaux = jLentnome.getText();
        int resp;
        int nproc =  de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
        int naux = nproc;
        int naux2 = 0;
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar o NOME da ENTIDADE?");
            if (resp == 0){
                aux = JOptionPane.showInputDialog(null, "Digite o NOME da ENTIDADE:", "Editar Entidade", JOptionPane.QUESTION_MESSAGE, null, null, this.jLentnome.getText()).toString();
            }
            if (aux != null){
                jLentnome.setText(aux);
                entidades.get(jCentidade.getSelectedIndex()).setNome(aux);
                while((nproc != 0)&&(nproc != naux2)){
                    de.setNomeEntidade(aux, nproc);
                    nproc = de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2 = naux;
                }                
                SisLog S = new SisLog("Visualiza", this.user, "Editar Nome da Entidade - " + aux);
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar Nome da Entidade, Cancelado");
            }
        }
    }//GEN-LAST:event_jLentnomeMouseClicked

    
    private void jLentcnpjMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLentcnpjMouseClicked
        String aux = null;
        String nomeaux = jLentnome.getText();
        int resp;
        int nproc = de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
        int naux = nproc;
        int naux2 = 0;
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar o CNPJ da ENTIDADE?");
            if (resp == 0){
                aux = JOptionPane.showInputDialog(null, "Digite o CNPJ da ENTIDADE:", "Editar Entidade", JOptionPane.QUESTION_MESSAGE, null, null, this.jLentcnpj.getText()).toString();
            }
            if (aux != null){
                jLentcnpj.setText(aux);
                entidades.get(jCentidade.getSelectedIndex()).setCnpj(aux);
                while((nproc != 0)&&(nproc!=naux2)){
                    de.setCnpjEntidade(aux, nproc);
                    nproc = de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2=naux;
                }
                SisLog S = new SisLog("Visualiza", this.user, "Editar Cnpj da Entidade - " + aux);
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar Cnpj da Entidade, Cancelado");
            }
        }
    }//GEN-LAST:event_jLentcnpjMouseClicked

    
    private void jLentrespMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLentrespMouseClicked
        String aux = null;
        String nomeaux = jLentnome.getText();
        int resp;
        int nproc = de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
        int naux = nproc;
        int naux2 = 0;
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar NOME do RESPONSAVEL?");
            if (resp == 0){
                aux = JOptionPane.showInputDialog(null, "Digite o NOME do RESPONSAVEL:", "Editar Entidade", JOptionPane.QUESTION_MESSAGE, null, null, this.jLentresp.getText()).toString();
            }
            if (aux != null){
                jLentresp.setText(aux);
                entidades.get(jCentidade.getSelectedIndex()).setResponsavel(aux);
                while((nproc != 0)&&(nproc != naux2)){
                    de.setRespEntidade(aux, nproc);
                    nproc = de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2 = naux;
                }                
                SisLog S = new SisLog("Visualiza", this.user, "Editar Responsavel pela Entidade - " + aux);
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar Responsavel pela Entidade, Cancelado");
            }
        }
    }//GEN-LAST:event_jLentrespMouseClicked

    
    private void jLentcpfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLentcpfMouseClicked
        String aux = null;
        String nomeaux = jLentnome.getText();
        int resp;
        int nproc = de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
        int naux = nproc;
        int naux2 = 0;
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar CPF do RESPONSAVEL?");
            if (resp == 0){
                aux = JOptionPane.showInputDialog(null, "Digite o CPF do RESPONSAVEL:", "Editar Entidade", JOptionPane.QUESTION_MESSAGE, null, null, this.jLentcpf.getText()).toString();
            }
            if (aux != null){
                jLentcpf.setText(aux);
                entidades.get(jCentidade.getSelectedIndex()).setCpf(aux);
                while((nproc != 0)&&(nproc != naux2)){
                    de.setCpfEntidade(aux, nproc);
                    nproc = de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2 = naux;
                }                
                SisLog S = new SisLog("Visualiza", this.user, "Editar Cpf do Responsavel pela Entidade - " + aux);
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar Cpf do Responsavel pela Entidade, Cancelado");
            }
        }
    }//GEN-LAST:event_jLentcpfMouseClicked

    
    private void jLconjuntoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLconjuntoMouseClicked
        String aux = null;
        int resp;
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar o Tipo de CONJUNTO da PROCURACAO?");
            if (resp == 0){
                String options[] = {"Em conjunto", "Isoladamente", "Em conjunto ou Isoladamente", "Nao consta"};
                aux = JOptionPane.showInputDialog(null, "Escolha o Tipo de CONJUNTO:", "Editar Procuracao", JOptionPane.QUESTION_MESSAGE, null, options, options[3]).toString();
            }
            if(aux!=null){
                jLconjunto.setText(aux);
                db.setConjunto(aux, Integer.valueOf(jLcod.getText()));
                SisLog S = new SisLog("Visualiza", this.user, "Editar tipo de conjunto - " + aux);
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar tipo de conjunto, Cancelado");
            }            
        }
    }//GEN-LAST:event_jLconjuntoMouseClicked

    
    private void jLdtiniMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLdtiniMouseClicked
        
        int resp; //resposta de sim ou nao na pergunta: deseja realmente editar?
        ComboData cb = new ComboData(Integer.valueOf(jLdtini.getText().split("-")[0]), Integer.valueOf(jLdtini.getText().split("-")[1]), Integer.valueOf(jLdtini.getText().split("-")[2]));
        JButton botao =new JButton("Ok");
        botao.addActionListener((ActionEvent e) -> {
            if((cb.dia.getSelectedItem()!= null)&&(cb.mes.getSelectedItem()!= null)&&(cb.ano.getSelectedItem()!= null)){
                String aux = Integer.toString(cb.ano.getSelectedIndex()+1800);
                aux = aux.concat("-");
                aux = aux.concat(Integer.toString(cb.mes.getSelectedIndex()+1));
                aux = aux.concat("-");
                aux = aux.concat(Integer.toString(cb.dia.getSelectedIndex()+1));
                mensagem = aux;
                dialog.setVisible(false);
            }
        });
        JOptionPane option = new JOptionPane();
        Object data[] = {"Escolha a Data Inicial da Procuracao:", cb.dia, cb.mes, cb.ano, botao};
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar DATA INICIAL da PROCURACAO?");
            if (resp == 0){
                option.setMessage(data);
                option.setMessageType(JOptionPane.QUESTION_MESSAGE);
                option.remove(1);
                dialog = option.createDialog(null, "Editar data da Procuracao");
                dialog.setVisible(true);
            }
            if(mensagem!=null){
                jLdtini.setText(mensagem);
                if (verificaMaior(mensagem, jLdtfin.getText()) == 1){
                    int a;
                    a = db.setDtini(Integer.valueOf(jLcod.getText()), mensagem);
                    if (a==1){
                        SisLog S = new SisLog("Visualiza", this.user, "Editar Data Inicial - " + mensagem);
                        JOptionPane.showMessageDialog(null, "DATA INICIAL modificada com sucesso.");
                    }
                    mensagem=null;                    
                }else{
                    JOptionPane.showMessageDialog(null, "DATA INICIAL MAIOR DO QUE A DATA FINAL. Tente Novamente.");
                    SisLog S = new SisLog("Visualiza", this.user, "Editar Data Inicial, Cancelado");
                }
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar Data Inicial, Cancelado");
            }
        }
        
    }//GEN-LAST:event_jLdtiniMouseClicked

    private void jLdtfinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLdtfinMouseClicked
        int resp; //resposta de sim ou nao na pergunta: deseja realmente editar?
        ComboData cb = new ComboData(Integer.valueOf(jLdtfin.getText().split("-")[0]), Integer.valueOf(jLdtfin.getText().split("-")[1]), Integer.valueOf(jLdtfin.getText().split("-")[2]));
        JButton botao =new JButton("Ok");
        botao.addActionListener((ActionEvent e) -> {
            if((cb.dia.getSelectedItem()!= null)&&(cb.mes.getSelectedItem()!= null)&&(cb.ano.getSelectedItem()!= null)){
                String aux = Integer.toString(cb.ano.getSelectedIndex()+1800);
                aux = aux.concat("-");
                aux = aux.concat(Integer.toString(cb.mes.getSelectedIndex()+1));
                aux = aux.concat("-");
                aux = aux.concat(Integer.toString(cb.dia.getSelectedIndex()+1));
                mensagem = aux;
                dialog.setVisible(false);
            }
        });
        JOptionPane option = new JOptionPane();
        Object data[] = {"Escolha a Data Final da Procuracao:", cb.dia, cb.mes, cb.ano, botao};
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar DATA FINAL da PROCURACAO?");
            if (resp == 0){
                option.setMessage(data);
                option.setMessageType(JOptionPane.QUESTION_MESSAGE);
                option.remove(1);
                dialog = option.createDialog(null, "Editar data da Procuracao");
                dialog.setVisible(true);
            }
            if(mensagem!=null){
                jLdtfin.setText(mensagem);
                if (verificaMaior(jLdtini.getText(), mensagem) == 1){
                    int a = db.setDtfin(Integer.valueOf(jLcod.getText()), mensagem);
                    if (a==1){
                        SisLog S = new SisLog("Visualiza", this.user, "Editar Data Final - " + mensagem);
                        JOptionPane.showMessageDialog(null, "DATA FINAL modificada com sucesso.");
                    }
                    mensagem=null;
                }else{
                    JOptionPane.showMessageDialog(null, "DATA INICIAL MAIOR DO QUE A DATA FINAL. Tente Novamente.");
                    SisLog S = new SisLog("Visualiza", this.user, "Editar Data Final, Cancelado");
                }
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar Data Final, Cancelado");
            }
        }
    }//GEN-LAST:event_jLdtfinMouseClicked

    private void jLentcpf2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLentcpf2MouseClicked
        String aux = null;
        String nomeaux = jLentnome.getText();
        int resp;
        int nproc = de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
        int naux = nproc;
        int naux2 = 0;
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar CPF do RESPONSAVEL?");
            if (resp == 0){
                aux = JOptionPane.showInputDialog(null, "Digite o CPF do RESPONSAVEL:", "Editar Entidade", JOptionPane.QUESTION_MESSAGE, null, null, this.jLentcpf.getText()).toString();
            }
            if (aux != null){
                jLentcpf.setText(aux);
                entidades.get(jCentidade.getSelectedIndex()).setCpf(aux);
                while((nproc != 0)&&(nproc != naux2)){
                    de.setCpfEntidade(aux, nproc);
                    nproc = de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2 = naux;
                }                
                SisLog S = new SisLog("Visualiza", this.user, "Editar Cpf do Responsavel pela Entidade - " + aux);
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar Cpf do Responsavel pela Entidade, Cancelado");
            }
        }
    }//GEN-LAST:event_jLentcpf2MouseClicked

    private void jLentresp2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLentresp2MouseClicked
        String aux = null;
        String nomeaux = jLentnome.getText();
        int resp;
        int nproc = de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
        int naux = nproc;
        int naux2 = 0;
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar NOME do RESPONSAVEL?");
            if (resp == 0){
                aux = JOptionPane.showInputDialog(null, "Digite o NOME do RESPONSAVEL:", "Editar Entidade", JOptionPane.QUESTION_MESSAGE, null, null, this.jLentresp.getText()).toString();
            }
            if (aux != null){
                jLentresp.setText(aux);
                entidades.get(jCentidade.getSelectedIndex()).setResponsavel(aux);
                while((nproc != 0)&&(nproc != naux2)){
                    de.setRespEntidade(aux, nproc);
                    nproc = de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2 = naux;
                }             
                SisLog S = new SisLog("Visualiza", this.user, "Editar Responsavel pela Entidade - " + aux);
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar Responsavel pela Entidade, Cancelado");
            }
        }
    }//GEN-LAST:event_jLentresp2MouseClicked

    private void JLentcnpj2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLentcnpj2MouseClicked
        String aux = null;
        String nomeaux = jLentnome.getText();
        int resp;
        int nproc = de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
        int naux = nproc;
        int naux2 = 0;
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar o CNPJ da ENTIDADE?");
            if (resp == 0){
                aux = JOptionPane.showInputDialog(null, "Digite o CNPJ da ENTIDADE:", "Editar Entidade", JOptionPane.QUESTION_MESSAGE, null, null, this.jLentcnpj.getText()).toString();
            }
            if (aux != null){
                jLentcnpj.setText(aux);
                entidades.get(jCentidade.getSelectedIndex()).setCnpj(aux);
                while((nproc != 0)&&(nproc!=naux2)){
                    de.setCnpjEntidade(aux, nproc);
                    nproc = de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2=naux;
                }                
                SisLog S = new SisLog("Visualiza", this.user, "Editar Cnpj da Entidade - " + aux);
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar Cnpj da Entidade, Cancelado");
            }
        }
    }//GEN-LAST:event_JLentcnpj2MouseClicked

    private void jLentnome2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLentnome2MouseClicked
        String aux = null;
        String nomeaux = jLentnome.getText();
        int resp;
        int nproc =  de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
        int naux = nproc;
        int naux2 = 0;
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar o NOME da ENTIDADE?");
            if (resp == 0){
                aux = JOptionPane.showInputDialog(null, "Digite o NOME da ENTIDADE:", "Editar Entidade", JOptionPane.QUESTION_MESSAGE, null, null, this.jLentnome.getText()).toString();
            }
            if (aux != null){
                jLentnome.setText(aux);
                entidades.get(jCentidade.getSelectedIndex()).setNome(aux);
                while((nproc != 0)&&(nproc != naux2)){
                    de.setNomeEntidade(aux, nproc);
                    nproc = de.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2 = naux;
                }
                SisLog S = new SisLog("Visualiza", this.user, "Editar Nome da Entidade - " + aux);
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar Nome da Entidade, Cancelado");
            }
        }
    }//GEN-LAST:event_jLentnome2MouseClicked

    private void JLprocpod2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLprocpod2MouseClicked
        String aux = null;
        String nomeaux = jLprocnome.getText();
        int resp;
        int nproc = dp.getIdExatoProc(Integer.valueOf(jLcod.getText()), nomeaux);
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar os PODERES do PROCURADOR?");
            if (resp == 0){
                aux = JOptionPane.showInputDialog(null, "Digite os PODERES do PROCURADOR:", "Editar Procurador", JOptionPane.QUESTION_MESSAGE, null, null, this.jLprocpod.getText()).toString();
            }
            if (aux != null){
                jLprocpod.setText(aux);
                procuradores.get(jCproc.getSelectedIndex()).setPoderes(aux);
                dp.setPoderesProcurador(aux, nproc);
                SisLog S = new SisLog("Visualiza", this.user, "Editar Poderes do Procurador - " + aux);
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar Poderes do Procurador, Cancelado");
            }
        }
    }//GEN-LAST:event_JLprocpod2MouseClicked

    private void jLproccpf2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLproccpf2MouseClicked
        String aux = null;
        String nomeaux = jLprocnome.getText();
        int resp;
        int nproc =  dp.getIdExatoProc(Integer.valueOf(jLcod.getText()), nomeaux);
        int naux = nproc;
        int naux2 = 0;
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar o CPF do PROCURADOR?");
            if (resp == 0){
                aux = JOptionPane.showInputDialog(null, "Digite o CPF do PROCURADOR:", "Editar Procurador", JOptionPane.QUESTION_MESSAGE, null, null, this.jLproccpf.getText()).toString();
            }
            if (aux != null){
                jLproccpf.setText(aux);
                procuradores.get(jCproc.getSelectedIndex()).setNome(aux);
                while((nproc != 0)&&(nproc!=naux2)){
                    dp.setCpfProcurador(aux, nproc);
                    nproc = dp.getIdExatoProc(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2=naux;
                }
                SisLog S = new SisLog("Visualiza", this.user, "Editar Cpf do Procurador - " + aux);
            }else{
                SisLog S = new SisLog("Visualiza", this.user, "Editar Cpf do Procurador, Cancelado");
            }
        }
    }//GEN-LAST:event_jLproccpf2MouseClicked

    private void jLprocnome2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLprocnome2MouseClicked
        if (tipo ==1){    
            String aux = null;
            String nomeaux = jLprocnome.getText();
            int resp;
            int nproc =  dp.getIdExatoProc(Integer.valueOf(jLcod.getText()), nomeaux);
            int naux = nproc;
            int naux2 = 0;
            if (evt.getClickCount() > 1){
                resp = JOptionPane.showConfirmDialog(null, "Deseja alterar o NOME do PROCURADOR?");
                if (resp == 0){
                        aux = JOptionPane.showInputDialog(null, "Digite o NOME do PROCURADOR:", "Editar Procurador", JOptionPane.QUESTION_MESSAGE, null, null, this.jLprocnome.getText()).toString();
                }
                if (aux != null){
                    SisLog S = new SisLog("Visualiza", this.user, "Editar Nome do Procurador - " + aux);
                    jLprocnome.setText(aux);
                    procuradores.get(jCproc.getSelectedIndex()).setNome(aux);
                    while((nproc != 0)&&(nproc!=naux2)){
                        dp.setNomeProcurador(aux, nproc);
                        nproc = dp.getIdExatoProc(Integer.valueOf(jLcod.getText()), nomeaux);
                        naux2=naux;
                    }                
                }else{
                    SisLog S = new SisLog("Visualiza", this.user, "Editar Nome do Procurador, Cancelado");
                }
            }
        }
    }//GEN-LAST:event_jLprocnome2MouseClicked

    private void jLremprocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLremprocMouseClicked
        if (tipo ==1){
            String aux = null;
            String nomeaux = jLprocnome.getText();
            int resp;
            int nproc =  Integer.valueOf(jLcod.getText());
            if (evt.getClickCount() > 1){
               if(jCproc.getItemCount()>1){
                   resp = JOptionPane.showConfirmDialog(null, "Tem Certeza que deseja ECLUIR o PROCURADOR?"); 
                   if (resp == 0){
                        SisLog S = new SisLog("Visualiza", this.user, "Remover Procurador - " + nomeaux);
                        dp.apaga(nomeaux, nproc);
                        this.dispose();
                        VisualizaProc v = new VisualizaProc(db.getCaminho(nproc), nproc, this.user);
                        JOptionPane.showMessageDialog(null, "Procurador removido com Sucesso!");
                    }else{
                        SisLog S = new SisLog("Visualiza", this.user, "Remover Procurador - Cancelado.");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Impossivel remover UNICO Procurador da Procuração.");
                    SisLog S = new SisLog("Visualiza", this.user, "Remover Procurador - Erro, UNICO PROCURADOR.");
                }
            }
        }
    }//GEN-LAST:event_jLremprocMouseClicked

    private void jLrementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLrementMouseClicked
        String nomeaux = jLentnome.getText();
        int resp;
        int nproc =  Integer.valueOf(jLcod.getText());
        if (evt.getClickCount() > 1){
            if(jCentidade.getItemCount()>1){
                resp = JOptionPane.showConfirmDialog(null, "Tem Certeza que deseja ECLUIR a ENTIDADE?");
                if (resp == 0){
                    SisLog S = new SisLog("Visualiza", this.user, "Remover Entidade - " + nomeaux);
                    de.apaga(nomeaux, nproc);
                    this.dispose();
                    VisualizaProc v = new VisualizaProc(db.getCaminho(nproc), nproc, this.user);
                    v.toFront();
                    JOptionPane.showMessageDialog(null, "Entidade removida com Sucesso!");
                }else{
                    SisLog S = new SisLog("Visualiza", this.user, "Remover Entidade - Cancelado.");
                }
            }else{
                JOptionPane.showMessageDialog(null, "Impossivel remover UNICA Entidade da Procuração.");
                SisLog S = new SisLog("Visualiza", this.user, "Remover Entidade - Erro, UNICA ENTIDADE.");
            }            
        }
    }//GEN-LAST:event_jLrementMouseClicked

    private void jLaddprocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLaddprocMouseClicked
        int resp; //resposta de sim ou nao na pergunta: deseja realmente editar?
        JOptionPane option = new JOptionPane();
        JTextField nome, cpf, poderes;
        nome = new JTextField();
        cpf = new JTextField(11);
        poderes = new JTextField();
        JButton botao =new JButton("Ok");
        botao.addActionListener((ActionEvent e) -> {
            if((!nome.getText().equals(""))&&(!poderes.getText().equals(""))){
                dp.connect();
                try {
                    if(!cpf.getText().equals("")){
                            if(new InsereProc(this.user).validaCPF(cpf.getText())){
                                dp.add(nome.getText(), cpf.getText(), poderes.getText(), Integer.valueOf(this.jLcod.getText()));
                                this.dispose();
                                VisualizaProc v = new VisualizaProc(db.getCaminho(Integer.valueOf(jLcod.getText())), Integer.valueOf(jLcod.getText()), this.user);
                                dialog.dispose();
                                v.toFront();
                            }else{
                                JOptionPane.showMessageDialog(null, "CPF invalido.");
                            }
                    }else{
                        dp.add(nome.getText(), poderes.getText(), Integer.valueOf(this.jLcod.getText()));
                        this.dispose();
                        VisualizaProc v = new VisualizaProc(db.getCaminho(Integer.valueOf(jLcod.getText())), Integer.valueOf(jLcod.getText()), this.user);
                        dialog.dispose();
                        v.toFront();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ADDPROC - Erro - " + ex);
                }
            }else{
                JOptionPane.showMessageDialog(null, "Insira pelo menos o NOME e os PODERES do PROCURADOR.");
            }
        });
        Object data[] = {"Insira os dados do Procurador:", new JLabel("Nome:"), nome, new JLabel("Cpf:"), cpf,new JLabel("Poderes"), poderes, botao};
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Adicionar um PROCURADOR?");
            if (resp == 0){
                option.setMessage(data);
                option.setMessageType(JOptionPane.QUESTION_MESSAGE);
                option.remove(1);
                dialog = option.createDialog(null, "Adicionar Procuracao");
                dialog.setVisible(true);
            }
        }
    }//GEN-LAST:event_jLaddprocMouseClicked

    private void jLaddentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLaddentMouseClicked
        int resp; //resposta de sim ou nao na pergunta: deseja realmente editar?
        JOptionPane option = new JOptionPane();
        JTextField nome, cnpj, responsavel, cpf;
        nome = new JTextField();
        cpf = new JTextField(11);
        cnpj = new JTextField(14);
        responsavel = new JTextField();
        JButton botao =new JButton("Ok");
        botao.addActionListener((ActionEvent e) -> {
            if((!nome.getText().equals(""))){
                de.connect();
                try {
                    if((!cpf.getText().equals("")) && (!cnpj.getText().equals(""))){
                            if(new InsereProc(this.user).validaCPF(cpf.getText())){
                                if(new InsereProc(this.user).validaCNPJ(cnpj.getText())){
                                    de.add(nome.getText(), cnpj.getText(), responsavel.getText(), cpf.getText(), Integer.valueOf(this.jLcod.getText()));
                                    this.dispose();
                                    VisualizaProc v = new VisualizaProc(db.getCaminho(Integer.valueOf(jLcod.getText())), Integer.valueOf(jLcod.getText()), this.user);
                                    dialog.dispose();
                                    v.toFront();
                                }else{
                                    JOptionPane.showMessageDialog(null, "CNPJ invalido.");
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, "CPF invalido.");
                            }
                    }else if(!cpf.getText().equals("")){
                        if(new InsereProc(this.user).validaCPF(cpf.getText())){
                            de.add(nome.getText(), null, responsavel.getText(), cpf.getText(), Integer.valueOf(this.jLcod.getText()));
                            this.dispose();
                            VisualizaProc v = new VisualizaProc(db.getCaminho(Integer.valueOf(jLcod.getText())), Integer.valueOf(jLcod.getText()), this.user);
                            dialog.dispose();
                            v.toFront();
                        }else{
                            JOptionPane.showMessageDialog(null, "CPF invalido.");
                        }
                    }else if(!cnpj.getText().equals("")){
                        if(new InsereProc(this.user).validaCNPJ(cnpj.getText())){
                            de.add(nome.getText(), cnpj.getText(), responsavel.getText(), null, Integer.valueOf(this.jLcod.getText()));
                            this.dispose();
                            VisualizaProc v = new VisualizaProc(db.getCaminho(Integer.valueOf(jLcod.getText())), Integer.valueOf(jLcod.getText()), this.user);
                            dialog.dispose();
                            v.toFront();
                        }else{
                            JOptionPane.showMessageDialog(null, "CPF invalido.");
                        }
                    }else{
                        de.add(nome.getText(), null, responsavel.getText(), null, Integer.valueOf(this.jLcod.getText()));
                        this.dispose();
                        VisualizaProc v = new VisualizaProc(db.getCaminho(Integer.valueOf(jLcod.getText())), Integer.valueOf(jLcod.getText()), this.user);
                        dialog.dispose();
                        v.toFront();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ADDENT - Erro - " + ex);
                }
            }else{
                JOptionPane.showMessageDialog(null, "Insira pelo menos o NOME e da ENTIDADE.");
            }
        });
        Object data[] = {"Insira os dados da Entidade:", new JLabel("Nome:"), nome, new JLabel("Cnpj:"), cnpj, new JLabel("Responsavel:"), responsavel, new JLabel("Cpf:"), cpf, botao};
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Adicionar uma ENTIDADE?");
            if (resp == 0){
                option.setMessage(data);
                option.setMessageType(JOptionPane.QUESTION_MESSAGE);
                option.remove(1);
                dialog = option.createDialog(null, "Adicionar Entidade");
                dialog.setVisible(true);
            }
        }
    }//GEN-LAST:event_jLaddentMouseClicked

    
    public int verificaMaior(String dtini, String dtfin){ //retorna 0 se o dia inicial for o maior ou 1 se o final for maior ou igual
        int di, df;
        int mi, mf;
        int ai, af;
        
        di = Integer.valueOf(jLdtini.getText().split("-")[2]);
        df = Integer.valueOf(jLdtfin.getText().split("-")[2]);
        
        mi = Integer.valueOf(jLdtini.getText().split("-")[1]);
        mf = Integer.valueOf(jLdtfin.getText().split("-")[1]);
        
        ai = Integer.valueOf(jLdtini.getText().split("-")[0]);
        af = Integer.valueOf(jLdtfin.getText().split("-")[0]);
        
        if(ai > af){
            return 0;
        }else if(ai == af){
            if (mi > mf){
                return 0;
            }else if(mi == mf){
                if (di > df){
                    return 0;
                }else{
                    return 1;
                }
            }else{
                return 1;
            }
        }else{
            return 1;
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLentcnpj2;
    private javax.swing.JLabel JLprocpod2;
    private javax.swing.JComboBox jCentidade;
    private javax.swing.JComboBox jCproc;
    private javax.swing.JLabel jL5;
    private javax.swing.JLabel jLaddent;
    private javax.swing.JLabel jLaddproc;
    private javax.swing.JLabel jLcod;
    private javax.swing.JLabel jLcod11;
    private javax.swing.JLabel jLcod15;
    private javax.swing.JLabel jLcod2;
    private javax.swing.JLabel jLcod4;
    private javax.swing.JLabel jLcod6;
    private javax.swing.JLabel jLconjunto;
    private javax.swing.JLabel jLdtfin;
    private javax.swing.JLabel jLdtini;
    private javax.swing.JLabel jLentcnpj;
    private javax.swing.JLabel jLentcpf;
    private javax.swing.JLabel jLentcpf2;
    private javax.swing.JLabel jLentidade;
    private javax.swing.JLabel jLentnome;
    private javax.swing.JLabel jLentnome2;
    private javax.swing.JLabel jLentresp;
    private javax.swing.JLabel jLentresp2;
    private javax.swing.JLabel jLproc;
    private javax.swing.JLabel jLproccpf;
    private javax.swing.JLabel jLproccpf2;
    private javax.swing.JLabel jLprocnome;
    private javax.swing.JLabel jLprocnome2;
    private javax.swing.JLabel jLprocpod;
    private javax.swing.JLabel jLrement;
    private javax.swing.JLabel jLremproc;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    // End of variables declaration//GEN-END:variables
}
