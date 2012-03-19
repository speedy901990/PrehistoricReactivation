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

    public static final int MAINMENUSTATE          = 0;
    public static final int RESOURCELOADERSTATE    = 1;
    public static final int GAMEPLAYSTATE          = 2;
    
    public PrehistoricReactivation() {
        super("Prehistoric Reactivation");

        this.addState(new MainMenuState(MAINMENUSTATE));
        this.addState(new GameplayState(GAMEPLAYSTATE));
        this.enterState(MAINMENUSTATE);
    }
 
    @Override
	public void initStatesList(GameContainer gameContainer) throws SlickException {
 
        this.getState(MAINMENUSTATE).init(gameContainer, this);
        this.getState(GAMEPLAYSTATE).init(gameContainer, this);
    }
    
    public static void main(String[] argv) throws SlickException {
            AppGameContainer container = 
                    new AppGameContainer(new PrehistoricReactivation(), 800, 600, false);
            
            container.start();
    }
}
 