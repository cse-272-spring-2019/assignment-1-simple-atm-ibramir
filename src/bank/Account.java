package bank;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Account implements Serializable {

    private static final long serialVersionUID = -9080234921050289248L;
    private String accountNum;
    private byte[] hashedPassword;
    private byte[] salt;
    private double balance;
    private List<Transaction> history = new ArrayList<>();

    public static Account newAccount(String accountNum, String password) {
        return newAccount(accountNum, password, 0);
    }
    public static Account newAccount(String accountNum, String password, double balance) {
        return new Account(accountNum, password, balance);
    }
    private Account(String accountNum, String password) {
        this(accountNum, password, 0);
    }
    private Account(String accountNum, String password, double balance) {
        this.accountNum = accountNum;
        this.balance = balance;
        this.salt = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        this.hashedPassword = hashPassword(password);
        AccountsManager.saveAccount(this);
    }

    String getAccountNum() {
        return accountNum;
    }
    double getBalance() {
        return balance;
    }
    List<Transaction> getHistory() {
        return new ArrayList<>(history);
    }

    boolean withdraw(double amount) {
        if(amount <= balance) {
            balance -= amount;
            history.add(new Transaction(amount, Transaction.TransactionType.WITHDRAW, balance));
            return true;
        }
        return false;
    }
    void deposit(double amount) {
        balance += amount;
        history.add(new Transaction(amount, Transaction.TransactionType.DEPOSIT, balance));
    }
    boolean comparePassword(String password) {
        return Arrays.equals(hashPassword(password),hashedPassword);
    }

    private byte[] hashPassword(String password) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(salt);
            sha.update(password.getBytes(StandardCharsets.UTF_8));
            return sha.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
