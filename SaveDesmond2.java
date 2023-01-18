//====================================================================================================================================================================================================================================================================
//
//"Save Desmond" Program
//Darren Liu
//January 17th, 2023
//Java 4.6.1
//====================================================================================================================================================================================================================================================================
//	
//Problem definition: 	Find a lost child (Desmond) who moves randomly once per turn, and return them to home whilst avoiding zombies
//	
//Input:				Name (text box)
//					Button presses:
//						Movement (up, down, left, right)
//						Menu (win, cheat, give up)
//						Difficulty settings (easy, medium, hard)
//						Navigation buttons (continue, start)
//						Buttons for zombie minigame
//						
//Processing:			Calculate placements of various items such as game board, controls tab, leaderboard using static int variables which store dimensions
//					Generate random numbers for:
//						Placement of zombies
//						Placement of Desmond
//						Movement of zombies
//						Movement of Desmond
//						Placement of zombie minigame targets
//					Selection used for:
//						Conditionally drawing different things
//							Draw red background, prompt text, etc when zombie game is triggered
//							Cover game board and print welcome message when player restarts
//							Check when player interacts with Desmond
//							Check when player interacts with zombie
//							If statements used to make sure no entities are able to exit the boundaries of the board
//							If statements used to make sure zombies do not randomly step on Desmond and vice versa
//							If statements used to perform actions when buttons are pressed
//					Constantly read and update Desmond, player, and zombie locations as they change and update them on the board
//					Sorting used to sort lowest amount of moves as well as shortest amount of time taken to win game
//					Counter which stores the amount of moves taken which is stored in a LinkedList
//					Constantly update time and calculate the time it takes for the player to win the game, information is stored in a LinkedList
//					
//Output:				Game board which displays player, zombies (conditionally), and Desmond (conditionally)
//					Messages which prompt the player on what to do
//					Buttons with text on them which tell the player what to do
//					Sorted highscore table
//					Legend to show the player what the colours represent
//====================================================================================================================================================================================================================================================================
//List of variables:	ROW, COL - a constant int that stores the size of the game board
//					ZOMBNUM - a constant int that stores the number of zombies spawned
//					WINDOWWIDTH, WINDOWHEIGHT - a constant int that stores the height and width of the game window
//					Any 'Color' type variable - a colour variable that stores RGB values
//					followStatus - a boolean variable that tells the program whether or not Desmond should be following the player
//					showWelcome - a boolean variable that tells the program whether or not it should be displaying the welcome message
//					zombieGame - a boolean variable that tells the program whether or not it should be playing the zombie minigame
//					desVisible - a boolean variable that determines if Desmond is visible or not
//					zombVisible - a boolean variable that determines if the zombies are visible or not
//					lives - an int variables that stores the number of lives
//					playX, playY, desX, desY - int variables that store the X and Y locations of the player & Desmond
//					
//					FORMATTING STATIC VARIABLES - these variables are here so that the location of entire sections (leaderboard, game pad, board, etc) can be changed easily just by changing the static variable
//						boardX, boardY - int variables that store the location of the top left corner of the game board
//						padX, padY - int variables that store the location of the game pad and the buttons next to them
//						legendY - int variable that stores the Y location of the legend
//						menuX, menuY, welcomeX, welcomeY, highscoreX - all the same idea as boardX and boardY
//						coverX, coverY - this variable stores the dimensions of the grey cover, which is supposed to cover the entire screen at times
//						
//					zombies - a two dimensional int array that stores the coordinates of the zombies. The first dimension (column array) represents the number of the zombie, the following row arrays only have a size of 2, meaning zombies[i][0] represents the x coordinate of zombie[i], and zombies[i][1] represents the y coordinate of zombie[i]
//					gridStatus - a two dimensional int array that stores the "status" of each tile on the grid (does it have the desmond of a zombie or none?) this array is used for selection, preventing zombies from randomly walking on desmond as well as for determining when a player hits a zombie or desmond
//					targetPressed - a boolean array that stores that status of each target in the zombie fight minigame
//					
//					VARIABLES MEANT TO BE EDITED AND REPAINTED - java applet uses the paint method to output anything you see on the screen, and a repaint() method used to update the screen, meaning we can output String variables to display something, then change the contents of the variable, and repaint() which will update the screen. This idea is used for basically any text that changes in the program.
//					The following variables are meant to be edited as the game progresses, making changing text
//						moves - an int that stores the number of moves taken
//						displayTime - a String that displays the current runtime of a game
//						name - a String that stores the name of the player
//					    distanceMessage - a String that displays the distance the player is from Desmond
//					    objectiveMessage - a String that displays the current objective for the player (find desmond, return desmond home, etc)
//					    cheatMessage - a String that displays the current location of desmond if the player decides to press the cheat button
//					    zombFightMessage - a String explaining the rules of the zombie fight minigame
//					    welcomeMessage - a String array that stores the welcome message
//					    highestNames, highscores - String and Integer LinkedLists that store the names and move count of players who won the game
//					    highTimesNames, highFormattedTimes, highTimes - a String, String, and Long LinkedList that stores the names, times and formatted times of players who won the game. highTimes and formattedTimes are seperate variables as highTimes is needed for sorting and formattedTimes is needed for clean display
//					    
//					targets - button array that stores the buttons displayed during zombie minigame
//					start, cont - navigation buttons
//					up, down, left, right - buttons for player movement
//					easy, medium, hard - buttons for changing difficulty
//					win, cheat, giveUp - buttons for cheat commands
//					zombieFightStart - button for navigation to start the zombie minigame after the user is finished reading the zombie minigame instructions
//					nameInput - a textField that inputs the name of the user
//====================================================================================================================================================================================================================================================================

//import statments
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
 

//start of class
public class SaveDesmond2 extends Applet implements ActionListener{
   
	
	//ALL VARIABLES EXPLAINED IN HEADER BLOCK
    static DecimalFormat twoDig = new DecimalFormat("00");
    static LocalTime startTime;
    static LocalTime stopTime;
    static LocalTime startZombieTime;
    static LocalTime stopZombieTime;
   
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
   
    static boolean followStatus = false;
    static boolean showWelcome = true;
    static boolean zombieGame = false;
    static boolean desVisible;
    static boolean zombVisible;
    static final int ZOMBIETIMELIM = 5;
    static int lives = 3;
    static int playX = 0;
    static int playY = 0;
    static int desX = roll(7, 9), desY = roll(7, 9);
    static int boardX = 30;
    static int boardY = 30;
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
    //0 = EMPTY
    //1 = HAS DESMOND
    //2 = HAS ZOMBIE
	static boolean [] targetPressed = new boolean[5];
   
    static int moves;
    static String displayTime = "00:00";
    static String displayZombieTime = "";
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
    static Button start, cont;
    Button up, down, left, right;
    Button easy, medium, hard;
    Button win, cheat, giveUp;
    Button zombieFightStart;
    static TextField nameInput;
   
   
    static String [] welcomeMessage= {"Welcome to SAVE DESMOND!", "In this game you will move your character using provided buttons to find and return Desmond home whilst avoiding zombies!", "EASY: Desmond and zombies visible", "MEDIUM: Zombies visible, Desmond invisible", "HARD: Zombies and Desmond invisible", "Please enter your NAME and press <START>"};
 
    
    //INIT METHOD, ALL APPLETS REQUIRE AND RUN THE INIT METHOD BEFORE ANYTHING ELSE    
    public void init(){
        resize(1500, 750);//resize window size
        setBackground(grey);//set background colour
       
        //--> CREATE, PLACE, AND INITIALIZE BUTTONS
        start = new Button("START"); //declare a new button that has the text "START"
        start.addActionListener(this);//make it so that start can be clicked
        nameInput = new TextField("                ");//create a text field to input the name, spaces are for resizing
        nameInput.setText("");//empty the text feild
        nameInput.addActionListener(this);//make it so that text can be taken from the textField
        add(nameInput);//add textfield to the window
        add(start);//add button to the window
        
        cont = new Button("CONTINUE");
        cont.addActionListener(this);
        cont.setBackground(red);//set background colour of button
        cont.setBounds(menuX, menuY+80, 250, 50);//set location and size of the button
       
        easy = new Button("Easy");
        easy.addActionListener(this);
        medium = new Button("Medium");
        medium.addActionListener(this);
        hard = new Button("Hard");
        hard.addActionListener(this);
        easy.setBounds(padX + 100, padY-40, 100, 30);//location of the button are relative to static variables padX and padY, makes for easier adjustments
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
        
        // lines 184-242 is repetitive button initializing
        
        for(int i = 0; i < 5; i++){//initialize all 5 target buttons
        	targets[i] = new Button();
        	targets[i].setBackground(lightGreen);
        	targets[i].setBounds(roll(0, WINDOWWIDTH), roll(0, WINDOWHEIGHT), 50, 50);//set their coordinates to random locations on the screen
        	targets[i].addActionListener(this);
        	targetPressed[i] = false;//initialize boolean array to all false
        }
       
    }//end of init()
    
    //PAINT METHOD - all Java applets require a paint method which is where thing are actually shown on the screen using Graphics object
    public void paint(Graphics g){
    	
    	
    	if(!zombieGame){//don't print any of this if we are currently playing the zombie game
    		
    		//--> PRINT INITIAL BOARD AND BOXES
    		//these rectangles are background "textboxes"
            g.setColor(lightGrey);
            g.fillRect(menuX + highscoreX-5, menuY-15, 400, 20*(highscores.size()+2));//leaderboard rectangle's size is based on amount of values in the LinkedList
            g.fillRect(boardX, (30*(COL+1)), (30*ROW), 20);
            g.fillRect(boardX, (30*(COL+1))+21, (10*ROW), 20);
            //fillRect method parameters: (x location of top left corner, y location of top left corner, width, height)
            
           
            //these rectanges are the borders to the above textboxes, their dimensions start 1 pixel before the textboxes and extend 1 extra pixel horizontally and vertically
            g.setColor(black);
            g.drawRect(boardX-1, boardY-1, (30*ROW)+1, (30*COL)+1);
            g.drawRect(boardX-1, (30*(COL+1))-1, (30*ROW)+1, 21);
            g.drawRect(menuX + highscoreX-6, menuY-16, 401, 20*(highscores.size()+2)+1);
            g.drawRect(boardX-1, (30*(COL+1))+20, (10*ROW)+1, 21);
            //drawRect method parameters: (x location of top left corner, y location of top left corner, width, height)

            g.drawString(displayTime, boardX+10, (30*(COL+1)+35));//output that displays the current time taken
            
    
          
      	   for(int i = 0; i < lives; i++){
        	  int heartX = heartCoordsX[i];
              int heartY = heartCoordsY[i];
              int heartSizeY = (int)(heartSizeX*0.75);
              int [] x = {heartX, heartX-(heartSizeX/2), heartX+(heartSizeX/2)};
              int [] y = {heartY, heartY-heartSizeY, heartY-heartSizeY};
              
              g.fillPolygon(x, y, 3);
              g.fillOval(heartX-(heartSizeX/2)+1, heartY-heartSizeY-(heartSizeY/4), heartSizeX/2, heartSizeX/2);
              g.fillOval(heartX-1, heartY-heartSizeY-(heartSizeY/4), heartSizeX/2, heartSizeX/2);
           }
     
           
           //--> PRINT GAME BOARD
           g.setColor(black);
            for(int i = 0; i < ROW; i++){//iterate through all rows
                for(int j = 0; j < COL; j++){//iterate through all columns
                    g.fillRect((30*j)+boardX,(30*i) + boardY, 30, 30);//draw a rectangle based on i and j coordinates
                    //TO CREATE CHECKER PATTERN
                    if(g.getColor() == lightGrey){//swap the colour from lightGrey to back
                        g.setColor(black);
                    }else if(g.getColor() == black){//and vice versa
                        g.setColor(lightGrey);
                    }
                }
            }
           
            //--> DISPLAY CHARACTERS
            //fillOval method parameters: (x location, y location, width, height), to draw a circle, width == height
            g.setColor(red);
            g.fillOval((30*playX)+boardX+5,(30*playY)+boardY+5,20,20);//draw the player based on player location variables
           
            if(desVisible){//if player is allowed to see Desmond
                g.setColor(skyBlue);
                g.fillOval((30*desX)+boardX+8,(30*desY)+boardY+8,14,14);//draw Desmond based on Desmond location variables
            }
     
           
            if(zombVisible) {//if player is allowed to see zombies
                g.setColor(lightGreen);
                for(int i = 0; i < ZOMBNUM; i++){//iterate through all zombies
                    g.fillOval((30*zombies[i][0])+boardX+3,(30*zombies[i][1])+boardY+3,24,24);//display each zombie based on their location
                }          
            }
     
     
          g.setColor(red);
            for(int i = 0; i < ROW; i++){//iterate through rows
                for(int j = 0; j < COL; j++){//iterate through columns
                    g.drawString(Integer.toString(gridStatus[j][i]), (30*j)+boardX,(30*i)+boardY+30);//output gridstatus FOR ERRORCHECKING
                }
            }
           
            g.setColor(black);
           
            
            //--> DISPLAY COORDINATE PLANE
            for(int i = 1; i < ROW+1; i++) {//iterate through the number of rows, starting from 1
                g.drawString(Integer.toString(i), (30*(i))+boardX-20, boardY-5);//print horizontal legend
                g.drawString(Integer.toString(i), boardX-20, (30*(i))+boardY-20);//print vertical legend
            }
           
            //--> DISPLAY MENU
            
            g.drawString(name + "'s GAME", boardX+10, boardY+(30*COL)+15);
            g.drawString(objectiveMessage, menuX, menuY);
            g.drawString("Distance from Desmond: " + distanceMessage, menuX, menuY+20);
            g.drawString("MOVES: " + moves, menuX, menuY+40);
            g.drawString(cheatMessage, menuX, menuY+60);
            g.drawString("HIGHSCORES:", menuX + highscoreX, menuY);
            //these variables are all meant to be updated periodically as the game is player
            
            //--> DISPLAY LEGEND
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
            
           
           //--> DISPLAY LEADERBOARD
            g.drawString("Lowest Moves:", menuX + highscoreX, menuY+20);
            g.drawString("Lowest Times:", menuX + highscoreX+200, menuY+20);
     
            sortHighscores();//sort the highscores
            for(int i = 0; i < highscores.size(); i++){//iterate through all highscores
                g.drawString(highestNames.get(i), menuX + highscoreX, menuY+((i+2)*20));//print corresponding names
                g.drawString(highscores.get(i) + " ", menuX + highscoreX+100, menuY+((i+2)*20));//print lowest moves
     
            }
           
            sortTimes();//sort the times
            for(int i = 0; i < highTimes.size(); i++){//iterate through all the times
                g.drawString(highFormattedTimes.get(i), menuX + highscoreX + 300, menuY+((i+2)*20));//print formatted times
                g.drawString(highTimesNames.get(i) + " ", menuX + highscoreX + 200, menuY+((i+2)*20));//print corresponding names
     
            }
           
            g.setColor(grey);
            g.fillRect(0, 0, coverX, coverY);//display cover rectange, conditionally, this rectange will cover the entire screen OR it will be invisible
    	}

    	//--> WELCOME BOX & MESSAGE
        if(showWelcome){//only run this code if we want to show the welcome message
            g.setColor(lightGrey);
            g.fillRect(welcomeX-5, welcomeY-welcomeBackgroundY, welcomeBackgroundX, ((welcomeBackgroundY+1)*welcomeMessage.length));//print message background based on how many rows of welcome message there are
 
            g.setColor(black);
            g.drawRect(welcomeX-6, welcomeY-welcomeBackgroundY-1, welcomeBackgroundX+1, ((welcomeBackgroundY+1)*welcomeMessage.length)+1); //message border with the same idea as above
           
            //print the welcome message
            g.drawString(welcomeMessage[0], welcomeX, welcomeY);
            g.drawString(welcomeMessage[1], welcomeX, welcomeY+20);
            g.drawString(welcomeMessage[2], welcomeX, welcomeY+40);
            g.drawString(welcomeMessage[3], welcomeX, welcomeY+60);
            g.drawString(welcomeMessage[4], welcomeX, welcomeY+80);
            g.drawString(welcomeMessage[5], welcomeX, welcomeY+100);            
        }
        
        //--> ZOMBIE GAME
        if(zombieGame){//only run this code if we are playing the zombie game
        	g.setColor(red);
        	g.fillRect(0, 0, WINDOWWIDTH, WINDOWHEIGHT);//cover the entire screen in red
        	g.setColor(white);
        	g.drawString(zombFightMessage, 300, 500);//print instructions
        	
        }
        
       
    }//end of graphics()
   
    
    //actionPerformed method - a method that runs every time an action is performed (key press, click, etc) and the action is stored in ActionEvent e
    public void actionPerformed(ActionEvent e){
       
        if(e.getSource() == start){//if start button is pressed
            startPressed();//run startPressed method
        }
       
        //--> MOVEMENT CONDITIONS
        if(e.getSource() == up){
            if(playY != 0){//check that the player isn't going to go out of bounds
                moves++;//increment move count
                playY--;//move player
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
        
        //all of the movement selection is the same
        
        //--> IF MENU BUTTONS ARE PRESSED
        if(e.getSource() == win){
            endGame(true);//run the endGame method with true, meaning the game counts it as a win
        }
       
        if(e.getSource() == giveUp){
            endGame(false);//run the endGame method with false, meaning the game counts it as a loss
        }
       
        if(e.getSource() == cheat) {
            cheatMessage = "Desmond's current location: [" + desX + ", " + desY + "]";//Display the cheat method with desmond's location
        }
       
        //--> NPC MOVEMENT
        if(e.getSource() == up || e.getSource() == down || e.getSource() == left || e.getSource() == right){//if the player moves:
            gridStatus[desX][desY] = 0; //reset gridStatus array location for Desmond
            desWalk(followStatus); //desmond will move randomly if followStatus is false and follow the player if followStatus is true
           
            for(int i = 0; i < ROW; i++){//iterate through array
                for(int j = 0; j < COL; j++){
                    if(gridStatus[i][j] == 2){//reset all grids marked to have a zombie to be 0
                        gridStatus[i][j] = 0;
                    }
                }
            }
           
           
            for(int i = 0; i < ZOMBNUM; i++){//iterate through all zombies
                zombWalk(i);//zombie will move randomly
            }
           
        }
       
        
        //--> DIFFICULTY BUTTONS
        if(e.getSource() == easy) {
            desVisible = true;//easy mode - desmond and zombies are visible
            zombVisible = true;
        }
       
        if(e.getSource() == medium) {
            desVisible = false;//medium mode - desmond is invisible and zombies are visible
            zombVisible = true;
        }
       
        if(e.getSource() == hard) {
            desVisible = false;//hard mode - both are invisible
            zombVisible = false;
        }
       
        
        //--> INTERACTIONS
        if(desX == playX && desY == playY){//if desmond and player are on the same tile
            desVisible = true;//we can see desmond
            objectiveMessage = "RETURN DESMOND TO HOME (1, 1)";//update objective message
            followStatus = true;//desmond will now follow the player
        }
       
        if(gridStatus[playX][playY] == 2){//if player is on a tile with a zombie
            objectiveMessage = "A ZOMBIE ATE YOU!";//update message
            
            //moves the player out of their spot with the zombie and makes sure they don't go out of bounds or to another zombie
            if(playX != 0 && gridStatus[playX-1][playY] != 2){
                playX--;
                if(followStatus){//if desmond is following the plyer, make sure they follow this movement too
                	desX--;
                }
            }else if(gridStatus[playX+1][playY] != 2){
                playX++;
                if(followStatus){
                	desX++;
                }
            }else if(playY != 0 && gridStatus[playX][playY-1] != 2){
                playY--;
                if(followStatus){
                	desY--;
                }
            }else{
                playY++;
                if(followStatus){
                	desY++;
                }
            }
            
            zombieFight();//run zombieFight method which starts the minigame

//            if(lives == 0){
//            	objectiveMessage = objectiveMessage + " YOU DIED";
//                endGame(false);
//            }else{
//            	objectiveMessage = objectiveMessage + " YOU LOST A LIFE";
//            }
            
        }
        
        //--> DISTANCE MENU
        //calculate distance
        int distance = Math.abs(desX-playX) + Math.abs(desY-playY);
       
        if(distance == 0){//display different messages based on the distance
            distanceMessage = "You got Desmond!";
        }else if((distance <= 2)){
            distanceMessage = "Very hot! (" + distance + " tiles away)";
        }else if(distance <= 4){
            distanceMessage = "Getting warmer! (" + distance + " tiles away)";
        }else{
            distanceMessage = "Pretty cold! (" + distance + " tiles away)";
        }
        
        //--> WIN
        if(followStatus && playX == 0 && playY == 0 && e.getSource() != cont){//if desmond is following the player and they reach home
            objectiveMessage = "YOU WON IN " + moves + " MOVES, PRESS START TO PLAY AGAIN";//update message
            endGame(true);//we win
        }
        
        //--> CONTINUE
        if(e.getSource() == cont){//if player clicks the continue button
            coverX = highscoreX+menuX-10;//cover everything but the leaderboard
            coverY = WINDOWHEIGHT;
            showWelcome = true;//show the welcome message
            nameInput.setVisible(true);//show the name input and start button, hide continue button
            start.setVisible(true);
            cont.setVisible(false);
        }
        
        //--> TIME CALCULATIONS
        stopTime = LocalTime.now();//set current time to stop time
        long timePassed = startTime.until(stopTime, ChronoUnit.SECONDS);//find the time passed from when we started this game to now
        long mins = 0, secs = timePassed;
       
        //convert time passed into minutes and seconds
        if(timePassed > 60){
            while(secs > 60){
                secs = secs - 60;
                mins++;
            }
        }
       
        displayTime = twoDig.format(mins) + ":" + twoDig.format(secs);//format minutes and seconds and store them in String displayTime
        
        //--> ZOMBIE FIGHT
        if(e.getSource() == zombieFightStart){//if we press zombie fight start button
        	for(int i = 0; i < targets.length; i++){//add all targets, set them to visible
        		add(targets[i]);
        		targets[i].setVisible(true);
        	}
        	zombieFightStart.setVisible(false); //hide fight button
        	zombFightMessage = "";//empty message
        }
        
        //--> TARGETS
        for(int i = 0; i < targets.length; i++){//iterate through all targets
        	if(e.getSource() == targets[i]){//if target is pressed
        		targets[i].setVisible(false);//make target invisible
        		targetPressed[i] = true;//update targetPressed
        	}
        }
        
        if(targetPressed[0] && targetPressed[1] && targetPressed[2] && targetPressed[3] && targetPressed[4]){//if all 5 targets are pressed
    		zombieGame = false;//end zombie game
    		//make all of the buttons visible again
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
            for(int i = 0; i < targetPressed.length; i++){//iterate through targetPressed and set all to false to prepare for next time we play the zombie minigame
            	targetPressed[i] = false;
            }
        }
        
        repaint();//repaints the entire board after all of our updates

    }//end of paint method
    
    //--> DICE ROLL METHOD
    public static int roll(int min, int max){
        return (int)Math.round((Math.random()*(max-min)) + min);//math equation to get random number between min and max
    }
   
    //--> DESMOND MOVEMENT METHOD
    public void desWalk(boolean follow){
        boolean validCommand = false;//set valid command to false
       
        if(follow){//if desmond is supposed to follow the player, set his coordinates to the player's coordinates and exit the method
            desX = playX;
            desY = playY;
            return;
        }
        do{//do-while loop that will run until valid command is true, meaning desmond will have to move
            int direction = roll(1, 6);//choose a random direction, 1-4 being up, down, left, right, and 5-6 being stand still
            switch(direction){
                case 1:
                    if(desY != COL-1 && gridStatus[desX][desY+1] != 2){//if desmond isnt on the rightmost column and if his location doesnt have a zombie
                        desY++;//moves desmond
                        validCommand = true;//lets us break the loop
                    }else{
                        validCommand = false;//if desmond wasn't able to move, let the while loop run again
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
                    
                //cases 1-4 are basically the same
                case 5:
                    validCommand = true;//desmond just stands still
                    break;
                case 6:
                    validCommand = true;
                    break;
                default:
                    validCommand = false;
            }
        }while(!validCommand);
        
        gridStatus[desX][desY] = 1;//set the gridstatus to 1 for future interactions
 
    }//end of desWalk method
   
    //--> ZOMBIE MOVEMENT METHOD
    public void zombWalk(int zombnum){//parameter of zombnum as this method must be called multiple times, zombnum specifies the "index" of the zombie we are moving
        boolean validCommand;//same purpose as desWalk method
       
        do{//same do-while loop as desWalk method
            int direction = roll(1, 6);//same idea as desWalk method
            switch(direction){
                case 1:
                    if(zombies[zombnum][1] != COL-1 && gridStatus[zombies[zombnum][0]][zombies[zombnum][1]+1] == 0){//if the zombie isnt walking out of bounds, and if it's location is an empty tile
                        zombies[zombnum][1]++;//move
                        validCommand = true;//break the loop
                    }else{//if the zombie was unable to move
                        validCommand = false;//stay in the loop
                    }
                    break;
                case 2:
                    if(zombies[zombnum][1] > 1 && gridStatus[zombies[zombnum][0]][zombies[zombnum][1]-1] == 0){
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
                    if(zombies[zombnum][0] > 1 && gridStatus[zombies[zombnum][0]-1][zombies[zombnum][1]] == 0){
                        zombies[zombnum][0]--;
                        validCommand = true;
                    }else{
                        validCommand = false;
                    }
                    break;
                //cases 1-4 are basically the same
                case 5:
                    validCommand = true;//zombie stays still
                    break;
                case 6:
                    validCommand = true;
                    break;
                default:
                    validCommand = false;
            }
        }while(!validCommand);
        gridStatus[zombies[zombnum][0]][zombies[zombnum][1]] = 2;//set grid status of zombie location to 2 for future selection
 
    }//end of zombWalk method
   
    //--> START PRESSED METHOD
    public void startPressed(){//runs when start button is pressed
    	
    	//--> RESETTING VARIABLES
    	//reset lives to 3
    	lives = 3;
        coverX = 0; coverY = 0;//make the cover "invisible"
        showWelcome = false;//hide the welcome message
       
        startTime = LocalTime.now();//set start time
        name = nameInput.getText();//set name to whatever is in the textField
        name = name.toUpperCase();//change name to uppercase
       
        nameInput.setText("");//empty the nameInput textField
        nameInput.setVisible(false);//hide nameInput
        start.setVisible(false);//hide start
 
        playX = 0;//reset player location, move count, and follow status
        playY = 0;
        moves = 0;
        followStatus = false;
 
        desX = roll(7, 9);//reroll desmond location (desmond can only spawn near the bottom right)
        desY = roll(7, 9);
       
        gridStatus[desX][desY] = 1;//update gridStatus with desmond location
       
        for(int i = 0; i < zombies.length; i++){//iterate through all zombies
            zombies[i][0] = roll(2,6);//reroll zombie locations (zombies can't spawn on top of you or desmond)
            zombies[i][1] = roll(2,6);
           
            gridStatus[zombies[i][0]][zombies[i][1]] = 2;//update gridStatus with zombies location
        }
       
        //ADD BUTTONS
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
       
        cont.setVisible(false);//hide continue button
        //show all 'gameplay' buttons
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
 
    }//end of startPressed method
   
    //--> END GAME METHOD
    public void endGame(boolean won){//boolean won will run different code based on if it's true or false
 
        if(won){//if we win
            //-- UPDATING LINKEDLISTS
            stopTime = LocalTime.now();//set stop time
            long timePassed = startTime.until(stopTime, ChronoUnit.SECONDS);//calculate time taken
           
        	//-- UPDATING LINKEDLISTS
            stopTime = LocalTime.now();//set stop time
            long timePassed = startTime.until(stopTime, ChronoUnit.SECONDS);//calculate time taken
            
            //add all scores to highscore LinkedLists
            highestNames.add(name);
            highscores.add(moves);
            highTimes.add(timePassed);
            highTimesNames.add(name);
            highFormattedTimes.add(displayTime);
        }
       
        //show continue button
        cont.setVisible(true);
        down.setVisible(false);
        right.setVisible(false);
        win.setVisible(false);
        cheat.setVisible(false);
        giveUp.setVisible(false);
        easy.setVisible(false);
        medium.setVisible(false);
        hard.setVisible(false);
       
    }//end of endGame method
   
    
    //--> SORT HIGHSCORES METHOD
    public void sortHighscores(){
    	//declare temporary score and name for swapping
        int tempScore;
        String tempName;
        boolean sorted = false;
        
        while(!sorted){//while it has not been sorted
            sorted = true; //if we get through the entire for loop below without triggering the if statement, that means our LinkedList is sorted and we can move on
            for(int i = 0; i < highscores.size()-1; i++){//iterate through highscores except for the last one
                if(highscores.get(i) > highscores.get(i+1)){//compare the highscore with its following one, if they are out of order,
                	
                	//--> SWAP
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
        
    }//end of sortHighscores method
   
    //--> SORT TIMES METHOD
    public void sortTimes(){
    	//temporary variables for swapping
        long tempScore;
        String tempFormattedScore;
        String tempName;
        boolean sorted = false;//same idea as above method
        while(!sorted){
            sorted = true;
            for(int i = 0; i < highTimes.size()-1; i++){//iterate through all times but the last one
                if(highTimes.get(i) > highTimes.get(i+1)){//compare the time with the next one, if they are out of order
                	
                	//--> SWAP
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
    }//end of sortTimes method
    
    //--ZOMBIE FIGHT METHOD
    public void zombieFight(){
    	zombieGame = true;//so that we will print zombie game section in the paint method
        zombFightMessage = "A zombie bit you... you must fight it now. Click it's weak points when they come up.";//update method
        zombieFightStart.setVisible(true);
    	add(zombieFightStart);//add start fight button
    	//make all gameplay buttons invisible
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
        repaint();//refresh screen




//        for(int i = 0; i < 5; i++){          
//          targets[i].setBounds(roll(0, WINDOWWIDTH), roll(0, WINDOWHEIGHT), 50, 50);
//        }
       
    }
   
}