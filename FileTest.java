import java.io.*;

public class FileTest {
	
	public static void main(String[]args) throws Exception{
		File highscores = new File("C:\\Users\\16473\\OneDrive\\Documents\\GitHub\\ICS3O1-ASSIGNMENT-2\\Highscores");
    	FileWriter fw = new FileWriter(highscores,true);
    	BufferedWriter bw = new BufferedWriter(fw);
		BufferedReader br= new BufferedReader(new FileReader(highscores));
		System.out.println(br.readLine());
		int i = 10;
		while(i-->0) {
			bw.write("Hello");
		}
		bw.close();
        br.close();
	}
}
