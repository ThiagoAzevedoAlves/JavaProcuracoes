/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procuracoes.classes;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author Thiago
 */
public class ComboData {

    public Calendar cal = new GregorianCalendar();
    public JComboBox dia;
    public JComboBox mes;
    public JComboBox ano;
    public ItemListener trocaMes;

    public ComboData(int DIA, int MES, int ANO) {

        cal.set(ANO, MES, DIA);
        int i = 1;
        String[] d = new String[31];
        String[] m = new String[12];
        String[] a = new String[401];
        while (i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            d[i - 1] = Integer.toString(i);
            i++;
        }
        dia = new JComboBox(d);
        i = 1;
        while (i <= 12) {
            m[i - 1] = Integer.toString(i);
            i++;
        }
        String z[] = new String[31];
        trocaMes = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int j = 1;
                cal.set(Calendar.MONTH, mes.getSelectedIndex());
                while (j <= cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    z[j - 1] = Integer.toString(j);
                    j++;
                }
                while(j<=31){
                    z[j-1] = "";
                }
                JComboBox diaux = new JComboBox(z);
                dia.setModel(diaux.getModel());
                dia.repaint();
            }
        };
        mes = new JComboBox(m);
        mes.addItemListener(trocaMes);
        i = 1800;
        while (i <= 2200) {
            a[i - 1800] = Integer.toString(i);
            i++;
        }
        ano = new JComboBox(a);

    }

}
