//LA PRIMA COSA DA SCRIVERE
package freccette;
//JAVA GRAPHIC STUFFS
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Freccette
{

    public static void main(String[] args) 
    {
        //APRO UNA FINESTRA
        JFrame jf = new JFrame("Darts");
     
        jf.setPreferredSize(new Dimension(300,400));
        jf.setContentPane(new Bersaglio
               (
                15,          //numero anelli
                15,          //munizioni
                1500,        //cooldown mano :)
                Color.green, //background
                Color.blue,  //munizioni
                Color.red,   //tabellone punteggi
                Color.black,             //scritte e mirino
                new Color(50,100,50), //base dell'anello
                new Color(30,5,5)       // incremento
               )
        );

        jf.pack();
        
        jf.setVisible(true);
        
           
    
    } 
    
}


class Bersaglio extends JPanel implements ActionListener 
{
    private int centri;
    private int xPos;
    private int yPos;
    private int xMouseTick;
    private int yMouseTick;
    private int punteggio;
    private int proiettili;
    private int[] xarrow;
    private int[] yarrow;
    private int[] arrowPosisitionX;
    private int[] arrowPositionY;
    private Timer timer = new Timer(50, this);
    private boolean hoSparato;
    private int tempoRipresa;
    private Color baseColor;
 //   private Color backgroundColor;
    private Color scoreColor;
    private Color weaponsColor;
    private Color fontAndAimColor;
    private Color additiveColor;
    private final int coolDownMillis;
    public Bersaglio(int numTargets, int numWeapons, int coolDown,Color backGround, Color weapons, Color score, Color fontAndAim, Color base, Color additive)         
    {
      xMouseTick    = 0;
      yMouseTick    = 0;
      punteggio     = 0;
      centri        = numTargets;
      proiettili    = numWeapons;
      hoSparato     = false;
      coolDownMillis = coolDown;
      tempoRipresa   = 0;
      //PLACEHOLDERS PER LE FRECCIE
      xarrow = new int[]{0,0,3,5,8,8,5,5,3,3};
      yarrow = new int[]{5,3,0,0,3,5,2,8,8,2};
      //CONTERRA' LA POSIZIONE TRASLATA
      arrowPosisitionX = new int[]{0,0,0,0,0,0,0,0,0,0};
      arrowPositionY   = new int[]{0,0,0,0,0,0,0,0,0,0};
      
      baseColor = base;
      scoreColor = score;
      weaponsColor = weapons;
 
      fontAndAimColor = fontAndAim;
      additiveColor = additive;
    
      //SETTO IL BACKGROUND
      setBackground(backGround);
      //AGGIUNGO UN GESTORE EVENTI MOUSE CLICK
      addMouseListener(new MouseListener() 
      {
          @Override
          public void mouseClicked(MouseEvent e)
          {    
               //POTREBBE ESSERE ALTERATA RISPETTO AL MOUSE DI QUALCHE FATTORE
               setAimPosition(e.getX(),e.getY());
               //UPDATE
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
      
      timer.start();
    
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
     
     double delta = (double)min / centri;
    
     //SPARO
     if(xPos > 0 && possoSparare())
     {
       //DECREMENTO I PROIETTILI
       proiettili--;
       //SETTO IL FLAG DI SPARO
       hoSparato   = true;
       //INIZIALIZO IL TEMPO DI RIPRESA MIILLESECONDI / TEMPROCONTROLLO
       tempoRipresa = coolDownMillis / 50;
      
     }
     //CALCOLO PUNTEGGIO E DISGNO ANELLI BERSGLIO
     for(int i = 0; i < centri;i++)
     {
         double diametro = min - i * delta;
        //VERIFICO IL PUNTEGGIO SOLO SE HO SPARATO
        if(hoSparato)
        {
        
         double d = Math.sqrt(Math.pow(centrox - xPos,2)+Math.pow(centroy - yPos,2));
         if (d <= delta / 4 )       
         {
            punteggio +=  100;
            //reset sparo se centro qualcosa
            hoSparato = false;
         }else
         if(2*d <= diametro && 2*d > diametro - delta)
         {
            punteggio +=  i + 1;
           //reset sparo se centro qualcosa
            hoSparato = false;
          }
           
        }
        //E' DENTRO IL LOOP(che comuqnue userei per determinare la logica
        //dei punteggi ad annello, SOLO xchè disegno a mano, se no fuori e carica immmagine
        //BERSAGLI
        drawTarget(g,i,diametro);
     }
     
     //RESET CLICK E SPARATO
     xPos      = 0;
     hoSparato = false;
     //PUNTEGGIO
     drawScore(g);
   
     //ARMI
     if(proiettili > 0)
     {    
       drawWeapons(g);
     }
      //MIRINO
     if(xMouseTick > 0)
     {
       drawAim(g);
     }
     
    }
    
    //PRENDO LA POSIZIONE DEL MOUSE E VERIFICO IL COOLDOWN OGNI 50 millsecondi
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //la mouse info mi da un punto in cordinate schermo
        Point location = MouseInfo.getPointerInfo().getLocation();
        //le converto in coordinate finestra
        SwingUtilities.convertPointFromScreen(location, this);
        
        //aggiorno il tempo di ripresa
        if(tempoRipresa > 0)
        {
          tempoRipresa--;
        }
        //salvo i dati e ridisegno se necessario  
        if(location.x!= xMouseTick || location.x!=yMouseTick)
        {
          xMouseTick = location.x;
          yMouseTick = location.y;
          //UPDATE SE NECESSARIO
          repaint();
        }
      
    }
    
    
    //DISEGNA UN ANELLO
    private void drawTarget(Graphics g,int i,double diametro)
    {
        int deltac = i % 2 == 0 ? additiveColor.getRed() : additiveColor.getGreen();
     //TODO: vorrei togliere la new qua
        g.setColor(new Color((baseColor.getRed() + i*deltac) % 255 ,(baseColor.getGreen()+i*deltac) % 255,(baseColor.getBlue()+i*additiveColor.getBlue()) % 255));
        double x = getWidth()  / 2 - diametro / 2; 
        double y = getHeight() / 2 - diametro / 2; 
        
        g.fillOval((int)x, (int)y, (int)diametro, (int)diametro);
        
    }
     //DISEGNA IL CARICATORE DI MUNIZIONI
    private void drawWeapons(Graphics g)
    {
      g.setColor(weaponsColor); 
     
      for(int i = 0;i <proiettili;i++)
      {
       
       for(int ii = 0; ii < arrowPosisitionX.length;ii++)
       {
         arrowPosisitionX[ii] = xarrow[ii] + getWidth() - (i * 10);
         arrowPositionY[ii] = yarrow[ii] * getHeight() / 100  + 10 ;
       }
       
       g.fillPolygon(arrowPosisitionX,arrowPositionY,arrowPosisitionX.length);
       
      }
    }
     //DISEGNA IL MIRINO
    private void drawAim(Graphics g)
    {
      
      g.setColor(fontAndAimColor);

      for(int i = -10 ; i < 10; i++)
      {
         g.fillOval(xMouseTick + i * 3, yMouseTick - 1, 2, 2);
         g.fillOval(xMouseTick - 1, yMouseTick + i * 3, 2, 2);
      }

    }
    //DISEGNA IL PUNTEGGIO
    private void drawScore(Graphics g)
    {
     g.setColor(fontAndAimColor);
     g.fillRect(2, 2, 40 + getWidth() / 10, 10 + getHeight() / 10);
     g.setColor(scoreColor);
     g.fillRect(4, 4, 40+getWidth() / 10 - 4, 10+getHeight() / 10 - 4);
     
     g.setColor(fontAndAimColor);
     //TODO: vorrei togliere la new qua
     g.setFont(new Font("TimesRoman", Font.PLAIN, getHeight() / 11)); 
     g.drawString(String.format("%-5d",punteggio),5,5+getHeight() / 10 );
    }
    
    //DEFINISCO DOVE STO MIRANDO
    private void setAimPosition(int x,int y)
    {
               xPos = x;
               yPos = y;
    }
    
    private boolean possoSparare()
    {
        //la condizione del non ho sparato non dovrebbe piu servire
        //con l'introduzione del cool down
      return (tempoRipresa <= 0 && proiettili > 0 && !hoSparato );
    }
}

