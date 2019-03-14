package app;

import bank.Atm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class MainController implements Initializable, AccountChanger {

    private interface EnterFunction {
        void enter();
    }

    @FXML
    private Pane rootPane;
    @FXML
    private Label mainText;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField numTextField;
    @FXML
    private Label accountNumText;

    private Set<Button> opButtons;
    @FXML
    private Button wButton;
    @FXML
    private Button dButton;
    @FXML
    private Button bButton;
    @FXML
    private Button hButton;
    @FXML
    private Button pButton;

    private TextInputControl writeTo;
    private EnterFunction enterFunction = null;
    private String accountNum;

    private Atm atm = Atm.getInstance();

    private EnterFunction login = () -> {
        if(accountNum == null || accountNum.equals(""))
            return;
        if(atm.login(accountNum,passwordField.getText())) {
            rootPane.getChildren().remove(passwordField);
            passwordField = null;
            mainText.setText("Welcome\nChoose an operation");
            showButtons(true);
            enterFunction = null;
            writeTo = numTextField;
        }
        else {
            mainText.setText("Wrong password");
            passwordField.setText("");
        }
    };
    private EnterFunction deposit = () -> {
        String num = numTextField.getText();
        if(num == null || num.equals("") || Integer.parseInt(num) == 0) {
            mainText.setText("Enter a valid amount to deposit");
            return;
        }
        atm.deposit(Double.parseDouble(num));
        mainText.setText("Deposit successful\nNew balance: " + atm.inquire());
        enterFunction = null;
        hideTextField();
    };
    private EnterFunction withdraw = () -> {
        String num = numTextField.getText();
        if(num == null || num.equals("") || Integer.parseInt(num) == 0) {
            mainText.setText("Enter a valid amount to deposit");
            return;
        }
        if(atm.withdraw(Double.parseDouble(numTextField.getText()))) {
            mainText.setText("Withdraw successful\nNew balance: " + atm.inquire());
        }
        else {
            mainText.setText("Withdraw failed - not enough money in this account\n" +
                    "Tried to withdraw: " + numTextField.getText() +
                    "\nCurrent balance: " + atm.inquire());
        }
        enterFunction = null;
        hideTextField();
    };

    private void showButtons(boolean val) {
        for(Node b: opButtons) {
            b.setVisible(val);
            b.setDisable(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLogin();
        initializeButtons();
    }
    private void initializeLogin() {
        if (passwordField == null) {
            passwordField = new PasswordField();
            passwordField.setFont(new Font(64));
            passwordField.setPrefWidth(240);
            passwordField.setPrefHeight(80);
            passwordField.setLayoutX(240);
            passwordField.setLayoutY(308);
            passwordField.setEditable(false);
            passwordField.setAlignment(Pos.CENTER);
            rootPane.getChildren().add(passwordField);
        }
        writeTo = passwordField;
        enterFunction = login;
        accountNumText.setText("Account: " + accountNum);
    }
    private void initializeButtons() {
        opButtons = new HashSet<>();
        opButtons.addAll(Arrays.asList(dButton, wButton, bButton, hButton));
    }

    private void showHistory() {
        Scene historyScene = new Scene(new HistoryPane(atm.getHistory()),400,600);
        Stage stage = new Stage();
        stage.setTitle("Transaction History");
        stage.setScene(historyScene);
        stage.show();
    }

    @FXML
    void opPressed(ActionEvent event) {
        Button b = (Button)event.getSource();
        if(b == hButton) {
            showHistory();
            return;
        }
        showButtons(false);
        b.setVisible(true);
        b.setDisable(true);
        pButton.setDisable(false);
        if(b == wButton) {
            enterFunction = withdraw;
            mainText.setText("Enter an amount to withdraw");
            hideTextField(false);
        }
        else if(b == dButton) {
            enterFunction = deposit;
            mainText.setText("Enter an amount to deposit");
            hideTextField(false);
        }
        else if(b == bButton) {
            mainText.setText("Current balance: " + atm.inquire());
            pButton.setDisable(false);
        }
    }
    @FXML
    void writeText(ActionEvent event) {
        if(writeTo == null || writeTo == passwordField && passwordField.getLength() >=4)
            return;
        writeTo.appendText(((Labeled)event.getSource()).getText());
    }
    @FXML
    void delChar() {
        if(writeTo.getLength()!=0)
            writeTo.deleteText(writeTo.getLength()-1,writeTo.getLength());
    }
    @FXML
    void enter() {
        if(enterFunction != null)
            enterFunction.enter();
    }
    @FXML
    void prev() {
        showButtons(true);
        pButton.setDisable(true);
        mainText.setText("Welcome\nChoose an operation");
        hideTextField();
        enterFunction = null;
    }

    private void hideTextField() {
        hideTextField(true);
    }
    private void hideTextField(boolean val) {
        numTextField.setText("");
        numTextField.setVisible(!val);
    }

    @FXML
    private void accountsPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("account.fxml"));
            Parent root = loader.load();
            ((AccountController) loader.getController()).accountChanger = this;
            Stage stage = new Stage();
            stage.setTitle("Change account");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeAccount(String accountNum) {
        prev();
        mainText.setText("Enter pin code");
        showButtons(false);
        this.accountNum = accountNum;
        atm.logout();
        initializeLogin();
    }
}
