package prehistoricreactivation;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author slawek
 */
class PauseGameState extends BasicGameState {

    int stateID = 4;
    
    public PauseGameState(int PAUSEGAMESTATE) {
        this.stateID=PAUSEGAMESTATE;
    }
    
    @Override
    public int getID() {
        return stateID;
    }
    
    @Override
    public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
        
    }
    
    @Override
    public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
    
    }
    
    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    }
        
}
