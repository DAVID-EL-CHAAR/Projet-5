package com.safetyNet.safetyNetAlerts.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/flood")
public class FloodController {

    private static final Logger logger = LoggerFactory.getLogger(FloodController.class);
	
    @Autowired
    private FirestationService firestationService;

    @Autowired
    private PersonService personService;

    @GetMapping("/stations")
    public ResponseEntity<Map<String, Map<String, Object>>> getFloodInfo(@RequestParam List<String> stations) {
        logger.info("Requête reçue pour obtenir les informations d'inondation pour les stations : {}", stations);

        List<String> addresses = firestationService.getAddressesByStationNumbers(stations);

        Map<String, Map<String, Object>> response = new LinkedHashMap<>();
        
        for(String address : addresses) {
        	
            List<PersonInfo> personInfos = personService.getPersonByAddressForFire(address);

            // Récupérer le numéro de station pour cette adresse
            String stationNumber = firestationService.getstationByAddress(address);

            if(stationNumber == null){
                logger.error("Impossible de trouver le numéro de la station pour l'adresse : {}", address);
                continue;
            }

            // Création nouveau Map pour stocker le numéro de station et les informations de la personne
            Map<String, Object> addressInfo = new LinkedHashMap<>();
            addressInfo.put("stationNumber", stationNumber);
            addressInfo.put("persons", personInfos);

            response.put(address, addressInfo);
        }

        logger.info("Réponse générée avec succès pour les stations : {}", stations);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
