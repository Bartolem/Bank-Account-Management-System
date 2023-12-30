package user_interface;

import accounts.*;
import file_manipulation.AccountsToCSV;
import users.User;

import java.math.BigDecimal;
import java.util.Scanner;

public class AccountOwnerPanel extends UserPanel {
    private final User user;
    private final Account account;

    public AccountOwnerPanel(String ID, Scanner scanner, int accountNumber) {
        super(ID, scanner);
        this.user = getUser();
        this.account = getBank().getAccount(accountNumber);
    }

    @Override
    public void start() {
        greetings();
        loop:while (true) {
            System.out.println("\nChoose action");
            System.out.println("(1) Deposit");
            System.out.println("(2) Withdraw");
            System.out.println("(3) Transfer");
            System.out.println("(4) Settings");
            System.out.println("(X) Log out");
            printCursor();
            String action = getScanner().nextLine();

            switch (action) {
                case "1" -> deposit();
                case "2" -> withdraw();
                case "3" -> transfer();
                case "4" -> settings();
                case "x", "X" -> {
                    break loop;
                }
            }
        }
    }

    private void deposit() {
        String action = "deposit";
        provideAmountMessage(action);
        BigDecimal amount = checkAmount(getScanner().nextLine());

        if (amount == null) {
            deposit();
        }

        if (account.deposit(amount)) {
            printBalanceAfterTransaction("+", amount);
            AccountsToCSV.write(getBank().getAllAccounts(), "accounts.csv");
        } else printFailedMessage(action);
    }

    private void withdraw() {
        String action = "withdraw";
        provideAmountMessage(action);
        BigDecimal amount = checkAmount(getScanner().nextLine());

        if (amount == null) {
            withdraw();
        }

        if (account.withdraw(amount)) {
            printBalanceAfterTransaction("-", amount);
            AccountsToCSV.write(getBank().getAllAccounts(), "accounts.csv");
        } else printFailedMessage(action);
    }

    private void transfer() {
        String action = "transfer";
        provideAmountMessage(action);
        BigDecimal amount = checkAmount(getScanner().nextLine());

        if (amount == null) {
            transfer();
        }

        System.out.print("Enter the account number to which you want to make the transfer: ");
        String accountNumber = getScanner().nextLine();

        if (!checkAccountNumber(accountNumber) || accountNumber.length() != 8) {
            transfer();
        }

        if (account.transfer(amount, Integer.parseInt(accountNumber))) {
            printBalanceAfterTransaction("-", amount);
            AccountsToCSV.write(getBank().getAllAccounts(), "accounts.csv");
        } else printFailedMessage(action);
    }

    private void settings() {

    }

    private void greetings() {
        System.out.println("\nWelcome " + user.getPerson().getFullName() + ".");
        System.out.println("\nYour account: " + account.getType() + " " +account.getAccountNumber());
    }

    private void provideAmountMessage(String action) {
        System.out.print("Provide the amount you want to " + action + ": ");
    }

    private void printBalanceAfterTransaction(String operator, BigDecimal amount) {
        System.out.println(operator + amount);
        System.out.println("Balance: " + account.getFormattedBalanceWithCurrency());
    }

    private void printFailedMessage(String action) {
        System.out.println(action.substring(0, 1).toUpperCase() + action.substring(1) + " process failed.");
    }

    private BigDecimal checkAmount(String amount) {
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException e) {
            System.out.println("Enter only numbers.");
            return null;
        }
    }
}
