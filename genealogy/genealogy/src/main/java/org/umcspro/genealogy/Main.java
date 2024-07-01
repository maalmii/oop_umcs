package org.umcspro.genealogy;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Person> persons = Person.fromCsv("family.csv");

//        for(Person person: people)
//            System.out.println(person);

        System.out.println(persons.get(3).generateTree());

    }
}