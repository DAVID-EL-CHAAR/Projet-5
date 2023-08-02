package com.safetyNet.safetyNetAlerts.service;

import com.safetyNet.safetyNetAlerts.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonServiceInterface {
    List<Person> getAllPersons();
    Optional<Person> getPersonByNameAndFirstName(String firstName, String lastName);
    Person createPerson(Person person);
    Person updatePerson(Person person);
    void deletePerson(String firstName, String lastName);
}