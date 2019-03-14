package bank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

final class AccountsManager {
    private AccountsManager() {
        throw new UnsupportedOperationException("Cannot instantiate this utility class");
    }
    static Account getAccount(String accountNum) {
        File accountFile = new File("accounts/"+accountNum+".ser");
        if(!accountFile.exists())
            return null;
        Account account = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(accountFile));
            account = (Account) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }
    static boolean saveAccount(Account account) {
        try {
            new File("accounts").mkdir();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("accounts/" + account.getAccountNum() + ".ser"));
            out.writeObject(account);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();

        return null;
    }
}
