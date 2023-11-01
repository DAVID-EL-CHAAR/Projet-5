package com.safetyNet.safetyNetAlerts.controller;

import com.safetyNet.safetyNetAlerts.model.Firestation;
import com.safetyNet.safetyNetAlerts.model.PersonInfo;
import com.safetyNet.safetyNetAlerts.service.FirestationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/firestation")
public class FirestationController {

    private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);

    private final FirestationService firestationService;

    @Autowired
    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping("/all")
    public List<Firestation> getAllFirestations() {
        logger.info("Requête reçue pour obtenir toutes les stations de pompiers");
        return firestationService.getAllFirestations();
    }

    @GetMapping("/address/{address}")
    public List<Firestation> getFirestationByAddress(@PathVariable String address) {
        logger.info("Requête reçue pour obtenir la station de pompiers à l'adresse : {}", address);
        List<Firestation> firestations = firestationService.getFirestationByAddress(address);
        if(firestations == null){
            logger.error("Impossible de trouver une station de pompiers à l'adresse : {}", address);
            throw new RuntimeException("Unable to find firestation with the given address");
        }
        return firestations;
    }

    @GetMapping("/station/{station}")
    public List<Firestation> getFirestationsByStation(@PathVariable String station) {
        logger.info("Requête reçue pour obtenir la station de pompiers numéro : {}", station);
        List<Firestation> firestations = firestationService.getFirestationsByStation(station);
        if(firestations.isEmpty()){
            logger.error("Impossible de trouver une station de pompiers avec le numéro : {}", station);
            throw new RuntimeException("Unable to find firestations with the given station number");
        }
        return firestations;
    }

    
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Firestation addFirestation(@RequestBody Firestation firestation) {
        logger.info("Requête reçue pour ajouter une nouvelle station de pompiers");
        return firestationService.addFirestation(firestation);
    }

    @DeleteMapping("/{address}")
    public void deleteFirestation(@PathVariable String address) {
        logger.info("Requête reçue pour supprimer la station de pompiers à l'adresse : {}", address);
        firestationService.deleteFirestation(address);
    }

    @DeleteMapping("/station/{station}")
    public void deleteFirestationByStation(@PathVariable String station) {
        logger.info("Requête reçue pour supprimer la station de pompiers numéro : {}", station);
        firestationService.deleteFirestationByStation(station);
    }

    @PutMapping("/{stationNumber}/{address}")
    public Firestation updateFirestation(@PathVariable String stationNumber, @PathVariable String address, @RequestBody Firestation updatedFirestation) throws NotFoundException {
        logger.info("Requête reçue pour mettre à jour la numero station de pompiers : {} à l'adresse : {}", stationNumber, address);
        Firestation existingFirestation = firestationService.getFirestationByNumberAndAddress(stationNumber, address);
        return firestationService.updateFirestation(existingFirestation, updatedFirestation);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getPersonsByStationNumber(@RequestParam String stationNumber) {
        logger.info("Requête reçue pour obtenir des informations sur les personnes liées au numero station de pompiers  : {}", stationNumber);
        List<PersonInfo> personInfos = firestationService.getPersonsByStationNumber(stationNumber);
        
        List<PersonInfo> adults = personInfos.stream()
                .filter(personInfo -> personInfo.getAge() > 18)
                .collect(Collectors.toList());
        
        List<PersonInfo> children = personInfos.stream()
                .filter(personInfo -> personInfo.getAge() <= 18)
                .collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("persons", personInfos);
        response.put("adultCount", adults.size());
        response.put("childrenCount", children.size());
        
        logger.info("Réponse générée avec succès pour numero station de pompiers  : {}", stationNumber);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
