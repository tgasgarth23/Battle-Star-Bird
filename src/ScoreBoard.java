import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class ScoreBoard {
    ArrayList<Score> easyScores;
    ArrayList<Score> medScores;
    ArrayList<Score> hardScores;
    public static boolean viewEasyLeaders;
    public static boolean viewMediumLeaders;
    public static boolean viewHardLeaders;
    public static boolean promptLeaderBoard;

    public ScoreBoard(){
        easyScores = new ArrayList<>();
        medScores = new ArrayList<>();
        hardScores = new ArrayList<>();
    }
    //adds scores to appropriate arraylist depending on difficulty
    public void addScore(String name, int score, int difficulty){
        if(difficulty==1){
            easyScores.add(new Score(name,score,difficulty));
            sortScores(easyScores);
        }
        else if(difficulty==2) {
            medScores.add(new Score(name,score,difficulty));
            sortScores(medScores);
        }
        else {
            hardScores.add(new Score(name,score,difficulty));
            sortScores(hardScores);
        }
    }
    //gamewide top scores based on difficulty
    public Score getTop(int difficulty){
        int max = 0;
        Score best = null;
        if(difficulty==1) {
            for (Score easyScore : easyScores) {
                if (easyScore.score >= max) {
                    max = easyScore.score;
                    best = easyScore;
                }
            }
            return best;
        }
        else if(difficulty==2) {
            for (Score medScore : medScores) {
                if (medScore.score >= max) {
                    max = medScore.score;
                    best = medScore;
                }
            }
            return best;
        }
        else {
            for (Score hardScore : hardScores) {
                if (hardScore.score >= max) {
                    max = hardScore.score;
                    best = hardScore;
                }
            }
            return best;
        }
    }
    //sorts scores in descending order, highest ones first
    public void sortScores(ArrayList<Score> toSort) {
        Collections.sort(toSort, new SortByScore());
    }
    //average gamewide scores based on difficulty
    public double averageScore(ArrayList<Score> average){
        double sum = 0;
        for(Score a : average){
            sum+=a.score;
        }
        return sum/easyScores.size();
    }
    //Overloading averageScore() to get individual average statistics
    public double averageScore(String name, ArrayList<Score> average){
        double sum = 0;
        double size = 0;
        for (Score a : average) {
            if (a.name.equals(name)) {
                sum += a.score;
                size++;
            }
        }
        return sum/size;
    }
}
