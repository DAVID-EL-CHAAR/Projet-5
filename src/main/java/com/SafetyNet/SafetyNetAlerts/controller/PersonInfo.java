package com.SafetyNet.SafetyNetAlerts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.SafetyNetAlerts.model.PersonInfo2;
import com.SafetyNet.SafetyNetAlerts.service.PersonService;


@RestController

public class PersonInfo {

	 @Autowired
	 private PersonService personService;
	
	@GetMapping("/personInfo")
	public ResponseEntity<List<PersonInfo2>> getPersonsByName(@RequestParam String firstName, @RequestParam String lastName) {
	    List<PersonInfo2> persons = personService.getPersonInfoByName(firstName, lastName);
	    
	    return new ResponseEntity<>(persons, HttpStatus.OK);
	}

}
