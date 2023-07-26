package com.SafetyNet.SafetyNetAlerts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.SafetyNetAlerts.service.PersonService;


@RestController
public class CommunityEmail {
	
	@Autowired
	private PersonService personService;
	
	
	
	@GetMapping("/communityEmail")
	public List<String> getEmailByCity(@RequestParam String city){
		
		List <String> Email = personService.getEmailByCity(city);
		
		return Email;
	}

}
