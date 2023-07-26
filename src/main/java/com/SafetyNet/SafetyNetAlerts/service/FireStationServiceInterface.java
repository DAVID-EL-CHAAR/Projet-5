package com.SafetyNet.SafetyNetAlerts.service;

import com.SafetyNet.SafetyNetAlerts.model.Firestation;

import java.util.List;

public interface FireStationServiceInterface {
    List<Firestation> getAllFirestations();
    Firestation getFirestationByAddress(String address);
    Firestation createFirestation(Firestation firestation);
    Firestation updateFirestation(Firestation firestation);
    void deleteFirestation(String address);
    Firestation getFirestationByNumber(Integer stationNumber);
}
