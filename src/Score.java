import java.util.Comparator;

public class Score{
        public String name;
        public int score;
        public int difficulty;
        public Score(String name, int score, int difficulty){
            this.name = name;
            this.score = score;
            this.difficulty = difficulty;
        }
}
//code inspired by https://stackoverflow.com/questions/15326248/sort-an-array-of-custom-objects-in-descending-order-on-an-int-property/15326312
class SortByScore implements Comparator<Score>
{
    // Used for sorting score in descending order, highest first
    public int compare(Score a, Score b)
    {
        return b.score-a.score;
    }
}