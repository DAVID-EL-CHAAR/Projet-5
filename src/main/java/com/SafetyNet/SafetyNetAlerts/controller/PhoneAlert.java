package com.SafetyNet.SafetyNetAlerts.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.SafetyNetAlerts.model.PersonInfo;
import com.SafetyNet.SafetyNetAlerts.service.FirestationService;
import com.SafetyNet.SafetyNetAlerts.service.PersonService;




@RestController
@RequestMapping("/phoneAlert")
public class PhoneAlert {
	
	@Autowired
    private  PersonService personService;

	@Autowired
    private FirestationService firestationService;
	
    @GetMapping
    public ResponseEntity<List<String>> getPhoneAlert(@RequestParam String firestation) {
        List<PersonInfo> persons = firestationService.getPersonsByStationNumber(firestation);

        List<String> phoneNumbers = persons.stream()
            .map(PersonInfo::getPhone)
            .collect(Collectors.toList());

        return new ResponseEntity<>(phoneNumbers, HttpStatus.OK);
    }

}
