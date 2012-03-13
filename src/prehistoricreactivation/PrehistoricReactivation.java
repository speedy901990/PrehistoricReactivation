package prehistoricreactivation;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Polygon;

/**
 *
 * @author speedy
 */
public class PrehistoricReactivation extends BasicGame{
private float playerX=240;
	private float playerY=140;
	private Animation player;
    private Polygon playerPoly;
	public BlockMap map;
   
	public PrehistoricReactivation() {
		super("Prehistoric Reactivation");
	}
 
    @Override
	public void init(GameContainer container) throws SlickException {
		container.setVSync(true);
		SpriteSheet sheet = new SpriteSheet("data/pic/chlop.png", 32, 64);
		map = new BlockMap("data/map/map1.tmx");
		player = new Animation();
		player.setAutoUpdate(false);
        for (int frame=0;frame<1;frame++) {
			player.addFrame(sheet.getSprite(frame,0), 150);
		}
		playerPoly = new Polygon(new float[]{
				playerX,playerY,
				playerX+32,playerY,
				playerX+32,playerY+64,
				playerX,playerY+64
		});
	}
    
    @Override
	public void update(GameContainer container, int delta) throws SlickException {
		if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
			playerX-=2;
			playerPoly.setX(playerX);
			if (entityCollisionWith()){
				playerX+=2;
                playerPoly.setX(playerX);
			}
		}
		if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
 
			playerX+=2;
			playerPoly.setX(playerX);
			if (entityCollisionWith()){
				playerX-=2;
				playerPoly.setX(playerX);
			}
		}
		if (container.getInput().isKeyDown(Input.KEY_UP)) {
 
			playerY-=2;
			playerPoly.setY(playerY);
			if (entityCollisionWith()){
				playerY+=2;
				playerPoly.setY(playerY);
			}
		}
		if (container.getInput().isKeyDown(Input.KEY_DOWN)) {
			playerY+=2;
			playerPoly.setY(playerY);
			if (entityCollisionWith()){
				playerY-=2;                
                playerPoly.setY(playerY);
			}
		}
        if (container.getInput().isKeyDown(Input.KEY_ESCAPE)){
            container.exit();
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
 
    @Override
	public void render(GameContainer container, Graphics g)  {
		BlockMap.tmap.render(0, 0);
		g.drawAnimation(player, playerX, playerY);
		g.draw(playerPoly);
 
	}
 
	public static void main(String[] argv) throws SlickException {
		AppGameContainer container = 
			new AppGameContainer(new PrehistoricReactivation(), 800, 600, false);
		container.start();
	}
}
