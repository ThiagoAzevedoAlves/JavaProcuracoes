/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procuracoes.classes;
import SK.gnome.morena.*;
import SK.gnome.twain.TwainException;
import SK.gnome.twain.TwainManager;
import SK.gnome.twain.TwainSource;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import procuracoes.db.Database;
import procuracoes.frames.InsereProc;


/**
 *
 * @author Thiago
 */
public class Digitalizacao extends JFrame{
    
    public TwainSource source;
    public JPanel jPanel1;
    public Database db;
    
    static int resposta;
    static MorenaImage morenaImage;
    static Image image;
    
    static int npag;
    
    static List<String> s = new ArrayList<>();
    static List<Image> i = new ArrayList<>();
    static List<JLabel> l = new ArrayList<>();
    
    static BufferedImage resizedImg;
    static BufferedImage bimg;
    static Graphics2D g;
    static Graphics2D g2;
    
    static JLabel x;
    static int cx, cy;
    
    public void salva(String user)throws MorenaException, SQLException{
                    
        source = TwainManager.selectSource(null);
        npag = 0;
        
        if (source!=null){
            morenaImage = new MorenaImage(source);                   //cria um objeto MorenaImage que manipula a imagem direta do scanner
            image = Toolkit.getDefaultToolkit().createImage(morenaImage);  //cria um objeto Image para receber a imagem
            i.add(npag, image);
            bimg = new BufferedImage(morenaImage.getWidth(),  //cria um bufferedImage para poder salvar a imagem
                                                   morenaImage.getHeight(), 
                                                   BufferedImage.TYPE_INT_RGB);
            
            g = bimg.createGraphics();//cria um graphics2d para manipular a imagem do buffer
            g.drawImage(i.get(npag), 0, 0, null);//desenha o objeto Image no BufferedImage
            try {
                ImageIO.write(bimg, "jpg", new File("C:/temp/teste" + Integer.toString(npag) + ".jpg")); //Cria um novo arquivo jpg; 
                s.add("C:/temp/teste" + Integer.toString(npag) + ".jpg");                               //Se a pasta não existir, a conversão não funciona;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao salvar imagem " + ex);
            }
            //---------------------------------------//
            JFrame f = new JFrame();
            f.setBounds(50, 200, 1500, 600);
            f.setTitle("Visualizador");
            f.setMaximumSize(new Dimension(1500, 600));
            f.setMinimumSize(new Dimension(1500, 600));
            
            cx = 0;
            cy = 0;
            
            resizedImg = new BufferedImage(100, 150, BufferedImage.TYPE_INT_ARGB);
            g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(i.get(npag), 0, 0, 100, 150, null);
            g2.dispose();
            
            x = new JLabel(new ImageIcon(resizedImg));
            
            GridBagLayout grid = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();
            GridBagConstraints d = new GridBagConstraints();
            
            c.fill = GridBagConstraints.HORIZONTAL;
            d.gridheight = 8;
            d.gridwidth = 3;
            c.ipadx = 10;
            c.ipady = 10;
            c.gridx = cx;
            c.gridy = cy;
            
            d.gridheight = 0;
            d.gridwidth = 3;
            d.fill = GridBagConstraints.VERTICAL;
            d.anchor = d.PAGE_END;
            d.gridy = 5;
            
            f.setLayout(grid);
            f.add(new JLabel("Deseja continuar escaniando?"), d);
            
            JButton j1 = new JButton("Sim");
            j1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    morenaImage = new MorenaImage(source);                   //cria um objeto MorenaImage que manipula a imagem direta do scanner
                    image = Toolkit.getDefaultToolkit().createImage(morenaImage);  //cria um objeto Image para receber a imagem
                    i.add(npag, image);
                    bimg = new BufferedImage(morenaImage.getWidth(),  //cria um bufferedImage para poder salvar a imagem
                                         morenaImage.getHeight(), 
                                         BufferedImage.TYPE_INT_RGB);
            
                    g = bimg.createGraphics();                                //cria um graphics2d para manipular a imagem do buffer
                    g.drawImage(i.get(npag), 0, 0, null); 
                    //desenha o objeto Image no BufferedImage
                    try {
                        ImageIO.write(bimg, "jpg", new File("C:/temp/teste" + Integer.toString(npag) + ".jpg"));            //Cria um novo arquivo jpg;
                        s.add("C:/temp/teste" + Integer.toString(npag) + ".jpg");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao salvar imagem " + ex);
                    }
                    //--------------------------------------------//
                    resizedImg = new BufferedImage(100, 150, BufferedImage.TYPE_INT_ARGB);
                    g2 = resizedImg.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(i.get(npag), 0, 0, 100, 150, null);
                    g2.dispose();
                
                    if(npag < 8){
                        cx = npag;
                    }else if(npag >= 24){
                        cx = npag - 24;
                        cy = 3;
                    }else if(npag >= 16){
                        cx = npag - 16;
                        cy = 2;
                    }else if(npag >= 8){
                        cx = npag - 8;
                        cy = 1;
                    }
                
                    x = new JLabel(new ImageIcon(resizedImg));
                    c.gridx = cx;
                    c.gridy = cy;
            
                    f.add(x, c);
                    f.validate();
                    //----------------------------------------------//
                    npag++;
                }
            });
            f.add(j1, d);
            
            JButton j2 = new JButton("Nao");
            j2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Document document = new Document();
                    Rectangle r = new Rectangle(morenaImage.getWidth(), morenaImage.getHeight());
                    document.setPageSize(r);
                    try {
                        String output = getNovoCaminho(); 
                        FileOutputStream fos = new FileOutputStream(output);
                        PdfWriter writer = PdfWriter.getInstance(document, fos);
                        writer.open();
                        document.open();
                        int j = 0;
                        while(j< npag){
                            document.add(com.itextpdf.text.Image.getInstance(s.get(j)));
                            j++;
                        }
                
                        document.close();
                        writer.close();
                    }catch (DocumentException | IOException | SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao criar arquivo pdf " + ex);
                    }
                    try {
                        TwainManager.close();
                        InsereProc in;
                        in = new InsereProc(user);
                        in.setVisible(true);
                        f.dispose();
                    } catch (SQLException | TwainException ex) {
                        Logger.getLogger(Digitalizacao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            f.add(j2, d);
            f.add(x, c);
            
            npag++;
            f.validate();
            f.setVisible(true);
            f.toFront();
        }else{
            JOptionPane.showMessageDialog(null, "Documneto nao encontrado !");
        }
    }
        
    public String getNovoCaminho() throws SQLException{
        this.db = new Database();
        this.db.connect();
        int cod;
        cod = db.getProcod();
        String ret;
        ret = "\\\\servidor\\Repositorio\\SistemaMezzari\\mezzariproc\\"+ Integer.toString(db.getProcod()) + ".pdf";
        return ret;
    }
}
