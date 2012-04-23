/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prehistoricreactivation;

import java.util.ArrayList;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author slawek
 */
public class GameplayState extends BasicGameState {

    private float playerX = 240;
    private float playerY = 530;
    private Animation playerLeft, playerRight;
    private Polygon playerPoly;
    public BlockMap map;
    public ArrayList<Image> diamondsPic;
    private float jump_speed;
    private boolean jumping;
    private int stateId = 0;
    private SIDE currentSide = SIDE.RIGHT;
    TrueTypeFont trueTypeFont = null;
    TrueTypeFont trueTypeFont2= null;
    int actualScore;
    int [] diamondsPerMap;
    int mapIndex;
    int mapCount = 2;

    private enum STATES {

        START_GAME_STATE, PAUSE_GAME_STATE, HIGHSCORE_STATE, GAME_OVER_STATE
    }
    private enum SIDE{
        LEFT, RIGHT
    }
    
    private STATES currentState = null;

    public GameplayState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public int getID() {
        return stateId;
    }
    
    @Override
    public void init(GameContainer container, StateBasedGame sb) throws SlickException {
        container.setVSync(true);        
        map = new BlockMap("data/map/map1.tmx");
        SpriteSheet sheetLeft = loadCharacter('l');
        SpriteSheet sheetRight = loadCharacter('r');
        playerLeft = new Animation();
        playerRight = new Animation();
        playerLeft.setAutoUpdate(false);
        playerRight.setAutoUpdate(false);
        
        for (int frame = 0; frame < 2; frame++) 
            playerLeft.addFrame(sheetLeft.getSprite(frame, 0), 150);
        for (int frame = 0; frame < 2; frame++) 
            playerRight.addFrame(sheetRight.getSprite(frame, 0), 150);
        
        playerPoly = new Polygon(new float[]{
                    playerX, playerY,
                    playerX + 32, playerY,
                    playerX + 32, playerY + 64,
                    playerX, playerY + 64
                });
        
        //generating array of diamond images
        diamondsPic = new ArrayList<Image>();
        for (int i=0 ; i<BlockMap.diamonds.size() ; i++)
            diamondsPic.add(new Image("data/pic/diamond.png"));
        
        diamondsPerMap = new int[mapCount];
        diamondsPerMap[0] = BlockMap.diamonds.size();
        
        java.awt.Font font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 20);
        trueTypeFont = new TrueTypeFont(font, true);
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
        BlockMap.tmap.render(0, 0);
        if (currentSide == SIDE.RIGHT)
            g.drawAnimation(playerRight, playerX, playerY);
        else
            g.drawAnimation(playerLeft, playerX, playerY);

        //drawing actual scores
        trueTypeFont.drawString(650, 10, "SCORE: " + actualScore , Color.white);
        
        drawDiamonds();
    }

    @Override
    public void update(GameContainer container, StateBasedGame sb, int delta) throws SlickException {
        switch (currentState) {
            case START_GAME_STATE:
                getUserInput(container, sb);
                break;
            case HIGHSCORE_STATE:
                break;
            case PAUSE_GAME_STATE:
                break;
            case GAME_OVER_STATE:
                //Highscores.getInstance().addScore(score);
                sb.enterState(PrehistoricReactivation.MAINMENUSTATE);
                break;
        }
        
        loadNextMap(isMapFinnished(container, sb));
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sb) throws SlickException {
        super.enter(gc, sb);

        currentState = STATES.START_GAME_STATE;
    }
    
    private SpriteSheet loadCharacter(char side) throws SlickException {
        if (side == 'r') {
            if (MainMenuState.actualCharacterIndex == 0) {
                return new SpriteSheet("data/pic/chlop1Right.png", 32, 64);
            } else if (MainMenuState.actualCharacterIndex == 1) {
                return new SpriteSheet("data/pic/chlop2Right.png", 32, 64);
            } else if (MainMenuState.actualCharacterIndex == 2) {
                return new SpriteSheet("data/pic/chlop3Right.png", 32, 64);
            }
        }
       if (side == 'l'){
           if (MainMenuState.actualCharacterIndex == 0) {
               return new SpriteSheet("data/pic/chlop1Left.png", 32, 64);
            } else if (MainMenuState.actualCharacterIndex == 1) {
                return new SpriteSheet("data/pic/chlop2Left.png", 32, 64);
            } else if (MainMenuState.actualCharacterIndex == 2) {
                return new SpriteSheet("data/pic/chlop3Left.png", 32, 64);
           }
       }
        
        return new SpriteSheet("data/pic/chlop1Right.png", 32, 64);
    }

    public void getUserInput(GameContainer container, StateBasedGame sb) throws SlickException {
        if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
            playerLeft.setAutoUpdate(true);
            playerX -= 2;
            playerPoly.setX(playerX);
            if (entityCollisionWith()) {
                playerX += 2;
                playerPoly.setX(playerX);
            }
            currentSide = SIDE.LEFT;
        }
        if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
            playerRight.setAutoUpdate(true);
            playerX += 2;
            playerPoly.setX(playerX);
            if (entityCollisionWith()) {
                playerX -= 2;
                playerPoly.setX(playerX);
            }
            currentSide = SIDE.RIGHT;
        }
        
        if (!container.getInput().isKeyDown(Input.KEY_RIGHT))
            playerRight.setAutoUpdate(false);
        
        if ( !container.getInput().isKeyDown(Input.KEY_LEFT))
            playerLeft.setAutoUpdate(false);

        if (container.getInput().isKeyDown(Input.KEY_UP)) {
            if (jumping == false) {
                jump_speed = -15;
                jumping = true;
            }
        }
            
        if (jumping && (jump_speed < 14)) {
            jump_speed += 1;
            playerY += jump_speed;
            playerPoly.setY(playerY);
            while (entityCollisionWith()) {
                playerY -= jump_speed;
                jumping=false;
                playerPoly.setY(playerY);
            }
        } else {
            jumping = false;
        }

        if (!container.getInput().isKeyDown(Input.KEY_DOWN)
                || container.getInput().isKeyDown(Input.KEY_DOWN)) {
            playerY += 3;
            playerPoly.setY(playerY);
            if (entityCollisionWith()) {
                playerY -= 3;
                playerPoly.setY(playerY);
                jumping = false;
            }
            else
                jumping = true;
        }
        if (container.getInput().isKeyDown(Input.KEY_ESCAPE)) {
            sb.enterState(PrehistoricReactivation.MAINMENUSTATE);
        }
    }

    public boolean entityCollisionWith() throws SlickException {
        //gathering diamonds
        for (int i = 0; i < BlockMap.diamonds.size(); i++) {
            Block diamonds = (Block) BlockMap.diamonds.get(i);
            if (playerPoly.intersects(diamonds.poly)){
                actualScore++;
                BlockMap.diamonds.remove(i);
                diamondsPic.remove(i);
            }
        }
        
        //stopping at blocks
        for (int i = 0; i < BlockMap.entities.size(); i++) {
            Block walls = (Block) BlockMap.entities.get(i);
            if (playerPoly.intersects(walls.poly)) {
                return true;
            }
        }
        return false;
    }
    
    private void drawDiamonds(){
        for (int i = 0; i < diamondsPic.size(); i++) {
            Block diamonds = (Block) BlockMap.diamonds.get(i);
            // -16 and -6 is for better placement images in space
            diamondsPic.get(i).draw(diamonds.poly.getX()-16, diamonds.poly.getY()- 6);
        }
    }
    
    private boolean isMapFinnished(GameContainer container, StateBasedGame sb) throws SlickException {
        if (actualScore >= diamondsPerMap[mapIndex]){
            if((mapIndex + 1) >= mapCount) {
                java.awt.Font font1 = new java.awt.Font("Verdana", java.awt.Font.BOLD, 50);
                trueTypeFont2 = new TrueTypeFont(font1, true);
                trueTypeFont2.drawString(50, 50, "FINNISH !!!" , Color.yellow);
                //container.pause();
                sb.enterState(PrehistoricReactivation.MAINMENUSTATE);
                //container.exit();
                return false;
            }
            return true;
        }
        return false;
    }
    
    private void loadNextMap(boolean next) throws SlickException{
        if (next){
            actualScore = 0;
            map = new BlockMap("data/map/map2.tmx");
            diamondsPerMap[++mapIndex] = BlockMap.diamonds.size();
            diamondsPic = new ArrayList<Image>();
            for (int i=0 ; i<BlockMap.diamonds.size() ; i++)
                diamondsPic.add(new Image("data/pic/diamond.png")); 
        }
    }
}
