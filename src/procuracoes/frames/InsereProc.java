/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procuracoes.frames;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import procuracoes.classes.Entidade;
import procuracoes.classes.Procuracao;
import procuracoes.classes.Procurador;
import procuracoes.classes.SisLog;
import procuracoes.db.Database;
import procuracoes.db.Dataproc;

/**
 *
 * @author Thiago
 */
public class InsereProc extends javax.swing.JFrame {

    public Database db;
    public Procuracao proc;
    public List<Procurador> procuradores;
    public List<Entidade> entidades;
    public String user;
    
    public InsereProc(String u) throws SQLException {
        initComponents();
        procuradores = new ArrayList<>();
        entidades = new ArrayList<>();
        db = new Database();
        db.connect();
        jLcod.setText(String.valueOf(db.getProcod()));
        jTcaminho.setText("\\\\servidor\\Repositorio\\SistemaMezzari\\mezzariproc\\"+ Integer.toString(db.getProcod()) + ".pdf");
        this.getContentPane().setBackground(Color.white);
        ImageIcon image = new ImageIcon(getClass().getResource("/recursos/icon.png"));
        this.setIconImage(image.getImage());
        this.user=u;
        jTproccpf.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    for(ActionListener a: jBProcsalva.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(e, 1, null));
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    for(ActionListener a: jBProcsalva.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(e, 1, null));
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    for(ActionListener a: jBProcsalva.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(e, 1, null));
                    }
                }
            }
         });
    }
    
    public void setEntidades(){
        if(!jTnent.getText().isEmpty()){
            int i = Integer.valueOf(jTnent.getText());
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            String s;
            int n = 1;
            Entidade e = new Entidade(null, null, null, null);
            while(n <= i){
                entidades.add(n-1, e);
                s = "Entidade " + String.valueOf(n);
                model.addElement(s);
                n++;
            }
            this.jCEnt.setModel(model);
            this.jTnent.setEditable(false);
        }
    }
    
    public void setProcuradores(){
        if(!jTnproc.getText().isEmpty()){
            int i = Integer.valueOf(jTnproc.getText());
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            String s;
            int n = 1;
            Procurador p = new Procurador(null, null, null);
            while(n <= i){
                procuradores.add(n-1, p);
                s = "Procurador " + String.valueOf(n);
                model.addElement(s);
                n++;
            }
            this.jCProc.setModel(model);
            this.jTnproc.setEditable(false);
        }
    }
    
    public boolean revisaData(){
        try {
            Integer.parseInt(jTdi1.getText());
            Integer.parseInt(jTdi2.getText());
            Integer.parseInt(jTdi3.getText());
            Integer.parseInt(jTdf1.getText());
            Integer.parseInt(jTdf2.getText());
            Integer.parseInt(jTdf3.getText());
            return true;
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, revisar o campo de DATA\n" + e.getMessage());
        }
        return false;
    }
    
    public int revisaProc(){
        if (this.jTprocnome.getText().compareTo("") == 0) return 1;
        if (this.jTprocpod.getText().compareTo("") == 0) return 2;
        if (!validaCPF(this.jTproccpf.getText())) return 3;
        return 4;
    }
    
    public int revisaEnt(){
        if (this.jTentnome.getText().compareTo("") == 0) return 1;
        if (!validaCPF(this.jtentcpf.getText())) return 2;
        if (!validaCNPJ(this.jTentcnpj.getText())) return 3;
        return 4;
    }
    
    public boolean revisaCaminho(){
        return this.jTcaminho.getText().compareTo("") != 0;
    }
    
    public boolean validaCPF(String cpf){
        if ((cpf == null) || (cpf.compareTo("")==0)) return true;
        if(cpf.equals("00000000000") || cpf.equals("11111111111") ||
           cpf.equals("22222222222") || cpf.equals("33333333333") ||
           cpf.equals("44444444444") || cpf.equals("55555555555") ||
           cpf.equals("66666666666") || cpf.equals("77777777777") ||
           cpf.equals("88888888888") || cpf.equals("99999999999")
           || cpf.length() != 11){
                return false;
        }
        
        char dig10, dig11;
        int sm, i, r, num, peso;
        
        try{
            //Calculo do 1º digito verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++){
                // converte o i-esimo caractere do CPF em um numero: 
                // por exemplo, transforma o caractere '0' no inteiro 0 
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int) (cpf.charAt(i)-48);
                sm = sm + (num * peso);
                peso = peso-1;
            }
            r = 11 - (sm % 11);
            if((r==10)||(r==11)){
                dig10 = '0';
            }else{
                dig10 = (char)(r + 48); // converte no respectivo caractere numerico
            }
            //Calculo do 2º digito verificador.
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++){
                num = (int)(cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }
            r = 11 - (sm % 11);
            if((r == 10) || (r == 11)){
                dig11 = '0';
            } else{
                dig11 = (char)(r + 48);
            }
            
            // Verifica se os digitos calculados conferem com os digitos informados.
            if((dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10))){
                return(true);
            }else{
                return(false);
            }
        }catch (InputMismatchException erro){
            return(false);
        }
    }   
    
    public boolean validaCNPJ(String cnpj){
        if ((cnpj == null) || (cnpj.compareTo("")==0)) return true;
        if(cnpj.equals("00000000000000") || cnpj.equals("11111111111111") ||
           cnpj.equals("22222222222222") || cnpj.equals("33333333333333") ||
           cnpj.equals("44444444444444") || cnpj.equals("55555555555555") ||
           cnpj.equals("66666666666666") || cnpj.equals("77777777777777") ||
           cnpj.equals("88888888888888") || cnpj.equals("99999999999999")
           || cnpj.length() != 14){
                return false;
        }
        
        char dig13, dig14;
        int sm, i, r, num, peso;
        
        try{
            //Calculo do 1º digito verificador
            sm = 0;
            peso = 2;
            for (i=11; i>=0; i--){
                num = (int) (cnpj.charAt(i)-48);
                sm = sm + (num * peso);
                peso = peso+1;
                if (peso == 10) peso = 2;
            }
            r = sm % 11;
            if((r==0)||(r==1)){
                dig13 = '0';
            }else{
                dig13 = (char)((11-r) + 48); // converte no respectivo caractere numerico
            }
            //Calculo do 2º digito verificador.
            sm = 0;
            peso = 2;
            for(i=12; i>=0; i--){
                num = (int)(cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) peso = 2;
            }
            
            r = sm % 11;
            if((r == 10) || (r == 11)){
                dig14 = '0';
            } else{
                dig14 = (char)((11 -r) + 48);
            }
            
            // Verifica se os digitos calculados conferem com os digitos informados.
            if((dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13))){
                return(true);
            }else{
                return(false);
            }
        }catch (InputMismatchException erro){
            return(false);
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jCconjunto = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();
        jTcaminho = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jCEnt = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jTnent = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTentcnpj = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTentresp = new javax.swing.JTextField();
        jBEntsalva = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jTentnome = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jtentcpf = new javax.swing.JTextField();
        jBEntsalva1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTnproc = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jTprocnome = new javax.swing.JTextField();
        jTproccpf = new javax.swing.JTextField();
        jCProc = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        jTprocpod = new javax.swing.JTextField();
        jBProcsalva = new javax.swing.JButton();
        jBProcArquivo = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTdf2 = new javax.swing.JTextField();
        jTdf1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTdi2 = new javax.swing.JTextField();
        jTdi3 = new javax.swing.JTextField();
        jTdi1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTdf3 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLcod = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nova Procuração");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLabel2.setText("Conjunto:");

        jCconjunto.setFont(new java.awt.Font("Square721 BT", 1, 12)); // NOI18N
        jCconjunto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Em Conjunto", "Isoladamente", "Em conjunto ou Isoladamente", "Não Consta" }));

        jLabel23.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
        jLabel23.setText("Caminho:");

        jTcaminho.setText("\\\\\\servidor\\\\Repositorio\\\\SistemaMezzari\\\\mezzariproc\\\\");
            jTcaminho.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jTcaminhoActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
            jPanel6.setLayout(jPanel6Layout);
            jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel2)
                    .addGap(18, 18, 18)
                    .addComponent(jCconjunto, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(24, 24, 24)
                    .addComponent(jLabel23)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTcaminho)
                    .addContainerGap())
            );
            jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jCconjunto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23)
                        .addComponent(jTcaminho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanel2.setBackground(new java.awt.Color(255, 255, 255));
            jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabel10.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
            jLabel10.setText("Empresa:");

            jCEnt.setFont(new java.awt.Font("Square721 BT", 1, 12)); // NOI18N
            jCEnt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Empresa 1" }));
            jCEnt.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    jCEntItemStateChanged(evt);
                }
            });

            jLabel9.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
            jLabel9.setText("Quantidade de Empresas:");

            jTnent.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    jTnentFocusLost(evt);
                }
            });

            jLabel12.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
            jLabel12.setText("Cnpj:");

            jTentcnpj.setPreferredSize(new java.awt.Dimension(241, 20));

            jLabel13.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
            jLabel13.setText("Responsável:");

            jTentresp.setPreferredSize(new java.awt.Dimension(215, 20));

            jBEntsalva.setFont(new java.awt.Font("Square721 BT", 1, 12)); // NOI18N
            jBEntsalva.setText("Salva");
            jBEntsalva.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jBEntsalvaActionPerformed(evt);
                }
            });

            jLabel11.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
            jLabel11.setText("Nome:");

            jTentnome.setPreferredSize(new java.awt.Dimension(241, 20));
            jTentnome.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    jTentnomeFocusGained(evt);
                }
            });

            jLabel14.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
            jLabel14.setText("Cpf:");

            jtentcpf.setPreferredSize(new java.awt.Dimension(242, 20));
            jtentcpf.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    jtentcpfFocusGained(evt);
                }
            });

            jBEntsalva1.setFont(new java.awt.Font("Square721 BT", 1, 12)); // NOI18N
            jBEntsalva1.setText("Carregar de Arquivo");
            jBEntsalva1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jBEntsalva1ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jBEntsalva, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(19, 19, 19)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jtentcpf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTentnome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCEnt, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel12)
                            .addGap(24, 24, 24)
                            .addComponent(jTentcnpj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jTnent, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTentresp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(jBEntsalva1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(jTnent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCEnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(jTentcnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(jTentnome, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(jtentcpf, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(jTentresp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(5, 5, 5)
                    .addComponent(jBEntsalva)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jBEntsalva1)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanel4.setBackground(new java.awt.Color(255, 255, 255));
            jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabel17.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
            jLabel17.setText("Procuradores:");

            jLabel18.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
            jLabel18.setText("Quantidade de Procuradores:");

            jTnproc.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    jTnprocFocusLost(evt);
                }
            });

            jLabel19.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
            jLabel19.setText("Nome:");

            jLabel20.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
            jLabel20.setText("Cpf:");

            jTprocnome.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    jTprocnomeFocusGained(evt);
                }
            });

            jCProc.setFont(new java.awt.Font("Square721 BT", 1, 12)); // NOI18N
            jCProc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Procurador 1" }));
            jCProc.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    jCProcItemStateChanged(evt);
                }
            });

            jLabel24.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
            jLabel24.setText("Poderes:");

            jBProcsalva.setFont(new java.awt.Font("Square721 BT", 1, 12)); // NOI18N
            jBProcsalva.setText("Salva");
            jBProcsalva.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jBProcsalvaActionPerformed(evt);
                }
            });

            jBProcArquivo.setFont(new java.awt.Font("Square721 BT", 1, 12)); // NOI18N
            jBProcArquivo.setText("Carregar de Arquivo");
            jBProcArquivo.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jBProcArquivoActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
            jPanel4.setLayout(jPanel4Layout);
            jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTprocpod)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTprocnome)
                                .addComponent(jTproccpf)))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel17)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCProc, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel18)
                                    .addGap(18, 18, 18)
                                    .addComponent(jTnproc, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGap(128, 128, 128)
                                    .addComponent(jLabel24)))
                            .addGap(0, 72, Short.MAX_VALUE))
                        .addComponent(jBProcsalva, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBProcArquivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(jTnproc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17)
                        .addComponent(jCProc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20)
                        .addComponent(jTproccpf, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(jTprocnome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(7, 7, 7)
                    .addComponent(jLabel24)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTprocpod, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jBProcsalva)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jBProcArquivo)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanel5.setBackground(new java.awt.Color(255, 255, 255));
            jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabel6.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
            jLabel6.setText("Data Final:");

            jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
            jLabel7.setText("/");

            jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
            jLabel8.setText("/");

            jLabel3.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
            jLabel3.setText("Data Inicial:");

            jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
            jLabel4.setText("/");

            jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
            jLabel5.setText("/");

            javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
            jPanel5.setLayout(jPanel5Layout);
            jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel3)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTdi1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel4)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTdi2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTdi3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(218, 218, 218)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTdf1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel7)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTdf2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel8)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTdf3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jTdi2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTdi3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTdi1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)
                        .addComponent(jLabel6)
                        .addComponent(jTdf2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTdf1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)
                        .addComponent(jLabel8)
                        .addComponent(jTdf3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanel7.setBackground(new java.awt.Color(255, 255, 255));
            jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLcod.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
            jLcod.setText("<variavel>");

            jLabel22.setFont(new java.awt.Font("Square721 BT", 1, 14)); // NOI18N
            jLabel22.setText("Procuração cod:");

            javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
            jPanel7.setLayout(jPanel7Layout);
            jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel22)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLcod)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLcod)
                        .addComponent(jLabel22))
                    .addContainerGap(15, Short.MAX_VALUE))
            );

            jButton1.setFont(new java.awt.Font("Square721 BT", 1, 12)); // NOI18N
            jButton1.setText("Cancelar");
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

            jButton2.setFont(new java.awt.Font("Square721 BT", 1, 12)); // NOI18N
            jButton2.setText("Ok");
            jButton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
            );

            pack();
            setLocationRelativeTo(null);
        }// </editor-fold>//GEN-END:initComponents

    private void jTnentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTnentFocusLost
        this.setEntidades();
    }//GEN-LAST:event_jTnentFocusLost
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (revisaData() && revisaCaminho()){
            String datai = null;
            this.revisaData();
            if (!jTdi1.getText().isEmpty() || !jTdi2.getText().isEmpty() ||!jTdi3.getText().isEmpty()){
                datai = jTdi3.getText()+"-"+jTdi2.getText()+"-"+jTdi1.getText()+" "+"00:00:00";
            }
            String dataf = null;
            if (!jTdf1.getText().isEmpty() || !jTdf2.getText().isEmpty() ||!jTdf3.getText().isEmpty()){
                dataf = jTdf3.getText()+"-"+jTdf2.getText()+"-"+jTdf1.getText()+" "+"00:00:00";
            }
            Procuracao p = new Procuracao(Integer.valueOf(jLcod.getText()),
                                        Integer.valueOf(jTnproc.getText()), 
                                        Integer.valueOf(jTnent.getText()), 
                                        procuradores, 
                                        entidades, 
                                        datai,
                                        dataf, 
                                        jCconjunto.getSelectedItem().toString(), 
                                        jTcaminho.getText());            
            try {
                db.salva(p);
                SisLog S = new SisLog("InsereProcuracao", this.user, "Salva Procuracao (Procuracao " + this.jLcod.getText() + ") - No de Proc=" + jTnproc.getText() + ".-No de Ent=" + jTnent.getText());
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } catch (SQLException ex) {
                Logger.getLogger(InsereProc.class.getName()).log(Level.SEVERE, null, ex);
            }
            VisualizaProc v;
            v = new VisualizaProc(p.getCaminho(), Integer.valueOf(jLcod.getText()), this.user);
            v.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jBEntsalvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEntsalvaActionPerformed
        int r = revisaEnt();
        if (r ==4){
            int i = jCEnt.getSelectedIndex();
            Entidade e = new Entidade(jTentnome.getText(), jTentresp.getText(), jtentcpf.getText(), jTentcnpj.getText());
            entidades.add(i, e);
            SisLog S = new SisLog("InsereProcuracao", this.user, "Define Entidade (Procuracao " + this.jLcod.getText() + ") - Nome: " + jTentnome.getText() + ".-Cnpj: " + jTentcnpj.getText() + ".-Responsavel: " + jTentresp.getText() + ".-Cpf: " + jtentcpf.getText());
            
            if(jCEnt.getSelectedIndex()<jCEnt.getItemCount()-1){
                jCEnt.setSelectedIndex(jCEnt.getSelectedIndex()+1);
                jTentnome.requestFocus();
            }
        }else if (r == 3){ 
            JOptionPane.showMessageDialog(null, "Por favor, revisar o CNPJ das ENTIDADES.");
        }else if (r == 2){
            JOptionPane.showMessageDialog(null, "Por favor, revisar o CPF das ENTIDADES.");
        }else if (r == 1){
            JOptionPane.showMessageDialog(null, "Por favor, revisar o NOME das ENTIDADES.");
        }
    }//GEN-LAST:event_jBEntsalvaActionPerformed

    private void jCEntItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCEntItemStateChanged
        if(jCEnt.getSelectedIndex()<entidades.size()){
            jTentnome.setText(entidades.get(jCEnt.getSelectedIndex()).getNome());
            jTentresp.setText(entidades.get(jCEnt.getSelectedIndex()).getResponsavel());
            jtentcpf.setText(entidades.get(jCEnt.getSelectedIndex()).getCpf());
            jTentcnpj.setText(entidades.get(jCEnt.getSelectedIndex()).getCnpj());
        }else{
            jTentnome.setText("");
            jTentresp.setText("");
            jtentcpf.setText("");
            jTentcnpj.setText("");
        }
    }//GEN-LAST:event_jCEntItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        SisLog S = new SisLog("InsereProcuracao", this.user, "Cancelar");
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        SisLog S = new SisLog("InsereProcuracao", this.user, "Cancelar");
    }//GEN-LAST:event_formWindowClosed

    private void jBProcsalvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBProcsalvaActionPerformed
        int r = revisaProc();
        if(r == 4){
            int i = jCProc.getSelectedIndex();
            Procurador p = new Procurador(jTprocnome.getText(), jTproccpf.getText(), jTprocpod.getText());
            procuradores.add(i, p);
            while(i<=Integer.valueOf(jTnproc.getText())*2){
                if(procuradores.size()> i+1){
                    if ((procuradores.get(i+1).nome == null)||(procuradores.get(i+1).nome.equals(procuradores.get(i).nome))){
                        procuradores.remove(i+1);
                    }
                }
                i++;
            }
            SisLog S = new SisLog("InsereProcuracao", this.user, "Define Procurador (Procuracao " + this.jLcod.getText() + ") - Nome: " + jTprocnome.getText() + ".-Cpf: " + jTproccpf.getText() + ".-Poderes: " + jTprocpod.getText());

            if(jCProc.getSelectedIndex()<jCProc.getItemCount()-1){
                jCProc.setSelectedIndex(jCProc.getSelectedIndex()+1);
                jTproccpf.requestFocus();
            }
        }else if (r == 3){
            JOptionPane.showMessageDialog(null, "Por favor, revisar o CPF dos PROCURADORES.");
        }else if (r == 2){
            JOptionPane.showMessageDialog(null, "Por favor, revisar o PODER dos PROCURADORES.");
        }else if (r == 1){
            JOptionPane.showMessageDialog(null, "Por favor, revisar o NOME dos PROCURADORES.");
        }
    }//GEN-LAST:event_jBProcsalvaActionPerformed

    private void jCProcItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCProcItemStateChanged
        if(jCProc.getSelectedIndex()<procuradores.size()){
            jTprocnome.setText(procuradores.get(jCProc.getSelectedIndex()).getNome());
            jTproccpf.setText(procuradores.get(jCProc.getSelectedIndex()).getCpf());
        }else{
            jTprocnome.setText("");
            jTproccpf.setText("");
        }
    }//GEN-LAST:event_jCProcItemStateChanged

    private void jTnprocFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTnprocFocusLost
        this.setProcuradores();
    }//GEN-LAST:event_jTnprocFocusLost

    private void jTprocnomeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTprocnomeFocusGained
        if (!"".equals(this.jTproccpf.getText())){
            Dataproc dp = new Dataproc();
            dp.connect();
            jTprocnome.setText(dp.encontra(jTproccpf.getText()));
        }
    }//GEN-LAST:event_jTprocnomeFocusGained

    private void jTcaminhoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTcaminhoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTcaminhoActionPerformed

    private void jtentcpfFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtentcpfFocusGained
        if (!"".equals(this.jtentcpf.getText())){
            Dataproc dp = new Dataproc();
            dp.connect();
            jTentresp.setText(dp.encontra(jtentcpf.getText()));
        }
    }//GEN-LAST:event_jtentcpfFocusGained

    private void jTentnomeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTentnomeFocusGained
        if (!"".equals(this.jTentcnpj.getText())){
            Dataproc dp = new Dataproc();
            dp.connect();
            jTentnome.setText(dp.encontra(jTentcnpj.getText()));
        }
    }//GEN-LAST:event_jTentnomeFocusGained

    private void jBProcArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBProcArquivoActionPerformed
        JFileChooser jf = new JFileChooser();
        int ret = jf.showOpenDialog(null);
        if (ret == JFileChooser.APPROVE_OPTION) {
            try {
                File file = jf.getSelectedFile();
                //This is where a real application would open the file.
                String quant = readQuant(file.getPath()).substring(1);
                String [][] proc = new String[1000][1000];
                proc = readNomes(file.getPath());
                int i = 0;
                while(i < Integer.valueOf(quant)){
                    if (validaCPF(proc[i][0])==true){
                        Procurador p = new Procurador(proc[i][1], proc[i][0], "DIVERSOS");
                        procuradores.add(i, p);
                    }else{
                        Procurador p = new Procurador(proc[i][1], "", "DIVERSOS");
                        procuradores.add(i, p);
                    }                    
                    i++;
                }
                //CONTINUA AQUI ----------------------->//
                jTnproc.setText(quant);
                jTnproc.setEnabled(false);
                jCProc.setEnabled(false);
                jTprocnome.setEnabled(false);
                jTproccpf.setEnabled(false);
                jTprocpod.setEnabled(false);
                // --------------SEGUE AQUI--------------//
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.toString());
                ex.printStackTrace();
            }            
        }
    }//GEN-LAST:event_jBProcArquivoActionPerformed

    private String readQuant(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        return reader.readLine();
    }
    
    private String [][] readNomes(String filePath) throws IOException {
        FileReader f = new FileReader(filePath);
        BufferedReader reader = new BufferedReader(f);
        String [][] nomes = new String[1000][1000];
        String s = reader.readLine();
        s = reader.readLine();
        int i = 0;
        while(s != null){
            nomes[i][0] = s.split(" ", 2)[0];
            nomes[i][1] = s.split(" ", 2)[1];
            i++;
            s = reader.readLine();
        }
        return nomes;
    }
    
    private void jBEntsalva1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEntsalva1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBEntsalva1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBEntsalva;
    private javax.swing.JButton jBEntsalva1;
    private javax.swing.JButton jBProcArquivo;
    private javax.swing.JButton jBProcsalva;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jCEnt;
    private javax.swing.JComboBox jCProc;
    private javax.swing.JComboBox jCconjunto;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLcod;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTextField jTcaminho;
    private javax.swing.JTextField jTdf1;
    private javax.swing.JTextField jTdf2;
    private javax.swing.JTextField jTdf3;
    private javax.swing.JTextField jTdi1;
    private javax.swing.JTextField jTdi2;
    private javax.swing.JTextField jTdi3;
    private javax.swing.JTextField jTentcnpj;
    public javax.swing.JTextField jTentnome;
    private javax.swing.JTextField jTentresp;
    private javax.swing.JTextField jTnent;
    private javax.swing.JTextField jTnproc;
    private javax.swing.JTextField jTproccpf;
    private javax.swing.JTextField jTprocnome;
    private javax.swing.JTextField jTprocpod;
    private javax.swing.JTextField jtentcpf;
    // End of variables declaration//GEN-END:variables
}
