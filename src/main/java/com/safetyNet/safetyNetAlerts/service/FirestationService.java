package com.safetyNet.safetyNetAlerts.service;

import com.safetyNet.safetyNetAlerts.model.Firestation;
import com.safetyNet.safetyNetAlerts.model.MedicalRecord;
import com.safetyNet.safetyNetAlerts.model.Person;
import com.safetyNet.safetyNetAlerts.model.PersonInfo;
import com.safetyNet.safetyNetAlerts.repository.FirestationRepository;
import com.safetyNet.safetyNetAlerts.repository.MedicalRecordRepository;
import com.safetyNet.safetyNetAlerts.repository.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FirestationService {
	
	  private final FirestationRepository firestationRepository;
	    private final PersonRepository personRepository;
	    private final MedicalRecordRepository medicalRecordRepository;

	    public FirestationService(FirestationRepository firestationRepository, 
	                              PersonRepository personRepository,
	                              MedicalRecordRepository medicalRecordRepository) {
	        this.firestationRepository = firestationRepository;
	        this.personRepository = personRepository;
	        this.medicalRecordRepository = medicalRecordRepository;
	    }   
   

    public List<Firestation> getAllFirestations() {
        return firestationRepository.getAllFirestations();
    }

    public List<Firestation> getFirestationByAddress(String address) {
        List<Firestation> firestations = firestationRepository.getFirestationByAddress(address);
        return firestations;
    }
    
    public List<Firestation> getFirestationsByStation(String station) {
        List<Firestation> firestations = firestationRepository.getFirestationsByStation(station);
        return firestations;
    }



    public Firestation addFirestation(Firestation firestation) {
        return firestationRepository.addFirestation(firestation);
    }


    public void deleteFirestation(String address) {
        firestationRepository.deleteFirestation(address);
    }
    
    public void deleteFirestationByStation(String station) {
        firestationRepository.deleteFirestationByStation(station);
    }

    public Firestation updateFirestation(Firestation existingFirestation, Firestation updatedFirestation) {
        return firestationRepository.updateFirestation(existingFirestation, updatedFirestation);
    }
    

    
    public Firestation getFirestationByNumberAndAddress(String stationNumber, String address) throws NotFoundException 
    {
        
        List<Firestation> allFirestations = getAllFirestations();   
        Optional<Firestation> firestationOpt = allFirestations.stream()
            .filter(firestation -> stationNumber.equals(firestation.getStation()) && address.equals(firestation.getAddress()))
            .findFirst();

        return firestationOpt.orElseThrow(() -> new NotFoundException());
    }
    
    public List<PersonInfo> getPersonsByStationNumber(String stationNumber) {
        List<Firestation> firestations = firestationRepository.getFirestationsByStation(stationNumber);
        List<PersonInfo> personInfos = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        for (Firestation firestation : firestations) {
            List<Person> persons = personRepository.getPersonsByAddress(firestation.getAddress());
            for (Person person : persons) {
                Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.getMedicalRecordByName(person.getFirstName(), person.getLastName());

                if(optionalMedicalRecord.isPresent()) {
                    MedicalRecord medicalRecord = optionalMedicalRecord.get();
                    PersonInfo personInfo = new PersonInfo();
                    personInfo.setFirstName(person.getFirstName());
                    personInfo.setLastName(person.getLastName());
                    personInfo.setAddress(person.getAddress());
                    personInfo.setPhone(person.getPhone());
                    LocalDate birthdate = LocalDate.parse(medicalRecord.getBirthdate(), formatter);
                    personInfo.setAge(calculateAge(birthdate));
                    personInfos.add(personInfo);
                } else {
                    
                    throw new RuntimeException("Aucun MedicalRecord trouvé pour " + person.getFirstName() + " " + person.getLastName());
                }
            }
        }

        return personInfos;
    }
    
    //cette methode était private si il y a une erreur sa peut etre a cause de sa
    public int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public List<String> getAddressesByStationNumbers(List<String> stationNumbers) {
        List<String> addresses = new ArrayList<>();
        for (String stationNumber : stationNumbers) {
            List<Firestation> firestations = firestationRepository.getFirestationsByStation(stationNumber);
            for (Firestation firestation : firestations) {
                addresses.add(firestation.getAddress());
            }
        }
        return addresses;
    }

    public String getstationByAddress(String adress) {     
        String addresses = null;        
            List<Firestation> firestations = firestationRepository.getFirestationByAddress(adress);
            for (Firestation firestation : firestations) {
            	addresses = firestation.getStation();
            }
        
        return addresses;
    }
 


}