package com.safetyNet.safetyNetAlerts.controller;

import com.safetyNet.safetyNetAlerts.model.MedicalRecord;
import com.safetyNet.safetyNetAlerts.model.Person;
import com.safetyNet.safetyNetAlerts.model.PersonInfo;
import com.safetyNet.safetyNetAlerts.repository.MedicalRecordRepository;
import com.safetyNet.safetyNetAlerts.service.PersonService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ChildAlertControllerTest {

    @InjectMocks
    private ChildAlertController childAlertController;

    @Mock
    private PersonService personService;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getChildAlert() {
        // Prepare the mock responses
        List<PersonInfo> children = new ArrayList<>();
        PersonInfo child = new PersonInfo();
        child.setFirstName("John");
        child.setLastName("Doe");
        children.add(child);

        List<Person> persons = new ArrayList<>();
        Person person = new Person();
        person.setFirstName("Jane");
        person.setLastName("Doe");
        persons.add(person);

        // Configure the mock behaviors
        when(personService.getChildrenByAddress(anyString())).thenReturn(children);
        when(personService.getPersonsByAddress(anyString())).thenReturn(persons);
        when(medicalRecordRepository.getMedicalRecordByName(anyString(), anyString())).thenReturn(Optional.of(new MedicalRecord()));

        // Call the method to test
        ResponseEntity<Map<String, Object>> response = childAlertController.getChildAlert("123 Street");

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("children"));
        assertTrue(response.getBody().containsKey("otherHouseholdMembers"));

        // Verify the interactions with the mocks
        verify(personService, times(1)).getChildrenByAddress("123 Street");
        verify(personService, times(1)).getPersonsByAddress("123 Street");
        verify(medicalRecordRepository, times(1)).getMedicalRecordByName("Jane", "Doe");
    }
}
