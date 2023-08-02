package com.safetyNet.safetyNetAlerts.repository;

import com.safetyNet.safetyNetAlerts.model.Firestation;
import com.safetyNet.safetyNetAlerts.repository.FirestationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FirestationRepositoryTest {

    private FirestationRepository firestationRepository;
    private Firestation firestation1;
    private Firestation firestation2;

    @BeforeEach
    public void setUp() {
        firestationRepository = new FirestationRepository();

        firestation1 = new Firestation();
        firestation1.setAddress("Address1");
        firestation1.setStation("Station1");

        firestation2 = new Firestation();
        firestation2.setAddress("Address2");
        firestation2.setStation("Station2");

        firestationRepository.setFirestations(new ArrayList<>(Arrays.asList(firestation1, firestation2)));
    }


    @Test
    public void getAllFirestationsShouldReturnAllFirestations() {
        List<Firestation> firestations = firestationRepository.getAllFirestations();

        assertEquals(2, firestations.size());
        assertEquals(firestation1, firestations.get(0));
        assertEquals(firestation2, firestations.get(1));
    }

    @Test
    public void getFirestationByAddressShouldReturnCorrectFirestation() {
        List<Firestation> firestations = firestationRepository.getFirestationByAddress("Address1");

        assertEquals(1, firestations.size());
        assertEquals(firestation1, firestations.get(0));
    }

    @Test
    public void addFirestationShouldAddFirestationIfNotExists() {
        Firestation firestation3 = new Firestation();
        firestation3.setAddress("Address3");
        firestation3.setStation("Station3");

        Firestation addedFirestation = firestationRepository.addFirestation(firestation3);

        assertEquals(firestation3, addedFirestation);
        assertEquals(3, firestationRepository.getAllFirestations().size());
    }

    @Test
    public void addFirestationShouldThrowExceptionIfExists() {
        assertThrows(IllegalArgumentException.class, () -> firestationRepository.addFirestation(firestation1));
    }

    @Test
    public void deleteFirestationShouldDeleteCorrectFirestation() {
        firestationRepository.deleteFirestation("Address1");

        assertEquals(1, firestationRepository.getAllFirestations().size());
        assertEquals(firestation2, firestationRepository.getAllFirestations().get(0));
    }

    @Test
    public void deleteFirestationShouldThrowExceptionIfNotExists() {
        assertThrows(IllegalArgumentException.class, () -> firestationRepository.deleteFirestation("Address3"));
    }

    @Test
    public void updateFirestationShouldUpdateCorrectFirestation() {
        Firestation updatedFirestation = new Firestation();
        updatedFirestation.setAddress("Address1");
        updatedFirestation.setStation("UpdatedStation");

        Firestation firestation = firestationRepository.updateFirestation(firestation1, updatedFirestation);

        assertEquals(updatedFirestation, firestation);
        assertEquals("UpdatedStation", firestationRepository.getFirestationByAddress("Address1").get(0).getStation());
    }
}
