package com.SafetyNet.SafetyNetAlerts.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;


import com.SafetyNet.SafetyNetAlerts.model.MedicalRecord;
import com.SafetyNet.SafetyNetAlerts.model.Person;
import com.SafetyNet.SafetyNetAlerts.model.PersonInfo;
import com.SafetyNet.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.SafetyNet.SafetyNetAlerts.service.PersonService;

@RestController
@RequestMapping("/childAlert")
public class ChildAlert {

    @Autowired
    private  PersonService personService;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getChildAlert(@RequestParam String address) {
        List<PersonInfo> children = personService.getChildrenByAddress(address);

        List<String> childNames = children.stream()
            .map(child -> child.getFirstName() + " " + child.getLastName())
            .collect(Collectors.toList());

        
        List<Person> persons = personService.getPersonsByAddress(address);

       
        List<PersonInfo> otherHouseholdMembers = new ArrayList<>();

        for (Person person : persons) {
            if (!childNames.contains(person.getFirstName() + " " + person.getLastName())) {
                Optional<MedicalRecord> medicalRecordOptional = medicalRecordRepository.getMedicalRecordByName(person.getFirstName(), person.getLastName());

                if(medicalRecordOptional.isPresent()) {
                    PersonInfo personInfo = new PersonInfo();
                    personInfo.setFirstName(person.getFirstName());
                    personInfo.setLastName(person.getLastName());
                    personInfo.setAge(personService.calculateAge(medicalRecordOptional.get().getBirthdate())); 
                    personInfo.setAddress(person.getAddress());
                    personInfo.setPhone(person.getPhone());
                    otherHouseholdMembers.add(personInfo);
                }
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("children", children);
        response.put("otherHouseholdMembers", otherHouseholdMembers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

