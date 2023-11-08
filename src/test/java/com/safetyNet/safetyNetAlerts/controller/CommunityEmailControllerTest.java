package com.safetyNet.safetyNetAlerts.controller;

import com.safetyNet.safetyNetAlerts.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CommunityEmailControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @InjectMocks
    private CommunityEmailController communityEmailController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(communityEmailController).build();
    }

    @Test  
    public void getEmailByCityTest() throws Exception {
        
        //  variable pour la ville de test.
        String city = "TestCity";
        
        // Création d'une liste d'emails de test.
        List<String> emails = Arrays.asList("test1@test.com", "test2@test.com");

        
        when(personService.getEmailByCity(city)).thenReturn(emails);

        // Simuler une requête HTTP GET à l'URL '/communityEmail' avec le paramètre 'city' et vérifier les réponses.
        mockMvc.perform(get("/communityEmail")  // Créer une requête HTTP GET à l'URL "/communityEmail".
                .param("city", city)            // Ajouter un paramètre à la requête avec le nom "city" et la valeur de la variable 'city'.
                .contentType(MediaType.APPLICATION_JSON))  // Définir le type de contenu de la requête à JSON.
                .andExpect(status().isOk())  // Attendre un statut HTTP 200 OK en réponse.
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))  // Vérifier que le type de contenu de la réponse est compatible avec JSON.
                .andExpect(jsonPath("$", hasSize(2)))  // Attendre que la réponse JSON soit un tableau de taille 2.
                .andExpect(jsonPath("$[0]", is("test1@test.com")))  // Vérifier que le premier élément du tableau est "test1@test.com".
                .andExpect(jsonPath("$[1]", is("test2@test.com")));  // Vérifier que le second élément du tableau est "test2@test.com".

        // Vérifier que la méthode mockée 'getEmailByCity' a été appelée exactement une fois avec la valeur 'TestCity'.
        verify(personService, times(1)).getEmailByCity(city);
        
        // S'assurer qu'aucune autre interaction n'a eu lieu avec le service mocké après tout ce qui a été vérifié.
        verifyNoMoreInteractions(personService);
    }

}
