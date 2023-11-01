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
        person1.setFirstName("David");
        person1.setLastName("Chaar");
        person1.setCity("City1");
        person1.setAddress("Rue Ordener");
        person1.setEmail("david@gmail.com");

        person2 = new Person();
        person2.setFirstName("Nom");
        person2.setLastName("Prenom");
        person2.setCity("City1");
        person2.setAddress("address2");
        person2.setEmail("email2@gmail.com");

        medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName("David");
        medicalRecord1.setLastName("Chaar");
        medicalRecord1.setBirthdate("07/17/2000");
        medicalRecord1.setMedications(Arrays.asList("med1", "med2"));
        medicalRecord1.setAllergies(Arrays.asList("allergy1", "allergy2"));

        medicalRecord2 = new MedicalRecord();
        medicalRecord2.setFirstName("Nom");
        medicalRecord2.setLastName("Prenom");
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
        when(personRepository.getPersonByNameAndFirstName("David", "Chaar")).thenReturn(person1);

        Person person = personService.getPersonByNameAndFirstName("David", "Chaar").get();

        assertEquals(person1, person);
    }

    @Test
    public void getEmailByCityShouldReturnCorrectEmails() {
        person1.setEmail("david@gmail.com");
        person2.setEmail("email2@gmail.com");
        
        when(personRepository.getPersonByCity("City1")).thenReturn(Arrays.asList(person1, person2));

        List<String> emails = personService.getEmailByCity("City1");

        assertEquals(2, emails.size());
        assertEquals("david@gmail.com", emails.get(0));
        assertEquals("email2@gmail.com", emails.get(1));
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
    public void getPersonsByAddress() {
        when(personRepository.getPersonsByAddress("Rue Ordener")).thenReturn(Arrays.asList(person1, person2));

        List<Person> persons = personService.getPersonsByAddress("Rue Ordener");

        assertEquals(2, persons.size());
        assertEquals(person1, persons.get(0));
        assertEquals(person2, persons.get(1));
    }
    
    @Test
    public void deletePersonShouldDeleteCorrectPerson() {
        when(personRepository.getAllPersons()).thenReturn(Arrays.asList(person2));
        doNothing().when(personRepository).deletePerson("David", "Chaar");

        personService.deletePerson("David", "Chaar");

        assertEquals(1, personService.getAllPersons().size());
        assertEquals(person2, personService.getAllPersons().get(0));
    }
    
    @Test
    public void getPersonsByAddressShouldReturnPersonsAtCorrectAddress() {
        when(personRepository.getPersonsByAddress("Rue Ordener")).thenReturn(Arrays.asList(person1));

        List<Person> persons = personService.getPersonsByAddress("Rue Ordener");

        assertEquals(1, persons.size());
        assertEquals(person1, persons.get(0));
    }


    @Test
    public void updatePersonS() {
        Person existingPerson = new Person();
        existingPerson.setFirstName("David");
        existingPerson.setLastName("Chaar");
        existingPerson.setCity("City1");
        existingPerson.setAddress("Rue Ordener");
        existingPerson.setEmail("david@gmail.com");

        Person updatedPerson = new Person();
        updatedPerson.setFirstName("David");
        updatedPerson.setLastName("Chaar");
        updatedPerson.setCity("City2");
        updatedPerson.setAddress("Rue Ordener");
        updatedPerson.setEmail("david@gmail.com");

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
        when(this.medicalRecordRepository.getMedicalRecordByName(anyString(), anyString()))
            .thenReturn(Optional.of(new MedicalRecord()));

        List<Person> persons = new ArrayList<>();
        Person person = new Person();
        person.setFirstName("David");
        person.setLastName("Chaar");
        persons.add(person);

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setBirthdate("01/01/2006");

        when(personRepository.getPersonsByAddress("Rue Ordener")).thenReturn(persons);
        when(medicalRecordRepository.getMedicalRecordByName(anyString(), anyString())).thenReturn(Optional.of(medicalRecord));

        List<PersonInfo> children = personService.getChildrenByAddress("Rue Ordener");

        assertEquals(1, children.size());
        assertEquals("David", children.get(0).getFirstName());
        assertEquals("Chaar", children.get(0).getLastName());

        verify(personRepository, times(1)).getPersonsByAddress("Rue Ordener");
        verify(medicalRecordRepository, times(1)).getMedicalRecordByName("David", "Chaar");
    }

    @Test
    public void testGetPersonByAddressForFire() {
        Person person = new Person();
        person.setFirstName("David");
        person.setLastName("Chaar");
        person.setAddress("Rue Ordener");
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("David");
        medicalRecord.setLastName("Chaar");
        medicalRecord.setBirthdate("01/01/2000");
        medicalRecord.setMedications(Arrays.asList("Med1", "Med2"));
        medicalRecord.setAllergies(Arrays.asList("Allergy1", "Allergy2"));

        when(personRepository.getPersonsByAddress("Rue Ordener")).thenReturn(Arrays.asList(person));
        when(medicalRecordRepository.getMedicalRecordByName("David", "Chaar")).thenReturn(Optional.of(medicalRecord));

        List<PersonInfo> result = personService.getPersonByAddressForFire("Rue Ordener");

        assertEquals(1, result.size());
        PersonInfo personInfo = result.get(0);
        assertEquals("David", personInfo.getFirstName());
        assertEquals("Chaar", personInfo.getLastName());
        assertEquals(23, personInfo.getAge());  
        assertEquals(Arrays.asList("Med1", "Med2"), personInfo.getMedications());
        assertEquals(Arrays.asList("Allergy1", "Allergy2"), personInfo.getAllergies());

        verify(personRepository).getPersonsByAddress("Rue Ordener");
        verify(medicalRecordRepository).getMedicalRecordByName("David", "Chaar");
    }

    @Test
    public void testGetPersonInfoByName() {
        Person person = new Person();
        person.setFirstName("David");
        person.setLastName("Chaar");
        person.setAddress("Rue Ordener");
        person.setEmail("david@gmail.com");
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("David");
        medicalRecord.setLastName("Chaar");
        medicalRecord.setBirthdate("01/01/2000");
        medicalRecord.setMedications(Arrays.asList("Med1", "Med2"));
        medicalRecord.setAllergies(Arrays.asList("Allergy1", "Allergy2"));

        when(personRepository.getPersonByNameAndFirstName2("David", "Chaar")).thenReturn(Arrays.asList(person));
        when(medicalRecordRepository.getMedicalRecordByName("David", "Chaar")).thenReturn(Optional.of(medicalRecord));

        List<PersonInfo2> result = personService.getPersonInfoByName("David", "Chaar");

        assertEquals(1, result.size());
        PersonInfo2 personInfo = result.get(0);
        assertEquals("David", personInfo.getFirstName());
        assertEquals("Chaar", personInfo.getLastName());
        assertEquals("Rue Ordener", personInfo.getAddress());
        assertEquals("david@gmail.com", personInfo.getEmail());
        assertEquals(23, personInfo.getAge());  
        assertEquals(Arrays.asList("Med1", "Med2"), personInfo.getMedications());
        assertEquals(Arrays.asList("Allergy1", "Allergy2"), personInfo.getAllergies());

        verify(personRepository).getPersonByNameAndFirstName2("David", "Chaar");
        verify(medicalRecordRepository).getMedicalRecordByName("David", "Chaar");
    }

   
    
    
}
