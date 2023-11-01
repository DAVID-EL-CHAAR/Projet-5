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
import org.springframework.http.MediaType; // <--- Correct import
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChildAlertControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ChildAlertController childAlertController;

    @Mock
    private PersonService personService;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(childAlertController).build();
    }

    @Test
    public void getChildAlertUsingMockMvc() throws Exception {
        // Données simulées pour le test
        String address = "address1";
        
        List<PersonInfo> children = new ArrayList<>();
        PersonInfo child = new PersonInfo();
        child.setFirstName("Alex");
        child.setLastName("Martin");
        children.add(child);
        
        List<Person> persons = new ArrayList<>();
        Person person = new Person();
        person.setFirstName("Laura");
        person.setLastName("Martin");
        persons.add(person);

        // Configuration des mocks pour simuler les réponses des services
        when(personService.getChildrenByAddress(address)).thenReturn(children);
        when(personService.getPersonsByAddress(address)).thenReturn(persons);
        when(medicalRecordRepository.getMedicalRecordByName("Laura", "Martin")).thenReturn(Optional.of(new MedicalRecord()));

        // Effectuer la requête avec MockMvc
        mockMvc.perform(get("/childAlert")
                .param("address", address)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.children[0].firstName", is("Alex")))
                .andExpect(jsonPath("$.children[0].lastName", is("Martin")))
                .andExpect(jsonPath("$.otherHouseholdMembers[0].firstName", is("Laura")))
                .andExpect(jsonPath("$.otherHouseholdMembers[0].lastName", is("Martin")));

        // Vérification des interactions avec les services mockés
        verify(personService, times(1)).getChildrenByAddress(address);
        verify(personService, times(1)).getPersonsByAddress(address);
        verify(medicalRecordRepository, times(1)).getMedicalRecordByName("Laura", "Martin");
    }

}
