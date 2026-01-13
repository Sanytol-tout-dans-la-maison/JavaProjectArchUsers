package org.isep.javaprojectarchusers.Accounts;

import org.isep.javaprojectarchusers.Portfolio;

/**
 * {@code SavingAccount} is a subclass of {@link Account} that add functionalities specifically for saving.
 * <p>It's main new thing is having {@code calculateRetirement}.</p>
 */
public class SavingAccount extends Account {
    /**This is the rent the account user has to pay per month?*/
    final float RENT; // I don't think this is final. but I'll set this like that for now.

    public SavingAccount(String userName, String accountType, float OVERDRAW_LIMIT, double balance, Portfolio portfolio, float RENT) {
        super(userName, AccountType.SAVING, OVERDRAW_LIMIT, balance, portfolio);
        this.RENT = RENT;
    }



    /** What is this class supposed to do? does it have to calculate how much is left for retirement?
     * Do we get users age?
     * @return Retirement money, if I understand this correctly?
     */
    public double calculateRetirement() {

        return 0.0;
    }
}
