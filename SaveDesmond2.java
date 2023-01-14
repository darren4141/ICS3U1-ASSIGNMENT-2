import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
 
public class SaveDesmond2 extends Applet implements ActionListener{
    static final int ROW = 11;
    static final int COL = 11;
    static final int ZOMBNUM = 5;
    static Color black = new Color(0,0,0);
    static Color white = new Color(255,255,255);
    static Color red = new Color(255,0,0);
    static Color skyBlue = new Color(178,203,222);
    static Color lightGreen = new Color(122, 230, 76);
    static Color grey = new Color(211,211,211);
    static Color lightGrey = new Color(220,220,220);
    
    static boolean colourSwap = true;
    static boolean followStatus = false;
    static boolean desVisible;
    static boolean zombVisible;
    static int boardX = 30;
    static int boardY = 30;
    static int playX = 0;
    static int playY = 0;
    static int desX = roll(7, 9), desY = roll(7, 9);
    static int padX = 100, padY = 450;
    static int legendY = 200;
    static int highscoreX = 500;
    static int [][] zombies = new int [ZOMBNUM][2]; //zombies[i][0] = x zombies[i][1] = y
    static int [][] gridStatus = new int[ROW][COL]; //GRID STATUS
    //0 = EMPTY
    //1 = HAS DESMOND
    //2 = HAS ZOMBIE
   
    static int menuX = 350+boardX, menuY = 20+boardY;
   
    static int moves;
    static String name = "someone";
    static String distanceMessage;
    static String objectiveMessage = "FIND DESMOND";
    static String cheatMessage = "";
    static LinkedList<String> highestNames = new LinkedList<String>();
    static LinkedList<Integer> highscores = new LinkedList<Integer>();
 
    Button start, up, down, left, right;
    Button easy, medium, hard;
    Button win, cheat, giveUp;
    TextField nameInput;
   
    public void init(){
        resize(1500, 750);
        setBackground(grey);
        
        
        start = new Button("Start");
        start.addActionListener(this);
        nameInput = new TextField();
        nameInput.addActionListener(this);
        nameInput.setBounds(50, 100, 200, 30);
        add(nameInput);
        add(start);
       
        easy = new Button("Easy");
        easy.addActionListener(this);
        medium = new Button("Medium");
        medium.addActionListener(this);
        hard = new Button("Hard");
        hard.addActionListener(this);
        easy.setBounds(padX + 100, padY-40, 100, 30);
        medium.setBounds(padX + 210, padY-40, 100, 30);
        hard.setBounds(padX + 320, padY-40, 100, 30);

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
       
        win = new Button("WIN");
        win.addActionListener(this);
        win.setBounds(padX + 100, padY, 100, 30);
       
        cheat = new Button("CHEAT");
        cheat.addActionListener(this);
        cheat.setBounds(padX + 210, padY, 100, 30);
       
        giveUp = new Button("GIVE UP");
        giveUp.addActionListener(this);
        giveUp.setBounds(padX + 320, padY, 100, 30);
       
       
    }
   
    public void paint(Graphics g){
    	
        g.setColor(lightGrey);
        g.fillRect(menuX, menuY + legendY, 150, 90);
        g.fillRect(menuX + highscoreX-5, menuY-15, 220, 20*(highscores.size()+1));
        g.fillRect(boardX, (30*(COL+1)), (30*ROW), 20);
        
        g.setColor(black);
        g.drawRect(boardX-1, boardY-1, (30*ROW)+1, (30*COL)+1);
        g.drawRect(boardX-1, (30*(COL+1))-1, (30*ROW)+1, 21);
        g.drawRect(menuX-1, menuY+legendY-1, 151, 91);
        g.drawRect(menuX + highscoreX-6, menuY-16, 221, 20*(highscores.size()+1)+1);

        for(int i = 0; i < ROW; i++){
            for(int j = 0; j < COL; j++){
                g.fillRect((30*j)+boardX,(30*i) + boardY, 30, 30);
                if(g.getColor() == lightGrey){
                    g.setColor(black);
                }else if(g.getColor() == black){
                    g.setColor(lightGrey);
                }
            }
        }
       
        g.setColor(red);
        g.fillOval((30*playX)+boardX+5,(30*playY)+boardY+5,20,20);
       
        if(desVisible) {
            g.setColor(skyBlue);
            g.fillOval((30*desX)+boardX+8,(30*desY)+boardY+8,14,14);        	
        }
 
        
        if(zombVisible) {
            g.setColor(lightGreen);
            for(int i = 0; i < ZOMBNUM; i++){
                g.fillOval((30*zombies[i][0])+boardX+3,(30*zombies[i][1])+boardY+3,24,24);
            }        	
        }


    	g.setColor(red);
        for(int i = 0; i < ROW; i++){
            for(int j = 0; j < COL; j++){
                g.drawString(Integer.toString(gridStatus[j][i]), (30*j)+boardX,(30*i)+boardY+30);
            }
        }
        
        g.setColor(black);
        g.drawString(name + "'s GAME", boardX+10, boardY+(30*COL)+15);
        g.drawString(objectiveMessage, menuX, menuY);
        g.drawString("Distance from Desmond: " + distanceMessage, menuX, menuY+20);
        g.drawString("MOVES: " + moves, menuX, menuY+40);
        g.drawString(cheatMessage, menuX, menuY+60);
        g.drawString("HIGHSCORES:", menuX + highscoreX, menuY);
       
        g.setColor(red);
        g.fillRect(menuX, menuY + legendY, 30, 30);
        g.setColor(skyBlue);
        g.fillRect(menuX, menuY + legendY+30, 30, 30);
        g.setColor(lightGreen);
        g.fillRect(menuX, menuY + legendY+60, 30, 30);

        g.setColor(black);
        g.drawString("YOU", menuX + 40, menuY + 220);
        g.drawString("DESMOND", menuX + 40, menuY + 250);
        g.drawString("ZOMBIE", menuX + 40, menuY + 280);
        
        sortHighscores();
        for(int i = 0; i < highscores.size(); i++){
            g.drawString(highestNames.get(i), menuX + highscoreX, menuY+((i+1)*20));
            g.drawString(highscores.get(i) + " ", menuX + highscoreX+100, menuY+((i+1)*20));
 
        }
       
    }
   
    public void actionPerformed(ActionEvent e){
        repaint();

        for(int i = 0; i < ROW; i++){
            for(int j = 0; j < COL; j++){
                gridStatus[i][j] = 0;
            }
        }
       
        if(e.getSource() == start){
            startPressed();
        }
       
        if(e.getSource() == up){
            if(playY != 0){
                moves++;
                playY--;
            }
        }
       
        if(e.getSource() == down){
            if(playY != 10){
                moves++;
                playY++;
            }
        }
       
        if(e.getSource() == left){
            if(playX != 0){
                moves++;
                playX--;
            }
        }
       
        if(e.getSource() == right){
            if(playX != 10){
                moves++;
                playX++;
            }
        }
       
        if(e.getSource() == win){
            endGame(true);
        }
       
        if(e.getSource() == giveUp){
            endGame(false);
        }
       
        if(e.getSource() == cheat) {
        	cheatMessage = "Desmond's current location: [" + desX + ", " + desY + "]";
        }
        
        if(e.getSource() == up || e.getSource() == down || e.getSource() == left || e.getSource() == right){
            desWalk(followStatus);
            for(int i = 0; i < ZOMBNUM; i++){
                zombWalk(i);
            }
            
        }
        
        if(e.getSource() == easy) {
        	desVisible = true;
        	zombVisible = true;
        }
       
        if(e.getSource() == medium) {
        	desVisible = false;
        	zombVisible = true;
        }
        
        if(e.getSource() == hard) {
        	desVisible = false;
        	zombVisible = false;
        }
        
        if(desX == playX && desY == playY){
        	desVisible = true;
            objectiveMessage = "RETURN DESMOND TO HOME (0, 0)";
            followStatus = true;
        }
       
        if(gridStatus[playX][playY] == 2){
            objectiveMessage = "A ZOMBIE ATE YOU! YOU DIED, PRESS START TO PLAY AGAIN";
            endGame(false);
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
            objectiveMessage = "YOU WON IN " + moves + " MOVES, PRESS START TO PLAY AGAIN";
            endGame(true);
        }
       
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
                    if(desY > 0 && gridStatus[desX][desY-1] != 2){
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
                    if(desX > 0 && gridStatus[desX-1][desY] != 2){
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
                    if(zombies[zombnum][1] != COL-1 && gridStatus[zombies[zombnum][0]][zombies[zombnum][1]+1] == 0){
                        zombies[zombnum][1]++;
                        validCommand = true;
                    }else{
                        validCommand = false;
                    }
                    break;
                case 2:
                    if(zombies[zombnum][1] >= 1 && gridStatus[zombies[zombnum][0]][zombies[zombnum][1]-1] == 0){
                        zombies[zombnum][1]--;
                        validCommand = true;
                    }else{
                        validCommand = false;
                    }
                    break;
                case 3:
                    if(zombies[zombnum][0] != ROW-1 && gridStatus[zombies[zombnum][0]+1][zombies[zombnum][1]] == 0){
                        zombies[zombnum][0]++;
                        validCommand = true;
                    }else{
                        validCommand = false;
                    }
                    break;
                case 4:
                    if(zombies[zombnum][0] >= 1 && gridStatus[zombies[zombnum][0]-1][zombies[zombnum][1]] == 0){
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
        gridStatus[zombies[zombnum][0]][zombies[zombnum][1]] = 2;
 
    }
   
    public void startPressed(){
        name = nameInput.getText();
        nameInput.setText("");
        nameInput.setVisible(false);
        start.setVisible(false);
 
        playX = 0;
        playY = 0;
        moves = 0;
        followStatus = false;
 
        desX = roll(7, 9);
        desY = roll(7, 9);
       
        gridStatus[desX][desY] = 1;
       
        for(int i = 0; i < zombies.length; i++){
            zombies[i][0] = roll(2,6);
            zombies[i][1] = roll(2,6);
           
            gridStatus[zombies[i][0]][zombies[i][1]] = 2;
        }
       
        add(up);
        add(down);
        add(left);
        add(right);
        add(win);
        add(cheat);
        add(giveUp);
        add(easy);
        add(medium);
        add(hard);
        
        up.setVisible(true);
        down.setVisible(true);
        left.setVisible(true);
        right.setVisible(true);
        win.setVisible(true);
        cheat.setVisible(true);
        giveUp.setVisible(true);
        easy.setVisible(true);
        medium.setVisible(true);
        hard.setVisible(true);
 
    }
   
   
   
    public void endGame(boolean won){
        if(won){
            highestNames.add(name);
            highscores.add(moves);
        }
       
        nameInput.setVisible(true);
        start.setVisible(true);
        up.setVisible(false);
        down.setVisible(false);
        left.setVisible(false);
        right.setVisible(false);
        win.setVisible(false);
        cheat.setVisible(false);
        giveUp.setVisible(false);
        easy.setVisible(false);
        medium.setVisible(false);
        hard.setVisible(false);
    }
   
    public void sortHighscores(){
        int tempScore;
        String tempName;
        boolean sorted = false;
        while(!sorted){
            sorted = true;
            for(int i = 0; i < highscores.size()-1; i++){
                if(highscores.get(i) > highscores.get(i+1)){
                    sorted = false;
                    tempScore = highscores.get(i);
                    tempName = highestNames.get(i);
                   
                    highscores.set(i, highscores.get(i+1));
                    highestNames.set(i, highestNames.get(i+1));
                   
                    highscores.set(i+1, tempScore);
                    highestNames.set(i+1, tempName);
                   
                }
            }          
        }
    }
   
}
 
 
 
 

