package org.isep.javaprojectarchusers.Accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.isep.javaprojectarchusers.Portfolio;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * {@code SavingAccount} is a subclass of {@link Account} that add functionalities specifically for saving.
 * <p>It's main new thing is having {@code calculateRetirement}.</p>
 */
public class SavingAccount extends Account {
    /**This is the rent the account user has to pay per month?*/
    @JsonProperty("RENT")
    private float RENT ; // I don't think this is final. but I'll set this like that for now.
    private double INTEREST_RATES;
    private LocalDate retirementDate;
    private static ArrayList<SavingAccount> savingAccountArrayList = new ArrayList<>();

    public SavingAccount(@JsonProperty("userName") String userName, @JsonProperty("OVERDRAW_LIMIT") float OVERDRAW_LIMIT, @JsonProperty("balance") double balance, @JsonProperty("portfolio") String portfolio, @JsonProperty("RENT") float RENT) {
        super(userName, AccountType.SAVING ,OVERDRAW_LIMIT, balance, portfolio);
        this.RENT = RENT;
        savingAccountArrayList.add(this);
    }

    public double getINTEREST_RATES() {
        return INTEREST_RATES;
    }

    public void setINTEREST_RATES(double INTEREST_RATES) {
        this.INTEREST_RATES = INTEREST_RATES;
    }

    public @JsonProperty("RENT") float getRENT() {
        return RENT;
    }

    public void setRENT(@JsonProperty("RENT") float RENT) {
        this.RENT = RENT;
    }

    /** What is this class supposed to do? does it have to calculate how much is left for retirement?
     * Do we get users age?
     * @return Retirement money, if I understand this correctly?
     */
    public double calculateRetirement() {
        LocalDate givenDate = retirementDate;
        LocalDate now = LocalDate.now();
        long monthsUntilRetirement = ChronoUnit.MONTHS.between(givenDate, now);

        double balance = this.getBalance();

        for (long i = 1; i <= monthsUntilRetirement; i++) {
            balance -= RENT;
            if (monthsUntilRetirement % 12 == 0) {
                balance += (1 + (INTEREST_RATES / 100)) * balance;
            }
        }

        return balance;
    }

    public LocalDate getRetirementDate() {
        return retirementDate;
    }

    public void setRetirementDate(LocalDate retirementDate) {
        this.retirementDate = retirementDate;
    }

    public static ArrayList<SavingAccount> getSavingAccountArrayList() {
        return savingAccountArrayList;
    }
}
