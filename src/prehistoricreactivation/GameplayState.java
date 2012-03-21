/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prehistoricreactivation;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
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
    private Animation player;
    private Polygon playerPoly;
    public BlockMap map;
    private float jump_speed;
    private boolean jumping;
    private int stateId = 0;

    private enum STATES {

        START_GAME_STATE, PAUSE_GAME_STATE, HIGHSCORE_STATE, GAME_OVER_STATE
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
        SpriteSheet sheet = new SpriteSheet("data/pic/chlopTest.png", 32, 64);
        map = new BlockMap("data/map/map2.tmx");
        player = new Animation();
        player.setAutoUpdate(false);
        for (int frame = 0; frame < 2; frame++) {
            player.addFrame(sheet.getSprite(frame, 0), 150);
        }
        playerPoly = new Polygon(new float[]{
                    playerX, playerY,
                    playerX + 32, playerY,
                    playerX + 32, playerY + 64,
                    playerX, playerY + 64
                });
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {

        BlockMap.tmap.render(0, 0);
        g.drawAnimation(player, playerX, playerY);
        //g.draw(playerPoly);
        //testline
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
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sb) throws SlickException {
        super.enter(gc, sb);

        currentState = STATES.START_GAME_STATE;
    }

    public void getUserInput(GameContainer container, StateBasedGame sb) throws SlickException {
        if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
            player.setCurrentFrame(1);
            playerX -= 2;
            playerPoly.setX(playerX);
            if (entityCollisionWith()) {
                playerX += 2;
                playerPoly.setX(playerX);
            }
        }
        if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
            player.setCurrentFrame(0);
            playerX += 2;
            playerPoly.setX(playerX);
            if (entityCollisionWith()) {
                playerX -= 2;
                playerPoly.setX(playerX);
            }
        }

        if (container.getInput().isKeyPressed(Input.KEY_UP)) {
            if (jumping == false) {
                jump_speed = -15;
                jumping = true;
            }
        }

        if (jumping && jump_speed < 14) {
            jump_speed += 1;
            playerY = playerY + jump_speed;
            playerPoly.setY(playerY);
            while (entityCollisionWith()) {
                playerY -= 1;
                playerPoly.setY(playerY);
            }
        } else {
            jumping = false;
        }

        //playerY = playerY + jump_speed;

        if (!container.getInput().isKeyDown(Input.KEY_DOWN)
                || container.getInput().isKeyDown(Input.KEY_DOWN)) {
            playerY += 3;
            playerPoly.setY(playerY);
            if (entityCollisionWith()) {
                playerY -= 3;
                playerPoly.setY(playerY);
            }
        }
        if (container.getInput().isKeyDown(Input.KEY_ESCAPE)) {
            sb.enterState(PrehistoricReactivation.MAINMENUSTATE);
        }
    }

    public boolean entityCollisionWith() throws SlickException {
        for (int i = 0; i < BlockMap.entities.size(); i++) {
            Block entity1 = (Block) BlockMap.entities.get(i);
            if (playerPoly.intersects(entity1.poly)) {
                return true;
            }
        }
        return false;
    }
}
