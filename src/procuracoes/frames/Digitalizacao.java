/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procuracoes.frames;
import SK.gnome.morena.*;
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
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import procuracoes.db.Database;


/**
 *
 * @author Thiago
 */
public class Digitalizacao extends JFrame{
    
    public TwainSource source;
    public JPanel jPanel1;
    public Database db;
    
    public void salva(String user)throws MorenaException, SQLException{
                    
        source = TwainManager.selectSource(null);
        int npag = 0;
        int resposta;
        List<String> s = new ArrayList<>();
        List<Image> i = new ArrayList<>();
        MorenaImage morenaImage;
        
        if (source!=null){
            morenaImage = new MorenaImage(source);                   //cria um objeto MorenaImage que manipula a imagem direta do scanner
            Image image = Toolkit.getDefaultToolkit().createImage(morenaImage);  //cria um objeto Image para receber a imagem
            i.add(npag, image);
            BufferedImage bimg = new BufferedImage(morenaImage.getWidth(),  //cria um bufferedImage para poder salvar a imagem
                                                   morenaImage.getHeight(), 
                                                   BufferedImage.TYPE_INT_RGB);
            
            Graphics2D g = bimg.createGraphics();                                //cria um graphics2d para manipular a imagem do buffer
            g.drawImage(i.get(npag), 0, 0, null);                                      //desenha o objeto Image no BufferedImage
            try {
                ImageIO.write(bimg, "jpg", new File("C:/temp/teste" + Integer.toString(npag) + ".jpg"));            //Cria um novo arquivo jpg;
                s.add("C:/temp/teste" + Integer.toString(npag) + ".jpg");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao salvar imagem " + ex);
            }
            resposta = JOptionPane.showConfirmDialog(null, "Deseja continuar escaneando? ", null, JOptionPane.YES_NO_OPTION);
            npag++;
            
            while(resposta == JOptionPane.YES_OPTION){
                source = TwainManager.selectSource(null);
                morenaImage = new MorenaImage(source);                   //cria um objeto MorenaImage que manipula a imagem direta do scanner
                image = Toolkit.getDefaultToolkit().createImage(morenaImage);  //cria um objeto Image para receber a imagem
                i.add(npag, image);
                bimg = new BufferedImage(morenaImage.getWidth(),  //cria um bufferedImage para poder salvar a imagem
                                         morenaImage.getHeight(), 
                                         BufferedImage.TYPE_INT_RGB);
            
                g = bimg.createGraphics();                                //cria um graphics2d para manipular a imagem do buffer
                g.drawImage(i.get(npag), 0, 0, null);                                      //desenha o objeto Image no BufferedImage
                try {
                    ImageIO.write(bimg, "jpg", new File("C:/temp/teste" + Integer.toString(npag) + ".jpg"));            //Cria um novo arquivo jpg;
                    s.add("C:/temp/teste" + Integer.toString(npag) + ".jpg");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao salvar imagem " + ex);
                }
                resposta = JOptionPane.showConfirmDialog(null, "Deseja continuar escaneando? ", null, JOptionPane.YES_NO_OPTION);
                npag++;
            }
            
            Document document = new Document();
            Rectangle r = new Rectangle(morenaImage.getWidth(), morenaImage.getHeight());
            document.setPageSize(r);
            String output = this.getNovoCaminho();
            try {
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
            }catch (DocumentException | IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao criar arquivo pdf " + e);
            }
            TwainManager.close();
        }else{
            JOptionPane.showMessageDialog(null, "Documneto nao encontrado !");
        }
        TwainManager.close();
        Insere in = new Insere(user);
        in.setVisible(true);
    }
        
    public String getNovoCaminho() throws SQLException{
        this.db = new Database();
        this.db.connect();
        int cod;
        cod = db.getProcod();
        String ret;
        ret = "\\\\servidor\\procuracoes\\mezzariproc\\"+ Integer.toString(db.getProcod()) + ".pdf";
        return ret;
    }
}
