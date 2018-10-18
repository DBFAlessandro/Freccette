//LA PRIMA COSA DA SCRIVERE
package freccette;
//JAVA GRAPHIC STUFFS
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Freccette
{

    public static void main(String[] args) 
    {
        //APRO UNA FINESTRA
        JFrame jf = new JFrame("Darts");
     
        jf.setPreferredSize(new Dimension(300,400));
        jf.setContentPane(new Bersaglio(15));

        jf.pack();
        
        jf.setVisible(true);
        
           
    
    } 
    
}


class Bersaglio extends JPanel
{
    private int centri;
    private int xPos;
    private int yPos;
    private int punteggio;
    
    public Bersaglio(int c)
    {
      punteggio     = 0;
      centri        = c;
      setBackground(Color.GREEN);
      
      addMouseListener(new MouseListener() 
      {
          @Override
          public void mouseClicked(MouseEvent e)
          {
             
               xPos = e.getX();
               yPos = e.getY();
               repaint();
             
          }

          @Override
          public void mousePressed(MouseEvent e) {
             
              //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
          }

          @Override
          public void mouseReleased(MouseEvent e) {
             // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
          }

          @Override
          public void mouseEntered(MouseEvent e) {
             // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
          }

          @Override
          public void mouseExited(MouseEvent e) {
             // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
          }
      });
    
    }
    

 
    @Override
    public void paintComponent(Graphics g)
    {
     super.paintComponent(g);
     
    
     
     int width   = this.getWidth();
     int height  = this.getHeight();
     
     int centrox = width / 2;
     int centroy = height / 2;
             
     int min = width > height ? height : width;
     
     double delta = min / centri;
     //TODO INSERIRE NUMERO PROETTILI IN ALTO A DEX O BASSO COME FRECCETTE
     //ICONA MOUSE FRECCETTA UN PO DIVERSA
     
     //BERSAGLIO
     for(int i = 0; i < centri; i++)
     {
       
        double diametro = min - i * delta;
        
        //CALCOLO PUNTEGGIO 
       
        if(xPos > 0)
        {
         double d = Math.sqrt(Math.pow(centrox - xPos,2)+Math.pow(centroy - yPos,2));
         if (d <= delta / 4)       
         {
            punteggio +=  100;
            xPos   = 0;
         }else
         if(2*d <= diametro && 2*d > diametro - delta)
         {
            punteggio +=  i + 1;
            xPos   = 0;
            //yPos   = 0;
          }
        
        }
        int deltac = i % 2 == 0 ? 5 : 15;
        g.setColor(new Color((150 + i*deltac) % 255 ,((100+i*deltac) % 255),100));
        double x = width  / 2 - diametro / 2; 
        double y = height / 2 - diametro / 2; 
        
        g.fillOval((int)x, (int)y, (int)diametro, (int)diametro);
     }
  
     //PUNTEGGIO
     g.setColor(Color.BLACK);
     g.fillRect(2, 2, 40+width / 10, 10+height / 10);
     g.setColor(Color.RED);
     g.fillRect(4, 4, 40+width / 10 - 4, 10+height / 10 - 4);
     g.setColor(Color.BLACK);
     
     
     g.setFont(new Font("TimesRoman", Font.PLAIN, min / 11)); 
   
     g.drawString(String.format("%-5d",punteggio),5,5+height / 10 );
    }

}

