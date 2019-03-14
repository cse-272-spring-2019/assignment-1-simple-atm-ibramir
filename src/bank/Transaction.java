package bank;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {

    private static final long serialVersionUID = -5714600629435585164L;
    public enum TransactionType {
        DEPOSIT,
        WITHDRAW
    }

    private double amount, newBalance;
    private TransactionType type;
    private Date date;

    Transaction(double amount, TransactionType type, double newBalance) {
        this.amount = amount;
        this.type = type;
        this.date = new Date();
        this.newBalance = newBalance;
    }

    public double getAmount() {
        return amount;
    }
    public double getNewBalance() {
        return newBalance;
    }
    public TransactionType getType() {
        return type;
    }
    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Transaction type: " + type +
                "\nAmount: " + amount +
                "\nBalance: " + newBalance +
                "\nDate: " + date;
    }
}
