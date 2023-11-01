package com.safetyNet.safetyNetAlerts.controller;

import com.safetyNet.safetyNetAlerts.model.PersonInfo;
import com.safetyNet.safetyNetAlerts.service.FirestationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class PhoneAlertControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PhoneAlertController phoneAlertController; // Assuming the above method is in PhoneAlertController class

    @Mock
    private FirestationService firestationService;

    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(phoneAlertController).build();
    }

    @Test
    public void getPhoneAlertTest() throws Exception {
        String firestationNumber = "1";

       
        PersonInfo person1 = new PersonInfo();
        person1.setAddress("address1");
        person1.setFirstName("david");
        person1.setLastName("chaar");
        person1.setPhone("123-456-7890");

        PersonInfo person2 = new PersonInfo();
        person2.setAddress("address1");
        person2.setFirstName("martin");
        person2.setLastName("nom2");
        person2.setPhone("987-654-3210");

        List<PersonInfo> persons = Arrays.asList(person1, person2);

   
        when(firestationService.getPersonsByStationNumber(firestationNumber)).thenReturn(persons);

        
        mockMvc.perform(get("/phoneAlert")
                .param("firestation", firestationNumber)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['address1']", hasSize(2)))
                .andExpect(jsonPath("$.['address1'][0]", is("david chaar: 123-456-7890")))
                .andExpect(jsonPath("$.['address1'][1]", is("martin nom2: 987-654-3210")));
    }
}

