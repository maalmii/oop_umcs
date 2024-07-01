public class AmbiguousPersonException extends Exception{
    public AmbiguousPersonException(Person person) {
        super("Ambiguous person: " + person.getName() + ", born in: " + person.getDateBirth() +
                ", dead in: " + person.getDateDeath());
    }
}
