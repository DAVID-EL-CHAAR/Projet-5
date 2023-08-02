package com.safetyNet.safetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetyNet.safetyNetAlerts.controller.PersonController;
import com.safetyNet.safetyNetAlerts.model.Person;
import com.safetyNet.safetyNetAlerts.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllPersons() throws Exception {
        Person person = new Person();
        // set person fields
        List<Person> persons = Arrays.asList(person);
        given(personService.getAllPersons()).willReturn(persons);

        mockMvc.perform(get("/person"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(persons)));
        verify(personService).getAllPersons();
    }

    @Test
    void getPersonByNameAndFirstName() throws Exception {
        Person person = new Person();
        // set person fields
        given(personService.getPersonByNameAndFirstName(anyString(), anyString())).willReturn(java.util.Optional.of(person));

        mockMvc.perform(get("/person/{firstName}/{lastName}", "firstName", "lastName"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(person)));
        verify(personService).getPersonByNameAndFirstName("firstName", "lastName");
    }

    @Test
    void addPerson() throws Exception {
        Person person = new Person();
        // set person fields
        given(personService.addPerson(any(Person.class))).willReturn(person);

        mockMvc.perform(post("/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(person)))
            .andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(person)));
        verify(personService).addPerson(any(Person.class));
    }

    @Test
    public void testDeletePerson() throws Exception {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";

        // Act & Assert
        mockMvc.perform(delete("/person/" + firstName + "/" + lastName))
                .andExpect(status().isOk());

        verify(personService, times(1)).deletePerson(firstName, lastName);
    }

    @Test
    public void testUpdatePerson() throws Exception {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";

        Person existingPerson = new Person();
        existingPerson.setFirstName(firstName);
        existingPerson.setLastName(lastName);

        Person updatedPerson = new Person();
        updatedPerson.setFirstName(firstName);
        updatedPerson.setLastName(lastName);
        // set any other updated fields on the updatedPerson object here

        when(personService.getPersonByNameAndFirstName(firstName, lastName)).thenReturn(Optional.of(existingPerson));
        when(personService.updatePerson(existingPerson, updatedPerson)).thenReturn(updatedPerson);

        // Act & Assert
        mockMvc.perform(put("/person/" + firstName + "/" + lastName)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(updatedPerson)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(updatedPerson)));

        verify(personService, times(1)).updatePerson(existingPerson, updatedPerson);
    }

    
}




    
    
    
