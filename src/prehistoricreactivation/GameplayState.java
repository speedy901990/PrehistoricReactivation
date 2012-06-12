/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prehistoricreactivation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author slawek
 */
public class GameplayState extends BasicGameState {

    private float playerX = 240;
    private float playerY = 525;
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
    TrueTypeFont trueTypeFont3 = null;
    TrueTypeFont fakeVarForSlowMotion;
    int actualScore;
    int tempScore;
    int[] diamondsPerMap;
    int mapIndex;
    int mapCount = 3;
    private static boolean isGameplayStateRunning = false;
    private Sound jumpSound;
    private Sound collectSound;
    private Sound finnishSound;
    private Music gameplayMusic;
    public static boolean isGameFinnished = false;
    private long actualTime;
    private long mapTime;
    private Image danger, danger1, danger2;
    private boolean isTrapped;
    private int howManyTimesTrapped = 0;
    private ArrayList<String> mapsList;
    
    
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
        generateMapsList();
        mapIndex = 0;
        danger = new Image("pic/tablet.png");
        danger1 = new Image("pic/tablet.png");
        danger2 = new Image("pic/tablet.png");
        actualTime = System.currentTimeMillis();
        isTrapped = false;
        isGameFinnished = false;
        
        // loading first map
        try {
            map = new BlockMap(mapsList.get(mapIndex));
        } catch (IOException | MessagingException ex) {
            try {
                SlickLogger.writeLog(GameplayState.class.getName(), Level.SEVERE, "Map creating error");
            } catch (    IOException | MessagingException ex1) {
                Logger.getLogger(GameplayState.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        // ~!
        
        container.setVSync(true);
        loadSounds();
        createFonts();
        createCharacter();
        setPlayerStartPosition();
        generateDiamonds();
        
        // logging success init
        try {
            SlickLogger.writeLog(MainMenuState.class.getName(), Level.INFO, "Init passed");
        } catch (IOException | MessagingException ex) {
            Logger.getLogger(GameplayState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
        BlockMap.tmap.render(0, 0);
        drawOtherPictures();
        animatePlayer(g);
        drawDiamonds();
        drawInterface();
        performSlowMotion();
    }

    @Override
    public void update(GameContainer container, StateBasedGame sb, int delta) throws SlickException {
        selectStateAction(container, sb);
        
        // load next map
        try {
            loadNextMap(isMapFinnished());
        } catch (IOException | MessagingException ex) {
            try {
                SlickLogger.writeLog(GameplayState.class.getName(), Level.SEVERE, "Next map loading error");
            } catch (    IOException | MessagingException ex1) {
                Logger.getLogger(GameplayState.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        // ~!
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sb) throws SlickException {
        super.enter(gc, sb);
        currentState = STATES.START_GAME_STATE;
        isGameplayStateRunning = true;
        gameplayMusic.loop();
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
    
    public static boolean isGameFinnished(){
        return isGameFinnished;
    }

    public boolean entityCollisionWith() throws SlickException {
        // gathering diamonds
        for (int i = 0; i < BlockMap.diamonds.size(); i++) {
            Block diamonds = (Block) BlockMap.diamonds.get(i);
            if (playerPoly.intersects(diamonds.poly)) {
                collectSound.play();
                actualScore++;
                BlockMap.diamonds.remove(i);
                diamondsPic.remove(i);
            }
        }
        
        // jumping on traps
        if (playerX > 800 || playerX < 0 || playerY > 600 || playerY < 0)
            isTrapped = true;

        // stopping move at blocks
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
                mapTime = 0;
                finnishSound.play();
                return false;
            }
            countTime();
            return true;
        }
        return false;
    }

    private void loadNextMap(boolean next) throws SlickException, IOException, MessagingException {
        if (next) {
            actualScore = 0;
            map = new BlockMap(mapsList.get(++mapIndex));
            diamondsPerMap[mapIndex] = BlockMap.diamonds.size();
            diamondsPic = new ArrayList<>();
            for (int i = 0; i < BlockMap.diamonds.size(); i++) {
                diamondsPic.add(new Image("pic/diamond.png"));
            }
            if (mapIndex == 2){
                playerX = 40;
                playerY = 25;
                playerPoly.setX(playerX);
                playerPoly.setY(playerY);
            }
        }
    }
    
    // TODO
    // bad timeCounting
    private void countTime(){
        mapTime = System.currentTimeMillis();
        mapTime = (mapTime - actualTime) / 1000;
        tempScore += ((actualScore*100)/mapTime); 
    }

    private void createFonts() {
        java.awt.Font font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 20);
        trueTypeFont = new TrueTypeFont(font, true);
        java.awt.Font font2 = new java.awt.Font("Verdana", java.awt.Font.BOLD, 50);
        trueTypeFont2 = new TrueTypeFont(font2, true);
        java.awt.Font font3 = new java.awt.Font("Verdana", java.awt.Font.BOLD, 50);
        trueTypeFont3 = new TrueTypeFont(font3, true); 
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
        if (isGameFinnished && !isTrapped) {
            trueTypeFont2.drawString(200, 100, "FINNISH !!!", Color.yellow);
        }
        else if (isTrapped){
            trueTypeFont3.drawString(200, 100, "GAME OVER !!!", Color.yellow);
            isGameFinnished = true;
            if (howManyTimesTrapped++ == 0) {
                countTime();
                Highscores.getInstance().addScore(tempScore);
                actualScore = 0;
                mapTime = 0;
                howManyTimesTrapped--;
            }
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
                gameplayMusic.stop();
                sb.enterState(PrehistoricReactivation.MAINMENUSTATE);
                break;
        }
    }
    
    private void loadSounds() throws SlickException {
        jumpSound = new Sound("sound/jump.wav");
        collectSound = new Sound("sound/collect.wav");
        gameplayMusic = new Music("sound/gameplay.ogg");
        finnishSound = new Sound("sound/finnish.ogg");
    }
    
    private void drawOtherPictures() throws SlickException{
        if (mapIndex == 0){
            danger.draw(285, 500, 0.25f);
        }
        else if (mapIndex == 1){
            danger.draw(135, 500, 0.25f);
        }
    }
    
    private void performSlowMotion(){
        if (mapIndex == 2){
            if (playerX > 205 && playerX < 500 && playerY < 260){
                java.awt.Font fontX = new java.awt.Font("Verdana", java.awt.Font.BOLD, 50);
                fakeVarForSlowMotion = new TrueTypeFont(fontX, true);
                fakeVarForSlowMotion.drawString(200, 400, "Aaaaaaaa....", Color.red);
            }
        }
    }
    
    private void generateMapsList() {
        mapsList = new ArrayList<>();
        mapsList.add("map/map1.tmx");
        mapsList.add("map/map2.tmx");
        mapsList.add("map/map3.tmx");
    }
    
    private void setPlayerStartPosition(){
        playerX = 240;
        playerY = 525;
        playerPoly.setX(playerX);
        playerPoly.setY(playerY);
    }
}
