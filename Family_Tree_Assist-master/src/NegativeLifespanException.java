public class NegativeLifespanException extends Exception {
    public NegativeLifespanException(Person person) {
        super("Person: " + person.getName() + ", born in: " + person.getDateBirth() +
                ", died in: " + person.getDateDeath() +
                ". Died before Birthdate!!!");
    }

}
