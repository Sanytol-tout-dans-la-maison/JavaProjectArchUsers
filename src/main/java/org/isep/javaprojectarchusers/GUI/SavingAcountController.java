package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.isep.javaprojectarchusers.Accounts.Account;
import org.isep.javaprojectarchusers.Accounts.SavingAccount;

import java.time.LocalDate;

public class SavingAcountController extends AccountController {
    @FXML
    public TextField rentField;
    @FXML
    public TextField ratesFiels;
    @FXML
    public DatePicker retirementDatePicker;
    @FXML
    public Label retirementResultLabel;
    private SavingAccount savingAccount;

    @FXML
    public void initialize() {
        rentField.setTextFormatter(numberOnly());

        rentField.textProperty().addListener(
                (_, oldText, newText) -> {
                    if (newText == null || newText.isBlank()) {
                        rentField.setText(oldText);
                    }
                }
        );

        rentField.focusedProperty().addListener(
                (_, _, focused) -> {
                    if (!focused) updateRentField();
                }
        );


        ratesFiels.setTextFormatter(numberOnly());
        ratesFiels.textProperty().addListener(
                (_, oldText, newText) -> {
                    if (newText == null || newText.isBlank()) {
                        ratesFiels.setText(oldText);
                    }
                }
        );

        ratesFiels.focusedProperty().addListener(
                (_, _, focused) -> {
                    if (!focused) updateRateField();
                }
        );

        retirementDatePicker.valueProperty().addListener(
                (_, oldDate, newDate) -> {
                    if (newDate == null) {
                        retirementDatePicker.setValue(oldDate);
                    }
                }
        );

    }

    public void updateRentField() {
        savingAccount.setRENT(Float.parseFloat(rentField.getText()));
    }

    public void updateRateField() {
        savingAccount.setINTEREST_RATES(Double.parseDouble(ratesFiels.getText()));
    }

    public void updateRetirementDateField() {
        savingAccount.setRetirementDate(retirementDatePicker.getValue());
    }

    public void calculateRetirement() {
        retirementDatePicker.getValue();
        try {
            double afterRetirement = savingAccount.calculateRetirement();
            retirementResultLabel.setText(String.valueOf(afterRetirement));
        } catch (Exception e) {

        }

    }

    public void init() {
        savingAccount.setRetirementDate(LocalDate.now());
        savingAccount.setINTEREST_RATES(2.0);
    }

    @Override
    public void updateDisplay() {
        super.updateDisplay();
        rentField.setText(String.valueOf(savingAccount.getRENT()));
        ratesFiels.setText(String.valueOf(savingAccount.getINTEREST_RATES()));
        retirementDatePicker.setValue(savingAccount.getRetirementDate());


    }

    @Override
    public void setAccount(Account account) {
        super.setAccount(account);
        savingAccount = (SavingAccount) account;
    }
}
