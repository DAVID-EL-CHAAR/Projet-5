package com.safetyNet.safetyNetAlerts.controller;

import com.safetyNet.safetyNetAlerts.model.PersonInfo2;
import com.safetyNet.safetyNetAlerts.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PersonInfoControllerTest {
    
    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonInfoController personInfoController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPersonsByName() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        PersonInfo2 person1 = new PersonInfo2();
        person1.setFirstName(firstName);
        person1.setLastName(lastName);
        List<PersonInfo2> persons = Arrays.asList(person1);
        when(personService.getPersonInfoByName(firstName, lastName)).thenReturn(persons);

        // Act
        ResponseEntity<List<PersonInfo2>> response = personInfoController.getPersonsByName(firstName, lastName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(persons, response.getBody());
    }
}
