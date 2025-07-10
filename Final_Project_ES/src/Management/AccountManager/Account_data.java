package Management.AccountManager;

import java.time.LocalDate;

public class Account_data {

    public String accountNumber;
    public String type;   // Withdraw or Deposit
    public double amount;
    public LocalDate dateOperation;

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