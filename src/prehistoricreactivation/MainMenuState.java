/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prehistoricreactivation;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import java.util.ArrayList;

/**
 *
 * @author slawek
 */
public class MainMenuState extends BasicGameState{
    
    Image background = null;
    Image startGameOption = null;
    Image exitOption = null;
    Image [] character;
    Image actualCharacter;
 
    int stateID = 0;
    static int actualCharacterIndex;
 
    Highscores highscores = null;
 
    private static int menuX = 200;
    private static int menuY = 260;
 
    float startGameScale = 1;
    float exitScale = 1;
 
    Sound fx = null;
    
    TrueTypeFont trueTypeFont = null;
    
    public MainMenuState(int stateID )
    {
        this.stateID = stateID;
    }
 
    @Override
    public int getID() {
        return stateID;
    }
    
    @Override
    public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
        background = new Image("data/pic/menu.jpg");
        
        character = new Image[3];
        character[0] = new Image("data/pic/chlop1menu.png");
        character[1] = new Image("data/pic/chlop2menu.png");
        character[2] = new Image("data/pic/chlop3menu.png");
        
        actualCharacter = character[actualCharacterIndex];
 
        // Load the menu images
        Image menuOptions = new Image("data/pic/icons.png");
        startGameOption = menuOptions.getSubImage(0, 0, 377, 71);
 
        exitOption = menuOptions.getSubImage(0, 71, 377, 71);
 
        //--------------------------------------------------
 
//        fx = new Sound("Heartbeat120.wav");
 
        //--------------------------------------------------
 
        java.awt.Font font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 20);
        trueTypeFont = new TrueTypeFont(font, true);
 
        //highscores = Highscores.getInstance();
    }
    
    @Override
    public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
        // render the background
        background.draw(0, 0);
        actualCharacter.draw(65,475);
 
        // Draw menu
        startGameOption.draw(menuX, menuY, startGameScale);
 
        exitOption.draw(menuX, menuY+80, exitScale);
 
        // Draw Highscores
        int index = 1;
        int posY = 300;
 
        ArrayList<Integer> highScoreList = new ArrayList();// = highscores.getScores();
 
        for(Integer score : highScoreList )
        {
            trueTypeFont.drawString(20, posY, " " + ( index < highScoreList.size() ? "0" + index : "" + index) + "  ." + score, Color.orange);
            index++;
            posY += 20;
        }
    }
    
    float scaleStep = 0.0001f;
 
    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException{
        Input input = gc.getInput();
 
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
 
        boolean insideStartGame = false;
        boolean insideExit = false;
 
        if( ( mouseX >= menuX && mouseX <= menuX + startGameOption.getWidth()) &&
            ( mouseY >= menuY && mouseY <= menuY + startGameOption.getHeight()) )
        {
            insideStartGame = true;
        }else if( ( mouseX >= menuX && mouseX <= menuX+ exitOption.getWidth()) &&
            ( mouseY >= menuY+80 && mouseY <= menuY+80 + exitOption.getHeight()) )
        {
            insideExit = true;
        }
 
        if(insideStartGame)
        {
            if(startGameScale < 1.05f)
                startGameScale += scaleStep * delta;
 
            if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
  //              fx.play();
                sb.getState(PrehistoricReactivation.GAMEPLAYSTATE).init(gc, sb);
                sb.enterState(PrehistoricReactivation.GAMEPLAYSTATE);
            }
        }else{
            if(startGameScale > 1.0f)
                startGameScale -= scaleStep * delta;
 
            if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) )
                gc.exit();
        }
 
        if(insideExit)
        {
            if(exitScale < 1.05f)
                exitScale +=  scaleStep * delta;
        }else{
            if(exitScale > 1.0f)
                exitScale -= scaleStep * delta;
        }
        
        if(gc.getInput().isKeyPressed(Input.KEY_LEFT)){
            actualCharacterIndex = Math.abs(--actualCharacterIndex) % 3;
            actualCharacter = character[actualCharacterIndex];

        }
        if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)){
            actualCharacterIndex = Math.abs(++actualCharacterIndex) % 3;
            actualCharacter = character[actualCharacterIndex];
        }
    }
}
