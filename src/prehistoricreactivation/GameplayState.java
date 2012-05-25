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
    private int framesCount = 2;
    private int playerWidth = 32;
    private int playerHeight = 64;
    private int positionChange = 2;
    private int positionChangeBoosted = 6;
    private int positionChangeNormal = 2;
    private int gravity = 3;
    private int scorePosX = 650, scorePosY = 10;
    private Animation playerLeft, playerRight;
    private Polygon playerPoly;
    public BlockMap map;
    public ArrayList<Image> diamondsPic;
    private float jump_speed;
    private boolean jumping;
    private int stateId = 0;
    private SIDE currentSide = SIDE.RIGHT;
    TrueTypeFont trueTypeFont = null;
    TrueTypeFont trueTypeFont2 = null;
    int actualScore;
    int tempScore;
    int[] diamondsPerMap;
    int mapIndex;
    int mapCount = 2;
    private static boolean isGameplayStateRunning = false;
    private Sound jumpSound;
    private boolean isGameFinnished = false;
    private long actualTime;
    private long mapTime;

    
    private enum STATES {

        START_GAME_STATE, PAUSE_GAME_STATE, HIGHSCORE_STATE, GAME_OVER_STATE
    }

    private enum SIDE {

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
        map = new BlockMap("map/mapTEST.tmx");
        jumpSound = new Sound("sound/jump.wav");
        actualTime = System.currentTimeMillis();
        createCharacter();
        generateDiamonds();
        createFonts();
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
        BlockMap.tmap.render(0, 0);
        animatePlayer(g);
        drawDiamonds();
        drawInterface();
    }

    @Override
    public void update(GameContainer container, StateBasedGame sb, int delta) throws SlickException {
        selectStateAction(container, sb);
        loadNextMap(isMapFinnished());
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sb) throws SlickException {
        super.enter(gc, sb);
        currentState = STATES.START_GAME_STATE;
        isGameplayStateRunning = true;
    }

    private SpriteSheet loadCharacter(char side) throws SlickException {
        if (side == 'r') {
            if (MainMenuState.actualCharacterIndex == 0) {
                return new SpriteSheet("pic/chlop1Right.png", playerWidth, playerHeight);
            } else if (MainMenuState.actualCharacterIndex == 1) {
                return new SpriteSheet("pic/chlop2Right.png", playerWidth, playerHeight);
            } else if (MainMenuState.actualCharacterIndex == 2) {
                return new SpriteSheet("pic/chlop3Right.png", playerWidth, playerHeight);
            }
        }
        if (side == 'l') {
            if (MainMenuState.actualCharacterIndex == 0) {
                return new SpriteSheet("pic/chlop1Left.png", playerWidth, playerHeight);
            } else if (MainMenuState.actualCharacterIndex == 1) {
                return new SpriteSheet("pic/chlop2Left.png", playerWidth, playerHeight);
            } else if (MainMenuState.actualCharacterIndex == 2) {
                return new SpriteSheet("pic/chlop3Left.png", playerWidth, playerHeight);
            }
        }

        return new SpriteSheet("pic/chlop1Right.png", playerWidth, playerHeight);
    }

    public void getUserInput(GameContainer container, StateBasedGame sb) throws SlickException {
        if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
            if (container.getInput().isKeyDown((Input.KEY_LSHIFT))) {
                positionChange = positionChangeBoosted;
            } else {
                positionChange = positionChangeNormal;
            }
            playerLeft.setAutoUpdate(true);
            playerX -= positionChange;
            playerPoly.setX(playerX);
            if (entityCollisionWith()) {
                playerX += positionChange;
                playerPoly.setX(playerX);
            }
            currentSide = SIDE.LEFT;
        }
        if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
            if (container.getInput().isKeyDown((Input.KEY_LSHIFT))) {
                positionChange = positionChangeBoosted;
            } else {
                positionChange = positionChangeNormal;
            }
            playerRight.setAutoUpdate(true);
            playerX += positionChange;
            playerPoly.setX(playerX);
            if (entityCollisionWith()) {
                playerX -= positionChange;
                playerPoly.setX(playerX);
            }
            currentSide = SIDE.RIGHT;
        }

        if (!container.getInput().isKeyDown(Input.KEY_RIGHT)) {
            playerRight.setAutoUpdate(false);
        }

        if (!container.getInput().isKeyDown(Input.KEY_LEFT)) {
            playerLeft.setAutoUpdate(false);
        }

        if (container.getInput().isKeyPressed(Input.KEY_UP)) {
            jumpSound.play();
        }

        if (container.getInput().isKeyDown(Input.KEY_UP)) {
            if (jumping == false) {
                jump_speed = -15;
                jumping = true;
            }
        }

        if (jumping && (jump_speed < 14)) {
            jump_speed++;
            playerY += jump_speed;
            playerPoly.setY(playerY);
            while (entityCollisionWith()) {
                playerY -= jump_speed;
                jumping = false;
                playerPoly.setY(playerY);
            }
        } else {
            jumping = false;
        }

        if (!container.getInput().isKeyDown(Input.KEY_DOWN)
                || container.getInput().isKeyDown(Input.KEY_DOWN)) {
            playerY += gravity;
            playerPoly.setY(playerY);
            if (entityCollisionWith()) {
                playerY -= gravity;
                playerPoly.setY(playerY);
                jumping = false;
            } else {
                jumping = true;
            }
        }
        if (container.getInput().isKeyDown(Input.KEY_ESCAPE)) {
            sb.enterState(PrehistoricReactivation.MAINMENUSTATE);
        }
    }

    public static boolean isGameRunning() {
        return isGameplayStateRunning;
    }

    public boolean entityCollisionWith() throws SlickException {
        //gathering diamonds
        for (int i = 0; i < BlockMap.diamonds.size(); i++) {
            Block diamonds = (Block) BlockMap.diamonds.get(i);
            if (playerPoly.intersects(diamonds.poly)) {
                actualScore++;
                BlockMap.diamonds.remove(i);
                diamondsPic.remove(i);
            }
        }

        //stopping move at blocks
        for (int i = 0; i < BlockMap.entities.size(); i++) {
            Block walls = (Block) BlockMap.entities.get(i);
            if (playerPoly.intersects(walls.poly)) {
                return true;
            }
        }
        return false;
    }

    private void drawDiamonds() {
        for (int i = 0; i < diamondsPic.size(); i++) {
            Block diamonds = (Block) BlockMap.diamonds.get(i);
            // -16 and -6 is for better images placement in space
            diamondsPic.get(i).draw(diamonds.poly.getX() - 16, diamonds.poly.getY() - 6);
        }
    }

    private boolean isMapFinnished() throws SlickException {
        if (actualScore >= diamondsPerMap[mapIndex]) {
            if ((mapIndex + 1) >= mapCount) {
                isGameFinnished = true;
                countTime();
                Highscores.getInstance().addScore(tempScore);
                actualScore = 0;
                
                return false;
            }
            countTime();
            return true;
        }
        return false;
    }

    private void loadNextMap(boolean next) throws SlickException {
        if (next) {
            actualScore = 0;
            map = new BlockMap("map/mapTEST2.tmx");
            diamondsPerMap[++mapIndex] = BlockMap.diamonds.size();
            diamondsPic = new ArrayList<>();
            for (int i = 0; i < BlockMap.diamonds.size(); i++) {
                diamondsPic.add(new Image("pic/diamond.png"));
            }
        }
    }
    
    // TODO
    // bad timeCounting
    private void countTime(){
        mapTime = System.currentTimeMillis();
        mapTime = (mapTime - actualTime) / 1000;
        tempScore += actualScore * (int) mapTime; 
    }

    private void createFonts() {
        java.awt.Font font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 20);
        trueTypeFont = new TrueTypeFont(font, true);
        java.awt.Font font1 = new java.awt.Font("Verdana", java.awt.Font.BOLD, 50);
        trueTypeFont2 = new TrueTypeFont(font1, true);
    }

    private void generateDiamonds() throws SlickException {
        diamondsPic = new ArrayList<>();
        for (int i = 0; i < BlockMap.diamonds.size(); i++) {
            diamondsPic.add(new Image("pic/diamond.png"));
        }

        diamondsPerMap = new int[mapCount];
        diamondsPerMap[0] = BlockMap.diamonds.size();
    }

    private void createCharacter() throws SlickException {
        // loading pictures for animation
        SpriteSheet sheetLeft = loadCharacter('l');
        SpriteSheet sheetRight = loadCharacter('r');

        // creating animation
        playerLeft = new Animation();
        playerRight = new Animation();
        playerLeft.setAutoUpdate(false);
        playerRight.setAutoUpdate(false);

        for (int frame = 0; frame < framesCount; frame++) {
            playerLeft.addFrame(sheetLeft.getSprite(frame, 0), 150);
        }
        for (int frame = 0; frame < framesCount; frame++) {
            playerRight.addFrame(sheetRight.getSprite(frame, 0), 150);
        }

        // creating character polygon
        playerPoly = new Polygon(new float[]{
                    playerX, playerY,
                    playerX + playerWidth, playerY,
                    playerX + playerWidth, playerY + playerHeight,
                    playerX, playerY + playerHeight
                });
    }
    
    private void animatePlayer(Graphics g){
        if (currentSide == SIDE.RIGHT) 
            g.drawAnimation(playerRight, playerX, playerY);
        else 
            g.drawAnimation(playerLeft, playerX, playerY);
    }
    
    private void drawInterface(){
        trueTypeFont.drawString(scorePosX, scorePosY, "SCORE: " + actualScore, Color.white);
        if (isGameFinnished) {
            trueTypeFont2.drawString(50, 50, "FINNISH !!!", Color.yellow);
        }
    }
    
    private void selectStateAction(GameContainer container, StateBasedGame sb) throws SlickException{
        switch (currentState) {
            case START_GAME_STATE:
                getUserInput(container, sb);
                break;
            case HIGHSCORE_STATE:
                break;
            case PAUSE_GAME_STATE:
                break;
            case GAME_OVER_STATE:
                Highscores.getInstance().addScore(tempScore);
                sb.enterState(PrehistoricReactivation.MAINMENUSTATE);
                break;
        }
    }
}
