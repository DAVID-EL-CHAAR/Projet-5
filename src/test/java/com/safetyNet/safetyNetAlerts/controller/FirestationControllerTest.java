package com.safetyNet.safetyNetAlerts.controller;


import com.safetyNet.safetyNetAlerts.model.Firestation;
import com.safetyNet.safetyNetAlerts.model.PersonInfo;
import com.safetyNet.safetyNetAlerts.service.FirestationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(FirestationController.class)
public class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FirestationService firestationService;

    private Firestation firestation;

    @BeforeEach
    public void setUp() {
        firestation = new Firestation();
        firestation.setStation("station1");
        firestation.setAddress("address1");
    }

    @Test
    public void getAllFirestationsTest() throws Exception {
        List<Firestation> allFirestations = Arrays.asList(firestation);

        when(firestationService.getAllFirestations()).thenReturn(allFirestations);

        mockMvc.perform(get("/firestation/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].station", is(firestation.getStation())));
    }

    @Test
    public void addFirestationTest() throws Exception {
        when(firestationService.addFirestation(org.mockito.ArgumentMatchers.<Firestation>any())).thenReturn(firestation);

        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"address\": \"address1\", \"station\": \"station1\" }"))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteFirestationTest() throws Exception {
        doNothing().when(firestationService).deleteFirestation(firestation.getAddress());

        mockMvc.perform(delete("/firestation/" + firestation.getAddress()))
                .andExpect(status().isOk());

        verify(firestationService, times(1)).deleteFirestation(firestation.getAddress());
    }

    @Test
    public void deleteFirestationByStationTest() throws Exception {
        doNothing().when(firestationService).deleteFirestationByStation(firestation.getStation());

        mockMvc.perform(delete("/firestation/station/" + firestation.getStation()))
                .andExpect(status().isOk());

        verify(firestationService, times(1)).deleteFirestationByStation(firestation.getStation());
    }

    @Test
    public void updateFirestationTest() throws Exception {
        when(firestationService.updateFirestation(org.mockito.ArgumentMatchers.<Firestation>any(), org.mockito.ArgumentMatchers.<Firestation>any())).thenReturn(firestation);

        mockMvc.perform(put("/firestation/" + firestation.getStation() + "/" + firestation.getAddress())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"address\": \"address1\", \"station\": \"station1\" }"))
                .andExpect(status().isOk());

        verify(firestationService, times(1)).updateFirestation(org.mockito.ArgumentMatchers.<Firestation>any(), org.mockito.ArgumentMatchers.<Firestation>any());
    }


    // Note: You may need to add additional setup in the testGetPersonsByStationNumber method
    // to properly test the logic of that endpoint.

    @Test
    public void testGetPersonsByStationNumber() throws Exception {
        // Arrange
        PersonInfo personInfo = new PersonInfo();
        // Set any properties on the personInfo object here
        List<PersonInfo> personInfos = Arrays.asList(personInfo);

        when(firestationService.getPersonsByStationNumber(anyString())).thenReturn(personInfos);

        // Act & Assert
        mockMvc.perform(get("/firestation?stationNumber=1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.persons", hasSize(1)));

        verify(firestationService, times(1)).getPersonsByStationNumber(anyString());
    }

}

