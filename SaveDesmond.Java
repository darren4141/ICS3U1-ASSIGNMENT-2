import java.io.*;
import java.util.LinkedList;
public class SaveDesmond {
    static final int ROW = 10;
    static final int COL = 10;
    static final int NUMZOMBIES = 5;
    static final String LETTERCORDS = "   A  B  C  D  E  F  G  H  I  J";
    static String name;
    static String player;
    static String desmond;
    static String zombie;
    static int playX;
    static int playY;
    static int desX;
    static int desY;
    static int [][] zombies = new int [5][2];

    static boolean validCommand;
    static LinkedList<String> highscoreNames = new LinkedList<String>();
    static LinkedList<Integer> highscores = new LinkedList<Integer>();
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static SaveDesmond SD = new SaveDesmond();
    static File highscoresFile = new File("C:\\Users\\16473\\OneDrive\\Documents\\GitHub\\ICS3O1-ASSIGNMENT-2\\Highscores");
    static FileWriter fw;
    static BufferedWriter bw;
    static BufferedReader bfr;

    public static void main(String[]args) throws IOException{
        fw = new FileWriter(highscoresFile,true);
        bfr = new BufferedReader(new FileReader(highscoresFile));
        bw = new BufferedWriter(fw);
        player = "O";
        playX = 0;
        playY = 0;
        int moves = 0;
        int mode;
        name = "";
        String command;
        String[]menu = new String[COL];
        for(int i = 0; i < COL; i++) menu[i] = "";
        menu[0] = "MENU:";
        menu[2] = "Past moves: ";
        menu[6] = "Desmond location: (" + desX + ", " + desY + ")";
        menu[7] = "Cheat codes: <CHEAT> <QUIT> <WIN>";
        String [][] grid;
        LinkedList<String> searched = new LinkedList<String>();
 
 
        boolean found = false;
        boolean won = false;
 
        desX = SD.roll(3, 9);
        desY = SD.roll(3, 9);
        for(int i = 0; i < zombies.length; i++){
            zombies[i][0] = SD.roll(1,9);
            zombies[i][1] = SD.roll(1,9);
        }
 
        System.out.println("\n\n<SAVE DESMOND>");
       
        SD.sortHighscores();
       
        System.out.println("HIGHSCORES: ");
        System.out.println(".----------------------------.");
        for(int i = 0; i < highscores.size(); i++){
            System.out.printf("|%-20s |%-5s |%n", highscoreNames.get(i), highscores.get(i));
        }
        System.out.println("'----------------------------'");
 
        System.out.println("|Welcome! In this game you <O> will save Desmond <D>");
        System.out.println("|Make sure you don't hit any zombies <Z>");
        System.out.println("|Desmond and the zombies will move randomly in one direction each turn");
        System.out.println("|Move around using <W><A><S><D>, Stay in place using any other input. If you're having too much trouble, try using some cheat codes!");
        System.out.println("|Once you save Desmond, bring him to the upper left corner");
        System.out.println("|What mode would you like to play?\n|1. Easy (Zombies and Desmond are visible)\n|2. Medium (Desmond is invisible)\n|3. Hard (Desmond and Zombies are invisible)\n|4. Exit");
 
        mode = SD.readInt("Enter your choice:", 1, 4);
        if(mode == 1){
            zombie = "Z";
            desmond = "D";
        }else if(mode == 2){
            zombie = "Z";
            desmond = " ";
        }else if(mode == 3){
            zombie = " ";
            desmond = " ";
        }else if(mode == 4){
            System.out.println("Goodbye, come again!");
            bw.close();
            br.close();
            System.exit(0);
        }
 
 
        name = SD.inpString("What is your name?");
 
        while(!found){
        	
            searched.add("(" + playX + ", " + playY + ")");
            if((Math.abs(desX-playX) + Math.abs(desY-playY) <= 2)){
                menu[1] = "Distance from Desmond: Very hot!";
            }else if((Math.abs(desX-playX) + Math.abs(desY-playY) <= 4)){
                menu[1] = "Distance from Desmond: Getting warmer!";
            }else{
                menu[1] = "Distance from Desmond: Pretty cold!";
            }
 
            moves++;
            menu[3] = "MOVES: " + moves;
            menu[4] = "CURRENT LOCATION: (" + playX + ", " + playY + ")";
            menu[6] = "Desmond location: (" + desX + ", " + desY + ")";

            if(moves <= 5){
                menu[2] += searched.get(moves-1) + " ";
            }else{
                menu[2] = "Past moves: ";
                for(int i = moves-6; i < moves; i++){
                    menu[2] += searched.get(i) + " ";
                }
            }
 
            menu[5] = "Zombie locations: ";
 
            for(int i = 0; i < zombies.length; i++){
                menu[5] += "(" + zombies[i][0] + ", " + zombies[i][1] + ")";
            }

            System.out.println("\nFind DESMOND!");
            grid = SD.fillArr(desX, desY, playX, playY, found, zombies);
            SD.printArr(grid, menu);

            do{
                command = SD.inpString("Enter a command");
                SD.readCommand(command, moves);
            }while(!validCommand);
 
            do{
                SD.desWalk(grid, "["+zombie+"]");
            }while(!validCommand);

            grid = SD.fillArr(desX, desY, playX, playY, found, zombies);

            for(int i = 0; i < NUMZOMBIES; i++){
                SD.zombWalk(grid, i);
            }

            menu[6] = "Desmond location: (" + desX + ", " + desY + ")";
            
            grid = SD.fillArr(desX, desY, playX, playY, found, zombies);

            for(int i = 0; i < NUMZOMBIES; i++){
                if((playX == zombies[i][0]) && (playY == zombies[i][1])){
                    System.out.println("You died!");
                    grid = SD.fillArr(desX, desY, playX, playY, found, zombies);
                    SD.printArr(grid, menu);
                    SD.endGame(false, moves);
                }
            }
 
            if((desX == playX) && (desY == playY)){
                found = true;
                System.out.println("You saved Desmond! Bring him back to the upper left corner!");
            }
        }
 
        player = "@";
 
        while(!won){
            menu[1] = "You found Desmond!";
            moves++;
            grid = SD.fillArr(desX, desY, playX, playY, found, zombies);
            SD.printArr(grid, menu);
 
            for(int i = 0; i < NUMZOMBIES; i++){
                do{
                    SD.zombWalk(grid, i);
                }while(!validCommand);
            }
 
            do{
                command = SD.inpString("Enter a command");
                SD.readCommand(command, moves);
            }while(!validCommand);
 
            for(int i = 0; i < NUMZOMBIES; i++){
                if((playX == zombies[i][0]) && (playY == zombies[i][1])){
                    System.out.println("You died!");
                    grid = SD.fillArr(desX, desY, playX, playY, found, zombies);
                    SD.printArr(grid, menu);
                    SD.endGame(false, moves);
                }
            }
 
            if((playX == 0) && (playY == 0)){
                System.out.println("You saved DESMOND!");
                won = true;
                grid = SD.fillArr(desX, desY, playX, playY, found, zombies);
                SD.printArr(grid, menu);
            }
        }
 
        SD.endGame(true, moves);
 
    }
 
    public String[][] fillArr(int desX, int desY, int playX, int playY, boolean found, int[][]zombies){
        String [][] grid = new String[ROW][COL];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                grid[i][j] = "[ ]";
                if((i == playY) && (j == playX)){
                    grid[i][j] = "["+player+"]";
                } 
 
                for(int k = 0; k < zombies.length; k++){
                    if((i == zombies[k][1]) && (j == zombies[k][0])){
                        grid[i][j] = "["+zombie+"]";
                    }
                }

                if((i == desY) && (j == desX) && (!found)){
                    grid[i][j] = "["+desmond+"]";
                }
 
            }
        }
        return grid;
    }
 
    public void printArr(String[][]grid, String [] menu){
 
        System.out.println(LETTERCORDS);

        for(int i = 0; i < grid.length; i++){
            System.out.print(i + " ");
            for(int j = 0; j < grid[i].length; j++)
                System.out.print(grid[i][j]);
            System.out.print(" " + i + "\t" + menu[i]);
            System.out.println();
        }
        System.out.println(LETTERCORDS);
    }
 
    public int readInt(String msg, int min, int max) throws IOException{
        System.out.println(msg);
        int input;
       
        while(true){
            try{
                input = Integer.parseInt(br.readLine());
                if(input >= min && input <= max){
                    break;
                }else{
                    System.out.println("Enter a number beteween [" + min + ", "+ max +"]");
                }
            }catch(NumberFormatException e){
                System.out.println("Enter a number!");
                System.out.println(e.getMessage());
            }
        }
        return input;
    }
   
    public String inpString(String msg) throws IOException{
        System.out.println(msg);
        String input;
        input = br.readLine();
        return input;
    }
 
 
    public void readCommand(String command, int moves) throws IOException{
        if(command.equalsIgnoreCase("S")){
            if(playY != COL-1){
                playY++;
                validCommand = true;
            }else{
                validCommand = false;
                System.out.println("You can't go out of bounds!");
            }
 
        }
 
        if(command.equalsIgnoreCase("W")){
            if(playY != 0){
                playY--;
                validCommand = true;
            }else{
                validCommand = false;
                System.out.println("You can't go out of bounds!");
            }
        }
 
        if(command.equalsIgnoreCase("A")){
            if(playX != 0){
                playX--;
                validCommand = true;
            }else{
                validCommand = false;
                System.out.println("You can't go out of bounds!");
            }
        }
 
        if(command.equalsIgnoreCase("D")){
            if(playX != 9){
                playX++;
                validCommand = true;
            }else{
                validCommand = false;
                System.out.println("You can't go out of bounds!");
            }
        }
 
        if(command.equalsIgnoreCase("cheat")){
            System.out.println("Desmond's current location is (" + desX + ", " + desY + ")");
        }
 
        if(command.equalsIgnoreCase("win")){
            System.out.println("You saved Desmond!");
            SD.endGame(true, moves);
        }
 
        if(command.equalsIgnoreCase("quit")){
            System.out.println("You lost!");
            SD.endGame(false, moves);
        }
 
    }
 
    public int roll(int min, int max){
        return (int)Math.round((Math.random()*(max-min)) + min);
    }
 
    public void desWalk(String[][]grid, String avoid){
        int direction = SD.roll(1, 6);
        switch(direction){
            case 1:
                if(desY != COL-1 && !grid[desX][desY+1].equals(avoid)){
                    desY++;
                    validCommand = true;
                }else{
                    validCommand = false;
                }
                break;
            case 2:
                if(desY != 0 && !grid[desX][desY-1].equals(avoid)){
                    desY--;
                    validCommand = true;
                }else{
                    validCommand = false;
                }
                break;
            case 3:
                if(desX != ROW-1 && !grid[desX+1][desY].equals(avoid)){
                    desX++;
                    validCommand = true;
                }else{
                    validCommand = false;
                }
                break;
            case 4:
                if(desX != 0 && !grid[desX-1][desY].equals(avoid)){
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
       
    }
 
    public void zombWalk(String[][]grid, int zombnum){
        int direction = SD.roll(1, 6);
        switch(direction){
            case 1:
                if(zombies[zombnum][1] != COL-1){// && !grid[zombies[zombnum][0]][zombies[zombnum][1]+1].equals(avoid)
                    zombies[zombnum][1]++;
                	if(zombies[zombnum][0] == desX && zombies[zombnum][1] == desY){
                        zombies[zombnum][1]--;
                	}
                }
                break;
            case 2:
                if(zombies[zombnum][1] != 0){// && !grid[zombies[zombnum][0]][zombies[zombnum][1]-1].equals(avoid)
                    zombies[zombnum][1]--;
                	if(zombies[zombnum][0] == desX && zombies[zombnum][1] == desY){
                        zombies[zombnum][1]++;
                	}
                }
                System.out.println("Desmond: "+desX + ", " + desY + " Zombie before: " + zombies[zombnum][0] + ", " + zombies[zombnum][1]);
                break;
            case 3:
                if(zombies[zombnum][0] != ROW-1){// && !grid[zombies[zombnum][0]+1][zombies[zombnum][1]].equals(avoid)
                    zombies[zombnum][0]++;
                	if(zombies[zombnum][0] == desX && zombies[zombnum][1] == desY){
                        zombies[zombnum][0]--;
                	}
                }
                break;
            case 4:
                if(zombies[zombnum][0] != 0){// && !grid[zombies[zombnum][0]-1][zombies[zombnum][1]].equals(avoid)
                    zombies[zombnum][0]--;
                	if(zombies[zombnum][0] == desX && zombies[zombnum][1] == desY){
                        zombies[zombnum][0]++;
                	}
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
                    tempName = highscoreNames.get(i);
                   
                    highscores.set(i, highscores.get(i+1));
                    highscoreNames.set(i, highscoreNames.get(i+1));
                   
                    highscores.set(i+1, tempScore);
                    highscoreNames.set(i+1, tempName);
                }
            }          
        }
    }
   
    public void endGame(boolean won, int moves) throws IOException{
        if(won){
            System.out.println("You won! You took " + moves + " steps!");
            highscoreNames.add(name);
            highscores.add(moves);
            fw.write(name);

           
        }if(!won){
            System.out.println("Better luck next time.");
        }
        main(null);
    }
 
}