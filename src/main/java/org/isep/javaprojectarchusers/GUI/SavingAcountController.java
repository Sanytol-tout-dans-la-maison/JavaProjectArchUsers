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

    SavingAccount account;

    @FXML
    public void initialize() {
        rentField.setTextFormatter(numberOnly());

        rentField.textProperty().addListener(
                (_,oldText,newText)->{
                    if (newText == null || newText.isBlank()) {
                        rentField.setText(oldText);
                    }
                }
        );

        rentField.focusedProperty().addListener(
                (_,_,focused) -> {
                    if (!focused) updateRentField();
                }
        );


        ratesFiels.setTextFormatter(numberOnly());
        ratesFiels.textProperty().addListener(
                (_,oldText,newText)->{
                    if (newText == null || newText.isBlank()) {
                        ratesFiels.setText(oldText);
                    }
                }
        );

        ratesFiels.focusedProperty().addListener(
                (_,_,focused) -> {
                    if (!focused) updateRateField();
                }
        );

        retirementDatePicker.valueProperty().addListener(
                (_,oldDate,newDate) -> {
                    if (newDate == null) {
                        retirementDatePicker.setValue(oldDate);
                    }
                }
        );

    }

    public void updateRentField() {
        account.setRENT(Float.parseFloat(rentField.getText()));
    }

    public void updateRateField() {
        account.setINTEREST_RATES(Double.parseDouble(ratesFiels.getText()));
    }

    public void updateRetirementDateField() {
        account.setRetirementDate(retirementDatePicker.getValue());
    }

    public void calculateRetirement() {
        retirementDatePicker.getValue();
        try {
            double afterRetirement= account.calculateRetirement();
            retirementResultLabel.setText(String.valueOf(afterRetirement));
        } catch (Exception e) {

        }

    }

    @Override
    public void setAccount(Account account) {
        this.account = (SavingAccount) account;
    }

    public void init() {
        account.setRetirementDate(LocalDate.now());
        account.setINTEREST_RATES(2.0);
    }

    public void updateDisplay() {


    }
}
