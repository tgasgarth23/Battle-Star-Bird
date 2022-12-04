import java.io.File;
import java.util.Scanner;
//code inspired by https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
public class fileReader {
    private Scanner x;

    public void open(){
        try {
            x = new Scanner(new File("score.txt"));//search for file called score.txt
        }
        catch (Exception e){
            System.out.println("No scores inputted");//if no score.txt file found, no historical scores are inputted
        }
    }
    public void read(){
        while(x.hasNext()){
            String nameToAdd = x.next();
            String score = x.next();
            String difficulty = x.next();
            int scoreToAdd = Integer.parseInt(score);
            int difficultyToAdd = Integer.parseInt(difficulty);
            World.scores.addScore(nameToAdd,scoreToAdd,difficultyToAdd);
        }
    }
    public void close(){
        x.close();
    }
}
