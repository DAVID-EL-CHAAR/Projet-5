package com.safetyNet.safetyNetAlerts.service;

import com.safetyNet.safetyNetAlerts.model.Firestation;

import java.util.List;

public interface FireStationServiceInterface {
    List<Firestation> getAllFirestations();
    Firestation getFirestationByAddress(String address);
    Firestation createFirestation(Firestation firestation);
    Firestation updateFirestation(Firestation firestation);
    void deleteFirestation(String address);
    Firestation getFirestationByNumber(Integer stationNumber);
}
