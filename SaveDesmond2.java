import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
 
public class SaveDesmond2 extends Applet implements ActionListener{
    static final int ROW = 11;
    static final int COL = 11;
    static Color black = new Color(0,0,0);
    static Color white = new Color(255,255,255);
    static Color red = new Color(255,0,0);
    static boolean colourSwap = true;
    static int playX = 0;
    static int playY = 0;
   
    Button start, up, down, left, right;
   
    public void init(){
        resize(500, 750);
       
        start = new Button("Start");
        start.addActionListener(this);
        add(start);
       
        up = new Button("^");
        up.addActionListener(this);
        up.setBounds(400, 100, 30, 30);
       
        down = new Button("v");
        down.addActionListener(this);
        down.setBounds(400, 160, 30, 30);
 
        left = new Button("<");
        left.addActionListener(this);
        left.setBounds(370, 130, 30, 30);
 
        right = new Button(">");
        right.addActionListener(this);
        right.setBounds(430, 130, 30, 30);
    }
   
    public void paint(Graphics g){
       
        g.setColor(black);
        for(int i = 0; i < ROW; i++){
            for(int j = 0; j < COL; j++){
                g.fillRect(30*j,30*i, 30, 30);
                if(g.getColor() == white){
                    g.setColor(black);
                }else if(g.getColor() == black){
                    g.setColor(white);
                }
            }
        }
       
        g.setColor(red);
        g.fillOval((30*playX)+5,(30*playY)+5,20,20);
       
       
    }
   
    public void actionPerformed(ActionEvent e){
       
        if(e.getSource() == start){
            startPressed();
        }
       
        if(e.getSource()== up){
            if(playY != 0){
                playY--;
            }
            repaint();
        }
       
        if(e.getSource()== down){
            if(playY != 10){
                playY++;
            }
            repaint();
        }
       
        if(e.getSource()== left){
            if(playX != 0){
                playX--;
            }
            repaint();
        }
       
        if(e.getSource()== right){
            if(playX != 10){
                playX++;
            }
            repaint();
        }
    }
 
    public void startPressed(){
        add(up);
        add(down);
        add(left);
        add(right);
    }
   
}
 

