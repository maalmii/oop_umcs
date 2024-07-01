import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Person> peopleList = Person.fromCsv("family.csv");
        for (Person person : peopleList) {
            System.out.println(person + "\n");
        }
    }
}