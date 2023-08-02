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
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class FloodControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @Mock
    private FirestationService firestationService;

    @InjectMocks
    private FloodController floodController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(floodController).build();
    }

    @Test
    public void getFloodInfoTest() throws Exception {
        String stationNumber = "1";
        String address = "TestAddress";
        String stationAddress = "TestStationAddress";

        List<String> stations = Collections.singletonList(stationNumber);
        List<String> addresses = Collections.singletonList(address);
        PersonInfo person = new PersonInfo();
        person.setAddress(address);
        List<PersonInfo> personInfoList = Collections.singletonList(person);

        when(firestationService.getAddressesByStationNumbers(stations)).thenReturn(addresses);
        when(personService.getPersonByAddressForFire(address)).thenReturn(personInfoList);
        when(firestationService.getstationByAddress(address)).thenReturn(stationAddress);

        mockMvc.perform(get("/flood/stations")
                .param("stations", stationNumber)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$."+address+".stationNumber", is(stationAddress)))
                .andExpect(jsonPath("$."+address+".persons[0].address", is(address)));

        verify(firestationService, times(1)).getAddressesByStationNumbers(stations);
        verify(personService, times(1)).getPersonByAddressForFire(address);
        verify(firestationService, times(1)).getstationByAddress(address);
        verifyNoMoreInteractions(firestationService);
        verifyNoMoreInteractions(personService);
    }
}
