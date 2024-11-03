package tn.esprit.spring.services;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.dto.PisteDTO;
import tn.esprit.spring.repositories.IPisteRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Skier;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PisteServiceJUnitTest {
    @Autowired
    private PisteServicesImpl pisteService;
    @Autowired
    private IPisteRepository pisteRepository;
    @Autowired
    private ISkierRepository skierRepository;
    private static Long savedPisteId;

    @BeforeEach
    @Order(1)
    void setUp() {
        // Arrange - Create a new Skier for later use
        Skier skier1 = new Skier();
        skier1.setFirstName("John");
        skier1.setLastName("Doe");
        skier1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        skier1.setCity("Paris");
        skier1.setPistes(new HashSet<>()); // Initialize the pistes collection
        skierRepository.save(skier1); // Persist skier1

        Skier skier2 = new Skier();
        skier2.setFirstName("Jane");
        skier2.setLastName("Smith");
        skier2.setDateOfBirth(LocalDate.of(1985, 5, 15));
        skier2.setCity("Lyon");
        skier2.setPistes(new HashSet<>());
        skierRepository.save(skier2); // Persist skier2
    }

    @Test
    @Order(2)
    void addPiste() {
        // Arrange
        PisteDTO pisteDTO = new PisteDTO();
        pisteDTO.setNamePiste("Piste A");
        pisteDTO.setColor("RED");
        pisteDTO.setLength(1500);
        pisteDTO.setSlope(25);
        // Create a mock Piste entity to be returned by the repository
        Piste piste = new Piste();
        piste.setNumPiste(1L); // Mocked ID for the saved entity
        piste.setNamePiste(pisteDTO.getNamePiste());
        piste.setColor(Color.RED);
        piste.setLength(pisteDTO.getLength());
        piste.setSlope(pisteDTO.getSlope());
        // Act
        Piste savedPiste = pisteService.addPiste(pisteDTO);
        // Store the ID for later use
        savedPisteId = savedPiste.getNumPiste();

        // Assert
        assertNotNull(savedPiste, "Saved Piste should not be null");
        assertNotNull(savedPiste.getNumPiste(), "Piste ID should be generated");
        System.out.println("Add Piste: Ok");

    }


    @Test
    @Order(3)
    void retrieveAllPistes() {
        // Act
        List<Piste> pistes = pisteService.retrieveAllPistes();
        // Assert
        assertNotNull(pistes, "Retrieved Pistes list should not be null");
        assertFalse(pistes.isEmpty(), "Pistes list should not be empty");
        System.out.println("Retrieve All Pistes: Ok");
    }

    @Test
    @Order(4)
    void retrievePiste() {
        // Ensure that the Piste was added in the previous test
        assertNotNull(savedPisteId, "Saved Piste ID should not be null");
        // Act
        Piste retrievedPiste = pisteService.retrievePiste(savedPisteId);
        // Assert
        assertNotNull(retrievedPiste, "Retrieved Piste should not be null");
        assertEquals("Piste A", retrievedPiste.getNamePiste(), "Piste name should match");
        assertEquals(Color.RED, retrievedPiste.getColor(), "Piste color should match");
        assertEquals(1500, retrievedPiste.getLength(), "Piste length should match");
        assertEquals(25, retrievedPiste.getSlope(), "Piste slope should match");
        System.out.println("Retrieve Piste: Ok");
    }

    @Test
    @Order(5)
    void removePiste() {
        // Ensure that the Piste was added in the previous tests
        assertNotNull(savedPisteId, "Saved Piste ID should not be null");
        // Act
        pisteService.removePiste(savedPisteId);
        // Assert
        assertThrows(EntityNotFoundException.class, () -> {
            pisteService.retrievePiste(savedPisteId);
        }, "Retrieving a removed Piste should throw EntityNotFoundException");
        System.out.println("Remove Piste: Ok");
    }
}
