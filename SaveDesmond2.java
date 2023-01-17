import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
 
public class SaveDesmond2 extends Applet implements ActionListener{
   
    static DecimalFormat twoDig = new DecimalFormat("00");
    static LocalTime startTime;
    static LocalTime stopTime;
   
    static final int ROW = 11;
    static final int COL = 11;
    static final int ZOMBNUM = 5;
    static final int WINDOWWIDTH = 1500;
    static final int WINDOWHEIGHT = 750;
    static Color black = new Color(0,0,0);
    static Color white = new Color(255,255,255);
    static Color red = new Color(255,0,0);
    static Color skyBlue = new Color(178,203,222);
    static Color lightGreen = new Color(122, 230, 76);
    static Color grey = new Color(211,211,211);
    static Color lightGrey = new Color(220,220,220);
    static Color darkGrey = new Color(105,105,105);
   
    static boolean colourSwap = true;
    static boolean followStatus = false;
    static boolean showWelcome = true;
    static boolean zombieGame = false;
    static boolean desVisible;
    static boolean zombVisible;
    static int lives = 3;
    static int boardX = 30;
    static int boardY = 30;
    static int playX = 0;
    static int playY = 0;
    static int desX = roll(7, 9), desY = roll(7, 9);
    static int padX = 100, padY = 450;
    static int legendY = 200;
    static int menuX = 350+boardX;
    static int menuY = 20+boardY;
    static int highscoreX = 500;
    static int coverX = WINDOWWIDTH;
    static int coverY = WINDOWHEIGHT;
    static int welcomeX = (WINDOWWIDTH/2)-500;
    static int welcomeY = (WINDOWHEIGHT/2)-100;
    static int welcomeBackgroundX = 1000;
    static int welcomeBackgroundY = 20;
    static int [][] zombies = new int [ZOMBNUM][2]; //zombies[i][0] = x zombies[i][1] = y
    static int [][] gridStatus = new int[ROW][COL]; //GRID STATUS
	static boolean [] targetPressed = new boolean[5];

    //0 = EMPTY
    //1 = HAS DESMOND
    //2 = HAS ZOMBIE
   
   
    static int moves;
    static String displayTime = "00:00";
    static String name = "SOMEONE";
    static String distanceMessage;
    static String objectiveMessage = "FIND DESMOND";
    static String cheatMessage = "";
    static String zombFightMessage = "A zombie bit you... you must fight it now. Click it's weak points when they come up.";
    static LinkedList<String> highestNames = new LinkedList<String>();
    static LinkedList<String> highTimesNames = new LinkedList<String>();
    static LinkedList<String> highFormattedTimes = new LinkedList<String>();
    static LinkedList<Integer> highscores = new LinkedList<Integer>();
    static LinkedList<Long> highTimes = new LinkedList<Long>();
    
	static Button [] targets = new Button[5];
	static Button target;
    static Button start, cont;
    Button up, down, left, right;
    Button easy, medium, hard;
    Button win, cheat, giveUp;
    Button zombieFightStart;
    static TextField nameInput;
   
   
    static String [] welcomeMessage= {"Welcome to SAVE DESMOND!", "In this game you will move your character using provided buttons to find and return Desmond home whilst avoiding zombies!", "EASY: Desmond and zombies visible", "MEDIUM: Zombies visible, Desmond invisible", "HARD: Zombies and Desmond invisible", "Please enter your NAME and press <START>"};
 
    public void init(){
        resize(1500, 750);
        setBackground(grey);
       
       
        start = new Button("START");
        start.addActionListener(this);
        nameInput = new TextField("                ");
        nameInput.setText("");
        nameInput.addActionListener(this);
        add(nameInput);
        add(start);
        
        cont = new Button("CONTINUE");
        cont.addActionListener(this);
        cont.setBackground(red);
        cont.setBounds(menuX, menuY+80, 250, 50);
       
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
        up.setBackground(darkGrey);
       
        down = new Button("v");
        down.addActionListener(this);
        down.setBounds(padX, padY+30, 30, 30);
        down.setBackground(darkGrey);
 
        left = new Button("<");
        left.addActionListener(this);
        left.setBounds(padX-30, padY, 30, 30);
        left.setBackground(darkGrey);
        left.setForeground(black);
 
        right = new Button(">");
        right.addActionListener(this);
        right.setBounds(padX+30, padY, 30, 30);
        right.setBackground(darkGrey);
 
        win = new Button("WIN");
        win.addActionListener(this);
        win.setBounds(padX + 100, padY, 100, 30);
       
        cheat = new Button("CHEAT");
        cheat.addActionListener(this);
        cheat.setBounds(padX + 210, padY, 100, 30);
       
        giveUp = new Button("GIVE UP");
        giveUp.addActionListener(this);
        giveUp.setBounds(padX + 320, padY, 100, 30);
       
        zombieFightStart = new Button("FIGHT");
        zombieFightStart.addActionListener(this);
        zombieFightStart.setBounds(500, 350, 150, 50);
        
        for(int i = 0; i < 5; i++){        	
        	targets[i] = new Button();
        	targets[i].setBackground(lightGreen);
        	targets[i].setBounds(roll(0, WINDOWWIDTH), roll(0, WINDOWHEIGHT), 50, 50);
        	targets[i].addActionListener(this);
        	targetPressed[i] = false;
        }
       
    }
    
    public void paint(Graphics g){
    	
    	if(!zombieGame){
            g.setColor(lightGrey);
            g.fillRect(menuX + highscoreX-5, menuY-15, 400, 20*(highscores.size()+2));
            g.fillRect(boardX, (30*(COL+1)), (30*ROW), 20);
            g.fillRect(boardX, (30*(COL+1))+21, (10*ROW), 20);
           
            g.setColor(black);
            g.drawRect(boardX-1, boardY-1, (30*ROW)+1, (30*COL)+1);
            g.drawRect(boardX-1, (30*(COL+1))-1, (30*ROW)+1, 21);
            g.drawRect(menuX + highscoreX-6, menuY-16, 401, 20*(highscores.size()+2)+1);
            g.drawRect(boardX-1, (30*(COL+1))+20, (10*ROW)+1, 21);
            g.drawString(displayTime, boardX+10, (30*(COL+1)+35));
            
//            g.setColor(red);
//    		int heartSizeX = 20;
//            int [] heartCoordsX = {(int) (boardX+(30*ROW)-(1.25*heartSizeX))+10, (int) (boardX+(30*ROW)-(2.5*heartSizeX))+10, (int) (boardX+(30*ROW)-(3.75*heartSizeX)+10)};
//            int [] heartCoordsY = {30*(COL+1)+40, 30*(COL+1)+40, 30*(COL+1)+40};
//            
//     
//    		
//    		for(int i = 0; i < lives; i++){
//    			int heartX = heartCoordsX[i];
//    			int heartY = heartCoordsY[i];
//    			int heartSizeY = (int)(heartSizeX*0.75);
//    			int [] x = {heartX, heartX-(heartSizeX/2), heartX+(heartSizeX/2)};
//    			int [] y = {heartY, heartY-heartSizeY, heartY-heartSizeY};
//    			
//    			g.fillPolygon(x, y, 3);
//    			g.fillOval(heartX-(heartSizeX/2)+1, heartY-heartSizeY-(heartSizeY/4), heartSizeX/2, heartSizeX/2);
//    			g.fillOval(heartX-1, heartY-heartSizeY-(heartSizeY/4), heartSizeX/2, heartSizeX/2);
//    		}
     
           
    		
           g.setColor(black);
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
           
            for(int i = 1; i < ROW+1; i++) {
                g.drawString(Integer.toString(i), (30*(i))+boardX-20, boardY-5);
                g.drawString(Integer.toString(i), boardX-20, (30*(i))+boardY-20);
            }
           
            g.drawString(name + "'s GAME", boardX+10, boardY+(30*COL)+15);
            g.drawString(objectiveMessage, menuX, menuY);
            g.drawString("Distance from Desmond: " + distanceMessage, menuX, menuY+20);
            g.drawString("MOVES: " + moves, menuX, menuY+40);
            g.drawString(cheatMessage, menuX, menuY+60);
            g.drawString("HIGHSCORES:", menuX + highscoreX, menuY);
           
            g.setColor(lightGrey);
            g.fillRect(menuX, menuY + legendY, 150, 90);
           
            g.setColor(red);
            g.fillRect(menuX, menuY + legendY, 30, 30);
            g.setColor(skyBlue);
            g.fillRect(menuX, menuY + legendY+30, 30, 30);
            g.setColor(lightGreen);
            g.fillRect(menuX, menuY + legendY+60, 30, 30);
     
            g.setColor(black);
            g.drawRect(menuX-1, menuY+legendY-1, 151, 91);
           
            g.setColor(black);
            g.drawString("YOU", menuX + 40, menuY + 220);
            g.drawString("DESMOND", menuX + 40, menuY + 250);
            g.drawString("ZOMBIE", menuX + 40, menuY + 280);
           
           
           
            g.drawString("Lowest Moves:", menuX + highscoreX, menuY+20);
            g.drawString("Lowest Times:", menuX + highscoreX+200, menuY+20);
     
            sortHighscores();
            for(int i = 0; i < highscores.size(); i++){
                g.drawString(highestNames.get(i), menuX + highscoreX, menuY+((i+2)*20));
                g.drawString(highscores.get(i) + " ", menuX + highscoreX+100, menuY+((i+2)*20));
     
            }
           
            sortTimes();
            for(int i = 0; i < highTimes.size(); i++){
                g.drawString(highFormattedTimes.get(i), menuX + highscoreX + 300, menuY+((i+2)*20));
                g.drawString(highTimesNames.get(i) + " ", menuX + highscoreX + 200, menuY+((i+2)*20));
     
            }
           
            g.setColor(grey);
            g.fillRect(0, 0, coverX, coverY);    		
    	}

       
        if(showWelcome) {
            g.setColor(lightGrey);
            g.fillRect(welcomeX-5, welcomeY-welcomeBackgroundY, welcomeBackgroundX, ((welcomeBackgroundY+1)*welcomeMessage.length));
 
            g.setColor(black);
            g.drawRect(welcomeX-6, welcomeY-welcomeBackgroundY-1, welcomeBackgroundX+1, ((welcomeBackgroundY+1)*welcomeMessage.length)+1);
           
            g.drawString(welcomeMessage[0], welcomeX, welcomeY);
            g.drawString(welcomeMessage[1], welcomeX, welcomeY+20);
            g.drawString(welcomeMessage[2], welcomeX, welcomeY+40);
            g.drawString(welcomeMessage[3], welcomeX, welcomeY+60);
            g.drawString(welcomeMessage[4], welcomeX, welcomeY+80);
            g.drawString(welcomeMessage[5], welcomeX, welcomeY+100);            
        }
 
        if(zombieGame){
        	g.setColor(red);
        	g.fillRect(0, 0, WINDOWWIDTH, WINDOWHEIGHT);
        	g.setColor(white);
        	g.drawString(zombFightMessage, 300, 500);
        	
        }
        
       
    }
   
    public void actionPerformed(ActionEvent e){
       
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
            gridStatus[desX][desY] = 0;
            desWalk(followStatus);
           
            for(int i = 0; i < ROW; i++){
                for(int j = 0; j < COL; j++){
                    if(gridStatus[i][j] == 2){
                        gridStatus[i][j] = 0;
                    }
                }
            }
           
           
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
            objectiveMessage = "RETURN DESMOND TO HOME (1, 1)";
            followStatus = true;
        }
       
        if(gridStatus[playX][playY] == 2){
            objectiveMessage = "A ZOMBIE ATE YOU!";
            if(playX != 0 && gridStatus[playX-1][playY] != 2){
            	playX--;
            }else if(gridStatus[playX+1][playY] != 2){
            	playX++;
            }else if(playY != 0 && gridStatus[playX][playY-1] != 2){
            	playY--;
            }else{
            	playY++;
            }
            zombieFight();

//            if(lives == 0){
//            	objectiveMessage = objectiveMessage + " YOU DIED";
//                endGame(false);
//            }else{
//            	objectiveMessage = objectiveMessage + " YOU LOST A LIFE";
//            }
            
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
       
        if(followStatus && playX == 0 && playY == 0 && e.getSource() != cont){
            objectiveMessage = "YOU WON IN " + moves + " MOVES, PRESS START TO PLAY AGAIN";
            endGame(true);
        }
        
        if(e.getSource() == cont){
            coverX = highscoreX+menuX-10;
            coverY = WINDOWHEIGHT;
            showWelcome = true;
            nameInput.setVisible(true);
            start.setVisible(true);
            cont.setVisible(false);
        }
        
        stopTime = LocalTime.now();
        long timePassed = startTime.until(stopTime, ChronoUnit.SECONDS);
        long mins = 0, secs = timePassed;
       
       
        if(timePassed > 60){
            while(secs > 60){
                secs = secs - 60;
                mins++;
            }
        }
       
        displayTime = twoDig.format(mins) + ":" + twoDig.format(secs);
        
        if(e.getSource() == zombieFightStart){
        	for(int i = 0; i < targets.length; i++){
        		add(targets[i]);
        		targets[i].setVisible(true);
        	}
        	zombieFightStart.setVisible(false);
        	zombFightMessage = "";
        }
        
        for(int i = 0; i < targets.length; i++){
        	if(e.getSource() == targets[i]){
        		targets[i].setVisible(false);
        		targetPressed[i] = true;
        	}
        }
        
        if(targetPressed[0] && targetPressed[1] && targetPressed[2] && targetPressed[3] && targetPressed[4]){
    		zombieGame = false;
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
            for(int i = 0; i < targetPressed.length; i++){
            	targetPressed[i] = false;
            }
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
    	lives = 3;
        coverX = 0; coverY = 0;
        showWelcome = false;
       
        startTime = LocalTime.now();
        name = nameInput.getText();
        name = name.toUpperCase();
       
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
        add(cont);
       
        cont.setVisible(false);
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
            stopTime = LocalTime.now();
            long timePassed = startTime.until(stopTime, ChronoUnit.SECONDS);
            highestNames.add(name);
            highscores.add(moves);
            highTimes.add(timePassed);
            highTimesNames.add(name);
            highFormattedTimes.add(displayTime);
        }
       
        cont.setVisible(true);
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
   
    public void sortTimes(){
        long tempScore;
        String tempFormattedScore;
        String tempName;
        boolean sorted = false;
        while(!sorted){
            sorted = true;
            for(int i = 0; i < highTimes.size()-1; i++){
                if(highTimes.get(i) > highTimes.get(i+1)){
                    sorted = false;
                    tempScore = highTimes.get(i);
                    tempName = highTimesNames.get(i);
                    tempFormattedScore = highFormattedTimes.get(i);
                   
                    highTimes.set(i, highTimes.get(i+1));
                    highTimesNames.set(i, highTimesNames.get(i+1));
                    highFormattedTimes.set(i, highFormattedTimes.get(i+1));
                   
                    highTimes.set(i+1, tempScore);
                    highTimesNames.set(i+1, tempName);
                    highFormattedTimes.set(i+1, tempFormattedScore);
                   
                }
            }          
        }
    }
    
    public void zombieFight(){
    	zombieGame = true;
        zombFightMessage = "A zombie bit you... you must fight it now. Click it's weak points when they come up.";
        zombieFightStart.setVisible(true);
    	add(zombieFightStart);
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
        repaint();

//        for(int i = 0; i < 5; i++){        	
//        	targets[i].setBounds(roll(0, WINDOWWIDTH), roll(0, WINDOWHEIGHT), 50, 50);
//        }
        
    }
   
}