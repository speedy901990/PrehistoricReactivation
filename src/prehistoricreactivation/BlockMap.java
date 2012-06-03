package prehistoricreactivation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.mail.MessagingException;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TileSet;
import org.newdawn.slick.tiled.TiledMap;

public class BlockMap {

    public static TiledMap tmap;
    public static int mapWidth;
    public static int mapHeight;
    private int square[] = {1, 1, 15, 1, 15, 15, 1, 15}; //square shaped tile
    public static ArrayList<Block> entities;
    public static ArrayList<Block> diamonds;
    private final int squareSize = 16;
    private final int[] layers = {0, 1, 2, 3};

    public BlockMap(String ref) throws SlickException, IOException, MessagingException {
        entities = new ArrayList<>();
        diamonds = new ArrayList<>();

        tmap = new TiledMap(ref);
        SlickLogger.writeLog(BlockMap.class.getName(), Level.INFO, "Map created");
        mapWidth = tmap.getWidth() * tmap.getTileWidth();
        mapHeight = tmap.getHeight() * tmap.getTileHeight();

        for (int x = 0; x < tmap.getWidth(); x++) {
            for (int y = 0; y < tmap.getHeight(); y++) {
                int tileID = tmap.getTileId(x, y, layers[0]);
                if (tileID == 1) {
                    entities.add(new Block(x * squareSize, y * squareSize, square, "square"));
                }

                tileID = tmap.getTileId(x, y, layers[1]);
                if (tileID == 2) {
                    diamonds.add(new Block(x * squareSize, y * squareSize, square, "diamond"));
                }
            }
        }
    }
}
