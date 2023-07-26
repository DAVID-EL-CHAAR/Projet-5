package com.SafetyNet.SafetyNetAlerts.repository;

import com.SafetyNet.SafetyNetAlerts.model.Firestation;
import com.SafetyNet.SafetyNetAlerts.model.Person;
import com.SafetyNet.SafetyNetAlerts.service.FirestationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FirestationRepository {
    private List<Firestation> firestations = new ArrayList<>();
    
  


    public List<Firestation> getAllFirestations() {
        return firestations;
    }

    public List<Firestation> getFirestationByAddress(String address) {
    	
    	// Récupérez toutes les stations de feu de la base de données.
        List<Firestation> allFirestations = getAllFirestations();
        
        return allFirestations.stream()
                .filter(firestation -> address.equals(firestation.getAddress()))
                .collect(Collectors.toList());
    }
    
    
    public List<Firestation> getFirestationsByStation(String station) {
        // Récupérez toutes les stations de feu de la base de données.
        List<Firestation> allFirestations = getAllFirestations();
        
        // Filtrer par le numéro de station.
        return allFirestations.stream()
            .filter(firestation -> station.equals(firestation.getStation()))
            .collect(Collectors.toList());
    }

    
    /*
     * findfirst renvoie un seul resultat
    public Firestation getFirestationByStation(String station) {
        return getAllFirestations().stream()
                .filter(firestation -> firestation.getStation().equals(station))
                .findFirst()
                .orElse(null);
    }

    */


    public Firestation addFirestation(Firestation firestation) {
        List<Firestation> firestations = getAllFirestations();

        for (Firestation existingFirestation : firestations) {
            if (existingFirestation.getAddress().equals(firestation.getAddress())) {
                System.out.println("La caserne de pompiers existe déjà, impossible de l'ajouter.");
                throw new IllegalArgumentException("La caserne de pompiers existe déjà");
            }
        }

        firestations.add(firestation);

        return firestation;
    }

    public void deleteFirestation(String address) {
        int originalSize = firestations.size();

        firestations = firestations.stream()
                .filter(firestation -> !firestation.getAddress().equals(address))
                .collect(Collectors.toList());

        if (firestations.size() == originalSize) {
            throw new IllegalArgumentException("La caserne de pompiers n'existe pas, impossible de la supprimer.");
        }
    }
    
    public void deleteFirestationByStation(String station) {
        firestations.removeIf(firestation -> firestation.getStation().equals(station));
    }

    public Firestation updateFirestation(Firestation existingFirestation, Firestation updatedFirestation) {
        List<Firestation> firestations = getAllFirestations();
        int index = firestations.indexOf(existingFirestation);
        if (index != -1) {
            firestations.set(index, updatedFirestation);

            return updatedFirestation;
        } else {
            throw new RuntimeException("Unable to find firestation to update.");
        }
    }
  
    public List<Firestation> getFirestations() {
        return firestations;
    }

    public void setFirestations(List<Firestation> firestations) {
        this.firestations = firestations;
    }
}
