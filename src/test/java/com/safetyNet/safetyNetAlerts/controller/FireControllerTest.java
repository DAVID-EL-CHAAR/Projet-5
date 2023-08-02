package com.safetyNet.safetyNetAlerts.controller;

import com.safetyNet.safetyNetAlerts.model.Firestation;
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

public class FireControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @Mock
    private FirestationService firestationService;

    @InjectMocks
    private FireController fireController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fireController).build();
    }

    @Test
    public void getPersonAndFireStationTest() throws Exception {
        String address = "TestAddress";
        PersonInfo person = new PersonInfo();
        person.setAddress(address);
        List<PersonInfo> personInfoList = Arrays.asList(person);
        Firestation firestation = new Firestation();
        firestation.setAddress(address);
        List<Firestation> firestationList = Arrays.asList(firestation);

        when(personService.getPersonByAddressForFire(address)).thenReturn(personInfoList);
        when(firestationService.getFirestationByAddress(address)).thenReturn(firestationList);

        mockMvc.perform(get("/fire")
                .param("address", address)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Person[0].address", is(address)))
                .andExpect(jsonPath("$.fireStation[0].address", is(address)));

        verify(personService, times(1)).getPersonByAddressForFire(address);
        verify(firestationService, times(1)).getFirestationByAddress(address);
        verifyNoMoreInteractions(personService);
        verifyNoMoreInteractions(firestationService);
    }
}
