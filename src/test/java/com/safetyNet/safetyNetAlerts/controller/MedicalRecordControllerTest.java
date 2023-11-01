package com.safetyNet.safetyNetAlerts.controller;

import com.safetyNet.safetyNetAlerts.model.MedicalRecord;
import com.safetyNet.safetyNetAlerts.service.MedicalRecordService;
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
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    private MedicalRecord medicalRecord;

    @BeforeEach
    public void setUp() {
        medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("firstName1");
        medicalRecord.setLastName("lastName1");
    }

    @Test
    public void getAllMedicalRecordsTest() throws Exception {
        List<MedicalRecord> allMedicalRecords = Arrays.asList(medicalRecord);

        when(medicalRecordService.getAllMedicalRecords()).thenReturn(allMedicalRecords);

        mockMvc.perform(get("/medicalRecord"))
                .andExpect(status().isOk())
               
                
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is(medicalRecord.getFirstName())));
    }

    @Test
    public void getMedicalRecordByNameTest() throws Exception {
        when(medicalRecordService.getMedicalRecordByName(medicalRecord.getFirstName(), medicalRecord.getLastName())).thenReturn(Optional.of(medicalRecord));

        mockMvc.perform(get("/medicalRecord/" + medicalRecord.getFirstName() + "/" + medicalRecord.getLastName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(medicalRecord.getFirstName())));
    }

    @Test
    public void addMedicalRecordTest() throws Exception {
    	when(medicalRecordService.addMedicalRecord(Mockito.any(MedicalRecord.class))).thenReturn(medicalRecord);

        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"firstName\": \"firstName1\", \"lastName\": \"lastName1\" }"))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteMedicalRecordTest() throws Exception {
        doNothing().when(medicalRecordService).deleteMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName());

        mockMvc.perform(delete("/medicalRecord/" + medicalRecord.getFirstName() + "/" + medicalRecord.getLastName()))
                .andExpect(status().isOk());

        verify(medicalRecordService, times(1)).deleteMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName());
    }

    @Test
    public void updateMedicalRecordTest() throws Exception {
        when(medicalRecordService.getMedicalRecordByName(medicalRecord.getFirstName(), medicalRecord.getLastName())).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordService.updateMedicalRecord(org.mockito.ArgumentMatchers.<MedicalRecord>any(), org.mockito.ArgumentMatchers.<MedicalRecord>any())).thenReturn(medicalRecord);

        mockMvc.perform(put("/medicalRecord/" + medicalRecord.getFirstName() + "/" + medicalRecord.getLastName())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"firstName\": \"firstName1\", \"lastName\": \"lastName1\" }"))
                .andExpect(status().isOk());

        verify(medicalRecordService, times(1)).updateMedicalRecord(org.mockito.ArgumentMatchers.<MedicalRecord>any(), org.mockito.ArgumentMatchers.<MedicalRecord>any());
    }

}
