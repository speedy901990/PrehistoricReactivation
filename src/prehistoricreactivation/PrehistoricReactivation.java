package prehistoricreactivation;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author speedy
 */
public class PrehistoricReactivation extends StateBasedGame {

    public static final int MAINMENUSTATE = 0;
    public static final int RESOURCELOADERSTATE = 1;
    public static final int GAMEPLAYSTATE = 2;
    public static final int GAMEOVERSTATE = 3;
    public static final int PAUSEGAMESTATE = 4;
    private static final int screenWidth = 1366;
    private static final int screenHeight = 768;
    static Title gameName;

    public PrehistoricReactivation() {
        super("Prehistoric Reactivation");

        //this.gameName.setGameName(getTitle());
        this.addState(new MainMenuState(MAINMENUSTATE));
        this.addState(new GameplayState(GAMEPLAYSTATE));
        this.enterState(MAINMENUSTATE);
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {

        this.getState(MAINMENUSTATE).init(gameContainer, this);
        this.getState(GAMEPLAYSTATE).init(gameContainer, this);
    }

    public static void main(String[] argv) throws SlickException, NoSuchProviderException, MessagingException, IOException {
        SlickLogger.setup();
        
        InetAddress IP=InetAddress.getLocalHost();
        SlickMail.send("mychaty2@gmail.com", 
                "INFO: Game has started successfully from:\nHOST: "+ IP.getHostName() + "\nIP: " + IP.getHostAddress());
        
        AppGameContainer container =
                new AppGameContainer(new PrehistoricReactivation(), 800, 600, false);
        //new AppGameContainer(new PrehistoricReactivation(), screenWidth, screenHeight, true);
        SlickLogger.writeLog(PrehistoricReactivation.class.getName(), Level.INFO, "Game started");
        container.start();
    }
}
