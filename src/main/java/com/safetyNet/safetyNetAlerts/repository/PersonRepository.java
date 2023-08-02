package com.safetyNet.safetyNetAlerts.repository;


import com.safetyNet.safetyNetAlerts.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Repository
public class PersonRepository {
	private List<Person> persons = new ArrayList<>();
	
    public List<Person> getAllPersons() {
        return persons;
    }

    
    public Person getPersonByNameAndFirstName(String firstName, String lastName) {
        List<Person> persons = getAllPersons();
        for (Person person : persons) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                return person;
            }
        }
        return null;
        
        
    }
    
    
    public List<Person> getPersonByNameAndFirstName2(String firstName, String lastName) {
        List<Person> allpersons = getAllPersons();
        
        return allpersons.stream()
            .filter(persons -> firstName.equals(persons.getFirstName()) && lastName.equals(persons.getLastName()) )
            .collect(Collectors.toList());
        
       
    }
    
    
    public List<Person> getPersonByCity(String city) {
        List<Person> allpersons = getAllPersons();
        
        return allpersons.stream()
            .filter(persons -> city.equals(persons.getCity()) )
            .collect(Collectors.toList());
        
       
    }
  
    /*
    public Optional<Person> getPersonByNameAndFirstName(String firstName, String lastName) {
        return getAllPersons().stream()
                .filter(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName))
                .findFirst();
    }
  */
    public Person addPerson(Person person) {
        List<Person> personn = getAllPersons();

        // Vérifier si la personne existe déjà
        for (Person existingPerson : personn) {
            if (existingPerson.equals(person)) {
                // Si la personne existe déjà, retourner null ou lever une exception
                System.out.println("La personne existe déjà, impossible de l'ajouter.");
                throw new IllegalArgumentException("La personne existe déjà");
            }
        }

        // Ajouter la nouvelle personne à la liste si elle n'existe pas déjà
        persons.add(person);
        
        return person;
    }

    
    
    public void deletePerson(String firstName, String lastName) {
        int originalSize = persons.size();

        persons = persons.stream()
                .filter(person -> !(person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)))
                .collect(Collectors.toList());

        if (persons.size() == originalSize) {
            throw new IllegalArgumentException("La personne n'existe pas, impossible de la supprimer.");
        }
    }



    public Person updatePerson(Person existingPerson, Person updatedPerson) {
        List<Person> persons = getAllPersons();
        int index = persons.indexOf(existingPerson);
        if (index != -1) {
            persons.set(index, updatedPerson);
            
            return updatedPerson;
        } else {
            throw new RuntimeException("Unable to find person to update.");
        }
    }
    
    public List<Person> getPersonsByAddress(String address) {
        return persons.stream()
            .filter(person -> person.getAddress().equals(address))
            .collect(Collectors.toList());
    }

    


	public List<Person> getPersons() {
		return persons;
	}


	public void setPersons(List<Person> persons) {
		this.persons = persons;
		
	}
}
