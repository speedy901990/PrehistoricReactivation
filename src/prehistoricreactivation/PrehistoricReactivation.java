package prehistoricreactivation;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author speedy
 */
public class PrehistoricReactivation extends StateBasedGame{

    public PrehistoricReactivation() {
        super("Prehistoric Reactivation");
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        //TODO add game states
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new PrehistoricReactivation());
        app.setDisplayMode(800, 600, false);
        app.setTargetFrameRate(30);
        app.start();
    }
}
