public class ParentingAgeException extends  Exception{
    public Person person;
    public ParentingAgeException (Person person,Person parent){
        super(String.format("It is hard to imagine that %s could be parent to %s.",
                person.getName(),parent.getName()));
        this.person = person;
    }
}
