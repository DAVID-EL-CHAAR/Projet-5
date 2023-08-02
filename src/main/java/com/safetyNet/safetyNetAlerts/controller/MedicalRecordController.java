package com.safetyNet.safetyNetAlerts.controller;

import com.safetyNet.safetyNetAlerts.model.MedicalRecord;
import com.safetyNet.safetyNetAlerts.service.MedicalRecordService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    public List<MedicalRecord> getAllMedicalRecords() {
        logger.info("Requête pour récupérer tous les dossiers médicaux");
        List<MedicalRecord> records = medicalRecordService.getAllMedicalRecords();
        logger.debug("Nombre total de dossiers médicaux récupérés : {}", records.size());
        return records;
    }

    @GetMapping("/{firstName}/{lastName}")
    public MedicalRecord getMedicalRecordByName(@PathVariable String firstName, @PathVariable String lastName) {
        logger.info("Requête pour récupérer le dossier médical pour {} {}", firstName, lastName);
        MedicalRecord record = medicalRecordService.getMedicalRecordByName(firstName, lastName)
                .orElseThrow(() -> {
                    logger.error("Impossible de trouver le dossier médical pour {} {}", firstName, lastName);
                    return new RuntimeException("Impossible de trouver le dossier médical avec le prénom et le nom donnés");
                });
        logger.debug("Dossier médical récupéré pour {} {}: {}", firstName, lastName, record);
        return record;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("Requête pour ajouter un nouveau dossier médical");
        MedicalRecord newRecord = medicalRecordService.addMedicalRecord(medicalRecord);
        logger.debug("Dossier médical ajouté avec succès : {}", newRecord);
        return newRecord;
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public void deleteMedicalRecord(@PathVariable String firstName, @PathVariable String lastName) {
        logger.info("Requête pour supprimer le dossier médical pour {} {}", firstName, lastName);
        medicalRecordService.deleteMedicalRecord(firstName, lastName);
        logger.debug("Dossier médical supprimé avec succès pour {} {}", firstName, lastName);
    }

    @PutMapping("/{firstName}/{lastName}")
    public MedicalRecord updateMedicalRecord(@PathVariable String firstName, @PathVariable String lastName, @RequestBody MedicalRecord updatedMedicalRecord) {
        logger.info("Requête pour mettre à jour le dossier médical pour {} {}", firstName, lastName);
        MedicalRecord existingMedicalRecord = getMedicalRecordByName(firstName, lastName);
        MedicalRecord updatedRecord = medicalRecordService.updateMedicalRecord(existingMedicalRecord, updatedMedicalRecord);
        logger.debug("Dossier médical mis à jour avec succès pour {} {}: {}", firstName, lastName, updatedRecord);
        return updatedRecord;
    }
}
