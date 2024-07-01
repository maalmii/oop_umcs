import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Person implements  Serializable {
    private String name;
    private LocalDate dateBirth;
    private LocalDate dateDeath;
    private List<Person> parents= new ArrayList<>();
    public String getName() {
        return name;
    }

    public LocalDate getDateBirth() {
        return dateBirth;
    }

    public LocalDate getDateDeath() {
        return dateDeath;
    }
    public void addParent(Person parent){
        this.parents.add(parent);
    }

    public static Person fromCsvLine(String csvLine){
        String[] dataPerson = csvLine.split(",");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Person person = new Person();
        person.name = dataPerson[0];
        person.dateBirth = person.dateBirth.parse(dataPerson[1],formatter);
        if(!dataPerson[2].isEmpty())  person.dateDeath = person.dateDeath.parse(dataPerson[2],formatter);
        return person;
    }

    public static List<Person> fromCsv(String path){
        List<Person> peopleList = new ArrayList<>();
        Map<String,PersonWithParentsNames> personWithParentsNamesMap = new HashMap<>();
        String csvLine;
        try (FileReader fileReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(fileReader))
        {
            bufferedReader.readLine();
            while((csvLine = bufferedReader.readLine()) != null) {
                PersonWithParentsNames personWithParentsNames = PersonWithParentsNames.fromCsvLine(csvLine);
                Person person = personWithParentsNames.getPerson();
                personWithParentsNamesMap.put(person.name,personWithParentsNames);

                person.validateLifespan();
                person.validateAmbiguousPerson(peopleList);

                peopleList.add(person);
            }
            PersonWithParentsNames.linkRelatives(personWithParentsNamesMap);
            try {
                for(Person person: peopleList)
                    person.validateParentingAge();
            } catch (ParentingAgeException e) {
                Scanner scanner = new Scanner(System.in);
                System.out.println(e.getMessage());
                System.out.println("Confirm this case 'Y' or reject 'N' :");
                String response = scanner.next();
                if(!response.equals("Y") && !response.equals("y") ){
                    peopleList.remove(e.person);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NegativeLifespanException e) {
            System.err.println(e.getMessage());
        } catch (AmbiguousPersonException e) {
            System.err.println(e.getMessage());
        }
        return peopleList;
    }
    public void validateLifespan() throws NegativeLifespanException {
        if(this.dateDeath != null && this.dateDeath.isBefore(this.dateBirth)){
            throw new NegativeLifespanException(this);
        }
    }
    public void validateAmbiguousPerson(List<Person> peopleList) throws AmbiguousPersonException {
        for(Person person : peopleList) {
            if (this.name.equals(person.name)){
                throw new AmbiguousPersonException(this);
            }
        }
    }
    private void validateParentingAge() throws ParentingAgeException {
        for(Person parent: parents)
            if(ChronoUnit.YEARS.between(parent.dateBirth,
                    (parent.dateDeath == null ? LocalDate.now() : parent.dateDeath )) < 15 ||
                    (parent.dateDeath != null && parent.dateDeath.isBefore(this.dateBirth))){
                throw new ParentingAgeException(this,parent);
            }
    }
    public  static void toBinaryFile(List<Person> peopleList,String path){
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream))
        {
            objectOutputStream.writeObject(peopleList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  static List<Person> fromBinaryFile(String path) throws IOException, ClassNotFoundException {
        try (FileInputStream fileInputStream = new FileInputStream(path);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (List<Person>) objectInputStream.readObject();
        }
    }
    @Override
    public String toString() {
        return "Person { " +
                "name = \'" + name + '\'' +
                ", birthDate = " + dateBirth +
                ", deathDate = " + dateDeath +
                ", parents=" + parents +
                " }";
    }
}
