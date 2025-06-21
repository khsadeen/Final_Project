package Emails;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.util.Random;


public class Email {
    static String fromEmail = "flexiiatmbanking@gmail.com"; // the app gmail sender
    static String password = "eyvq omtn oxkv hlto"; // Using App Password NOT Gmail password

    public static String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    // Sends an email using Jakarta Mail
    public static void sendVerificationCode(String toEmail, String code) throws MessagingException {

        // Email configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Authenticator for Gmail not used in the code directly
        Session session = Session.getInstance(props, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Your Verification Code");
        message.setText("Your verification code is: " + code);

        // Send the email
        Transport.send(message);
    }


    // General-purpose email sender
    public static void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        // SMTP configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create mail session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Send message
            Transport.send(message); //done with the process and send the email successfully
            System.out.println("✅ Email sent to " + toEmail);
        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email to " + toEmail);
            throw e;
        }
    }
}




