package procuracoes.frames;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import procuracoes.classes.ComboData;
import procuracoes.classes.Entidade;
import procuracoes.classes.Procurador;
import procuracoes.db.Database;

/**
 *
 * @author Thiago
 */

public class Visualiza extends javax.swing.JFrame {
    
    public Database db;
    public List<Procurador> procuradores;
    public List<Entidade> entidades;
    public int cod;
    static String mensagem = null;//usado nos Dialogos de edição de data
    static JDialog dialog = null;//usado nos Dialogos de edição de data
    
    public Visualiza(String caminho, int cod) {
        
        this.cod = cod;
        db = new Database();
        db.connect();
        JPanel jPanel1; //painel de visualizacao
        
        ImageIcon image = new ImageIcon(getClass().getResource("/procuracoes/recursos/icon.png"));
        this.setIconImage(image.getImage());
        
        JInternalFrame PDFrame = new JInternalFrame("Visualizacao"); //Frame Interno responsável pela exibição do painel de visualização
        PDFrame.getContentPane().setBackground(Color.white);
        Document document = new Document(); //cria um objeto Documento
        
        
        try {
            document.setFile(caminho); //envia o caminho para o documento
        } catch(PDFException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao analizar documento " +  ex.getMessage());
        } catch(PDFSecurityException ex) {
            JOptionPane.showMessageDialog(null,"Erro - encriptacao nao suportada " +  ex.getMessage());
        } catch(FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null,"Error - arquivo nao encontrado" +  ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"Error - IO -" +  ex.getMessage());
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(null,"Documento não encontrado " +  ex.getMessage());
        }       
        SwingController controller = new SwingController(); // inicia o controlador
        SwingViewBuilder factory = new SwingViewBuilder(controller); // Inicia um SwingViewFactory configurado com o controlador
        jPanel1 = factory.buildViewerPanel(); // Usa a fábrica para construir um jPanel pre-configurado com uma UI completa já pelo Viewer     
        jPanel1.setBackground(Color.white);
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
        carregaCampos();
        
    }
    
    public void carregaCampos(){
        procuradores = new ArrayList<>();                              //Lista auxiliar para carregar os procuradores
        procuradores.addAll(db.getProcuradores(this.cod));             //popula a lista de procuradores
        entidades = new ArrayList<>();                                 //Lista auxiliar para carregar as entidades
        entidades.addAll(db.getEntidades(this.cod));                   //popula a lista de entidades
        
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
        jLcod8 = new javax.swing.JLabel();
        jLcod9 = new javax.swing.JLabel();
        jLcod10 = new javax.swing.JLabel();
        jLcod11 = new javax.swing.JLabel();
        jCentidade = new javax.swing.JComboBox();
        jLentidade = new javax.swing.JLabel();
        jLcod13 = new javax.swing.JLabel();
        jLcod14 = new javax.swing.JLabel();
        jLcod15 = new javax.swing.JLabel();
        jLcod16 = new javax.swing.JLabel();
        jLcod17 = new javax.swing.JLabel();
        jLconjunto = new javax.swing.JLabel();
        jLprocpod = new javax.swing.JLabel();
        jLprocnome = new javax.swing.JLabel();
        jLproccpf = new javax.swing.JLabel();
        jLentnome = new javax.swing.JLabel();
        jLentcnpj = new javax.swing.JLabel();
        jLentresp = new javax.swing.JLabel();
        jLentcpf = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Visualizacao");
        setBackground(new java.awt.Color(255, 255, 255));
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
        jL5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jL5.setText("Data Inicial:");

        jLdtini.setBackground(new java.awt.Color(255, 255, 255));
        jLdtini.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLdtini.setText("<00/00/0000>");
        jLdtini.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLdtiniMouseClicked(evt);
            }
        });

        jLcod2.setBackground(new java.awt.Color(255, 255, 255));
        jLcod2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod2.setText("Data Final:");

        jLdtfin.setBackground(new java.awt.Color(255, 255, 255));
        jLdtfin.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLdtfin.setText("<00/00/0000>");
        jLdtfin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLdtfinMouseClicked(evt);
            }
        });

        jSeparator2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLcod4.setBackground(new java.awt.Color(255, 255, 255));
        jLcod4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod4.setText("Cod:");

        jLcod.setBackground(new java.awt.Color(255, 255, 255));
        jLcod.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod.setText("<00>");

        jLcod6.setBackground(new java.awt.Color(255, 255, 255));
        jLcod6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod6.setText("Procurador:");

        jCproc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Procurador 1" }));
        jCproc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCprocItemStateChanged(evt);
            }
        });

        jLproc.setBackground(new java.awt.Color(255, 255, 255));
        jLproc.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLproc.setText("</1>");

        jLcod8.setBackground(new java.awt.Color(255, 255, 255));
        jLcod8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod8.setText("Nome:");

        jLcod9.setBackground(new java.awt.Color(255, 255, 255));
        jLcod9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod9.setText("Cpf:");

        jLcod10.setBackground(new java.awt.Color(255, 255, 255));
        jLcod10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod10.setText("Poderes:");

        jLcod11.setBackground(new java.awt.Color(255, 255, 255));
        jLcod11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod11.setText("Entidade:");

        jCentidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Entidade 1" }));
        jCentidade.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCentidadeItemStateChanged(evt);
            }
        });

        jLentidade.setBackground(new java.awt.Color(255, 255, 255));
        jLentidade.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLentidade.setText("</1>");

        jLcod13.setBackground(new java.awt.Color(255, 255, 255));
        jLcod13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod13.setText("Nome:");

        jLcod14.setBackground(new java.awt.Color(255, 255, 255));
        jLcod14.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod14.setText("Cnpj:");

        jLcod15.setBackground(new java.awt.Color(255, 255, 255));
        jLcod15.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod15.setText("Conjunto:");

        jLcod16.setBackground(new java.awt.Color(255, 255, 255));
        jLcod16.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod16.setText("Responsável:");

        jLcod17.setBackground(new java.awt.Color(255, 255, 255));
        jLcod17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod17.setText("Cpf:");

        jLconjunto.setBackground(new java.awt.Color(255, 255, 255));
        jLconjunto.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLconjunto.setText("<Em conjunto>");
        jLconjunto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLconjuntoMouseClicked(evt);
            }
        });

        jLprocpod.setBackground(new java.awt.Color(255, 255, 255));
        jLprocpod.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLprocpod.setText("<Poderes>");
        jLprocpod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLprocpodMouseClicked(evt);
            }
        });

        jLprocnome.setBackground(new java.awt.Color(255, 255, 255));
        jLprocnome.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLprocnome.setText("<Nome>");
        jLprocnome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLprocnomeMouseClicked(evt);
            }
        });

        jLproccpf.setBackground(new java.awt.Color(255, 255, 255));
        jLproccpf.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLproccpf.setText("<Cpf>");
        jLproccpf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLproccpfMouseClicked(evt);
            }
        });

        jLentnome.setBackground(new java.awt.Color(255, 255, 255));
        jLentnome.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLentnome.setText("<Nome>");
        jLentnome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLentnomeMouseClicked(evt);
            }
        });

        jLentcnpj.setBackground(new java.awt.Color(255, 255, 255));
        jLentcnpj.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLentcnpj.setText("<Cnpj>");
        jLentcnpj.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLentcnpjMouseClicked(evt);
            }
        });

        jLentresp.setBackground(new java.awt.Color(255, 255, 255));
        jLentresp.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLentresp.setText("<Responsavel>");
        jLentresp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLentrespMouseClicked(evt);
            }
        });

        jLentcpf.setBackground(new java.awt.Color(255, 255, 255));
        jLentcpf.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLentcpf.setText("<Cpf>");
        jLentcpf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLentcpfMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLcod2)
                        .addGap(6, 6, 6)
                        .addComponent(jLdtfin)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLcod10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLprocpod, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLcod6)
                                .addGap(4, 4, 4)
                                .addComponent(jCproc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLproc))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLcod9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLproccpf, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLcod8)
                                    .addGap(10, 10, 10)
                                    .addComponent(jLprocnome, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLcod11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCentidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLentidade))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLcod13)
                                    .addComponent(jLcod14))
                                .addGap(59, 59, 59)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLentcnpj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLentnome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLcod17)
                                .addGap(72, 72, 72)
                                .addComponent(jLentcpf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLcod16)
                                .addGap(18, 18, 18)
                                .addComponent(jLentresp, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(54, 54, 54))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1059, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 1059, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(516, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLcod4)
                    .addComponent(jLcod)
                    .addComponent(jLcod15)
                    .addComponent(jLconjunto)
                    .addComponent(jL5)
                    .addComponent(jLdtini)
                    .addComponent(jLcod2)
                    .addComponent(jLdtfin))
                .addGap(6, 6, 6)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLcod11)
                                .addComponent(jCentidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLentidade))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLcod13)
                                .addComponent(jLentnome))
                            .addGap(6, 6, 6)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLcod14)
                                .addComponent(jLentcnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(6, 6, 6)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLcod16)
                                .addComponent(jLentresp))
                            .addGap(6, 6, 6)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLcod17)
                                .addComponent(jLentcpf))
                            .addGap(18, 18, 18))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(jLcod6))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jCproc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLproc)))
                            .addGap(13, 13, 13)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLprocnome)
                                .addComponent(jLcod8))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLproccpf)
                                .addComponent(jLcod9))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLcod10)
                                .addComponent(jLprocpod))
                            .addGap(24, 24, 24)))))
        );

        setSize(new java.awt.Dimension(1066, 743));
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
        int nproc =  db.getIdExatoProc(Integer.valueOf(jLcod.getText()), nomeaux);
        int naux = nproc;
        int naux2 = 0;
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar o NOME do PROCURADOR?");
            if (resp == 0){
                    aux = JOptionPane.showInputDialog(null, "Digite o NOME do PROCURADOR:", "Editar Procurador", JOptionPane.QUESTION_MESSAGE, null, null, this.jLprocnome.getText()).toString();
            }
            if (aux != null){
                jLprocnome.setText(aux);
                procuradores.get(jCproc.getSelectedIndex()).setNome(aux);
                while((nproc != 0)&&(nproc!=naux2)){
                    db.setNomeProcurador(aux, nproc);
                    nproc = db.getIdExatoProc(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2=naux;
                }                
            }
        }
    }//GEN-LAST:event_jLprocnomeMouseClicked

    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    
    private void jLproccpfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLproccpfMouseClicked
        String aux = null;
        String nomeaux = jLprocnome.getText();
        int resp;
        int nproc =  db.getIdExatoProc(Integer.valueOf(jLcod.getText()), nomeaux);
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
                    db.setCpfProcurador(aux, nproc);
                    nproc = db.getIdExatoProc(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2=naux;
                }                
            }
        }
    }//GEN-LAST:event_jLproccpfMouseClicked

    
    private void jLprocpodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLprocpodMouseClicked
        String aux = null;
        String nomeaux = jLprocnome.getText();
        int resp;
        int nproc = db.getIdExatoProc(Integer.valueOf(jLcod.getText()), nomeaux);
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar os PODERES do PROCURADOR?");
            if (resp == 0){
                aux = JOptionPane.showInputDialog(null, "Digite os PODERES do PROCURADOR:", "Editar Procurador", JOptionPane.QUESTION_MESSAGE, null, null, this.jLprocpod.getText()).toString();
            }
            if (aux != null){
                jLprocpod.setText(aux);
                procuradores.get(jCproc.getSelectedIndex()).setPoderes(aux);
                db.setPoderesProcurador(aux, nproc);                
            }
        }
    }//GEN-LAST:event_jLprocpodMouseClicked

    
    private void jLentnomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLentnomeMouseClicked
        String aux = null;
        String nomeaux = jLentnome.getText();
        int resp;
        int nproc =  db.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
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
                    db.setNomeEntidade(aux, nproc);
                    nproc = db.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2 = naux;
                }                
            }
        }
    }//GEN-LAST:event_jLentnomeMouseClicked

    
    private void jLentcnpjMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLentcnpjMouseClicked
        String aux = null;
        String nomeaux = jLentnome.getText();
        int resp;
        int nproc = db.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
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
                    db.setCnpjEntidade(aux, nproc);
                    nproc = db.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2=naux;
                }                
            }
        }
    }//GEN-LAST:event_jLentcnpjMouseClicked

    
    private void jLentrespMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLentrespMouseClicked
        String aux = null;
        String nomeaux = jLentnome.getText();
        int resp;
        int nproc = db.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
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
                    db.setRespEntidade(aux, nproc);
                    nproc = db.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2 = naux;
                }                
            }
        }
    }//GEN-LAST:event_jLentrespMouseClicked

    
    private void jLentcpfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLentcpfMouseClicked
        String aux = null;
        String nomeaux = jLentnome.getText();
        int resp;
        int nproc = db.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
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
                    db.setCpfEntidade(aux, nproc);
                    nproc = db.getIdExatoEnt(Integer.valueOf(jLcod.getText()), nomeaux);
                    naux2 = naux;
                }                
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
            }            
        }
    }//GEN-LAST:event_jLconjuntoMouseClicked

    
    private void jLdtiniMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLdtiniMouseClicked
        
        int resp; //resposta de sim ou nao na pergunta: deseja realmente editar?
        ComboData cb = new ComboData(Integer.valueOf(jLdtini.getText().split("-")[0]), Integer.valueOf(jLdtini.getText().split("-")[1]), Integer.valueOf(jLdtini.getText().split("-")[2]));
        JButton botao =new JButton("Ok");
        botao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if((cb.dia.getSelectedItem()!= null)&&(cb.mes.getSelectedItem()!= null)&&(cb.ano.getSelectedItem()!= null)){
                    String aux = Integer.toString(cb.ano.getSelectedIndex()+1800);
                    aux = aux.concat("-");
                    aux = aux.concat(Integer.toString(cb.mes.getSelectedIndex()+1));
                    aux = aux.concat("-");
                    aux = aux.concat(Integer.toString(cb.dia.getSelectedIndex()+1));
                    mensagem = aux;
                    dialog.setVisible(false);
                }
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
                int a = db.setDtini(Integer.valueOf(jLcod.getText()), mensagem);
                if (a==1){
                    JOptionPane.showMessageDialog(null, "DATA INICIAL modificada com sucesso.");
                }
                mensagem=null;
            }
        }
        
    }//GEN-LAST:event_jLdtiniMouseClicked

    private void jLdtfinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLdtfinMouseClicked
        int resp; //resposta de sim ou nao na pergunta: deseja realmente editar?
        ComboData cb = new ComboData(Integer.valueOf(jLdtfin.getText().split("-")[0]), Integer.valueOf(jLdtfin.getText().split("-")[1]), Integer.valueOf(jLdtfin.getText().split("-")[2]));
        JButton botao =new JButton("Ok");
        botao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if((cb.dia.getSelectedItem()!= null)&&(cb.mes.getSelectedItem()!= null)&&(cb.ano.getSelectedItem()!= null)){
                    String aux = Integer.toString(cb.ano.getSelectedIndex()+1800);
                    aux = aux.concat("-");
                    aux = aux.concat(Integer.toString(cb.mes.getSelectedIndex()+1));
                    aux = aux.concat("-");
                    aux = aux.concat(Integer.toString(cb.dia.getSelectedIndex()+1));
                    mensagem = aux;
                    dialog.setVisible(false);
                }
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
                int a = db.setDtfin(Integer.valueOf(jLcod.getText()), mensagem);
                if (a==1){
                    JOptionPane.showMessageDialog(null, "DATA FINAL modificada com sucesso.");
                }
                mensagem=null;
            }
        }
    }//GEN-LAST:event_jLdtfinMouseClicked

    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jCentidade;
    private javax.swing.JComboBox jCproc;
    private javax.swing.JLabel jL5;
    private javax.swing.JLabel jLcod;
    private javax.swing.JLabel jLcod10;
    private javax.swing.JLabel jLcod11;
    private javax.swing.JLabel jLcod13;
    private javax.swing.JLabel jLcod14;
    private javax.swing.JLabel jLcod15;
    private javax.swing.JLabel jLcod16;
    private javax.swing.JLabel jLcod17;
    private javax.swing.JLabel jLcod2;
    private javax.swing.JLabel jLcod4;
    private javax.swing.JLabel jLcod6;
    private javax.swing.JLabel jLcod8;
    private javax.swing.JLabel jLcod9;
    private javax.swing.JLabel jLconjunto;
    private javax.swing.JLabel jLdtfin;
    private javax.swing.JLabel jLdtini;
    private javax.swing.JLabel jLentcnpj;
    private javax.swing.JLabel jLentcpf;
    private javax.swing.JLabel jLentidade;
    private javax.swing.JLabel jLentnome;
    private javax.swing.JLabel jLentresp;
    private javax.swing.JLabel jLproc;
    private javax.swing.JLabel jLproccpf;
    private javax.swing.JLabel jLprocnome;
    private javax.swing.JLabel jLprocpod;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    // End of variables declaration//GEN-END:variables
}
