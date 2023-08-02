package com.safetyNet.safetyNetAlerts.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.safetyNet.safetyNetAlerts.model.MedicalRecord;
import com.safetyNet.safetyNetAlerts.model.Person;
import com.safetyNet.safetyNetAlerts.model.PersonInfo;
import com.safetyNet.safetyNetAlerts.repository.MedicalRecordRepository;
import com.safetyNet.safetyNetAlerts.service.PersonService;

@RestController
@RequestMapping("/childAlert")
public class ChildAlertController {
	
	private static final Logger logger = LoggerFactory.getLogger(ChildAlertController.class);

    @Autowired
    private  PersonService personService;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getChildAlert(@RequestParam String address) {
    	logger.info("Requête pour obtenir une alerte pour les enfants à l'adresse {}", address);

        List<PersonInfo> children = personService.getChildrenByAddress(address);
        logger.debug("Nombre d'enfants trouvés : {}", children.size());

        List<String> childNames = children.stream()
            .map(child -> child.getFirstName() + " " + child.getLastName())
            .collect(Collectors.toList());

        
        List<Person> persons = personService.getPersonsByAddress(address);

       
        List<PersonInfo> otherHouseholdMembers = new ArrayList<>();

        for (Person person : persons) {
            if (!childNames.contains(person.getFirstName() + " " + person.getLastName())) {
                Optional<MedicalRecord> medicalRecordOptional = medicalRecordRepository.getMedicalRecordByName(person.getFirstName(), person.getLastName());

                if(medicalRecordOptional.isPresent()) {
                    PersonInfo personInfo = new PersonInfo();
                    personInfo.setFirstName(person.getFirstName());
                    personInfo.setLastName(person.getLastName());
                    personInfo.setAge(personService.calculateAge(medicalRecordOptional.get().getBirthdate())); 
                    personInfo.setAddress(person.getAddress());
                    personInfo.setPhone(person.getPhone());
                    otherHouseholdMembers.add(personInfo);
                }
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("children", children);
        response.put("otherHouseholdMembers", otherHouseholdMembers);
        
        logger.info("Réponse avec alerte pour les enfants générée avec succès");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

