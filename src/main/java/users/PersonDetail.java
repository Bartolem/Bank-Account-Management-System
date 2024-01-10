package users;

public enum PersonDetail {
    FIRST_NAME("First name"),
    LAST_NAME("Last name"),
    DATE_OF_BIRTH("Date of birth"),
    STREET_ADDRESS("Street address"),
    CITY("City"),
    COUNTRY("Country"),
    ZIP_CODE("Zip code"),
    EMAIL("E-mail"),
    PHONE_NUMBER("Phone number");

    private final String name;

    PersonDetail(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
