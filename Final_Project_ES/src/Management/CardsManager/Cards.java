package Management.CardsManager;

public class
Cards {
    public String cardNumber;
    public String cvv;
    public String expiryDate;
    public String accountNumber;
    public String password;
    public double balance;
    public String ownerName;
    public String email;
    public String phone;
    public double overDraftLimit;
    public String gmailPassword;
    public String bankBranch;
    public String unLocked;

    public Cards(String cardNumber, String cvv, String expiryDate,
                 String accountNumber, String password, double balance,
                 String ownerName, String email, String phone, double overDraftLimit, String gmailPassword, String bankBranch, String unLocked) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = balance;
        this.ownerName = ownerName;
        this.email = email;
        this.phone = phone;
        this.overDraftLimit = overDraftLimit;
        this.gmailPassword = gmailPassword;
        this.bankBranch = bankBranch;
        this.unLocked = unLocked;

    }

    public void printCardInfo() {
        System.out.println("Card for " + ownerName + " , Account: " + accountNumber + " , Balance: " + balance);
    }
}

