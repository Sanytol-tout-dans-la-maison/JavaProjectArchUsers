package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.isep.javaprojectarchusers.Accounts.Account;
import org.isep.javaprojectarchusers.Portfolio;
import org.isep.javaprojectarchusers.PortfolioManager;
import org.isep.javaprojectarchusers.Transaction;

import java.util.List;

public class AccountController {
    protected Account account;
    protected List accountList;
    protected Portfolio portfolio;
    protected PortfolioController portfolioController;


    protected TextFormatter<String> numberOnly() {
        return new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        });
    }


    @FXML
    protected ComboBox<Account> senderMenu;
    @FXML
    protected ComboBox<Account> receiverMenu;
    @FXML
    protected TextField amountField;
    @FXML
    protected Label accountNameLabel;
    @FXML
    protected Label balanceLabel;


    @FXML
    public void initialize() {
        amountField.setTextFormatter(numberOnly());

    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setPortfolioController(PortfolioController portfolioController) {
        this.portfolioController = portfolioController;
        this.portfolio = portfolioController.getPortfolio();
        accountList = portfolio.getAccountList();
    }

    public void transfer() {
        if (!amountField.getText().isBlank()) {
            PortfolioManager.transferMoney(
                    portfolio.getAddress(),
                    senderMenu.getValue(),
                    receiverMenu.getValue(),
                    Integer.parseInt(amountField.getText())

            );

            portfolioController.genAccountList();

            amountField.setText("");

            updateDisplay();


        }

    }

    private void addAllAccounts() {
        senderMenu.getItems().addAll(accountList);
        receiverMenu.getItems().addAll(accountList);
        senderMenu.getSelectionModel().select(account);
        receiverMenu.getSelectionModel().selectFirst();
    }

    public void updateDisplay() {
        addAllAccounts();
        accountNameLabel.setText(account.getUserName());
        balanceLabel.setText("Balance: " + account.getBalance());

    }

}
