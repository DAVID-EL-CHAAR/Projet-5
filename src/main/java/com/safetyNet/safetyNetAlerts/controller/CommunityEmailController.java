package com.safetyNet.safetyNetAlerts.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.safetyNet.safetyNetAlerts.service.PersonService;


@RestController
public class CommunityEmailController {
	
	@Autowired
	private PersonService personService;
	
	private static final Logger logger = LoggerFactory.getLogger(CommunityEmailController.class);
	
	
	@GetMapping("/communityEmail")
	public List<String> getEmailByCity(@RequestParam String city){
		logger.info("Requête pour obtenir les emails des personnes dans la ville {}", city);
		
		List <String> Email = personService.getEmailByCity(city);
		logger.debug("Nombre d'emails trouvés : {}", Email.size());

	    logger.info("Réponse avec emails générée avec succès");
		return Email;
	}

}
