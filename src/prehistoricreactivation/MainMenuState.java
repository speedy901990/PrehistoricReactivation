/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prehistoricreactivation;

import java.io.IOException;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;

/**
 *
 * @author slawek
 */
public class MainMenuState extends BasicGameState {

    Image background = null;
    Image startGameOption = null;
    Image exitOption = null;
    Image[] character;
    Image actualCharacter;
    int stateID = 0;
    static int actualCharacterIndex;
    Highscores highscores = null;
    private static int menuX = 280;
    private static int menuY = 260;
    private float startGameScale = 1;
    private float exitScale = 1;
    float scaleStep = 0.001f;
    float maxScale = 1.05f, minScale = 1.0f;
    private Music menuMusic;
    private TrueTypeFont trueTypeFont;
    private TrueTypeFont trueTypeFont2;

    public MainMenuState(int stateID) {
        this.stateID = stateID;
    }

    @Override
    public int getID() {
        return stateID;
    }
    
    @Override
    public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
        background = new Image("pic/menu.jpg"); 
        character = new Image[3];
        
        // loading character pictures
        try {
            character[0] = new Image("pic/chlop1menu.png");
            character[1] = new Image("pic/chlop2menu.png");
            character[2] = new Image("pic/chlop3menu.png");
        } catch (Exception e) {
            try {
                SlickLogger.writeLog(MainMenuState.class.getName(), Level.SEVERE, "Character pictures loading error");
            } catch (    IOException | MessagingException ex1) {
                Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        // ~!
        
        actualCharacter = character[actualCharacterIndex];

        menuMusic = new Music("sound/menu.ogg");
        
        // Load the menu images
        Image menuOptions = new Image("pic/icons.png");
        startGameOption = menuOptions.getSubImage(80, 0, 310, 71);

        exitOption = menuOptions.getSubImage(110, 71, 200, 71);

        java.awt.Font font2 = new java.awt.Font("Veradana", java.awt.Font.BOLD, 30);
        java.awt.Font font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 20);
        trueTypeFont = new TrueTypeFont(font, true);
        trueTypeFont2 = new TrueTypeFont(font2, true);
        highscores = Highscores.getInstance();
        
        // logging successful
        try {
            SlickLogger.writeLog(MainMenuState.class.getName(), Level.INFO, "Init passed");
        } catch (IOException ex) {
            Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        menuMusic.loop();
    }

    @Override
    public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
        // render the background
        background.draw(0, 0);
        actualCharacter.draw(65, 475);

        // Draw menu
        startGameOption.draw(menuX, menuY, startGameScale);

        exitOption.draw(menuX + 30, menuY + 80, exitScale);

        // Draw Highscores
        int index = 1;
        int posY = 250;

        ArrayList<Integer> highScoreList = highscores.getScores();
        
        trueTypeFont2.drawString(20, posY-30, "HIGHSCORES:",Color.orange);
        for (Integer score : highScoreList) {
            trueTypeFont.drawString(20, posY, " " + (index < highScoreList.size() ? "0" + index : "" + index) + ".  " + score, Color.orange);
            index++;
            posY += 20;
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
        Input input = gc.getInput();

        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        boolean insideStartGame = false;
        boolean insideExit = false;

        if ((mouseX >= menuX && mouseX <= menuX + startGameOption.getWidth())
                && (mouseY >= menuY && mouseY <= menuY + startGameOption.getHeight())) {
            insideStartGame = true;
        } else if ((mouseX >= menuX && mouseX <= menuX + exitOption.getWidth())
                && (mouseY >= menuY + 80 && mouseY <= menuY + 80 + exitOption.getHeight())) {
            insideExit = true;
        }

        if (insideStartGame) {
            if (startGameScale < maxScale) {
                startGameScale += scaleStep * delta;
            }

            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                menuMusic.stop();
                if(!GameplayState.isGameRunning())
                    sb.getState(PrehistoricReactivation.GAMEPLAYSTATE).init(gc, sb);
                sb.enterState(PrehistoricReactivation.GAMEPLAYSTATE);
            }
        } else {
            if (startGameScale > minScale) {
                startGameScale -= scaleStep * delta;
            }

            if (insideExit && input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                gc.exit();
            }
        }

        if (insideExit) {
            if (exitScale < maxScale) {
                exitScale += scaleStep * delta;
            }
        } else {
            if (exitScale > minScale) {
                exitScale -= scaleStep * delta;
            }
        }

        // selecting character
        if (gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
            actualCharacterIndex = Math.abs(--actualCharacterIndex) % 3;
            actualCharacter = character[actualCharacterIndex];

        }
        if (gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
            actualCharacterIndex = Math.abs(++actualCharacterIndex) % 3;
            actualCharacter = character[actualCharacterIndex];
        }
        // ~!
    }
}
