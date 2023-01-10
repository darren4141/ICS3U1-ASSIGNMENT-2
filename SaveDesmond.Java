import java.io.*;
public class SaveDesmond {
    static final int ROW = 10;
    static final int COL = 10;
    static final String LETTERCORDS = "   A  B  C  D  E  F  G  H  I  J";
    static final String PLAYER = "O";
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

        boolean won = false;

        desX = SD.roll(0, 9);
        desY = SD.roll(0, 9);

        name = SD.inpString("What is your name?");

        while(!won){
            moves++;
            System.out.println("MOVES: " + moves + ", CURRENT LOCATION: (" + playX + ", " + playY + ")");
            String [][] grid = SD.fillArr(playX, playY);
            SD.printArr(grid);

            do{
                command = SD.inpString("Enter a command");
                SD.readCommand(command);
            }while(!validCommand);

            do{
                SD.desWalk();

            }while(!validCommand);
        }



    }

    public String[][] fillArr(int playX, int playY){
        String [][] grid = new String[ROW][COL];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                grid[i][j] = "[ ]";
                if((i == playY) && (j == playX)){
                    grid[i][j] = "["+PLAYER+"]";
                }else if((i == desY) && (j == desX)){
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
            if(playY != 9){
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

    public void desWalk(){
        int direction = SD.roll(1, 4);
        switch(direction){
            case 1:
                if(desY != 9){
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
                if(desX != 9){
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