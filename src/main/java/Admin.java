public class Admin extends User {
    private static Admin admin;

    private Admin(Person person, Role role) {
        super(person, role);
    }

    public static Admin getInstance() {
        if (admin == null) {
            Address address = new Address("Kasztanowa 16", "Lodz", "Poland", "28-990");
            admin = new Admin(new Person("c2d0efc8-5db9-448f-9de2-04551551c769","Bartosz", "Adminek", "1999-05-14", address, "bronko@mail.com", "926 616 547"), Role.ADMIN);
        }
        return admin;
    }
}
