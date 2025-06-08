
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;
import java.util.Random; // for the random number (the verification code we'll use)


public class Email {
    static String fromEmail = "flexiiatmbanking@gmail.com"; // your Gmail
    static String password = "eyvq omtn oxkv hlto"; // Using App Password NOT Gmail password

    //TODO LIST: verification code for (InputJavaCard signing in).
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

        // Authenticator for Gmail
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        // Compose the email
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Your Verification Code");
        message.setText("Your verification code is: " + code);

        // Send the email
        Transport.send(message);
    }

    public static void SendEmail(Cards card, String fullMessage) throws MessagingException {
        // إعدادات SMTP لـ Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // إنشاء جلسة بريد مع المُصادقة
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // إنشاء الرسالة
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(card.email));
            message.setSubject("✅ Process done successfully");
            message.setText(fullMessage);
            Transport.send(message);
            System.out.println("✅ Email sent to " + card.email);
        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email to " + card.email);
            throw e;
        }
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
            protected PasswordAuthentication getPasswordAuthentication() {
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
            Transport.send(message);
            System.out.println("✅ Email sent to " + toEmail);
        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email to " + toEmail);
            throw e;
        }
    }
    }




