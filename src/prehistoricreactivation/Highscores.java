package prehistoricreactivation;

import java.util.ArrayList;

/**
 *
 * @author slawek
 */
public class Highscores {
    private static Highscores instance = null;
 
    ArrayList<Integer> scores = null;
 
    private Highscores(int size)
    {
        scores = new ArrayList<>();
 
        for(int i = 0; i < size; i++)
            scores.add(new Integer(0));
    }
 
    public static Highscores getInstance()
    {
        if(instance == null)
            instance = new Highscores(10);
 
        return instance;
    }
 
    public boolean addScore(int score)
    {
        for(int idx = 0; idx < scores.size(); idx++)
        {
            if(score > scores.get(idx)) {
                scores.add(idx, new Integer(score));
                scores.remove(scores.size()-1);
 
                return true;
            }
        }
        return false;
    }
 
    public ArrayList<Integer> getScores()
    {
        return scores;
    }
}