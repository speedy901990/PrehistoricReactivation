package prehistoricreactivation;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
 
public class BlockMap {
	public static TiledMap tmap;
	public static int mapWidth;
	public static int mapHeight;
	private int square[] = {1,1,15,1,15,15,1,15}; //square shaped tile
	public static ArrayList<Object> entities;
 
	public BlockMap(String ref) throws SlickException {
		entities = new ArrayList<Object>();
		tmap = new TiledMap(ref, "data/map");
		mapWidth = tmap.getWidth() * tmap.getTileWidth();
		mapHeight = tmap.getHeight() * tmap.getTileHeight();
 
		for (int x = 0; x < tmap.getWidth(); x++) {
			for (int y = 0; y < tmap.getHeight(); y++) {
				int tileID = tmap.getTileId(x, y, 0);
				if (tileID == 1) {
					entities.add(new Block(x * 16, y * 16, square, "square"));
				}
			}
		}
	}
}
