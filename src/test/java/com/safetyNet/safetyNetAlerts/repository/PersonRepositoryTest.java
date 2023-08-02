package com.safetyNet.safetyNetAlerts.repository;

import com.safetyNet.safetyNetAlerts.model.Person;
import com.safetyNet.safetyNetAlerts.repository.PersonRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PersonRepositoryTest {

    private PersonRepository personRepository;
    private Person person1;
    private Person person2;

    @BeforeEach
    public void setUp() {
        initPersons();
    }

    private void initPersons() {
        person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setCity("City1");
        person1.setAddress("Address1");

        person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("Doe");
        person2.setCity("City1");
        person2.setAddress("Address2");

        personRepository = new PersonRepository();
        personRepository.setPersons(new ArrayList<>());
        personRepository.addPerson(person1);
        personRepository.addPerson(person2);
    }

    @Test
    public void getAllPersonsShouldReturnAllPersons() {
        List<Person> persons = personRepository.getAllPersons();

        assertEquals(2, persons.size());
        assertEquals(person1, persons.get(0));
        assertEquals(person2, persons.get(1));
    }

    @Test
    public void getPersonByNameAndFirstNameShouldReturnCorrectPerson() {
        Person person = personRepository.getPersonByNameAndFirstName("John", "Doe");

        assertEquals(person1, person);
    }

    @Test
    public void getPersonByCityShouldReturnPersonsInCorrectCity() {
        List<Person> persons = personRepository.getPersonByCity("City1");

        assertEquals(2, persons.size());
        assertEquals(person1, persons.get(0));
        assertEquals(person2, persons.get(1));
    }

    @Test
    public void addPersonShouldAddPersonIfNotExists() {
        Person person3 = new Person();
        person3.setFirstName("Alice");
        person3.setLastName("Smith");

        Person addedPerson = personRepository.addPerson(person3);

        assertEquals(person3, addedPerson);
        assertEquals(3, personRepository.getAllPersons().size());
    }

    @Test
    public void addPersonShouldThrowExceptionIfExists() {
        assertThrows(IllegalArgumentException.class, () -> personRepository.addPerson(person1));
    }

    @Test
    public void deletePersonShouldDeleteCorrectPerson() {
        personRepository.deletePerson("John", "Doe");

        assertEquals(1, personRepository.getAllPersons().size());
        assertEquals(person2, personRepository.getAllPersons().get(0));
    }

    @Test
    public void deletePersonShouldThrowExceptionIfNotExists() {
        assertThrows(IllegalArgumentException.class, () -> personRepository.deletePerson("Alice", "Smith"));
    }

    @Test
    public void updatePersonShouldUpdateCorrectPerson() {
        Person updatedPerson = new Person();
        updatedPerson.setFirstName("John");
        updatedPerson.setLastName("Doe");
        updatedPerson.setCity("City2");

        Person person = personRepository.updatePerson(person1, updatedPerson);

        assertEquals(updatedPerson, person);
        assertEquals("City2", personRepository.getPersonByNameAndFirstName("John", "Doe").getCity());
    }
    
    @Test
    public void updatePersonShouldThrowExceptionIfNotExists() {
        Person updatedPerson = new Person();
        updatedPerson.setFirstName("Alic");
        updatedPerson.setLastName("Smit");

        assertThrows(RuntimeException.class, () -> personRepository.updatePerson(updatedPerson, updatedPerson));
    }
    
    @Test
    public void getPersonsByAddressShouldReturnPersonsAtCorrectAddress() {
        List<Person> persons = personRepository.getPersonsByAddress("Address1");

        assertEquals(1, persons.size());
        assertEquals(person1, persons.get(0));
    }
}
