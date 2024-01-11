package accounts;

public enum AccountTypes {
    STANDARD("Standard"),
    SAVINGS("Savings"),
    CURRENT("Current");

    private final String name;

    AccountTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
