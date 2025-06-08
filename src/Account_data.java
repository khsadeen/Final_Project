import java.time.LocalDate;

public class Account_data {

    String accountNumber;
    String type;   // Withdraw or Deposit
    double amount;
    LocalDate dateOperation;

    public Account_data(String accountNumber, String type, double amount, LocalDate dateOperation) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.dateOperation = dateOperation ;
    }

    public String toString() {
        return accountNumber + " | " + type + " | " + amount + " | " + dateOperation;
    }

}