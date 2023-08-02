package com.safetyNet.safetyNetAlerts.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetyNet.safetyNetAlerts.model.PersonInfo2;
import com.safetyNet.safetyNetAlerts.service.PersonService;

@RestController
public class PersonInfoController {

    private static final Logger logger = LoggerFactory.getLogger(PersonInfoController.class);

    @Autowired
    private PersonService personService;
    
    @GetMapping("/personInfo")
    public ResponseEntity<List<PersonInfo2>> getPersonsByName(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("Requête pour obtenir les informations des personnes par nom : {} et prénom : {}", lastName, firstName);
        List<PersonInfo2> persons = personService.getPersonInfoByName(firstName, lastName);
        if(persons.isEmpty()){
            logger.error("Aucune information de personne n'a été trouvée pour le nom : {} et le prénom : {}", lastName, firstName);
        } else {
            logger.debug("Nombre d'informations de personne obtenues pour le nom : {} et le prénom : {} est : {}", lastName, firstName, persons.size());
        }
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }
}
