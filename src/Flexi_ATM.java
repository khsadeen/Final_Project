import jakarta.mail.MessagingException;
import javax.smartcardio.Card;
import javax.swing.*;
import java.util.List;

public class Flexi_ATM {
    public static void main(String[] args) throws MessagingException {
        MainMenu.screen();


//        ATMWelcomeScreen.show();
        CardsLoader car = new CardsLoader();
        List<Cards> cards = car.cardsListing();
//        Cards Stan = StandardLogin.loginWithAccount(cards);
//        Cards card = VisaCardInput.visaInput(cards);
//        String code = Email.generateVerificationCode();
//        Email.sendVerificationCode(card.email,code);
//        TempVisaCardCreator.AddDataToFile();
//        VisaCardInput.showVisaInput();
//        ATMWelcomeScreen.show();
    }
}

