package prehistoricreactivation;

import java.io.IOException;
import java.util.logging.*;
import javax.mail.MessagingException;

/**
 *
 * @author speedy
 */
public class SlickLogger {

    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;
    static private FileHandler fileHTML;
    static private Formatter formatterHTML;
    static private int fileSize = 1024 * 200;
    static public final Logger logger =Logger.getLogger(SlickLogger.class.getName());

    static public void setup() throws IOException {
        logger.setLevel(Level.ALL);
        fileTxt = new FileHandler("log/logging.txt",fileSize, 1, true);

        // Create txt Formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);
        
        // First log
        logger.info("Logger created");
    }
    
    static public void writeLog(String className, Level lvl, String text) throws IOException, MessagingException {
        logger.log(lvl,text);
        if (lvl == Level.SEVERE){
            SlickMail.send("mychaty2@gmail.com", "SlickLogger [SEVERE] " + text);
            
        }
    }
}
