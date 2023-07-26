package com.SafetyNet.SafetyNetAlerts.controller;

import com.SafetyNet.SafetyNetAlerts.model.Firestation;
import com.SafetyNet.SafetyNetAlerts.model.Person;
import com.SafetyNet.SafetyNetAlerts.model.PersonInfo;
import com.SafetyNet.SafetyNetAlerts.service.FirestationService;
import com.SafetyNet.SafetyNetAlerts.service.PersonService;

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

    private final FirestationService firestationService;
    
    @Autowired
    private PersonService personService;

    @Autowired
    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping("/all")
    public List<Firestation> getAllFirestations() {
        return firestationService.getAllFirestations();
    }

    @GetMapping("/address/{address}")
    public List<Firestation> getFirestationByAddress(@PathVariable String address) {
    	List<Firestation> firestations = firestationService.getFirestationByAddress(address);
        if(firestations == null){
            throw new RuntimeException("Unable to find firestation with the given address");
        }
        return firestations;
    }
    
    @GetMapping("/station/{station}")
    public List<Firestation> getFirestationsByStation(@PathVariable String station) {
        List<Firestation> firestations = firestationService.getFirestationsByStation(station);
        if(firestations.isEmpty()){
            throw new RuntimeException("Unable to find firestations with the given station number");
        }
        return firestations;
    }



    /*
    @GetMapping("/station/{station}")
    public Firestation getFirestationByStation(@PathVariable String station) {
        Firestation firestation = firestationService.getFirestationByStation(station);
        if(firestation == null){
            throw new RuntimeException("Unable to find firestation with the given station number");
        }
        return firestation;
    }
   */


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Firestation addFirestation(@RequestBody Firestation firestation) {
        return firestationService.addFirestation(firestation);
    }

    @DeleteMapping("/{address}")
    public void deleteFirestation(@PathVariable String address) {
        firestationService.deleteFirestation(address);
    }

    // Vous devez ajouter cette méthode à votre service et à votre repository
    @DeleteMapping("/station/{station}")
    public void deleteFirestationByStation(@PathVariable String station) {
        firestationService.deleteFirestationByStation(station);
    }

    @PutMapping("/{stationNumber}/{address}")
    public Firestation updateFirestation(@PathVariable String stationNumber, @PathVariable String address, @RequestBody Firestation updatedFirestation) throws NotFoundException {
        Firestation existingFirestation = firestationService.getFirestationByNumberAndAddress(stationNumber, address);
        return firestationService.updateFirestation(existingFirestation, updatedFirestation);
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getPersonsByStationNumber(@RequestParam String stationNumber) {
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

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

 
}

