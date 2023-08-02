package com.safetyNet.safetyNetAlerts.controller;

import com.safetyNet.safetyNetAlerts.model.PersonInfo;
import com.safetyNet.safetyNetAlerts.service.FirestationService;
import com.safetyNet.safetyNetAlerts.service.PersonService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PhoneAlertControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @Mock
    private FirestationService firestationService;

    @InjectMocks
    private PhoneAlertController phoneAlertController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(phoneAlertController).build();
    }

    @Test
    public void getPhoneAlertTest() throws Exception {
        String station = "Station1";
        PersonInfo person1 = new PersonInfo();
        person1.setPhone("1234567890");
        PersonInfo person2 = new PersonInfo();
        person2.setPhone("0987654321");
        List<PersonInfo> personInfoList = Arrays.asList(person1, person2);

        when(firestationService.getPersonsByStationNumber(station)).thenReturn(personInfoList);

        mockMvc.perform(get("/phoneAlert")
                .param("firestation", station)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is("1234567890")))
                .andExpect(jsonPath("$[1]", is("0987654321")));

        verify(firestationService, times(1)).getPersonsByStationNumber(station);
        verifyNoMoreInteractions(firestationService);
    }
}
