package com.SafetyNet.SafetyNetAlerts.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.SafetyNetAlerts.model.PersonInfo;
import com.SafetyNet.SafetyNetAlerts.service.FirestationService;
import com.SafetyNet.SafetyNetAlerts.service.PersonService;




@RestController
@RequestMapping("/flood")
public class Flood {
	
	@Autowired
	private FirestationService firestationService;

	
	@Autowired
	private PersonService personService;

	
	
	
	@GetMapping("/stations")
	public ResponseEntity<Map<String, Map<String, Object>>> getFloodInfo(@RequestParam List<String> stations) {
	    List<String> addresses = firestationService.getAddressesByStationNumbers(stations);

	    Map<String, Map<String, Object>> response = new LinkedHashMap<>();
	    for(String address : addresses) {
	        List<PersonInfo> personInfos = personService.getPersonByAddressForFire(address);

	        // Récupérer le numéro de station pour cette adresse
	        String stationNumber = firestationService.getstationByAddress(address);

	        // Créer un nouveau Map pour stocker le numéro de station et les informations de la personne
	        Map<String, Object> addressInfo = new LinkedHashMap<>();
	        addressInfo.put("stationNumber", stationNumber);
	        addressInfo.put("persons", personInfos);

	        response.put(address, addressInfo);
	    }

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}




}
