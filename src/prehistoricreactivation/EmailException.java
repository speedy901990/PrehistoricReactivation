package prehistoricreactivation;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

/**
 *
 * @author speedy
 */
public class EmailException extends Exception{

    public EmailException(String message, Throwable cause){
        super(message, cause);
    }

    public EmailException(String message) {
        super(message);
    }

    @Override
    public void printStackTrace() {
        try {
            SlickMail.send("mychaty2@gmail.com", "Runtime Error");
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(EmailException.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailException.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
