package prehistoricreactivation;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


/**
 *
 * @author speedy
 */
public class SlickMail {

    static public void send(String mailTo, String text) throws NoSuchProviderException, MessagingException {  
        String Email = "mychaty1@gmail.com",
                Uname = "mychaty1",
                Password = "1234QWER",
                Host = "smtp.gmail.com",
                Port = "465", //465,587
                To = mailTo,
                Subject = "Slick JavaMail!",
                Text = text;


        Properties props = System.getProperties();
        props.put("mail.smtp.user", Email);
        props.put("mail.smtp.host", Host);
        props.put("mail.smtp.port", Port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", Port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        SMTPAuthenticator auth = new SMTPAuthenticator();
        Session session = Session.getInstance(props, auth);
        session.setDebug(true);

        MimeMessage msg = new MimeMessage(session);
        msg.setText(Text);
        msg.setSubject(Subject);
        msg.setFrom(new InternetAddress(Email));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(To));

        Transport transport = session.getTransport("smtps");
        transport.connect(Host, 465, Uname, Password);
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
    }
}
class SMTPAuthenticator extends Authenticator {

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication("mychaty1@gmail.com", "1234QWER");
    }
}
