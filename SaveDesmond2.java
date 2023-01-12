import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
 
public class SaveDesmond2 extends Applet implements ActionListener{
    static final int ROW = 11;
    static final int COL = 11;
    static final int ZOMBNUM = 5;
    static Color black = new Color(0,0,0);
    static Color white = new Color(255,255,255);
    static Color red = new Color(255,0,0);
    static Color skyBlue = new Color(178,203,222);
    static Color lightGreen = new Color(122, 230, 76);
    
    static boolean colourSwap = true;
    static boolean followStatus = false;
    static int playX = 0;
    static int playY = 0;
    static int desX = roll(7, 9), desY = roll(7, 9);
    static int padX = 100, padY = 400;
    static int [][] zombies = new int [ZOMBNUM][2]; //zombies[i][0] = x zombies[i][1] = y
    static int [][] gridStatus = new int[ROW][COL]; //GRID STATUS
    //0 = EMPTY
    //1 = HAS DESMOND
    //2 = HAS ZOMBIE
    
    static int menuX = 350, menuY = 20;
    
    static int moves;
    static String distanceMessage;
    static String objectiveMessage = "FIND DESMOND";
   
    Button start, up, down, left, right;
   
    public void init(){
        resize(1500, 750);
       
        start = new Button("Start");
        start.addActionListener(this);
        add(start);
       
        up = new Button("^");
        up.addActionListener(this);
        up.setBounds(padX, padY-30, 30, 30);
       
        down = new Button("v");
        down.addActionListener(this);
        down.setBounds(padX, padY+30, 30, 30);
 
        left = new Button("<");
        left.addActionListener(this);
        left.setBounds(padX-30, padY, 30, 30);
 
        right = new Button(">");
        right.addActionListener(this);
        right.setBounds(padX+30, padY, 30, 30);
        
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
       
        g.setColor(skyBlue);
        g.fillOval((30*desX)+8,(30*desY)+8,14,14);

        g.setColor(lightGreen);
        
        for(int i = 0; i < ZOMBNUM; i++){
            g.fillOval((30*zombies[i][0])+3,(30*zombies[i][1])+3,24,24);

        }
        
        g.setColor(black);
        g.drawString(objectiveMessage, menuX, menuY);
        g.drawString("Distance from Desmond: " + distanceMessage, menuX, menuY+20);
        g.drawString("MOVES: " + moves, menuX, menuY+40);
        
       
    }
   
    public void actionPerformed(ActionEvent e){
    	
        if(e.getSource() == start){
            startPressed();
        }
       
        if(e.getSource()== up){
            if(playY != 0){
            	moves++;
                playY--;
            }
        }
       
        if(e.getSource()== down){
            if(playY != 10){
            	moves++;
                playY++;
            }
        }
       
        if(e.getSource()== left){
            if(playX != 0){
            	moves++;
                playX--;
            }
        }
       
        if(e.getSource()== right){
            if(playX != 10){
            	moves++;
                playX++;
            }
        }
        
    	if(e.getSource() == up || e.getSource() == down || e.getSource() == left || e.getSource() == right){
    		desWalk(followStatus);
    		for(int i = 0; i < ZOMBNUM; i++){
        		zombWalk(i);

    		}
    	}
    	
        if(desX == playX && desY == playY){
        	objectiveMessage = "RETURN DESMOND TO HOME (0, 0)";
        	followStatus = true;
        }
        
    	int distance = Math.abs(desX-playX) + Math.abs(desY-playY);
    	
    	if(distance == 0){
            distanceMessage = "You got Desmond!";
    	}else if((distance <= 2)){
            distanceMessage = "Very hot! (" + distance + " tiles away)";
        }else if(distance <= 4){
        	distanceMessage = "Getting warmer! (" + distance + " tiles away)";
        }else{
        	distanceMessage = "Pretty cold! (" + distance + " tiles away)";
        }
    	
    	if(followStatus && playX == 0 && playY == 0){
    		objectiveMessage = "YOU WON IN " + moves + " MOVES";
    	}
    	
        repaint();
    }
 
    public static int roll(int min, int max){
        return (int)Math.round((Math.random()*(max-min)) + min);
    }
    
    public void desWalk(boolean follow){
    	boolean validCommand = false;
    	
    	if(follow){
    		desX = playX;
    		desY = playY;
    		return;
    	}
    	do{
            int direction = roll(1, 6);
            switch(direction){
                case 1:
                    if(desY != COL-1 && gridStatus[desX][desY+1] != 2){
                        desY++;
                        validCommand = true;
                    }else{
                        validCommand = false;
                    }
                    break;
                case 2:
                    if(desY != 0 && gridStatus[desX][desY-1] != 2){
                        desY--;
                        validCommand = true;
                    }else{
                        validCommand = false;
                    }
                    break;
                case 3:
                    if(desX != ROW-1 && gridStatus[desX+1][desY] != 2){
                        desX++;
                        validCommand = true;
                    }else{
                        validCommand = false;
                    }
                    break;
                case 4:
                    if(desX != 0 && gridStatus[desX-1][desY] != 2){
                        desX--;
                        validCommand = true;
                    }else{
                        validCommand = false;
                    }
                    break;
                case 5:
                	validCommand = true;
                	break;
                case 6:
                	validCommand = true;
                	break;
                default:
                    validCommand = false;
            }
    	}while(!validCommand);
        gridStatus[desX][desY] = 1;

    }
    
    public void zombWalk(int zombnum){
    	boolean validCommand;
    	
    	do{
	        int direction = roll(1, 6);
	        switch(direction){
	            case 1:
	                if(zombies[zombnum][1] != COL-1 && gridStatus[zombies[zombnum][0]][zombies[zombnum][1]+1] != 1){
	                    zombies[zombnum][1]++;
	                    validCommand = true;
	                }else{
	                    validCommand = false;
	                }
	                break;
	            case 2:
	                if(zombies[zombnum][1] != 0 && gridStatus[zombies[zombnum][0]][zombies[zombnum][1]-1] != 1){
	                    zombies[zombnum][1]--;
	                    validCommand = true;
	                }else{
	                    validCommand = false;
	                }
	                break;
	            case 3:
	                if(zombies[zombnum][0] != ROW-1 && gridStatus[zombies[zombnum][0]+1][zombies[zombnum][1]] != 1){
	                    zombies[zombnum][0]++;
	                    validCommand = true;
	                }else{
	                    validCommand = false;
	                }
	                break;
	            case 4:
	                if(zombies[zombnum][0] != 0 && gridStatus[zombies[zombnum][0]-1][zombies[zombnum][1]] != 1){
	                    zombies[zombnum][0]--;
	                    validCommand = true;
	                }else{
	                    validCommand = false;
	                }
	                break;
	            case 5:
                	validCommand = true;
                	break;
                case 6:
                	validCommand = true;
                	break;
	            default:
	                validCommand = false;
	        }
    	}while(!validCommand);
    }
    
    public void startPressed(){
        gridStatus[desX][desY] = 1;
        
    	for(int i = 0; i < ROW; i++){
    		for(int j = 0; j < COL; j++){
    			gridStatus[i][j] = 0;
    		}
    	}
    	
        for(int i = 0; i < zombies.length; i++){
            zombies[i][0] = roll(1,6);
            zombies[i][1] = roll(1,6);
            
            gridStatus[zombies[i][0]][zombies[i][1]] = 2;
        }
        
        add(up);
        add(down);
        add(left);
        add(right);
    }
   
}
 

