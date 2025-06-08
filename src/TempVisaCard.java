public class TempVisaCard {
    public String cardNumber;
    public String pin;
    public double balance;
    public boolean isActive;

    public TempVisaCard(String cardNumber, String pin, double balance, boolean isActive) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
        this.isActive = isActive;
    }
}
