package prehistoricreactivation;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author slawek
 */
public class Highscores implements Serializable {
    private static Highscores instance = null;
 
    ArrayList<Integer> scores = null;
 
    private Highscores(int size){
        scores = new ArrayList<>();
 
        for(int i = 0; i < size; i++)
            scores.add(new Integer(0));
    }
 
    public static Highscores getInstance() {
        if(instance == null)
            instance = new Highscores(10);
 
        return instance;
    }
    
    public static void serialize() {
        try {
            FileOutputStream fileOut = new FileOutputStream("data/highscores.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(instance);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    
    public static void deserialize() {
        try {
            FileInputStream fileIn = new FileInputStream("data/highscores.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            instance = (Highscores) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
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