package com.safetyNet.safetyNetAlerts.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetyNet.safetyNetAlerts.model.PersonInfo;
import com.safetyNet.safetyNetAlerts.service.FirestationService;
import com.safetyNet.safetyNetAlerts.service.PersonService;

@RestController
@RequestMapping("/phoneAlert")
public class PhoneAlertController {

    private static final Logger logger = LoggerFactory.getLogger(PhoneAlertController.class);

    @Autowired
    private PersonService personService;

    @Autowired
    private FirestationService firestationService;
    
    @GetMapping
    public ResponseEntity<List<String>> getPhoneAlert(@RequestParam String firestation) {
        logger.info("Requête pour obtenir les numéros de téléphone de la station : {}", firestation);
        List<PersonInfo> persons = firestationService.getPersonsByStationNumber(firestation);

        if(persons.isEmpty()){
            logger.error("Aucune personne n'a été trouvée pour la station : {}", firestation);
        } else {
            logger.debug("Nombre de personnes obtenues pour la station : {} est : {}", firestation, persons.size());
        }

        List<String> phoneNumbers = persons.stream()
            .map(PersonInfo::getPhone)
            .collect(Collectors.toList());

        return new ResponseEntity<>(phoneNumbers, HttpStatus.OK);
    }

}
