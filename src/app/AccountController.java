package app;

import bank.Account;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

class AccountController implements Initializable {

    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private TextField accountNumText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private TextField balanceText;

    AccountChanger accountChanger;
    private String[] accountsFileName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File accounts = new File("accounts");
        accounts.mkdir();
        accountsFileName = accounts.list();
        for(int i=0; i<accountsFileName.length; i++) {
            accountsFileName[i] = accountsFileName[i].substring(0,accountsFileName[i].length()-4);
        }
        choiceBox.setItems(FXCollections.observableArrayList(accountsFileName));

        accountNumText.setTextFormatter(new TextFormatter(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if(!validFileName(change.getControlNewText()))
                    return null;
                return change;
            }
        }));

        passwordText.setTextFormatter(new TextFormatter(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                String newText = change.getControlNewText();
                if(newText.equals(""))
                    return change;
                if(!newText.matches("\\d+") || change.isContentChange() && newText.length() > 4) {
                    return null;
                }
                return change;
            }
        }));

        balanceText.setTextFormatter(new TextFormatter(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                String newText = change.getControlNewText();
                if(newText.equals(""))
                    return change;
                if(!newText.matches("(\\d*)\\.?(\\d*)"))
                    return null;
                return change;
            }
        }));
    }

    private boolean validFileName(String fName) {
        try {
            File f = new File(fName);
            f.getCanonicalPath();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @FXML
    private void chooseAccount() {
        if(choiceBox.getValue() == null || choiceBox.getValue().equals(""))
            return;
        accountChanger.changeAccount((String)choiceBox.getValue());
        ((Stage)choiceBox.getScene().getWindow()).close();
    }
    @FXML
    private void newAccount() {
        String accountNum = accountNumText.getText();
        String password = passwordText.getText();
        String balance = balanceText.getText();
        if(accountNum.equals("") || !validPassword(password) || Arrays.asList(accountsFileName).contains(accountNum))
            return;
        if(balance.equals(""))
            balance = "0";
        Account.newAccount(accountNum, password, Double.parseDouble(balance));
        accountChanger.changeAccount(accountNum);
        ((Stage)choiceBox.getScene().getWindow()).close();
    }

    private boolean validPassword(String password) {
        return password.matches("\\d+") && password.length() == 4;
    }
}
