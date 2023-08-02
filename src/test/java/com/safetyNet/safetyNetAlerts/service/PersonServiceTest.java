package com.safetyNet.safetyNetAlerts.service;

import com.safetyNet.safetyNetAlerts.model.Person;
import com.safetyNet.safetyNetAlerts.model.PersonInfo;
import com.safetyNet.safetyNetAlerts.model.PersonInfo2;
import com.safetyNet.safetyNetAlerts.model.MedicalRecord;
import com.safetyNet.safetyNetAlerts.repository.PersonRepository;
import com.safetyNet.safetyNetAlerts.repository.MedicalRecordRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @MockBean
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private PersonService personService;

    private Person person1;
    private Person person2;
    private MedicalRecord medicalRecord1;
    private MedicalRecord medicalRecord2;

    @BeforeEach
    public void setUp() {
    	MockitoAnnotations.openMocks(this);
        initPersonsAndMedicalRecords();
        this.medicalRecordRepository = Mockito.mock(MedicalRecordRepository.class);
        this.personRepository = Mockito.mock(PersonRepository.class);
        this.personService = new PersonService(this.personRepository, this.medicalRecordRepository);
    }

    private void initPersonsAndMedicalRecords() {
        person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setCity("City1");
        person1.setAddress("Address1");
        person1.setEmail("john@example.com");

        person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("Doe");
        person2.setCity("City1");
        person2.setAddress("Address2");
        person2.setEmail("jane@example.com");

        medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName("John");
        medicalRecord1.setLastName("Doe");
        medicalRecord1.setBirthdate("07/17/2000");
        medicalRecord1.setMedications(Arrays.asList("med1", "med2"));
        medicalRecord1.setAllergies(Arrays.asList("allergy1", "allergy2"));

        medicalRecord2 = new MedicalRecord();
        medicalRecord2.setFirstName("Jane");
        medicalRecord2.setLastName("Doe");
        medicalRecord2.setBirthdate("07/17/2005");
        medicalRecord2.setMedications(Arrays.asList("med3", "med4"));
        medicalRecord2.setAllergies(Arrays.asList("allergy3", "allergy4"));

    }


    @Test
    public void getAllPersonsShouldReturnAllPersons() {
        when(personRepository.getAllPersons()).thenReturn(Arrays.asList(person1, person2));

        List<Person> persons = personService.getAllPersons();

        assertEquals(2, persons.size());
        assertEquals(person1, persons.get(0));
        assertEquals(person2, persons.get(1));
    }

    @Test
    public void getPersonByNameAndFirstNameShouldReturnCorrectPerson() {
        when(personRepository.getPersonByNameAndFirstName("John", "Doe")).thenReturn(person1);

        Person person = personService.getPersonByNameAndFirstName("John", "Doe").get();


        assertEquals(person1, person);
    }

    
    // ... autres méthodes de test ici ...

    @Test
    public void getEmailByCityShouldReturnCorrectEmails() {
        person1.setEmail("john.doe@email.com");
        person2.setEmail("jane.doe@email.com");
        
        when(personRepository.getPersonByCity("City1")).thenReturn(Arrays.asList(person1, person2));

        List<String> emails = personService.getEmailByCity("City1");

        assertEquals(2, emails.size());
        assertEquals("john.doe@email.com", emails.get(0));
        assertEquals("jane.doe@email.com", emails.get(1));
    }


    @Test
    public void addPersonShouldAddPersonIfNotExists() {
        Person person3 = new Person();
        person3.setFirstName("Alice");
        person3.setLastName("Smith");

        when(personRepository.addPerson(person3)).thenReturn(person3);
        when(personRepository.getAllPersons()).thenReturn(Arrays.asList(person1, person2, person3));

        Person addedPerson = personService.addPerson(person3);

        assertEquals(person3, addedPerson);
        assertEquals(3, personService.getAllPersons().size());
    }

    @Test
    public void deletePersonShouldDeleteCorrectPerson() {
        when(personRepository.getAllPersons()).thenReturn(Arrays.asList(person2));
        doNothing().when(personRepository).deletePerson("John", "Doe");

        personService.deletePerson("John", "Doe");

        assertEquals(1, personService.getAllPersons().size());
        assertEquals(person2, personService.getAllPersons().get(0));
    }

  
    
    @Test
    public void getPersonsByAddressShouldReturnPersonsAtCorrectAddress() {
        when(personRepository.getPersonsByAddress("Address1")).thenReturn(Arrays.asList(person1));

        List<Person> persons = personService.getPersonsByAddress("Address1");

        assertEquals(1, persons.size());
        assertEquals(person1, persons.get(0));
    }

    @Test
    public void updatePersonShouldUpdateCorrectPerson() {
        Person existingPerson = new Person();
        existingPerson.setFirstName("John");
        existingPerson.setLastName("Doe");
        existingPerson.setCity("City1");

        Person updatedPerson = new Person();
        updatedPerson.setFirstName("John");
        updatedPerson.setLastName("Doe");
        updatedPerson.setCity("City2");

        when(personRepository.updatePerson(existingPerson, updatedPerson)).thenReturn(updatedPerson);

        Person returnedPerson = personService.updatePerson(existingPerson, updatedPerson);

        assertEquals(updatedPerson, returnedPerson);
        assertEquals("City2", returnedPerson.getCity());
    }

    @Test
    public void calculateAgeShouldReturnCorrectAge() {
        String birthdate = "07/17/2000";
        int expectedAge = 23;  
        assertEquals(expectedAge, personService.calculateAge(birthdate));
    }


    @Test
    void getChildrenByAddress() {
        // Prepare the mock responses
    	
    	 when(this.medicalRecordRepository.getMedicalRecordByName(anyString(), anyString()))
         .thenReturn(Optional.of(new MedicalRecord()));

        List<Person> persons = new ArrayList<>();
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        persons.add(person);
        
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setBirthdate("01/01/2006");

        // Configure the mock behaviors
        when(personRepository.getPersonsByAddress(anyString())).thenReturn(persons);
        when(medicalRecordRepository.getMedicalRecordByName(anyString(), anyString())).thenReturn(Optional.of(medicalRecord));

        // Call the method to test
        List<PersonInfo> children = personService.getChildrenByAddress("123 Street");

        // Verify the response
        assertEquals(1, children.size());
        assertEquals("John", children.get(0).getFirstName());
        assertEquals("Doe", children.get(0).getLastName());

        // Verify the interactions with the mocks
        verify(personRepository, times(1)).getPersonsByAddress("123 Street");
        verify(medicalRecordRepository, times(1)).getMedicalRecordByName("John", "Doe");
    }

  
    @Test
    public void testGetPersonByAddress() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate("01/01/2000");

        when(personRepository.getPersonsByAddress("123 Main St")).thenReturn(Arrays.asList(person));
        when(medicalRecordRepository.getMedicalRecordByName("John", "Doe")).thenReturn(Optional.of(medicalRecord));

        List<PersonInfo> result = personService.getPersonByAddress("123 Main St");

        assertEquals(1, result.size());
        PersonInfo personInfo = result.get(0);
        assertEquals("John", personInfo.getFirstName());
        assertEquals("Doe", personInfo.getLastName());
        assertEquals(23, personInfo.getAge());  // Assuming test is run in 2023

        verify(personRepository).getPersonsByAddress("123 Main St");
        verify(medicalRecordRepository).getMedicalRecordByName("John", "Doe");
    }
    
    @Test
    public void testGetPersonByAddressForFire() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate("01/01/2000");
        medicalRecord.setMedications(Arrays.asList("Med1", "Med2"));
        medicalRecord.setAllergies(Arrays.asList("Allergy1", "Allergy2"));

        when(personRepository.getPersonsByAddress("123 Main St")).thenReturn(Arrays.asList(person));
        when(medicalRecordRepository.getMedicalRecordByName("John", "Doe")).thenReturn(Optional.of(medicalRecord));

        List<PersonInfo> result = personService.getPersonByAddressForFire("123 Main St");

        assertEquals(1, result.size());
        PersonInfo personInfo = result.get(0);
        assertEquals("John", personInfo.getFirstName());
        assertEquals("Doe", personInfo.getLastName());
        assertEquals(23, personInfo.getAge());  // Assuming test is run in 2023
        assertEquals(Arrays.asList("Med1", "Med2"), personInfo.getMedications());
        assertEquals(Arrays.asList("Allergy1", "Allergy2"), personInfo.getAllergies());

        verify(personRepository).getPersonsByAddress("123 Main St");
        verify(medicalRecordRepository).getMedicalRecordByName("John", "Doe");
    }
    
    @Test
    public void testGetPersonInfoByName() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setEmail("john.doe@example.com");
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate("01/01/2000");
        medicalRecord.setMedications(Arrays.asList("Med1", "Med2"));
        medicalRecord.setAllergies(Arrays.asList("Allergy1", "Allergy2"));

        when(personRepository.getPersonByNameAndFirstName2("John", "Doe")).thenReturn(Arrays.asList(person));
        when(medicalRecordRepository.getMedicalRecordByName("John", "Doe")).thenReturn(Optional.of(medicalRecord));

        List<PersonInfo2> result = personService.getPersonInfoByName("John", "Doe");

        assertEquals(1, result.size());
        PersonInfo2 personInfo = result.get(0);
        assertEquals("John", personInfo.getFirstName());
        assertEquals("Doe", personInfo.getLastName());
        assertEquals("123 Main St", personInfo.getAddress());
        assertEquals("john.doe@example.com", personInfo.getEmail());
        assertEquals(23, personInfo.getAge());  // Assuming test is run in 2023
        assertEquals(Arrays.asList("Med1", "Med2"), personInfo.getMedications());
        assertEquals(Arrays.asList("Allergy1", "Allergy2"), personInfo.getAllergies());

        verify(personRepository).getPersonByNameAndFirstName2("John", "Doe");
        verify(medicalRecordRepository).getMedicalRecordByName("John", "Doe");
    }


    
    
    
}
