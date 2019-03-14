package bank;

import java.util.List;

public class Atm {
    private static Atm ourInstance = new Atm();
    public static Atm getInstance() {
        return ourInstance;
    }
    private Atm() {}

    private Account account;
    private boolean loggedIn = false;

    public boolean login(String accountNum, String password) {
        Account account = AccountsManager.getAccount(accountNum);
        if(account == null)
            return false;
        if(account.comparePassword(password)) {
            this.account = account;
            loggedIn = true;
            return true;
        }
        return false;
    }
    public void logout() {
        account = null;
        loggedIn = false;
    }

    public boolean withdraw(double amount) {
        if(!loggedIn)
            return false;
        boolean ret = account.withdraw(amount);
        if(ret)
            AccountsManager.saveAccount(account);
        return ret;
    }
    public void deposit(double amount) {
        if(!loggedIn)
            return;
        account.deposit(amount);
        AccountsManager.saveAccount(account);
    }
    public double inquire() {
        if(!loggedIn)
            return -1;
        return account.getBalance();
    }
    public List<Transaction> getHistory() {
        if(loggedIn)
            return account.getHistory();
        return null;
    }
    public void method() {

    }
}
