package transaction;

public enum TransactionTypes {
    DEPOSIT("Deposit", '+'), WITHDRAW("Withdraw", '-'), TRANSFER("Transfer", '-');

    private final String name;
    private final char operator;

    TransactionTypes(String name, char operator) {
        this.name = name;
        this.operator = operator;
    }

    public String getName() {
        return name;
    }

    public char getOperator() {
        return operator;
    }
}
