package com.SafetyNet.SafetyNetAlerts.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.SafetyNetAlerts.model.Firestation;
import com.SafetyNet.SafetyNetAlerts.model.PersonInfo;
import com.SafetyNet.SafetyNetAlerts.service.FirestationService;
import com.SafetyNet.SafetyNetAlerts.service.PersonService;


@RestController
public class FireController {
	

@Autowired
private PersonService personService;

@Autowired
private FirestationService firestationService;
	
	
    @GetMapping("/fire")
    public ResponseEntity<Map<String, Object>> getPersonAndFireStation(@RequestParam String address ){
    	
    	List<PersonInfo> personAddress = personService.getPersonByAddressForFire(address);
    	List<Firestation> firestation = firestationService.getFirestationByAddress(address);
    	
    	 Map<String, Object> response = new HashMap<>();
         response.put("Person", personAddress);
         response.put("fireStation",firestation );
    	
    	
    	return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
