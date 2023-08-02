/*package com.safetyNet.safetyNetAlerts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.safetyNet.safetyNetAlerts.model.MedicalRecord;
import com.safetyNet.safetyNetAlerts.model.Person;
import com.safetyNet.safetyNetAlerts.model.PersonInfo;
import com.safetyNet.safetyNetAlerts.model.PersonInfo2;
import com.safetyNet.safetyNetAlerts.repository.MedicalRecordRepository;
import com.safetyNet.safetyNetAlerts.repository.PersonRepository;

public class PersonServiceTest2 {
	
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
	        personRepository = Mockito.mock(PersonRepository.class);
	        medicalRecordRepository = Mockito.mock(MedicalRecordRepository.class);
	       
	        initPersonsAndMedicalRecords();
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

	
	
	
	
	 // Tests for getChildrenByAddress
    @Test
    public void getChildrenByAddressShouldReturnCorrectChildren() {
        // Mock data
        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setAddress("Address1");
        person1.setPhone("1234567890");
        
        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName("John");
        medicalRecord1.setLastName("Doe");
        medicalRecord1.setBirthdate("07/17/2005");

        // Mock behaviour
        when(personRepository.getPersonsByAddress("Address1")).thenReturn(Collections.singletonList(person1));
        when(medicalRecordRepository.getMedicalRecordByName("John", "Doe")).thenReturn(Optional.of(medicalRecord1));

        // Test the method
        List<PersonInfo> result = personService.getChildrenByAddress("Address1");
        
        // Verify results
        assertEquals(1, result.size());
        PersonInfo child = result.get(0);
        assertEquals("John", child.getFirstName());
        assertEquals("Doe", child.getLastName());
        assertEquals(18, child.getAge());
        assertEquals("Address1", child.getAddress());
        assertEquals("1234567890", child.getPhone());
    }



 // Tests for getPersonByAddress
 @Test
 public void getPersonByAddressShouldReturnCorrectPersons() {
     when(personRepository.getPersonsByAddress("Address1"))
         .thenReturn(Arrays.asList(person1, person2));
     when(medicalRecordRepository.getMedicalRecordByName("John", "Doe"))
         .thenReturn(Optional.of(medicalRecord1));
     when(medicalRecordRepository.getMedicalRecordByName("Jane", "Doe"))
         .thenReturn(Optional.of(medicalRecord2));

     List<PersonInfo> persons = personService.getPersonByAddress("Address1");

     assertEquals(2, persons.size());
     assertEquals("John", persons.get(0).getFirstName());
     assertEquals("Doe", persons.get(0).getLastName());
     assertEquals(23, persons.get(0).getAge());  
     assertEquals("Jane", persons.get(1).getFirstName());
     assertEquals("Doe", persons.get(1).getLastName());
     assertEquals(18, persons.get(1).getAge());  
 }

 
//Tests for getPersonByAddressForFire
@Test
public void getPersonByAddressForFireShouldReturnCorrectPersons() {
  when(personRepository.getPersonsByAddress("Address1"))
      .thenReturn(Arrays.asList(person1, person2));
  when(medicalRecordRepository.getMedicalRecordByName("John", "Doe"))
      .thenReturn(Optional.of(medicalRecord1));
  when(medicalRecordRepository.getMedicalRecordByName("Jane", "Doe"))
      .thenReturn(Optional.of(medicalRecord2));

  List<PersonInfo> persons = personService.getPersonByAddressForFire("Address1");

  assertEquals(2, persons.size());

  assertEquals("John", persons.get(0).getFirstName());
  assertEquals("Doe", persons.get(0).getLastName());
  assertEquals(23, persons.get(0).getAge());  
  assertEquals(Arrays.asList("medication1"), persons.get(0).getMedications());
  assertEquals(Arrays.asList("allergy1"), persons.get(0).getAllergies());

  assertEquals("Jane", persons.get(1).getFirstName());
  assertEquals("Doe", persons.get(1).getLastName());
  assertEquals(18, persons.get(1).getAge());  
  assertEquals(Arrays.asList("medication2"), persons.get(1).getMedications());
  assertEquals(Arrays.asList("allergy2"), persons.get(1).getAllergies());
}


 // Tests for getPersonInfoByName
 @Test
 public void getPersonInfoByNameShouldReturnCorrectPersons() {
     when(personRepository.getPersonByNameAndFirstName2("John", "Doe"))
         .thenReturn(Arrays.asList(person1));
     when(medicalRecordRepository.getMedicalRecordByName("John", "Doe"))
         .thenReturn(Optional.of(medicalRecord1));

     List<PersonInfo2> persons = personService.getPersonInfoByName("John", "Doe");

     assertEquals(1, persons.size());
     assertEquals("John", persons.get(0).getFirstName());
     assertEquals("Doe", persons.get(0).getLastName());
     assertEquals(23, persons.get(0).getAge());  
 }



}

*/

/*package com.safetyNet.safetyNetAlerts.service;

import com.safetyNet.safetyNetAlerts.model.MedicalRecord;
import com.safetyNet.safetyNetAlerts.model.Person;
import com.safetyNet.safetyNetAlerts.model.PersonInfo;
import com.safetyNet.safetyNetAlerts.model.PersonInfo2;
import com.safetyNet.safetyNetAlerts.repository.MedicalRecordRepository;
import com.safetyNet.safetyNetAlerts.repository.PersonRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class PersonServiceTest2 {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    public void setup() {
        this.medicalRecordRepository = Mockito.mock(MedicalRecordRepository.class);
        this.personRepository = Mockito.mock(PersonRepository.class);
        this.personService = new PersonService(this.personRepository, this.medicalRecordRepository);
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

*/