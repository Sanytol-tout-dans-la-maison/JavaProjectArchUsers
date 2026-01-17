package org.isep.javaprojectarchusers.Accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.isep.javaprojectarchusers.Portfolio;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * {@code SavingAccount} is a subclass of {@link Account} that add functionalities specifically for saving.
 * <p>It's main new thing is having {@code calculateRetirement}.</p>
 */
public class SavingAccount extends Account {
    /**
     * This is the rent the account user has to pay per month?
     */
    float RENT;
    double INTEREST_RATES;
    Date retirementDate;

    public SavingAccount(String userName, String accountType, float OVERDRAW_LIMIT, double balance, Portfolio portfolio, float RENT, double interestRates, Date retirementDate) {
        super(userName, AccountType.SAVING, OVERDRAW_LIMIT, balance, portfolio);
        this.RENT = RENT;
        INTEREST_RATES = interestRates;
        this.retirementDate = retirementDate;
    }

    public @JsonProperty("RENT") float getRENT() {
        return RENT;
    }

    /**
     *Calculate retirement money based on interest rate and rent.
     * @return Retirement money.
     */
    public double calculateRetirement() {
        LocalDate givenDate = retirementDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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

    public static ArrayList<SavingAccount> getSavingAccountArrayList() {
        return savingAccountArrayList;
    }
}
