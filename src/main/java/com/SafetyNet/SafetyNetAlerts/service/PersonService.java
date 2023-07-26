package com.SafetyNet.SafetyNetAlerts.service;

import com.SafetyNet.SafetyNetAlerts.model.Firestation;
import com.SafetyNet.SafetyNetAlerts.model.MedicalRecord;
import com.SafetyNet.SafetyNetAlerts.model.Person;
import com.SafetyNet.SafetyNetAlerts.model.PersonInfo;
import com.SafetyNet.SafetyNetAlerts.model.PersonInfo2;
import com.SafetyNet.SafetyNetAlerts.repository.FirestationRepository;
import com.SafetyNet.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.SafetyNet.SafetyNetAlerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;


    @Autowired
    private FirestationRepository firestationRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPersons() {
        return personRepository.getAllPersons();
    }

    /*
    public Person getPersonByNameAndFirstName(String firstName, String lastName) {
        return personRepository.findByName(firstName, lastName);
    }
   */

  
    public Optional<Person> getPersonByNameAndFirstName(String firstName, String lastName) {
        Person person = personRepository.getPersonByNameAndFirstName(firstName, lastName);
        return Optional.ofNullable(person);
    }

    public Person addPerson(Person person) {
        return personRepository.addPerson(person);
    }

    public void deletePerson(String firstName, String lastName) {
        personRepository.deletePerson(firstName, lastName);
    }

    public Person updatePerson(Person existingPerson, Person updatedPerson) {
        return personRepository.updatePerson(existingPerson, updatedPerson);
    }
    
    public List<Person> getPersonsByAddress(String address) {
        return personRepository.getPersonsByAddress(address);
    }
    
    
        /*

        public List<Person> getPersonsByFirestationNumber(Integer stationNumber) {
            // Obtenez toutes les stations de pompiers
            List<Firestation> firestations = firestationRepository.getAllFirestations();
            
            // Filtrer les stations de pompiers par le numéro de station
            List<Firestation> filteredFirestations = firestations.stream()
                .filter(firestation -> firestation.getStation().equals(stationNumber))
                .collect(Collectors.toList());

            List<Person> persons = new ArrayList<>();

            // Pour chaque station de pompiers correspondante, obtenir toutes les personnes qui ont cette adresse
            for (Firestation firestation : filteredFirestations) {
                List<Person> personsAtAddress = personRepository.getAllPersons().stream()
                        .filter(person -> person.getAddress().equals(firestation.getAddress()))
                        .collect(Collectors.toList());
                persons.addAll(personsAtAddress);
            }

            return persons;
       
        
       }
       
       */
    
    public int calculateAge(String birthdate) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthdateLocalDate = LocalDate.parse(birthdate, formatter);
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthdateLocalDate, now);
        return period.getYears();
    }
    
    public List<PersonInfo> getChildrenByAddress(String address) {
        List<Person> persons = personRepository.getPersonsByAddress(address);
        List<PersonInfo> personInfos = new ArrayList<>();

        for (Person person : persons) {
            Optional<MedicalRecord> medicalRecordOptional = medicalRecordRepository.getMedicalRecordByName(person.getFirstName(), person.getLastName());
            if(medicalRecordOptional.isPresent()) {
                int age = calculateAge(medicalRecordOptional.get().getBirthdate());
                if (age <= 18) {
                    PersonInfo personInfo = new PersonInfo();
                    personInfo.setFirstName(person.getFirstName());
                    personInfo.setLastName(person.getLastName());
                    personInfo.setAge(age);
                    personInfo.setAddress(person.getAddress()); 
                    personInfo.setPhone(person.getPhone()); 
                    personInfos.add(personInfo);
                }
            }
        }
        
        return personInfos;
    }

    public List<PersonInfo> getPersonByAddress(String address) {
        List<Person> persons = personRepository.getPersonsByAddress(address);
        List<PersonInfo> personInfos = new ArrayList<>();

        for (Person person : persons) {
            Optional<MedicalRecord> medicalRecordOptional = medicalRecordRepository.getMedicalRecordByName(person.getFirstName(), person.getLastName());
            if(medicalRecordOptional.isPresent()) {
                int age = calculateAge(medicalRecordOptional.get().getBirthdate());
                PersonInfo personInfo = new PersonInfo();
                personInfo.setFirstName(person.getFirstName());
                personInfo.setLastName(person.getLastName());
                personInfo.setAge(age);
                personInfo.setAddress(person.getAddress());
                personInfo.setPhone(person.getPhone());
                personInfos.add(personInfo);
            }
        }

        return personInfos;
    }

    public List<PersonInfo> getPersonByAddressForFire(String address) {
        List<Person> persons = personRepository.getPersonsByAddress(address);
        List<PersonInfo> personInfos = new ArrayList<>();

        for (Person person : persons) {
            Optional<MedicalRecord> medicalRecordOptional = medicalRecordRepository.getMedicalRecordByName(person.getFirstName(), person.getLastName());
            if(medicalRecordOptional.isPresent()) {
                MedicalRecord medicalRecord = medicalRecordOptional.get(); 
                int age = calculateAge(medicalRecord.getBirthdate());
                PersonInfo personInfo = new PersonInfo();
                personInfo.setFirstName(person.getFirstName());
                personInfo.setLastName(person.getLastName());
                personInfo.setAge(age);
                personInfo.setAddress(person.getAddress());
                personInfo.setPhone(person.getPhone());

                
                List<String> medications = medicalRecord.getMedications(); 
                List<String> Allergies   = medicalRecord.getAllergies();
                personInfo.setMedications(medications);
                personInfo.setAllergies(Allergies);

                personInfos.add(personInfo);
            }
        }

        return personInfos;

    }
   
    
  public List <PersonInfo2> getPersonInfoByName(String firstName, String lastName){
	   
	   List<Person> person = personRepository.getPersonByNameAndFirstName2(firstName, lastName);
	   
	   List<PersonInfo2> personInfo = new ArrayList <>();
    	
	   for(Person perso : person) {
		   
		   Optional<MedicalRecord> MedicalPerson = medicalRecordRepository. getMedicalRecordByName(perso.getFirstName(), perso.getLastName() );
		   
		   if(MedicalPerson.isPresent()) {
			   
			   MedicalRecord medicalRecord = MedicalPerson.get(); 
               int age = calculateAge(medicalRecord.getBirthdate());
               PersonInfo2 personInfo2 = new PersonInfo2();
               personInfo2.setFirstName(perso.getFirstName());
               personInfo2.setLastName(perso.getLastName());
               personInfo2.setAddress(perso.getAddress());
               personInfo2.setAge(age);
               personInfo2.setEmail(perso.getEmail());
               
           

               
               List<String> medications = medicalRecord.getMedications(); 
               List<String> Allergies   = medicalRecord.getAllergies();
               personInfo2.setMedications(medications);
               personInfo2.setAllergies(Allergies);

               personInfo.add(personInfo2);
           }
       }

       return personInfo;

		   }
		   
		   
	   
  public List<String> getEmailByCity(String city) {
	    List<Person> personsInCity = personRepository.getPersonByCity(city);
	    List<String> emails = new ArrayList<>();

	    for (Person person : personsInCity) {
	        String email = person.getEmail();
	        emails.add(email);
	    }

	    return emails;
	}

   }
    	
    

     
    

