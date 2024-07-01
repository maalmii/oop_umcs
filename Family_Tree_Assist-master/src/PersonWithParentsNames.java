import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonWithParentsNames {
    private Person person;
    List<String> parentsNames;
    public Person getPerson() {
        return person;
    }
    public PersonWithParentsNames( Person person, List<String> parentsNames) {
        this.person=person;
        this.parentsNames = parentsNames;
    }

    public static PersonWithParentsNames fromCsvLine(String csvLine) {
        String[] dataPerson = csvLine.split(",",-1);
        Person person = Person.fromCsvLine(csvLine);
        List<String> parentsNames = new ArrayList<>();
        for(int i = 3;i<=4;++i){
            if(!dataPerson[i].isEmpty()){
                parentsNames.add(dataPerson[i]);
            }
        }
        return new PersonWithParentsNames(person,parentsNames);
    }
    public static void linkRelatives( Map<String,PersonWithParentsNames> map){
        for(PersonWithParentsNames helperChild : map.values()){
            for(String parentName : helperChild.parentsNames){
                PersonWithParentsNames helperParent = map.get(parentName);
                Person parent = helperParent.getPerson();
                helperChild.getPerson().addParent(parent);
            }
        }

    }
}
