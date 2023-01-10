import java.io.*;
import java.util.LinkedList;
public class SaveDesmond {
    static final int ROW = 10;
    static final int COL = 10;
    static final int NUMZOMBIES = 5;
    static final String LETTERCORDS = "   A  B  C  D  E  F  G  H  I  J";
    static String player = "O";
    static final String DESMOND = "D";
    static final String ZOMBIE = "Z";
    static int playX = 0;
    static int playY = 0;
    static int desX;
    static int desY;
    static int [][] zombies = new int [5][2];
    static boolean validCommand = true;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static SaveDesmond SD = new SaveDesmond();


    public static void main(String[]args) throws IOException{
        int moves = 0;
        String name;
        String command;
        String [][] grid;
        String status;
        LinkedList<String> searched = new LinkedList<String>();


        boolean found = false;
        boolean won = false;

        desX = SD.roll(0, 9);
        desY = SD.roll(0, 9);
        for(int i = 0; i < zombies.length; i++){
            zombies[i][0] = SD.roll(0,9);
            zombies[i][1] = SD.roll(0,9);

        }

        name = SD.inpString("What is your name?");

        while(!found){
            searched.add("(" + playX + ", " + playY + ")");
            if((Math.abs(desX-playX) + Math.abs(desY-playY) <= 2)){
                status = "Very hot!";
            }else if((Math.abs(desX-playX) + Math.abs(desY-playY) <= 4)){
                status = "Getting warmer!";
            }else{
                status = "Pretty cold!";
            }

            moves++;
            System.out.println("MOVES: " + moves + ", CURRENT LOCATION: (" + playX + ", " + playY + ")");
            System.out.println("Past moves:");
            for(int i = 0; i < searched.size(); i++){
                System.out.print(searched.get(i) + " ");
            }

            System.out.println("\nFind DESMOND!");
            System.out.println(status);
            grid = SD.fillArr(playX, playY, found, zombies);
            SD.printArr(grid);

            for(int i = 0; i < NUMZOMBIES; i++){
                do{
                    SD.zombWalk(grid, "[D]", i);
                }while(!validCommand);
            }

            do{
                SD.desWalk(grid, "[Z]");

            }while(!validCommand);

            for(int i = 0; i < NUMZOMBIES; i++){
                do{
                    SD.zombWalk(grid, "[D]", i);
                }while(!validCommand);
            }

            do{
                command = SD.inpString("Enter a command");
                SD.readCommand(command);
            }while(!validCommand);

            for(int i = 0; i < NUMZOMBIES; i++){
                if((playX == zombies[i][1]) && (playX == zombies[i][0])){
                    System.out.println("You died!");
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
            moves++;
            System.out.println("MOVES: " + moves + ", CURRENT LOCATION: (" + playX + ", " + playY + ")");
            System.out.println("Bring DESMOND to safety!");
            grid = SD.fillArr(playX, playY, found, zombies);
            SD.printArr(grid);

            for(int i = 0; i < NUMZOMBIES; i++){
                do{
                    SD.zombWalk(grid, "[D]", i);
                }while(!validCommand);
            }

            do{
                command = SD.inpString("Enter a command");
                SD.readCommand(command);
            }while(!validCommand);

            if((playX == 0) && (playY == 0)){
                System.out.println("You saved DESMOND!");
                won = true;
                grid = SD.fillArr(playX, playY, found, zombies);
                SD.printArr(grid);

            }
        }

        SD.endGame(true, moves);


    }

    public String[][] fillArr(int playX, int playY, boolean found, int[][]zombies){
        String [][] grid = new String[ROW][COL];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                grid[i][j] = "[ ]";
                if((i == playY) && (j == playX)){
                    grid[i][j] = "["+player+"]";
                }else if((i == desY) && (j == desX) && (!found)){
                    grid[i][j] = "["+DESMOND+"]";
                }

                for(int k = 0; k < zombies.length; k++){
                    if((i == zombies[k][1]) && (j == zombies[k][0])){
                        grid[i][j] = "["+ZOMBIE+"]";
                    }
                }

            }
        }

        return grid;
    }

    public void printArr(String[][]grid){

        System.out.println(LETTERCORDS);

        for(int i = 0; i < grid.length; i++){
            System.out.print(i + " ");
            for(int j = 0; j < grid[i].length; j++)
                System.out.print(grid[i][j]);

            System.out.print(" " + i);
            System.out.println();
        }

        System.out.println(LETTERCORDS);

    }

    public String inpString(String msg) throws IOException{

        System.out.println(msg);
        String input;
        input = br.readLine();
        return input;
    }

    public void readCommand(String command){
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

    }

    public int roll(int min, int max){
        return (int)Math.round((Math.random()*max) + min);
    }

    public void desWalk(String[][]grid, String avoid){
        int direction = SD.roll(1, 4);
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
            default:
                validCommand = false;
        }
        
    }

    public void zombWalk(String[][]grid, String avoid, int zombnum){
        int direction = SD.roll(1, 4);
        switch(direction){
            case 1:
                if(zombies[zombnum][1] != COL-1 && !grid[zombies[zombnum][0]][zombies[zombnum][1]+1].equals(avoid)){
                    zombies[zombnum][1]++;
                    validCommand = true;
                }else{
                    validCommand = false;
                }
                break;
            case 2:
                if(zombies[zombnum][1] != 0 && !grid[zombies[zombnum][0]][zombies[zombnum][1]-1].equals(avoid)){
                    zombies[zombnum][1]--;
                    validCommand = true;
                }else{
                    validCommand = false;
                }
                break;
            case 3:
                if(zombies[zombnum][0] != ROW-1 && !grid[zombies[zombnum][0]+1][zombies[zombnum][1]].equals(avoid)){
                    zombies[zombnum][0]++;
                    validCommand = true;
                }else{
                    validCommand = false;
                }
                break;
            case 4:
                if(zombies[zombnum][0] != 0 && !grid[zombies[zombnum][0]-1][zombies[zombnum][1]].equals(avoid)){
                    zombies[zombnum][0]--;
                    validCommand = true;
                }else{
                    validCommand = false;
                }
                break;
            default:
                validCommand = false;
        }
    }

    public void endGame(boolean won, int moves) throws IOException{
        if(won){
            System.out.println("You won! You took " + moves + " steps!");
        }if(!won){
            System.out.println("Better luck next time.");
        }
        main(null);
    }

}