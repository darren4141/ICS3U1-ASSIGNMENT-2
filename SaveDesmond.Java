import java.io.*;
public class SaveDesmond {
    static final int ROW = 10;
    static final int COL = 10;
    static final String LETTERCORDS = "   A  B  C  D  E  F  G  H  I  J";
    static String player = "O";
    static final String DESMOND = "D";
    static int playX = 0;
    static int playY = 0;
    static int desX;
    static int desY;
    static boolean validCommand = true;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static SaveDesmond SD = new SaveDesmond();


    public static void main(String[]args) throws IOException{
        int moves = 0;
        String name;
        String command;
        String [][] grid;
        String status;


        boolean found = false;
        boolean won = false;

        desX = SD.roll(0, 9);
        desY = SD.roll(0, 9);

        name = SD.inpString("What is your name?");

        while(!found){

            if((Math.abs(desX-playX) + Math.abs(desY-playY) <= 2)){
                status = "Very hot!";
            }else if((Math.abs(desX-playX) + Math.abs(desY-playY) <= 4)){
                status = "Getting warmer!";
            }else{
                status = "Pretty cold!";
            }

            moves++;
            System.out.println("MOVES: " + moves + ", CURRENT LOCATION: (" + playX + ", " + playY + ")");
            System.out.println("Find DESMOND!");
            System.out.println(status);
            grid = SD.fillArr(playX, playY, found);
            SD.printArr(grid);

            do{
                SD.desWalk();

            }while(!validCommand);

            do{
                command = SD.inpString("Enter a command");
                SD.readCommand(command);
            }while(!validCommand);

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
            grid = SD.fillArr(playX, playY, found);
            SD.printArr(grid);

            do{
                command = SD.inpString("Enter a command");
                SD.readCommand(command);
            }while(!validCommand);

            if((playX == 0) && (playY == 0)){
                System.out.println("You saved DESMOND!");
                won = true;
                grid = SD.fillArr(playX, playY, found);
                SD.printArr(grid);

            }
        }

        System.out.println("You took " + moves + " steps!");


    }

    public String[][] fillArr(int playX, int playY, boolean found){
        String [][] grid = new String[ROW][COL];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                grid[i][j] = "[ ]";
                if((i == playY) && (j == playX)){
                    grid[i][j] = "["+player+"]";
                }else if((i == desY) && (j == desX) && (!found)){
                    grid[i][j] = "["+DESMOND+"]";
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
            if(playX != ROW-1){
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

    public void desWalk(){
        int direction = SD.roll(1, 4);
        switch(direction){
            case 1:
                if(desY != COL-1){
                    desY++;
                    validCommand = true;
                }else{
                    validCommand = false;
                }
                break;
            case 2:
                if(desY != 0){
                    desY--;
                    validCommand = true;
                }else{
                    validCommand = false;
                }
                break;
            case 3:
                if(desX != ROW-1){
                    desX++;
                    validCommand = true;
                }else{
                    validCommand = false;
                }
                break;
            case 4:
                if(desX != 0){
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

}