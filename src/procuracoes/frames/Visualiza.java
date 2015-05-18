package procuracoes.frames;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
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
    
    public Visualiza(String caminho, int cod) {
        
        this.cod = cod;
        db = new Database();
        db.connect();
        JPanel jPanel1; //painel de visualizacao
        JInternalFrame PDFrame = new JInternalFrame("Visualizacao"); //Frame Interno responsável pela exibição do painel de visualização
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
        
        ComponentKeyBinding.install(controller, jPanel1); // adiciona o o controlador no painel
        PDFrame.setBounds(0 , 0, 100, 100); //define a localização e tamanho do frame
        PDFrame.add(jPanel1); //adiciona o painel de visualização ao frame interno
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------//
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
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jSeparator1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jL5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jL5.setText("Data Inicial:");

        jLdtini.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLdtini.setText("<00/00/0000>");

        jLcod2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod2.setText("Data Final:");

        jLdtfin.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLdtfin.setText("<00/00/0000>");

        jSeparator2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLcod4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod4.setText("Cod:");

        jLcod.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod.setText("<00>");

        jLcod6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod6.setText("Procurador:");

        jCproc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Procurador 1" }));
        jCproc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCprocItemStateChanged(evt);
            }
        });

        jLproc.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLproc.setText("</1>");

        jLcod8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod8.setText("Nome:");

        jLcod9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod9.setText("Cpf:");

        jLcod10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod10.setText("Poderes:");

        jLcod11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod11.setText("Entidade:");

        jCentidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Entidade 1" }));
        jCentidade.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCentidadeItemStateChanged(evt);
            }
        });

        jLentidade.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLentidade.setText("</1>");

        jLcod13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod13.setText("Nome:");

        jLcod14.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod14.setText("Cnpj:");

        jLcod15.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod15.setText("Conjunto:");

        jLcod16.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod16.setText("Responsável:");

        jLcod17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLcod17.setText("Cpf:");

        jLconjunto.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLconjunto.setText("<Em conjunto>");

        jLprocpod.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLprocpod.setText("<Poderes>");
        jLprocpod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLprocpodMouseClicked(evt);
            }
        });

        jLprocnome.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLprocnome.setText("<Nome>");
        jLprocnome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLprocnomeMouseClicked(evt);
            }
        });

        jLproccpf.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLproccpf.setText("<Cpf>");
        jLproccpf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLproccpfMouseClicked(evt);
            }
        });

        jLentnome.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLentnome.setText("<Nome>");

        jLentcnpj.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLentcnpj.setText("<Cnpj>");

        jLentresp.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLentresp.setText("<Responsavel>");

        jLentcpf.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLentcpf.setText("<Cpf>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1059, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 1059, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLcod4)
                        .addGap(6, 6, 6)
                        .addComponent(jLcod)
                        .addGap(217, 217, 217)
                        .addComponent(jLcod15)
                        .addGap(6, 6, 6)
                        .addComponent(jLconjunto)
                        .addGap(29, 29, 29)
                        .addComponent(jL5)
                        .addGap(6, 6, 6)
                        .addComponent(jLdtini)
                        .addGap(176, 176, 176)
                        .addComponent(jLcod2)
                        .addGap(6, 6, 6)
                        .addComponent(jLdtfin))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLcod6)
                                .addGap(4, 4, 4)
                                .addComponent(jCproc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLproc))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLcod10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLprocpod, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLcod9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLproccpf, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLcod8)
                                    .addGap(10, 10, 10)
                                    .addComponent(jLprocnome, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLcod11)
                                .addGap(4, 4, 4)
                                .addComponent(jCentidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLcod13)
                            .addComponent(jLcod14)
                            .addComponent(jLcod16)
                            .addComponent(jLcod17))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLentnome, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLentcnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLentresp, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLentcpf, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLentidade))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(524, 524, 524)
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
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
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
                            .addComponent(jLprocpod)))
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLcod11))
                            .addComponent(jCentidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addComponent(jLcod13)
                        .addGap(6, 6, 6)
                        .addComponent(jLcod14)
                        .addGap(6, 6, 6)
                        .addComponent(jLcod16)
                        .addGap(6, 6, 6)
                        .addComponent(jLcod17))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLentidade)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLentnome)
                        .addGap(6, 6, 6)
                        .addComponent(jLentcnpj)
                        .addGap(6, 6, 6)
                        .addComponent(jLentresp)
                        .addGap(6, 6, 6)
                        .addComponent(jLentcpf)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(1006, 738));
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
        int nproc = db.getIdbyNomeProc(nomeaux);
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar o NOME do PROCURADOR?");
            if (resp == 0){
                aux = JOptionPane.showInputDialog(null, "Digite o NOME do PROCURADOR:", "Editar Procurador", JOptionPane.QUESTION_MESSAGE, null, null, this.jLprocnome.getText()).toString();
            }
            if (aux != null){
                jLprocnome.setText(aux);
                procuradores.get(jCproc.getSelectedIndex()).setNome(aux);
                while(nproc != 0){
                    db.setNomeProcurador(aux, nproc);
                    nproc = db.getIdbyNomeProc(nomeaux);
                }                
            }
        }
    }//GEN-LAST:event_jLprocnomeMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Inicial in;
        in = new Inicial();
        in.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void jLproccpfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLproccpfMouseClicked
        String aux = null;
        String cpfaux = jLproccpf.getText();
        int resp;
        int nproc = db.getIdbyCpfProc(cpfaux);
        if (evt.getClickCount() > 1){
            resp = JOptionPane.showConfirmDialog(null, "Deseja alterar o CPF do PROCURADOR?");
            if (resp == 0){
                aux = JOptionPane.showInputDialog(null, "Digite o CPF do PROCURADOR:", "Editar Procurador", JOptionPane.QUESTION_MESSAGE, null, null, this.jLproccpf.getText()).toString();
            }
            if (aux != null){
                jLproccpf.setText(aux);
                procuradores.get(jCproc.getSelectedIndex()).setCpf(aux);
                while(nproc != 0){
                    db.setCpfProcurador(aux, nproc);
                    nproc = db.getIdbyCpfProc(cpfaux);
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
