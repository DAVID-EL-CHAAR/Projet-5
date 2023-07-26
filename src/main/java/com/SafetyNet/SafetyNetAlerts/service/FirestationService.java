package com.SafetyNet.SafetyNetAlerts.service;

import com.SafetyNet.SafetyNetAlerts.model.Firestation;
import com.SafetyNet.SafetyNetAlerts.model.MedicalRecord;
import com.SafetyNet.SafetyNetAlerts.model.Person;
import com.SafetyNet.SafetyNetAlerts.model.PersonInfo;
import com.SafetyNet.SafetyNetAlerts.repository.FirestationRepository;
import com.SafetyNet.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.SafetyNet.SafetyNetAlerts.repository.PersonRepository;

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
	
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;
    
    private final FirestationRepository firestationRepository;

    public FirestationService(FirestationRepository firestationRepository) {
        this.firestationRepository = firestationRepository;
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
    

    
    public Firestation getFirestationByNumberAndAddress(String stationNumber, String address) throws NotFoundException {
        // Récupérez toutes les casernes de pompiers de la base de données
        List<Firestation> allFirestations = getAllFirestations();

        // Filtrer par le numéro de la station et l'adresse
        Optional<Firestation> firestationOpt = allFirestations.stream()
            .filter(firestation -> stationNumber.equals(firestation.getStation()) && address.equals(firestation.getAddress()))
            .findFirst();

        // Si la caserne de pompiers est trouvée, la retourner. Sinon, lancer une exception
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
                    // Lancer une exception si aucun MedicalRecord n'est trouvé pour un individu
                    throw new RuntimeException("Aucun MedicalRecord trouvé pour " + person.getFirstName() + " " + person.getLastName());
                }
            }
        }

        return personInfos;
    }
    
    
    private int calculateAge(LocalDate birthDate) {
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
