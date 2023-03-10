//====================================================================================================================================================================================================================================================================
//
//"Save Desmond" Program
//Darren Liu
//January 17th, 2023
//Java 4.6.1
//
//====================================================================================================================================================================================================================================================================
//	  
//Problem definition:	Find a lost child (Desmond) who moves randomly once per turn, and return them to home whilst avoiding zombies
//	  
//Input:            	Name (text box)
//	                	Button presses:
//	                  	Movement (up, down, left, right)
//	                  	Menu (win, cheat, give up)
//	                  	Difficulty settings (easy, medium, hard)
//	                  	Navigation buttons (continue, start)
//	                  	Buttons for zombie minigame
//	                      
//Processing:       	Calculate placements of various items such as game board, controls tab, leaderboard using static int variables which store dimensions
//	                  	Generate random numbers for:
//	                      	Placement of zombies
//	                      	Placement of Desmond
//	                      	Movement of zombies
//	                      	Movement of Desmond
//	                      	Placement of zombie minigame targets
//	                  	Selection used for:
//	                      	Conditionally drawing different things
//	                          	Draw red background, prompt text, etc when zombie game is triggered
//	                          	Cover game board and print welcome message when player restarts
//	                          	Check when player interacts with Desmond
//	                          	Check when player interacts with zombie
//	                          	If statements used to make sure no entities are able to exit the boundaries of the board
//	                          	If statements used to make sure zombies do not randomly step on Desmond and vice versa
//	                          	If statements used to perform actions when buttons are pressed
//	                  	Constantly read and update Desmond, player, and zombie locations as they change and update them on the board
//	                  	Sorting used to sort lowest amount of moves as well as shortest amount of time taken to win game
//	                  	Counter which stores the amount of moves taken which is stored in a LinkedList
//	                  	Constantly update time and calculate the time it takes for the player to win the game, information is stored in a LinkedList
//						Calculate highscore at the end of each game, using moves, time, and remaining lives
//	                  
//Output:           	Game board which displays player, zombies (conditionally), and Desmond (conditionally)
//	                  	Messages which prompt the player on what to do
//	                  	Buttons with text on them which tell the player what to do
//	                  	Sorted highscore table
//	                  	Legend to show the player what the colours represent
//						Changes the screen based on whether we are in welcome mode (menu), game mode (game board) or zombie fight mode (targets)
//
//	====================================================================================================================================================================================================================================================================
//
//List of variables:	ROW, COL - a constant int that stores the size of the game board
//	                  	zombnum - a random int that stores the number of zombies spawned, rerolled every time the game is started
//	                  	WINDOWWIDTH, WINDOWHEIGHT - a constant int that stores the height and width of the game window
//	                  	Any 'Color' type variable - a colour variable that stores RGB values used to make different things on the screen different colours
//	                  	followStatus - a boolean variable that tells the program whether or not Desmond should be following the player
//	                  	showWelcome - a boolean variable that tells the program whether or not it should be displaying the welcome message
//	                  	zombieGame - a boolean variable that tells the program whether or not it should be playing the zombie minigame
//						desmondEncounter - a boolean variable that tells the program whether or not it should be playing the desmond minigame
//	                  	desVisible - a boolean variable that determines if Desmond is visible or not
//	                  	zombVisible - a boolean variable that determines if the zombies are visible or not
//						heartSpawned - a boolean variable that indicated whether or not a heart has been spawned
//						desmondMinigamefailed - a boolean variable that stores whether or not the desmond minigame was failed
//	                  	lives - an int variables that stores the number of lives
//	                  	playX, playY, desX, desY - int variables that store the X and Y locations of the player & Desmond
//	                  	ZOMBIETIMELIM - a final int type variable that stores the time limit in seconds for the zombie fight minigame
//						heartSizeX, heartSizeY	- int variables that store the dimensions of the hearts					
//						moved - boolean variable that indicates whether or not the player has moved during a run of actionPerformed, used for selection (we may not want to run certain things if the player hasn't moved
//						canMove - a boolean variable that indicates whether or not the player can make a valid move
//						distance - an int variable that store the calculated distance between desmond and the player
//						modeSelected - boolean variable that indicates if the mode has been selected
//	                 	FORMATTING STATIC VARIABLES - these variables are here so that the location of entire sections (leaderboard, game pad, board, etc) can be changed easily just by changing the static variable
//	                      	playX, playY - int variables that store the location of the player
//							desX, desY - int variables that store the location of desmond, randomly generated at the start of each game
//							BOARDX, BOARDY - int variables that store the location of the top left corner of the game board
//	                      	PADX, PADY - int variables that store the location of the game pad and the buttons next to them
//	                      	LEGENDY - int variable that stores the Y location of the legend
//	                      	MENUX, MENUY, WELCOMEX, WELCOMEY - all the same idea as BOARDX and BOARDY
//	                      	coverX, coverY - this variable stores the dimensions of the grey cover, which is supposed to cover the entire screen at times
//							heartPowerupX, heartPowerupY - int variables that store the location of the heart powerup
//							HIGHSCOREX, HIGHSCOREY - int variables that store the location of the leaderboard
//	                      	heartPointsX, heartPointsY - int arrays that store calculated values for the coordinates of the heart based on the size and location. These need to be arrays because of the fillPolygon() method
//
//
//	                  	zombies - a two dimensional int array that stores the coordinates of the zombies. The first dimension (column array) represents the number of the zombie, the following row arrays only have a size of 2, meaning zombies[i][0] represents the x coordinate of zombie[i], and zombies[i][1] represents the y coordinate of zombie[i]
//	                  	searchedX, searchedY - two Integer LinkedLists that store th past moves of the player
//						gridStatus - a two dimensional int array that stores the "status" of each tile on the grid (does it have the desmond of a zombie, powerup, decaying path or none?) this array is used for selection, preventing zombies from randomly walking on other obstacles as well as for determining when a player hits a zombie, desmond, powerup, or decaying path
//	                  	targetPressed - a boolean array that stores that status of each target in the zombie fight minigame
//	                  	
//	                  	VARIABLES MEANT TO BE EDITED AND REPAINTED - java applet uses the paint method to output anything you see on the screen, and a repaint() method used to update the screen, meaning we can output String variables to display something, then change the contents of the variable, and repaint() which will update the screen. This idea is used for basically any text that changes in the program.
//	                  	The following variables are meant to be edited as the game progresses, making changing text
//	                      	moves - an int that stores the number of moves taken
//	                      	displayTime - a String that displays the current runtime of a game, it is a String as it uses decimal format to format the time nicely
//	                      	name - a String that stores the name of the player
//	                      	distanceMessage - a String that displays the distance the player is from Desmond
//	                      	objectiveMessage - a String that displays the current objective for the player (find desmond, return desmond home, etc)
//	                      	cheatMessage - a String that displays the current location of desmond if the player decides to press the cheat button
//	                      	zombFightMessage - a String explaining the rules of the zombie fight minigame
//	                      	welcomeMessage - a String array that stores the welcome message
//	                      	highestMovesNames, highscoreMoves - String and Integer LinkedLists that store the names and move count of players who won the game
//	                      	highTimesNames, highFormattedTimes, highTimes - a String, String, and Long LinkedList that stores the names, times and formatted times of players who won the game. highTimes and formattedTimes are seperate variables as highTimes is needed for sorting and formattedTimes is needed for clean display
//	                      	highscores, highnames - an Integer and String array which store the calculated highscores of players and their respective names
//	                  	targets - button array that stores the buttons displayed during zombie minigame
//	                  	start, cont - navigation buttons
//	                  	up, down, left, right - buttons for player movement
//	                  	easy, medium, hard - buttons for changing difficulty
//	                  	win, cheat, giveUp - buttons for cheat commands
//	                  	zombieFightStart - button for navigation to start the zombie minigame after the user is finished reading the zombie minigame instructions
//						desmondGameStart - button for navigation to start the desmond minigame after the user is finished reading the desmond minigame instructions
//	                  	nameInput - a textField that inputs the name of the user
//						LocalTime type variables:
//							startTime - stores time when player starts game, resetted every time a new game is started
//							stopTime - stores times when player wins game or when screen is updated
//							startZombieTime - stores the time when the zombie game is started, resetted every time a new minigame is started
//							stopZombieTime - stores the time when the zombie game is updated and when it is ended
//						Font type variables: different fonts with various sizes and options (bold, italics, default) which can be used for different situations
//						timePassed - a long type variable that stores the amount of seconds passed between startTime and stopTime
//						mins, secs - long type variables that stores the amount of seconds and minutes passed between startTime and stopTime
//						validCommand - a boolean variable that indicates whether or not a movement input from desmond or a zombie went through or not, if invalid it will run again
//
//	====================================================================================================================================================================================================================================================================




//import statments
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
 
//start of class
public class SaveDesmondApplet extends Applet implements ActionListener{
   
   
    //ALL VARIABLES EXPLAINED IN HEADER BLOCK
    static DecimalFormat twoDig = new DecimalFormat("00");
    
    //-->TIME VARIABLES
    static LocalTime startTime;
    static LocalTime stopTime;
    static LocalTime startZombieTime;
    static LocalTime stopZombieTime;
   
    //--> SIZE & LOCATION CONSTANTS
    static final int ROW = 17;
    static final int COL = 17;
    static final int ZOMBIETIMELIM = 5;
    static final int WINDOWWIDTH = 1900;
    static final int WINDOWHEIGHT = 900;
    static final int BOARDX = 30;
    static final int BOARDY = 30;
    static final int PADX = 100;
    static final int PADY = (COL*30) + 140;
    static final int LEGENDY = (30*(COL-4));
    static final int MENUX = (30*ROW)+20+BOARDX;
    static final int MENUY = 20+BOARDY;
    static final int HIGHSCOREX = 600;
    static final int HIGHSCOREY = 40;
    static final int WELCOMEX = (WINDOWWIDTH/2)-900;
    static final int WELCOMEY = (WINDOWHEIGHT/2)-100;
    static final int WELCOMEBACKGROUNDX = 1800;
    static final int WELCOMEBACKGROUNDY = 40;
    
    //--> COLOURS
    static Color black = new Color(0,0,0);
    static Color white = new Color(255,255,255);
    static Color red = new Color(255,0,0);
    static Color blue = new Color(3, 102, 252);
    static Color lightGreen = new Color(122, 230, 76);
    static Color grey = new Color(211,211,211);
    static Color lightGrey = new Color(220,220,220);
    static Color darkGrey = new Color(105,105,105);
    static Color orange = new Color(252, 132, 3);
    static Color pink = new Color(245, 56, 226);
    static Color aqua = new Color(52, 235, 219);
    
    //---> BOOLEANS
    static boolean followStatus = false;
    static boolean showWelcome = true;
    static boolean zombieGame = false;
    static boolean desmondEncounter = false;
    static boolean desVisible;
    static boolean zombVisible;
    static boolean heartSpawned = false;
    static boolean desmondMinigamefailed = false;
    static boolean modeSelected = false;
    
    //--> IMPORTANT NUMBERS
    static int zombNum;
    static int lives = 3;
    static int moves;

    //--> MOVING OBJECT LOCATIONS
    static int playX = 0;
    static int playY = 0;
    static int desX;
    static int desY;
    static int heartPowerupX;
    static int heartPowerupY;
    static int coverX = WINDOWWIDTH;
    static int coverY = WINDOWHEIGHT;
    static int [][] zombies = new int [18][2]; //zombies[i][0] = x zombies[i][1] = y
    static LinkedList<Integer> searchedX = new LinkedList<Integer>();
    static LinkedList<Integer> searchedY = new LinkedList<Integer>();

    //--> MISC
    static int [][] gridStatus = new int[ROW][COL]; //GRID STATUS
    //0 = EMPTY
    //1 = HAS DESMOND
    //2 = HAS ZOMBIE
    //3 = HAS UNWALKABLE PATH
    //4 = HAS HEART POWERUP
    static boolean [] targetPressed = new boolean[5];
   
    //--> STRING MESSAGES TO BE REPAINTED
    static String displayTime = "00:00";
    static String displayZombieTime = "";
    static String name = "SOMEONE";
    static String distanceMessage;
    static String objectiveMessage = "FIND DESMOND";
    static String cheatMessage = "";
    static String zombFightMessage = "A zombie bit you... you must fight it now. Click it's weak points when they come up.";
    static String desmondGameMessage = "desmond game message default";
    static String [] welcomeMessage = {"The year is 2050, a biological war has caused an infectious disease to infest the land. You are a lone survivor of a zombie apocalypse.", 
    "The future of humanity, a sentient being named                   is in danger.                   contains all memories of every human who has ever lived.",
    "You must navigate a dark expanse infested with the diseased. You are the last hope to preserve the will of humanity.",
    "Your task is to retrieve                   and bring them back to your base to reboot the spirit of the people.",
    "Welcome to SAVE DESMOND!", 
    "In this game you will move your character using provided buttons to find and return Desmond home whilst avoiding zombies!", 
    "EASY: Desmond and zombies visible (most enjoyable)", 
    "MEDIUM: Zombies visible, Desmond invisible", 
    "HARD: Zombies and Desmond invisible", 
    "Please enter your NAME and press <START>", 
    "Highscores are calculated using moves, times, and remaining lives",
    "DESMOND"
    };
    
    //--> HIGHSCORE LINKEDLISTS
    static LinkedList<String> highestMovesNames = new LinkedList<String>();
    static LinkedList<String> highTimesNames = new LinkedList<String>();
    static LinkedList<String> highFormattedTimes = new LinkedList<String>();
    static LinkedList<Integer> highscoreMoves = new LinkedList<Integer>();
    static LinkedList<Long> highTimes = new LinkedList<Long>();
    static LinkedList<Integer> highscores = new LinkedList<Integer>();
    static LinkedList<String> highnames = new LinkedList<String>();
   
    //--> BUTTONS
    static Button [] targets = new Button[5];
    static Button start, cont;
    static Button up, down, left, right;
    static Button easy, medium, hard;
    static Button win, cheat, giveUp;
    static Button zombieFightStart, desmondGameStart;
    static TextField nameInput;
   
    //--> FONTS
    static Font title = new Font("Roboto", Font.BOLD, 30);
    static Font gameBoard = new Font("Roboto", Font.BOLD, 25);
    static Font header = new Font("Roboto", Font.BOLD, 15);
    static Font subheader = new Font("Arial", Font.ITALIC, 14);
    static Font small = new Font("Arial", Font.PLAIN, 11);
    static Font def = new Font("Arial", Font.PLAIN, 13);
    static Font bold = new Font("Arial", Font.BOLD, 13);
    static Font menuBold = new Font("Arial", Font.BOLD, 20);
    static Font nameBold = new Font("Arial", Font.BOLD, 15);
    static Font welcome = new Font("Arial", Font.BOLD, 30);
    static Font time = new Font("Roboto", Font.BOLD, 40);
 
    public void init(){
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This procedural method init() is a required method by Java Applets and it runs every time you click run
        //This method initializes objects like buttons and textboxes as well as resizing the window width and height
    	//Parameters: void
    	//Returns: void
    	//----------------------------------------------------------------------------------------------------------------
        resize(WINDOWWIDTH, WINDOWHEIGHT);//resize window size
        setBackground(grey);//set background colour
        
        //--> CREATE, PLACE, AND INITIALIZE BUTTONS
        start = new Button("START"); //declare a new button that has the text "START"
        start.addActionListener(this);//make it so that start can be clicked, actions on start will be sent to actionPerformed
        nameInput = new TextField("                ");//create a text field to input the name, spaces are for resizing
        nameInput.setText("");//empty the text field
        //vvvv THIS WAS HANDED IN UNCOMMENTED AND CAUSED THE WORST BUG vvvv
        //nameInput.addActionListener(this);//make it so that text can be taken from the textField
        add(nameInput);//add textfield to the window
        add(start);//add button to the window
       
        cont = new Button("CONTINUE");
        cont.addActionListener(this);
        cont.setBackground(red);//set background colour of button
        cont.setBounds(MENUX, MENUY+180, 250, 50);//set location and size of the button
       
        easy = new Button("Easy");
        easy.addActionListener(this);
        
        medium = new Button("Medium");
        medium.addActionListener(this);
        
        hard = new Button("Hard");
        hard.addActionListener(this);
        
        easy.setBounds((WINDOWWIDTH/2)-400, (WINDOWHEIGHT/2)+200, 200, 50);//location of the button are relative to the screen so that they are centered
        medium.setBounds(WINDOWWIDTH/2-170, (WINDOWHEIGHT/2)+200, 200, 50);
        hard.setBounds((WINDOWWIDTH/2)+60, (WINDOWHEIGHT/2)+200, 200, 50);
 
        easy.setBackground(lightGreen);
        medium.setBackground(orange);
        hard.setBackground(red);
        
        up = new Button("^");
        up.addActionListener(this);
        up.setBounds(PADX, PADY-30, 30, 30);
        up.setBackground(darkGrey);
       
        down = new Button("v");
        down.addActionListener(this);
        down.setBounds(PADX, PADY+30, 30, 30);
        down.setBackground(darkGrey);
 
        left = new Button("<");
        left.addActionListener(this);
        left.setBounds(PADX-30, PADY, 30, 30);
        left.setBackground(darkGrey);
        left.setForeground(black);
 
        right = new Button(">");
        right.addActionListener(this);
        right.setBounds(PADX+30, PADY, 30, 30);
        right.setBackground(darkGrey);
 
        win = new Button("WIN");
        win.addActionListener(this);
        win.setBounds(PADX + 100, PADY, 100, 30);
       
        cheat = new Button("CHEAT");
        cheat.addActionListener(this);
        cheat.setBounds(PADX + 210, PADY, 100, 30);
       
        giveUp = new Button("GIVE UP");
        giveUp.addActionListener(this);
        giveUp.setBounds(PADX + 320, PADY, 100, 30);
       
        zombieFightStart = new Button("FIGHT");
        zombieFightStart.addActionListener(this);
        zombieFightStart.setBounds((WINDOWWIDTH/2)-75, 350, 150, 50);//WINDOWWIDTH/2 means that the button will be centered
       
        desmondGameStart = new Button("GO!");
        desmondGameStart.addActionListener(this);
        desmondGameStart.setBounds((WINDOWWIDTH/2)-75, 350, 150, 50);
        //lines 276-338 is repetitive button initializing
       
        for(int i = 0; i < 5; i++){//initialize all 5 target buttons
            targets[i] = new Button();
            targets[i].setBackground(lightGreen);
            targets[i].setBounds(roll(0, WINDOWWIDTH-100), roll(0, WINDOWHEIGHT-100), 50, 50);//set their coordinates to random locations on the screen, the -100 is to make sure the target does not spawn out of the screen
            targets[i].addActionListener(this);
            targetPressed[i] = false;//initialize boolean array to all false
        }
       
    }//end of init()
   
    public void paint(Graphics g){
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This procedural method paint() is a required method by Java Applets
    	//PARAMETER: Graphics g
    	//This is where things are actually shown on the screen using Graphics object
        //The method paint draws the entire board, and contains conditions that make it so that under different
    	//circumstances it will draw different things, like a welcome message, board, or minigame
    	//Parameters: object Graphics g
    	//Returns: void
    	//----------------------------------------------------------------------------------------------------------------
    	
    	g.setFont(def);//set font to default font
    	g.setColor(black);
    	g.drawString("Made and trademarked by Darren Liu, Jan 2023", WINDOWWIDTH-280, WINDOWHEIGHT-5);
    	
    	
        if(!zombieGame && !showWelcome && !desmondEncounter && modeSelected){//don't print any of this if we are currently playing the zombie game, displaying the welcome message, or playing the desmond minigame
           
          //--> PRINT INITIAL BOARD AND BOXES
          //these rectangles are background "textboxes"
          g.setColor(lightGrey);
          g.fillRect(MENUX + HIGHSCOREX-5, MENUY-15+HIGHSCOREY, 600, 20*(highscoreMoves.size()+2));//leaderboard rectangle's size is based on amount of values in the LinkedList
          g.fillRect(BOARDX, (30*(COL+1)), (30*ROW), 20);//<name>'s game DISPLAY BOX
          g.fillRect(BOARDX, (30*(COL+1))+21, (20*ROW), 30);//time and lives DISPLAY BOX
          //fillRect method parameters: (X location of top left corner, Y location of top left corner, width, height)
          
          
          //these rectanges are the borders to the above textboxes, their dimensions start 1 pixel before the textboxes and extend 1 extra pixel horizontally and vertically
          g.setColor(black);
          g.drawRect(BOARDX-1, BOARDY-1, (30*ROW)+1, (30*COL)+1);
          g.drawRect(MENUX + HIGHSCOREX-6, MENUY-16+HIGHSCOREY, 601, 20*(highscoreMoves.size()+2)+1);//highscore display border
          
          g.drawRect(BOARDX-1, (30*(COL+1))-1, (30*ROW)+1, 21);//<name>'s game DISPLAY BORDER
          g.drawRect(BOARDX-1, (30*(COL+1))+20, (20*ROW)+1, 31);//time and lives DISPLAY BORDER
          //drawRect method parameters: (x location of top left corner, y location of top left corner, width, height)

          g.setFont(menuBold);
          g.drawString(displayTime, BOARDX+10, (30*(COL+1)+42));//output that displays the current time taken
          g.drawString("MOVES: " + moves, BOARDX+220, (30*(COL+1)+42));
           
          //--> OUTPUT HEART
          g.setColor(red);
          int heartSizeX = 20;//declare heart width as 20
          int heartSizeY = (int)(heartSizeX*0.75);//heart height is 3/4 of heart width
    
      	  for(int i = 0; i < lives; i++){//iterate through the number of lives
      		  int heartX = (int)(BOARDX+(30*ROW)-((1.25+(i*1.25))*heartSizeX))+10;//calculate the location of the heart
              int heartY = 30*(COL+1)+42;
              int [] x = {heartX, heartX-(heartSizeX/2), heartX+(heartSizeX/2)};//calculate the points of the heart using the heart location and size
              int [] y = {heartY, heartY-heartSizeY, heartY-heartSizeY};
              
              //draw the heart
              g.fillPolygon(x, y, 3);
              g.fillOval(heartX-(heartSizeX/2)+1, heartY-heartSizeY-(heartSizeY/4)-2, heartSizeX/2, heartSizeX/2);
              g.fillOval(heartX-1, heartY-heartSizeY-(heartSizeY/4)-2, heartSizeX/2, heartSizeX/2);
           }
           
           //--> PRINT GAME BOARD
           g.setColor(black);
            for(int i = 0; i < ROW; i++){//iterate through all rows
                for(int j = 0; j < COL; j++){//iterate through all columns
                    g.fillRect((30*j)+BOARDX,(30*i) + BOARDY, 30, 30);//draw a rectangle based on i and j coordinates
                    //TO CREATE CHECKER PATTERN
                    if(g.getColor() == lightGrey){//swap the colour from lightGrey to back
                        g.setColor(black);
                    }else if(g.getColor() == black){//and vice versa
                        g.setColor(lightGrey);
                    }
                }
            }
            
            //--> DISPLAY PAST MOVES
            g.setColor(red);
            g.setFont(gameBoard);
            if(searchedX.size() < 10){//if there have been less than 10 moves
            	for(int i = searchedX.size()-2; i >= 0; i--){//iterate through all the moves
            		g.drawString("X",30*(searchedX.get(i)+1)+7, 30*(searchedY.get(i)+2)-5);//draw an X on all of the decaying paths
            	}
            }else{//if there have been more than 10 moves
            	for(int i = searchedX.size()-2; i >= searchedX.size()-10; i--){//iterate through the last 10 moves
            		g.drawString("X",30*(searchedX.get(i)+1)+7, 30*(searchedY.get(i)+2)-5);//draw an X on all of the decaying paths
            	}
            }
            
            //--> DISPLAY CHARACTERS
            //fillOval method parameters: (x location, y location, width, height), to draw a circle, width == height
            g.setColor(red);
            g.fillOval((30*playX)+BOARDX+5,(30*playY)+BOARDY+5,20,20);//draw the player based on player location variables
           
            if(desVisible){//if player is allowed to see Desmond
                g.setColor(blue);
                g.fillOval((30*desX)+BOARDX+8,(30*desY)+BOARDY+8,14,14);//draw Desmond based on Desmond location variables
            }
     
            //--> DISPLAY ZOMBIES
            if(zombVisible) {//if player is allowed to see zombies
                g.setColor(lightGreen);
                for(int i = 0; i < zombNum; i++){//iterate through all zombies
                    g.fillOval((30*zombies[i][0])+BOARDX+3,(30*zombies[i][1])+BOARDY+3,24,24);//display each zombie based on their location
                }          
            }
     
            g.setFont(def);
            
            //--> DISPLAY POWERUPS
            if(heartSpawned){//if the heart power up has been spawned
            	g.setColor(red);

            	int [] x = {(heartPowerupX*30)+45, ((heartPowerupX*30)-(heartSizeX/2))+45, ((heartPowerupX*30)+(heartSizeX/2))+45};//calculate the points of the heart using the heart location and size
            	int [] y = {(heartPowerupY*30)+55, ((heartPowerupY*30)-heartSizeY)+55, ((heartPowerupY*30)-heartSizeY)+55};
            	 
            	//print the heart
            	g.fillPolygon(x, y, 3);
            	g.fillOval((heartPowerupX*30)-(heartSizeX/2)+46, (heartPowerupY*30)-heartSizeY-(heartSizeY/4)+53, heartSizeX/2, heartSizeX/2);
            	g.fillOval((heartPowerupX*30)+44, (heartPowerupY*30)-heartSizeY-(heartSizeY/4)+53, heartSizeX/2, heartSizeX/2);
            }
            
            if(followStatus){//if desmond is following the player
            	g.setFont(gameBoard);
            	g.setColor(pink);
            	g.drawString("H", BOARDX+5, BOARDY+25);//print out the home symbol on (1,1)
            }
            
            //--> FOR ERRORCHECKING PRINT GRIDSTATUS
//          g.setFont(def);
//          g.setColor(red);
//          for(int i = 0; i < ROW; i++){//iterate through rows
//              for(int j = 0; j < COL; j++){//iterate through columns
//                  g.drawString(Integer.toString(gridStatus[j][i]), (30*j)+BOARDX,(30*i)+BOARDY+30);//output gridstatus
//              }
//          }
            
            g.setFont(def);
            g.setColor(black);
            //--> DISPLAY COORDINATE PLANE
            for(int i = 1; i < ROW+1; i++) {//iterate through the number of rows, starting from 1
                g.drawString(Integer.toString(i), (30*(i))+BOARDX-20, BOARDY-5);//print horizontal legend
                g.drawString(Integer.toString(i), BOARDX-20, (30*(i))+BOARDY-10);//print vertical legend
            }
            
            //--> DISPLAY TITLE
            g.setFont(title);
            g.drawString(objectiveMessage, MENUX, MENUY);
            
            g.setFont(nameBold);
            g.drawString(name + "'s GAME", BOARDX+10, BOARDY+(30*COL)+16);//display underneath the game board
            
            //--> DISPLAY MENU
            g.setFont(menuBold);
            if(desVisible && zombVisible) {//if easy mode
            	g.drawString("CURRENT DIFFICULTY:",MENUX , MENUY + 30);
            	g.setColor(red);//set colour to red for the word easy
            	g.drawString("                                      EASY",MENUX , MENUY + 30);
            }else if(!desVisible && zombVisible) {
            	g.drawString("CURRENT DIFFICULTY:",MENUX , MENUY + 30);
            	g.setColor(red);//set colour to red for the word medium
            	g.drawString("                                      MEDIUM",MENUX , MENUY + 30);
            }else if(!desVisible && !zombVisible) {
            	g.drawString("CURRENT DIFFICULTY:",MENUX , MENUY + 30);
            	g.setColor(red);//set colour to red for the word hard
            	g.drawString("                                      HARD",MENUX , MENUY + 30);
            }
            g.setColor(black);
            g.drawString("Distance from Desmond: ", MENUX, MENUY+60);

            //--> TEMPERATURE MENU
            //display text in different colours based on how close player is to desmond
            if(distanceMessage.equals("You got Desmond!")){
            	g.setColor(lightGreen);
            }else if(distanceMessage.contains("Very hot!")){
            	g.setColor(red);
            }else if(distanceMessage.contains("Getting warmer!")){
            	g.setColor(orange);
            }else{
            	g.setColor(blue);
            }
            
            //empty space is for other text to go in so that there can be 2 different colours in one line
            g.drawString("                                        " + distanceMessage, MENUX, MENUY+60);
            
            g.setColor(black);
            g.drawString("Careful! You can't cross your own path", MENUX, MENUY+90);
            g.drawString("Step on the          to gain an extra life!", MENUX, MENUY+120);
            g.setColor(red);
            g.drawString("                   heart", MENUX, MENUY+120);
            
            g.drawString("                                                               <X>", MENUX, MENUY+90);
            
            if(heartSpawned){//if heart is spawned
                g.drawString("Heart location: (" + (heartPowerupX+1) + ", " + (heartPowerupY+1) + ")", MENUX, MENUY+150);//show user the heart location
            }else{
                g.drawString("Heart location: not spawned", MENUX, MENUY+150);
            }

            g.setColor(black);
            g.setFont(title);
            g.drawString(cheatMessage, MENUX, MENUY+210);
            
            g.setFont(header);
            g.drawString("HIGHSCORES:", MENUX + HIGHSCOREX, MENUY + HIGHSCOREY);
            //these variables are all meant to be updated periodically as the game is played
           
            //--> DISPLAY LEGEND
            g.setFont(def);
            g.setColor(lightGrey);
            g.fillRect(MENUX, MENUY + LEGENDY, 150, 90);
           
            g.setColor(red);
            g.fillRect(MENUX, MENUY + LEGENDY, 30, 30);
            g.setColor(blue);
            g.fillRect(MENUX, MENUY + LEGENDY+30, 30, 30);
            g.setColor(lightGreen);
            g.fillRect(MENUX, MENUY + LEGENDY+60, 30, 30);
     
            g.setColor(black);
            g.drawRect(MENUX-1, MENUY+LEGENDY-1, 151, 91);
           
            g.setFont(bold);
            g.setColor(black);
            g.drawString("YOU", MENUX + 40, MENUY + LEGENDY+20);
            g.drawString("DESMOND", MENUX + 40, MENUY + LEGENDY+50);
            g.drawString("ZOMBIE", MENUX + 40, MENUY + LEGENDY+80);
           
            //--> DISPLAY LEADERBOARD
            g.setFont(subheader);
            g.drawString("Overall Scores:", MENUX + HIGHSCOREX, MENUY + HIGHSCOREY + 20);
            g.drawString("Lowest Moves:", MENUX + HIGHSCOREX + 200, MENUY + HIGHSCOREY + 20);
            g.drawString("Lowest Times:", MENUX + HIGHSCOREX + 400, MENUY + HIGHSCOREY + 20);
     
            sorthighscoreMoves();//sort the highscoreMoves
            g.setFont(def);
            
            for(int i = 0; i < highscores.size(); i++){//iterate through all highscoreMoves
                g.drawString(highnames.get(i), MENUX + HIGHSCOREX, MENUY+((i+2)*20) + HIGHSCOREY);//print corresponding names
                g.drawString(highscores.get(i) + " ", MENUX + HIGHSCOREX + 150, MENUY+((i+2)*20) + HIGHSCOREY);//print lowest moves
            }
            
            sortHighscores();
            for(int i = 0; i < highscoreMoves.size(); i++){//iterate through all highscoreMoves
                g.drawString(highestMovesNames.get(i), MENUX + HIGHSCOREX + 200, MENUY+((i+2)*20)+HIGHSCOREY);//print corresponding names
                g.drawString(highscoreMoves.get(i) + " ", MENUX + HIGHSCOREX + 350, MENUY+((i+2)*20)+HIGHSCOREY);//print lowest moves
            }
           
            sortTimes();//sort the times
            for(int i = 0; i < highTimes.size(); i++){//iterate through all the times
                g.drawString(highTimesNames.get(i) + " ", MENUX + HIGHSCOREX + 400, MENUY+((i+2)*20)+HIGHSCOREY);//print corresponding names
                g.drawString(highFormattedTimes.get(i), MENUX + HIGHSCOREX + 550, MENUY+((i+2)*20)+HIGHSCOREY);//print formatted times
            }
           
            g.setColor(grey);
            g.fillRect(0, 0, coverX, coverY);//display cover rectangle, conditionally, this rectangle will cover the entire screen OR it will be invisible
        }

        //--> WELCOME BOX & MESSAGE
        if(showWelcome){//only run this code if we want to show the welcome message
            g.setColor(lightGrey);
            g.fillRect(WELCOMEX-5, WELCOMEY-WELCOMEBACKGROUNDY-145, WELCOMEBACKGROUNDX, ((WELCOMEBACKGROUNDY+1)*welcomeMessage.length));//print message background based on how many rows of welcome message there are
 
            g.setColor(black);
            g.drawRect(WELCOMEX-6, WELCOMEY-WELCOMEBACKGROUNDY-146, WELCOMEBACKGROUNDX+1, ((WELCOMEBACKGROUNDY+1)*welcomeMessage.length)+1); //message border with the same idea as above
           
            //--> STORYLINE
            g.setFont(menuBold);
            g.drawString(welcomeMessage[0], WELCOMEX, WELCOMEY-150);
            g.drawString(welcomeMessage[1], WELCOMEX, WELCOMEY-120);
            g.setColor(blue);
            g.drawString(welcomeMessage[11], WELCOMEX+455, WELCOMEY-120);//welcomeMessage[11] contains the word "DESMOND" meaning that it can print it at different colours
            g.drawString(welcomeMessage[11], WELCOMEX+685, WELCOMEY-120);
            g.drawString(welcomeMessage[11], WELCOMEX+225, WELCOMEY-60);
            g.setColor(black);
            g.drawString(welcomeMessage[2], WELCOMEX, WELCOMEY-90);
            g.drawString(welcomeMessage[3], WELCOMEX, WELCOMEY-60);

            //--> WELCOME MESSAGE
            g.setFont(welcome);
            g.setColor(blue);
            g.drawString(welcomeMessage[4], WELCOMEX, WELCOMEY);
            g.setColor(black);
            g.drawString(welcomeMessage[5], WELCOMEX, WELCOMEY+40);
            
            //ORIGINAL CODE THAT I HANDED IN --> INCORRECT COLOURING
//            g.drawString(welcomeMessage[6], WELCOMEX, WELCOMEY+80);
//            g.setColor(lightGreen);
//            g.drawString(welcomeMessage[7], WELCOMEX, WELCOMEY+120);
//            g.setColor(orange);
//            g.drawString(welcomeMessage[8], WELCOMEX, WELCOMEY+160);
//            g.setColor(red);
//            g.drawString(welcomeMessage[9], WELCOMEX, WELCOMEY+200);
//            g.setColor(black);
//            g.drawString(welcomeMessage[10], WELCOMEX, WELCOMEY+240); 
            
            //FIXED COLOURING{
            g.setColor(lightGreen);
            g.drawString(welcomeMessage[6], WELCOMEX, WELCOMEY+80);
            g.setColor(orange);
            g.drawString(welcomeMessage[7], WELCOMEX, WELCOMEY+120);
            g.setColor(red);
            g.drawString(welcomeMessage[8], WELCOMEX, WELCOMEY+160);
            g.setColor(black);
            g.drawString(welcomeMessage[9], WELCOMEX, WELCOMEY+200);
            g.drawString(welcomeMessage[10], WELCOMEX, WELCOMEY+240);      
          //}
        }
       
        //--> ZOMBIE GAME
        if(zombieGame){//only run this code if we are playing the zombie game
            g.setColor(red);
            g.fillRect(0, 0, WINDOWWIDTH, WINDOWHEIGHT);//cover the entire screen in red
            g.setColor(white);
            g.setFont(title);
            g.drawString(zombFightMessage, (WINDOWWIDTH/2)-850, 300);//print instructions
            g.setColor(lightGreen);
            
            if(!zombFightMessage.equals("")){//if there is no zombie fight method, do not print the "weak points" message
                g.drawString("weak points", (WINDOWWIDTH/2)-140, 300);//print alternate coloured text
            }
            g.setColor(white);
            g.setFont(time);
            g.drawString(displayZombieTime, (WINDOWWIDTH/2)-200, (WINDOWHEIGHT/2));
           
        }
       
        //--> DESMOND GAME
        if(desmondEncounter){//only run this code if we are playing the desmond game
            g.setColor(blue);
            g.fillRect(0, 0, WINDOWWIDTH, WINDOWHEIGHT);//cover the entire screen in red
            g.setColor(white);
            g.setFont(title);
            g.drawString(desmondGameMessage, (WINDOWWIDTH/2)-850, 300);//print instructions
            g.setColor(aqua);
            
            if(!desmondGameMessage.equals("")){//if desmond game message is not empty
                g.drawString("the buttons", (WINDOWWIDTH/2)+395, 300);//print alternate coloured text
            }
            g.setColor(white);
           
        }
        
        
    }//end of graphics()
    
    public void actionPerformed(ActionEvent e){
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This procedural method actionPerformed is a method that runs every time an action is performed (button press)
    	//PARAMETER: ActionEvent e
    	//and the action is stored in ActionEvent e
    	//This method allows for interactivity with the user
        //This method contains many if statements with e.getSource(), which means that the if statement will only run if
    	//a certain button is pressed. Other if statements have other boolean conditions which means that I just want some
    	//conditions to be checked/dealt with every time an action is performed
    	//Parameters: ActionEvent e
    	//Returns: void
    	//----------------------------------------------------------------------------------------------------------------
    	
    	boolean moved = false;//declare moved and set it to false
    	
    	if(lives <= 0){//if player has 0 lives
    		objectiveMessage = name + "you have 0 lives, you died!";//update message
    		endGame(false);//end the game without winning
    	}else if(playX != desX || playY != desY){//if player hasn't found desmond
    		objectiveMessage = "Find Desmond!";//update message
    	}
    	
    	if(followStatus){//if desmond is following the player
    		objectiveMessage = "Return DESMOND to home! <H>";//update message
    	}
    	
    	if(moves > 5 && !heartSpawned){//if heart hasn't spawned and it's been more than 5 turns
    		heartPowerupX = roll(ROW - 8, ROW - 1);//randomly generate the location of a heart
    		heartPowerupY = roll(COL - 8, COL - 1);
    		heartSpawned = true;//set heart spawned to true
    	}
    	
    	if(heartSpawned){//if heart is spawned
    		gridStatus[heartPowerupX][heartPowerupY] = 4;//update gridStatus
    		if(gridStatus[playX][playY] == 4){//if the player is standing on a heart
    			lives++;//give them a life
    			heartSpawned = false;//remove the heart
    			gridStatus[heartPowerupX][heartPowerupY] = 0;//reset the grid
    		}
    	}
    	
        if(e.getSource() == start){//if start button is pressed
            startPressed();//run startPressed method
        }
    	
        //--> MOVEMENT CONDITIONS
        if(e.getSource() == up){
        	boolean canMove = true;
            if(playY != 0){//check that the player isn't going to go out of bounds
            	if(gridStatus[playX][playY - 1] == 3){
            		canMove = false;
            	}
                
                if(canMove){
                	moves++;//increment move count
                	playY--;//move player
                	moved = true;
                }
            }
        }
       
        if(e.getSource() == down){
        	boolean canMove = true;
            if(playY != ROW-1){
            	if(gridStatus[playX][playY + 1] == 3){
            		canMove = false;
            	}
                
                if(canMove){
                    moves++;
                    playY++;
                    moved = true;
                }
            }
        }
       
        if(e.getSource() == left){
        	boolean canMove = true;
            if(playX != 0){
            	if(gridStatus[playX - 1][playY] == 3){
            		canMove = false;
            	}
                
                if(canMove){
                	moves++;
                	playX--;     
                	moved = true;
                }
            }
        }
       
        if(e.getSource() == right){
        	boolean canMove = true;
            if(playX != COL-1){
            	if(gridStatus[playX + 1][playY] == 3){
            		canMove = false;
            	}
                if(canMove){
                	moves++;
                	playX++;
                	moved = true;
                }
            }
        }
        //UP, DOWN, LEFT, RIGHT	are all the same
        
        
        if(searchedX.size() < 10){//if the player has moved less than 10 moves
        	for(int i = searchedX.size()-1; i >= 0; i--){//loop through all elements
        		if(gridStatus[searchedX.get(i)][searchedY.get(i)] == 0){//if the grid is empty, 
            		gridStatus[searchedX.get(i)][searchedY.get(i)] = 3;//turn it into decaying path, the reason there is an if statement is because I don't want the decaying path to override a more important token
        		}
        	}
        }else{//if the player has moved more than 10 moves
        	for(int i = searchedX.size()-1; i >= searchedX.size()-10; i--){//loop through last 10 elements
        		if(gridStatus[searchedX.get(i)][searchedY.get(i)] == 0){//same as above
            		gridStatus[searchedX.get(i)][searchedY.get(i)] = 3;
            		
        		}        	
        	}
        	//IMPORTANT
        	for(int i = 0; i < searchedX.size()-10; i++){//loop through the rest of the elements in the linkedList
        		if(gridStatus[searchedX.get(i)][searchedY.get(i)] == 3){//if they are decaying path
            		gridStatus[searchedX.get(i)][searchedY.get(i)] = 0;//reset them
        		}
        	}	
        }
               
        //--> IF MENU BUTTONS ARE PRESSED
        if(e.getSource() == win){
            objectiveMessage = name + ",YOU WON IN " + moves + " MOVES";//update message
            endGame(true);//run the endGame method with true, meaning the game counts it as a win
        }
       
        if(e.getSource() == giveUp){
            endGame(false);//run the endGame method with false, meaning the game counts it as a loss
        }
       
        if(e.getSource() == cheat) {
            cheatMessage = "Desmond's current location: [" + (desX+1) + ", " + (desY+1) + "]";//Display the cheat method with desmond's location
        }
       
        if(!cheatMessage.equals("") && e.getSource() != cheat){//hide the cheat message after 1 turn as it becomes irrelevant anyways
        	cheatMessage = "";
        }
        
        //--> NPC MOVEMENT
        if((e.getSource() == up || e.getSource() == down || e.getSource() == left || e.getSource() == right) && moved){//if the player moved, we utilized the moved boolean
            gridStatus[desX][desY] = 0; //reset gridStatus array location for Desmond
            searchedX.add(playX);
            searchedY.add(playY);
            desWalk(followStatus); //desmond will move randomly if followStatus is false and follow the player if followStatus is true
           
            for(int i = 0; i < ROW; i++){//iterate through array
                for(int j = 0; j < COL; j++){
                    if(gridStatus[i][j] == 2){//reset all grids marked to have a zombie to be 0
                        gridStatus[i][j] = 0;
                    }
                }
            }
           
            for(int i = 0; i < zombNum; i++){//iterate through all zombies
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
       
        if(e.getSource() == easy || e.getSource() == medium || e.getSource() == hard){//if any difficulty button is clicked
        	//hide them all
        	modeSelected = true;
        	easy.setVisible(false);
        	medium.setVisible(false);
        	hard.setVisible(false);
        	begin();
        	
        }
       
        //--> INTERACTIONS
        
        //--> PLAYER & DESMOND
        if(desX == playX && desY == playY && !followStatus){//if desmond and player are on the same tile
            //moves the player out of their spot with the zombie and makes sure they don't go out of bounds or to another zombie
            if(playX != 0 && gridStatus[playX-1][playY] != 2){//if player's destination isn't equal to a zombie
                playX--;//perform the update
            }else if(playX != ROW && gridStatus[playX+1][playY] != 2){
                playX++;
            }else if(playY != 0 && gridStatus[playX][playY-1] != 2){
                playY--;
            }else if (playY != COL && gridStatus[playX][playY+1] != 2){
                playY++;
            }else if(playX > 1 && gridStatus[playX-2][playY] != 2){//starts testing 2 spaces out just in case
                playX -= 2;
            }else if(playX < ROW-1 && gridStatus[playX+2][playY] != 2){
                playX += 2;
            }else if(playY > 1 && gridStatus[playX][playY-2] != 2){
                playY -= 2;
            }else if(playY < COL-1 && gridStatus[playX][playY+2] != 2){
                playY += 2;
            }
        	desmondEncounter();//start desmond minigame
        }
        
        //--> PLAYER & ZOMBIE
        if(gridStatus[playX][playY] == 2){//if player is on a tile with a zombie
            objectiveMessage = name + ", A ZOMBIE ATE YOU!";//update message
           
            //moves the player out of their spot with the zombie and makes sure they don't go out of bounds or to another zombie
            if(playX != 0 && gridStatus[playX-1][playY] != 2){//if player isn't stepping on a zombie
                playX--;
                if(followStatus){//if desmond is following the player, make sure they follow this movement too
                	desX--;
                }
            }else if(playX != ROW && gridStatus[playX+1][playY] != 2){
                playX++;
                if(followStatus){
                	desX++;
                }
            }else if(playY != 0 && gridStatus[playX][playY-1] != 2){
                playY--;
                if(followStatus){
                	desY--;
                }
            }else if (playY != COL && gridStatus[playX][playY+1] != 2){
                playY++;
                if(followStatus){
                	desY++;
                }
            }else if(playX > 1 && gridStatus[playX-2][playY] != 2){
                playX -= 2;
                if(followStatus){//if desmond is following the plyer, make sure they follow this movement too
                	desX -= 2;
                }
            }else if(playX < ROW-1 && gridStatus[playX+2][playY] != 2){
                playX += 2;
                if(followStatus){//if desmond is following the plyer, make sure they follow this movement too
                	desX += 2;
                }
            }else if(playY > 1 && gridStatus[playX][playY-2] != 2){
                playY -= 2;
                if(followStatus){//if desmond is following the plyer, make sure they follow this movement too
                	desY -= 2;
                }
            }else if(playY < COL-1 && gridStatus[playX][playY+2] != 2){
                playY += 2;
                if(followStatus){//if desmond is following the plyer, make sure they follow this movement too
                	desY += 2;
                }
            }
            	 
            zombieFight();//run zombieFight method which starts the minigame
           
        }
       
        //--> DISTANCE MENU
        //calculate distance
        int distance = Math.abs(desX-playX) + Math.abs(desY-playY);
       
        if(distance == 0){//display different messages based on the distance
            distanceMessage = "You got Desmond!";
        }else if((distance <= 3)){
            distanceMessage = "Very hot! (" + distance + " tiles away)";
        }else if(distance <= 7){
            distanceMessage = "Getting warmer! (" + distance + " tiles away)";
        }else{
            distanceMessage = "Pretty cold! (" + distance + " tiles away)";
        }
       
        //--> WIN
        if(followStatus && playX == 0 && playY == 0 && e.getSource() != cont){//if desmond is following the player and they reach home
        	if(e.getSource() == up || e.getSource() == down || e.getSource() == left || e.getSource() == right){//only run this once since this if statement is on ActionPerformed
                objectiveMessage = name + ", YOU WON IN " + moves + " MOVES";//update message
                endGame(true);//we win        		
        	}
        }
       
        //--> CONTINUE
        if(e.getSource() == cont){//if player clicks the continue button
            coverX = HIGHSCOREX+MENUX-10;//cover everything but the leaderboard
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
            	targets[i].setBackground(lightGreen);
            	targets[i].setLabel("");
                add(targets[i]);
                targets[i].setVisible(true);
            }
            zombieFightStart.setVisible(false); //hide fight button
            zombFightMessage = "";//empty message
            startZombieTime = LocalTime.now();
        	stopZombieTime = LocalTime.now();
            displayZombieTime = "Time left: " + ZOMBIETIMELIM + " seconds";
        }
       
        //--> TARGETS PRESSED
        for(int i = 0; i < targets.length; i++){//iterate through all targets
            if(e.getSource() == targets[i]){//if target is pressed
                targets[i].setVisible(false);//make target invisible
                targetPressed[i] = true;//update targetPressed
                
                //-->CHECKING FOR DESMOND MINIGAME FAIL
                for(int j = i; j >= 0; j--){//if we update targetPressed
                	if(!targetPressed[j]){//check that all previous targets are pressed, if not
                		desmondMinigamefailed = true;//minigame failed
                	}
                }
                for(int j = 0; j < targets.length; j++){//iterate through all targets
                	targets[j].setBounds(roll(0, WINDOWWIDTH-100), roll(0, WINDOWHEIGHT-100), 50, 50);//set their coordinates to random locations on the screen
                }
            }
        }
       
        //--> DISPLAY ZOMBIE TIME
        if(zombieGame && zombFightMessage.equals("")){//if we are playing the zombie game and start has been pressed
        	stopZombieTime = LocalTime.now();
            displayZombieTime = "Time left: " + (ZOMBIETIMELIM-startZombieTime.until(stopZombieTime, ChronoUnit.SECONDS)) + " seconds";//find the time passed from when we started this game to now
        }
        
        //--> END ZOMBIE GAME CONDITIONS
    	if(zombieGame){//if we are currently playing the zombie game
    		stopZombieTime = LocalTime.now();
    		if(startZombieTime.until(stopZombieTime, ChronoUnit.SECONDS) > ZOMBIETIMELIM) {//if the time passed is greater than the time limit
    			takeZombieDamage(true);//end the zombie fight and take damage
    		}else if((targetPressed[0] && targetPressed[1] && targetPressed[2] && targetPressed[3] && targetPressed[4])){//if all 5 targets are pressed
	        	displayZombieTime = "";//empty display zombie time
	        	if(startZombieTime.until(stopZombieTime, ChronoUnit.SECONDS) > ZOMBIETIMELIM){//if the time they took is greater than the time limit
	        		takeZombieDamage(true);//end zombie fight and take damage
	        	}else {
	        		takeZombieDamage(false);//end zombie fight and take no damage
	        	}
	        }
    	}
    	
    	//--> DESMOND MINIGAME START
        if(e.getSource() == desmondGameStart){//if we press desmond fight start button
            for(int i = 0; i < targets.length; i++){//add all targets, set them to visible
            	targets[i].setBackground(aqua);//set target background colours
            	targets[i].setLabel(Integer.toString(i));//label targets with their number
                add(targets[i]);//add targets
                targets[i].setVisible(true);//make targetse visible
            }
            desmondGameStart.setVisible(false); //hide fight button
            desmondGameMessage = "";//empty message
        }
        
    	if(desmondEncounter){
    		 if((targetPressed[0] && targetPressed[1] && targetPressed[2] && targetPressed[3] && targetPressed[4])){//if all 5 targets are pressed
    			 endDesmondEncounter(desmondMinigamefailed);//end desmond game, decide whether or not you lose lives based on desmondMinigamefailed boolean
	        }
    	}
    	
        repaint();//repaints the entire board after all of our updates
    }//end of ActionListener method
   
    public static int roll(int min, int max){
    	//----[METHOD]----------------------------------------------------------------------------------------------------
        //This functional method roll generates a random number based on specified user minimum and maximum values
    	//PARAMETER: int min, int max --> desired minimum and maximum values from the user
    	//RETURN: the random number generated
    	//This method allows for more convenient generation of random numbers
    	//----------------------------------------------------------------------------------------------------------------
    	
        return (int)Math.round((Math.random()*(max-min)) + min);//math equation to get random number between min and max
    }
   
    public void desWalk(boolean follow){
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This procedural method desWalk is a method that is called whenever the player moves
    	//PARAMETER: boolean follow, a parameter that the method uses to dictate whether or not it should make desmond
    	//Returns: void
    	//follow the player or walk on his own
    	//----------------------------------------------------------------------------------------------------------------
    	
        boolean validCommand = false;//set valid command to false so that do-while loop will run until valid command is entered
       
        if(follow){//if desmond is supposed to follow the player, set his coordinates to the player's coordinates and exit the method
            desX = playX;
            desY = playY;
            return;
        }
        do{//do-while loop that will run until valid command is true, meaning desmond will have to move
            int direction = roll(1, 6);//choose a random direction, 1-4 being up, down, left, right, and 5-6 being stand still
            switch(direction){
                case 1:
                    if(desY != COL-1 && gridStatus[desX][desY+1] == 0){//if desmond isnt on the rightmost column and if his location doesnt have a zombie
                        desY++;//moves desmond
                        validCommand = true;//lets us break the loop
                    }else{
                        validCommand = false;//if desmond wasn't able to move, let the while loop run again
                    }
                    break;
                case 2:
                    if(desY > 0 && gridStatus[desX][desY-1] == 0){
                        desY--;
                        validCommand = true;
                    }else{
                        validCommand = false;
                    }
                    break;
                case 3:
                    if(desX != ROW-1 && gridStatus[desX+1][desY] == 0){
                        desX++;
                        validCommand = true;
                    }else{
                        validCommand = false;
                    }
                    break;
                case 4:
                    if(desX > 0 && gridStatus[desX-1][desY] == 0){
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
   
    public void zombWalk(int zombnum){//parameter of zombnum as this method must be called multiple times, zombnum specifies the "index" of the zombie we are moving
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This procedural method zombWalk is a method that is called whenever the player moves
    	//PARAMETER: int zombnum --> dictates which zombie the method is affecting
    	//Returns: void
    	//The method is meant to be run once for each zombie, in a loop, with zombnum incrementing
    	//----------------------------------------------------------------------------------------------------------------
    	
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
   
    public void startPressed(){//runs when start is pressed
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This procedural method startPressed is a method that is called whenever the user presses the start button
    	//The method is mainly just resetting variables, making sure that things that need to be visible are visible, especially the difficulty selection buttons
    	//Parameters: void
    	//Returns: void
    	//----------------------------------------------------------------------------------------------------------------
        add(easy);
        add(medium);
        add(hard);
        easy.setVisible(true);
        medium.setVisible(true);
        hard.setVisible(true);
        
        up.setVisible(false);
        down.setVisible(false);
        left.setVisible(false);
        right.setVisible(false);
        win.setVisible(false);
        cheat.setVisible(false);
        giveUp.setVisible(false);

        name = nameInput.getText();//set name to whatever is in the textField
        name = name.toUpperCase();//change name to uppercase
       
        nameInput.setText("");//empty the nameInput textField
        nameInput.setVisible(false);//hide nameInput
        start.setVisible(false);//hide start
 
        playX = 0;//reset player location, move count, and follow status
        playY = 0;
        moves = 0;
        followStatus = false;
        showWelcome = false;//hide the welcome message

        desX = roll(ROW-3, ROW-1);//reroll desmond location (desmond can only spawn near the bottom right)
        desY = roll(COL-3, COL-1);
        
        gridStatus[desX][desY] = 1;//update gridStatus with desmond location
       
        for(int i = 0; i < zombies.length; i++){//iterate through all zombies
            zombies[i][0] = roll(2,ROW-4);//reroll zombie locations (zombies can't spawn on top of you or desmond)
            zombies[i][1] = roll(2,ROW-4);
            
            gridStatus[zombies[i][0]][zombies[i][1]] = 2;//update gridStatus with zombies location
        }
       
        //CLEAR MOVE HISTORY
        searchedX.clear();
        searchedY.clear();
        zombNum = roll(14, 18);//randomly roll the number of zombies
        objectiveMessage = "SELECT A DIFFICULTY";//update message
    }
    
    public void begin(){//runs when difficulty is selected
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This procedural method begin is a method that is called whenever the user selects a difficulty
    	//The method is mainly just resetting variables, making sure that things that need to be visible are visible
    	//Parameters: void
    	//Returns: void
    	//----------------------------------------------------------------------------------------------------------------
        //--> RESETTING VARIABLES
        //reset lives to 3
        lives = 3;
        coverX = 0; coverY = 0;//make the cover "invisible"
        heartSpawned = false;//despawn heart
        
        for(int i = 0; i < gridStatus.length; i++){//iterate through entire gridStatus array 
        	for(int j = 0; j < gridStatus.length; j++){
        		gridStatus[i][j] = 0;//set everything to 0
        	}
        }
        
        startTime = LocalTime.now();//set start time
        
        //ADD BUTTONS
        add(up);
        add(down);
        add(left);
        add(right);
        add(win);
        add(cheat);
        add(giveUp);
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
        
        //hide difficulty buttons
        easy.setVisible(false);
        medium.setVisible(false);
        hard.setVisible(false);
 
    }//end of startPressed method
   
    public void endGame(boolean won){//boolean won will run different code based on if it's true or false
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This procedural method endGame is a method that is called whenever the game ends:
    	//		Player reaches home w/ Desmond
    	//		Player lives reaches 0
    	//		Player presses win or give up buttons
    	//PARAMETER: boolean won, a parameter that the method uses to dictate whether or not you won and therefore if it should 
    	//add your highscores
    	//Returns: void
    	//----------------------------------------------------------------------------------------------------------------
    	
        boolean updated;
        if(won){//if we win
            //-- UPDATING LINKEDLISTS
            stopTime = LocalTime.now();//set stop time
            long timePassed = startTime.until(stopTime, ChronoUnit.SECONDS);//calculate time taken
           
            //add all scores to highscore LinkedLists
            updated = false;
            
            for(int i = 0; i < highnames.size(); i++){//loop through highscore names
            	if(name.equals(highnames.get(i))){//if our name is already on the leaderboard
            		//update the elements with the name instead of adding new elements
                	highscores.set(i, calcHighscore(moves, timePassed, lives));
            		updated = true;//to prevent the code from adding the values after we updated them
            	}
            	if(name.equals(highTimesNames.get(i))) {
            		highFormattedTimes.set(i, displayTime);
            		highTimes.set(i, timePassed);
            	}
            	if(name.equals(highestMovesNames.get(i))) {
            		highscoreMoves.set(i, moves);
            	}
            }
            
            if(!updated){//if we haven't changed anything yet
            	highestMovesNames.add(name);
            	highscoreMoves.add(moves);
            	highTimes.add(timePassed);
            	highTimesNames.add(name);
            	highFormattedTimes.add(displayTime);
            	
            	//calls calcHighscore method, highscore calculation is explained at method
            	highscores.add(calcHighscore(moves, timePassed, lives));
            	highnames.add(name);       	
            }
            
        }
       
        //modeSelected = false;
        //show continue button
        cont.setVisible(true);
       
        //hide all 'gameplay' buttons
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
       
    }//end of endGame method
   
    public int calcHighscore(int moveCount, long timePassedSeconds, int remainingLives){
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This functional method calcHighscore is a method that calculates the highscore given various statistics
    	//PARAMETER: int moveCount, int remainingLives, long timePassedSeconds
    	//RETURN: int calcHighscore --> calculated highscore
    	//----------------------------------------------------------------------------------------------------------------
    	
    	//--> HIGHSCORE FORMULA - highscore is a combined score that reflects the player's performance in move count, time taken, and remaining lives
    	//100/moves and 600/timePassed means that the lower they are, the higher the score will  be
    	//(0.5*lives) means that the more remaining lives, the higher the score will be
    	int score;
    	if(moves != 0){
    		score = (int) (((100/moveCount) + (600/timePassedSeconds) + 1000)+(250*remainingLives));
    	}else{
    		score = (int) ((100 + (600/timePassedSeconds) + 1000)+(250*remainingLives));
    	}
    	
    	
    	return score;
    }
    
    public void sorthighscoreMoves(){//sort from smallest (i == 0) to largest (i == highscoreMoves.size())
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This procedural method sorthighscoreMoves is a method that sorts the lowest moves and their respective names with bubble sort
    	//Parameters: void
    	//Returns: void
    	//----------------------------------------------------------------------------------------------------------------
        //declare temporary score and name for swapping
        int tempScore;
        String tempName;
        boolean sorted = false;
        while(!sorted){//while it has not been sorted
            sorted = true; //if we get through the entire for loop below without triggering the if statement, that means our LinkedList is sorted and we can move on
            for(int i = 0; i < highscoreMoves.size()-1; i++){//iterate through highscoreMoves except for the last one
                if(highscoreMoves.get(i) > highscoreMoves.get(i+1)){//compare the highscore with its following one, if they are out of order,
                   
                    //--> SWAP
                    sorted = false;
                    tempScore = highscoreMoves.get(i);
                    tempName = highestMovesNames.get(i);
                   
                    highscoreMoves.set(i, highscoreMoves.get(i+1));
                    highestMovesNames.set(i, highestMovesNames.get(i+1));
                   
                    highscoreMoves.set(i+1, tempScore);
                    highestMovesNames.set(i+1, tempName);
                   
                }
            }
        }
       
    }//end of sorthighscoreMoves method
   
    public void sortTimes(){//sort from smallest (i == 0) to largest (i == highTimes.size())
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This procedural method sortTimes is a method that sorts the lowest moves and their respective names with bubble sort
    	//Parameters: void
    	//Returns: void
    	//----------------------------------------------------------------------------------------------------------------
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
    
    public void sortHighscores(){//sort from largest (i == 0) to smallest (i == highscores.size())
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This procedural method sortHighscores is a method that sorts the lowest moves and their respective names with bubble sort
    	//Parameters: void
    	//Returns: void
    	//----------------------------------------------------------------------------------------------------------------
        //declare temporary score and name for swapping
        int tempScore;
        String tempName;
        boolean sorted = false;
       
        while(!sorted){//while it has not been sorted
            sorted = true; //if we get through the entire for loop below without triggering the if statement, that means our LinkedList is sorted and we can move on
            for(int i = 0; i < highscores.size()-1; i++){//iterate through highscoreMoves except for the last one
                if(highscores.get(i) < highscores.get(i+1)){//compare the highscore with its following one, if they are out of order,
                   
                    //--> SWAP
                    sorted = false;
                    tempScore = highscores.get(i);
                    tempName = highnames.get(i);
                   
                    highscores.set(i, highscores.get(i+1));
                    highnames.set(i, highnames.get(i+1));
                   
                    highscores.set(i+1, tempScore);
                    highnames.set(i+1, tempName);
                   
                }
            }
        }
       
    }//end of sortTimes method
    
    public void desmondEncounter(){
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This procedural method desmondEncounter is a method that starts the desmond encounter minigame
    	//The function sets boolean zombieGame to true which will trigger the paint and actionPerformed method to
    	//play the target game instead of the default game
    	//Parameters: void
    	//Returns: void
    	//----------------------------------------------------------------------------------------------------------------
        desmondEncounter = true;//so that we will print zombie game section in the paint method
        desmondGameMessage = "You have encountered DESMOND. DESMOND would like to test your intelligence. Click                      in ascending order";//update method
        desmondMinigamefailed = false;
        add(desmondGameStart);//add start fight button
        //make all gameplay buttons invisible
        desmondGameStart.setVisible(true);
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
    }
    
    public void endDesmondEncounter(boolean loseLife){
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This procedural method endDesmondEncounter will run when the user loses the desmond minigame
    	//PARAMETER: loseLife --> method will remove a life if loseLife is true
    	//Returns: void
    	//----------------------------------------------------------------------------------------------------------------
        desmondEncounter = false;//end zombie game
        
        if(loseLife) {
        	objectiveMessage = name + ", you proved to be remarkably unintelligent and DESMOND took one of your lives!";
        	lives--;//decrease the number of lives      	
        }else {
        	objectiveMessage = "Good job " + name + ", You proved your worth!";
            desVisible = true;//we can see desmond
            objectiveMessage = "Return DESMOND to home! <H>";//update objective message
            followStatus = true;//desmond will now follow the player
        }
		
        //make all of the buttons visible again
        up.setVisible(true);
        down.setVisible(true);
        left.setVisible(true);
        right.setVisible(true);
        win.setVisible(true);
        cheat.setVisible(true);
        giveUp.setVisible(true);

        desmondGameStart.setVisible(false);

        for(int i = 0; i < targetPressed.length; i++){//iterate through targetPressed and set all to false to prepare for next time we play the zombie minigame
            targetPressed[i] = false;
            targets[i].setVisible(false);//make target invisible
        }
        
		if(lives <= 0) {//if the lives reaches 0
    		objectiveMessage = name + ", you have 0 lives, you died!";
    		endGame(false);//end the game, with a loss
		}
    }
    
    public void zombieFight(){
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This procedural method zombieFight is a method that starts the zombie target fight minigame
    	//The function sets boolean zombieGame to true which will trigger the paint and actionPerformed method to
    	//play the target game instead of the default game
    	//Parameters: void
    	//Returns: void
    	//----------------------------------------------------------------------------------------------------------------
        zombieGame = true;//so that we will print zombie game section in the paint method
        zombFightMessage = "A zombie bit you... you must fight it now. Click it's                       when they come up. Are you ready? You'll have " + ZOMBIETIMELIM + " seconds";//update method        zombieFightStart.setVisible(true);
        startZombieTime = LocalTime.now();
        stopZombieTime = LocalTime.now();
        displayZombieTime = "";
        add(zombieFightStart);//add start fight button
        //make all gameplay buttons invisible
        zombieFightStart.setVisible(true);
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
       
    }
    
    public void takeZombieDamage(boolean loseLife) {
        //----[METHOD]----------------------------------------------------------------------------------------------------
        //This procedural method takeZombieDamage will run when the user loses the zombie minigame
    	//PARAMETER: loseLife --> method will remove a life if loseLife is true
    	//Returns: void
    	//----------------------------------------------------------------------------------------------------------------
        zombieGame = false;//end zombie game
        
        if(loseLife) {
        	objectiveMessage = name + ", you took too long to kill the zombie and lost a life!";
        	lives--;//decrease the number of lives      	
        }else {
        	objectiveMessage = "You narrowly escaped, " + name + "!";
        }
		
        //make all of the buttons visible again
        up.setVisible(true);
        down.setVisible(true);
        left.setVisible(true);
        right.setVisible(true);
        win.setVisible(true);
        cheat.setVisible(true);
        giveUp.setVisible(true);
        zombieFightStart.setVisible(false);

        for(int i = 0; i < targetPressed.length; i++){//iterate through targetPressed and set all to false to prepare for next time we play the zombie minigame
            targetPressed[i] = false;
            targets[i].setVisible(false);//make target invisible
        }
        
		if(lives <= 0) {//if the lives reaches 0
    		objectiveMessage = name + ",you have 0 lives, you died!";
    		endGame(false);//end the game, with a loss
		}
    }

}//end of class
