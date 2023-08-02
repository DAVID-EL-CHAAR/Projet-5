
package com.safetyNet.safetyNetAlerts.controller;


import com.safetyNet.safetyNetAlerts.model.Person;
import com.safetyNet.safetyNetAlerts.model.PersonInfo;
import com.safetyNet.safetyNetAlerts.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    
   

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<Person> getAllPersons() {
        logger.info("Requête pour obtenir tous les individus");
        List<Person> persons = personService.getAllPersons();
        logger.debug("Nombre total d'individus obtenus : {}", persons.size());
        return persons;
    }

    @GetMapping("/{firstName}/{lastName}")
    public Person getPersonByNameAndFirstName(@PathVariable String firstName, @PathVariable String lastName) {
        logger.info("Requête pour obtenir l'individu avec prénom {} et nom {}", firstName, lastName);
        Person person = personService.getPersonByNameAndFirstName(firstName, lastName)
                .orElseThrow(() -> {
                    logger.error("Impossible de trouver l'individu avec prénom {} et nom {}", firstName, lastName);
                    return new RuntimeException("Impossible de trouver l'individu avec le nom et le prénom donnés");
                });
        logger.debug("Individu récupéré pour prénom {} et nom {}: {}", firstName, lastName, person);
        return person;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person addPerson(@RequestBody Person person) {
        logger.info("Requête pour ajouter un nouvel individu");
        Person newPerson = personService.addPerson(person);
        logger.debug("Nouvel individu ajouté avec succès : {}", newPerson);
        return newPerson;
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public void deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
        logger.info("Requête pour supprimer l'individu avec prénom {} et nom {}", firstName, lastName);
        personService.deletePerson(firstName, lastName);
        logger.debug("Individu supprimé avec succès pour prénom {} et nom {}", firstName, lastName);
    }

    @PutMapping("/{firstName}/{lastName}")
    public Person updatePerson(@PathVariable String firstName, @PathVariable String lastName, @RequestBody Person updatedPerson) {
        logger.info("Requête pour mettre à jour l'individu avec prénom {} et nom {}", firstName, lastName);
        Person existingPerson = getPersonByNameAndFirstName(firstName, lastName);
        Person updatedPersonResult = personService.updatePerson(existingPerson, updatedPerson);
        logger.debug("Individu mis à jour avec succès pour prénom {} et nom {}: {}", firstName, lastName, updatedPersonResult);
        return updatedPersonResult;
    }


}
