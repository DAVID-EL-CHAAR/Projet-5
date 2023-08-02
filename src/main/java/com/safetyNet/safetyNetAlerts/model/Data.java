package com.safetyNet.safetyNetAlerts.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Data {
	
	    @JsonProperty("persons")
	    private List<Person> persons;

	    @JsonProperty("medicalrecords")
	    private List<MedicalRecord> medicalRecords;

	    @JsonProperty("firestations")
	    private List<Firestation> firestations;

    // getters et setters pour chaque liste

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Firestation> getFirestations() {
        return firestations;
    }

    public void setFirestations(List<Firestation> firestations) {
        this.firestations = firestations;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }
}
