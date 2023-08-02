package com.safetyNet.safetyNetAlerts.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetyNet.safetyNetAlerts.model.Firestation;
import com.safetyNet.safetyNetAlerts.model.PersonInfo;
import com.safetyNet.safetyNetAlerts.service.FirestationService;
import com.safetyNet.safetyNetAlerts.service.PersonService;

@RestController
public class FireController {

    private static final Logger logger = LoggerFactory.getLogger(FireController.class);

    @Autowired
    private PersonService personService;

    @Autowired
    private FirestationService firestationService;
    
    @GetMapping("/fire")
    public ResponseEntity<Map<String, Object>> getPersonAndFireStation(@RequestParam String address ){
        logger.info("Requête reçue pour obtenir des personnes et des informations de station de pompiers pour l'adresse : {}", address);
    	
        List<PersonInfo> personAddress = personService.getPersonByAddressForFire(address);
        List<Firestation> firestation = firestationService.getFirestationByAddress(address);
    	
        Map<String, Object> response = new HashMap<>();
        response.put("Person", personAddress);
        response.put("fireStation",firestation );

        logger.info("Réponse générée avec succès pour l'adresse : {}", address);
    	
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

