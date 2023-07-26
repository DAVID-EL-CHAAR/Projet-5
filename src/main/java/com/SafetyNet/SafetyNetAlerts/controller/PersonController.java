/*
package com.SafetyNet.SafetyNetAlerts.controller;

import com.SafetyNet.SafetyNetAlerts.model.Person;
import com.SafetyNet.SafetyNetAlerts.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger logger = LogManager.getLogger(PersonController.class);

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<Person> getAllPersons() {
        logger.info("Requête GET : récupération de toutes les personnes");
        List<Person> persons = personService.getAllPersons();
        if (persons.isEmpty()) {
            logger.info("Aucune personne trouvée.");
        } else {
            logger.info("Récupération de toutes les personnes réussie : {}", persons);
        }
        return persons;
    }

    @GetMapping("/{firstName}/{lastName}")
    public Person getPersonByNameAndFirstName(@PathVariable String firstName, @PathVariable String lastName) {
        logger.info("Requête GET : récupération de la personne avec le prénom {} et le nom {}", firstName, lastName);
        Person person = personService.getPersonByNameAndFirstName(firstName, lastName)
                .orElseThrow(() -> {
                    logger.error("Échec de la récupération de la personne : la personne n'existe pas.");
                    return new RuntimeException("Unable to find person with given name and firstname");
                });
        logger.info("Récupération de la personne réussie : {}", person);
        return person;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person addPerson(@RequestBody Person person) {
        logger.info("Requête POST : création d'une nouvelle personne {}", person);
        Person newPerson = personService.addPerson(person);
        logger.info("Personne créée avec succès : {}", newPerson);
        return newPerson;
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public void deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
        logger.info("Requête DELETE : suppression de la personne avec le prénom {} et le nom {}", firstName, lastName);
        personService.deletePerson(firstName, lastName);
        logger.info("Personne supprimée avec succès.");
    }

    @PutMapping("/{firstName}/{lastName}")
    public Person updatePerson(@PathVariable String firstName, @PathVariable String lastName, @RequestBody Person updatedPerson) {
        logger.info("Requête PUT : mise à jour de la personne {}", updatedPerson);
        Person existingPerson = getPersonByNameAndFirstName(firstName, lastName);
        Person personUpdated = personService.updatePerson(existingPerson, updatedPerson);
        logger.info("Personne mise à jour avec succès : {}", personUpdated);
        return personUpdated;
    }
}
 */


package com.SafetyNet.SafetyNetAlerts.controller;

import com.SafetyNet.SafetyNetAlerts.model.Firestation;
import com.SafetyNet.SafetyNetAlerts.model.Person;
import com.SafetyNet.SafetyNetAlerts.model.PersonInfo;
import com.SafetyNet.SafetyNetAlerts.service.FirestationService;
import com.SafetyNet.SafetyNetAlerts.service.PersonService;
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
    
    @Autowired
    private  FirestationService firestationService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/{firstName}/{lastName}")
    public Person getPersonByNameAndFirstName(@PathVariable String firstName, @PathVariable String lastName) {
        return personService.getPersonByNameAndFirstName(firstName, lastName)
                .orElseThrow(() -> new RuntimeException("Unable to find person with given name and firstname"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person addPerson(@RequestBody Person person) {
        return personService.addPerson(person);
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public void deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
        personService.deletePerson(firstName, lastName);
    }

    @PutMapping("/{firstName}/{lastName}")
    public Person updatePerson(@PathVariable String firstName, @PathVariable String lastName, @RequestBody Person updatedPerson) {
        Person existingPerson = getPersonByNameAndFirstName(firstName, lastName);
        return personService.updatePerson(existingPerson, updatedPerson);
    }
    
    @GetMapping("/childAlert/{address}")
    public ResponseEntity<Map<String, Object>> getChildAlert(@PathVariable String address) {
        List<PersonInfo> children = personService.getChildrenByAddress(address);

        List<String> childNames = children.stream()
            .map(child -> child.getFirstName() + " " + child.getLastName())
            .collect(Collectors.toList());

        // Créer une liste de noms pour les autres membres du ménage
        List<String> otherHouseholdMembers = personService.getPersonsByAddress(address).stream()
            .map(person -> person.getFirstName() + " " + person.getLastName())
            .filter(name -> !childNames.contains(name))
            .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("children", children);
        response.put("otherHouseholdMembers", otherHouseholdMembers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
