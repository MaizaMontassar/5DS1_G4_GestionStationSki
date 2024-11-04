package tn.esprit.spring.services;

import org.junit.jupiter.api.*; // Importing JUnit 5 annotations
import org.springframework.beans.factory.annotation.Autowired; // Import for dependency injection
import org.springframework.boot.test.context.SpringBootTest; // Annotation to specify that the class is a Spring Boot test
import tn.esprit.spring.dto.PisteDTO; // Data transfer object for Piste
import tn.esprit.spring.repositories.IPisteRepository; // Repository interface for Piste entities
import tn.esprit.spring.repositories.ISkierRepository; // Repository interface for Skier entities
import tn.esprit.spring.entities.Color; // Enum for Piste color
import tn.esprit.spring.entities.Piste; // Entity class for Piste
import tn.esprit.spring.entities.Skier; // Entity class for Skier

import javax.persistence.EntityNotFoundException; // Exception thrown when an entity is not found
import java.time.LocalDate; // Class for date handling
import java.util.HashSet; // HashSet implementation
import java.util.List; // List interface

import static org.junit.jupiter.api.Assertions.*; // Static imports for JUnit assertions

@SpringBootTest // Loads the application context for integration tests
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Orders the test methods based on the specified order
class PisteServiceJUnitTest {

    @Autowired // Spring will inject the PisteServicesImpl bean into this variable
    private PisteServicesImpl pisteService; // Service class under test

    @Autowired // Spring will inject the actual Piste repository bean
    private IPisteRepository pisteRepository; // Repository for Piste entities

    @Autowired // Spring will inject the actual Skier repository bean
    private ISkierRepository skierRepository; // Repository for Skier entities

    private static Long savedPisteId; // Static variable to hold the ID of the saved Piste for later tests

    @BeforeEach // This method will run before each test method
    @Order(1) // Indicates the order in which this method should run
    void setUp() {
        // Arrange - Setup necessary data before each test.
        // Creating two Skier objects for testing, ensuring they are persisted in the repository.

        Skier skier1 = new Skier();
        skier1.setFirstName("John");
        skier1.setLastName("Doe");
        skier1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        skier1.setCity("Paris");
        skier1.setPistes(new HashSet<>()); // Initialize the pistes collection for the skier
        skierRepository.save(skier1); // Persist skier1 to the database

        Skier skier2 = new Skier();
        skier2.setFirstName("Jane");
        skier2.setLastName("Smith");
        skier2.setDateOfBirth(LocalDate.of(1985, 5, 15));
        skier2.setCity("Lyon");
        skier2.setPistes(new HashSet<>()); // Initialize the pistes collection for the skier
        skierRepository.save(skier2); // Persist skier2 to the database
    }

    @Test // Indicates that this method is a test case
    @Order(2) // Specifies the order of execution for this test case
    void addPiste() {
        // Arrange
        // Create a PisteDTO to represent the data for a new Piste.
        PisteDTO pisteDTO = new PisteDTO();
        pisteDTO.setNamePiste("Piste A");
        pisteDTO.setColor("RED");
        pisteDTO.setLength(1500); // Length of the piste in meters
        pisteDTO.setSlope(25); // Slope of the piste in degrees

        // Act
        // Call the service method to add a new Piste and store its ID for later use.
        Piste savedPiste = pisteService.addPiste(pisteDTO);
        savedPisteId = savedPiste.getNumPiste(); // Save the ID of the newly added Piste

        // Assert
        // Validate that the saved Piste is not null and has a generated ID.
        assertNotNull(savedPiste, "Saved Piste should not be null");
        assertNotNull(savedPiste.getNumPiste(), "Piste ID should be generated");
        System.out.println("Add Piste: Ok");
    }

    @Test // Indicates that this method is a test case
    @Order(3) // Specifies the order of execution for this test case
    void retrieveAllPistes() {
        // Act
        // Call the service method to retrieve all pistes from the repository.
        List<Piste> pistes = pisteService.retrieveAllPistes();

        // Assert
        // Validate that the retrieved pistes list is not null and is not empty.
        assertNotNull(pistes, "Retrieved Pistes list should not be null");
        assertFalse(pistes.isEmpty(), "Pistes list should not be empty");
        System.out.println("Retrieve All Pistes: Ok");
    }

    @Test // Indicates that this method is a test case
    @Order(4) // Specifies the order of execution for this test case
    void retrievePiste() {
        // Ensure that the Piste was added in the previous test
        assertNotNull(savedPisteId, "Saved Piste ID should not be null");

        // Act
        // Call the service method to retrieve the specific Piste by its ID.
        Piste retrievedPiste = pisteService.retrievePiste(savedPisteId);

        // Assert
        // Validate that the retrieved Piste matches the expected values.
        assertNotNull(retrievedPiste, "Retrieved Piste should not be null");
        assertEquals("Piste A", retrievedPiste.getNamePiste(), "Piste name should match");
        assertEquals(Color.RED, retrievedPiste.getColor(), "Piste color should match");
        assertEquals(1500, retrievedPiste.getLength(), "Piste length should match");
        assertEquals(25, retrievedPiste.getSlope(), "Piste slope should match");
        System.out.println("Retrieve Piste: Ok");
    }

    @Test // Indicates that this method is a test case
    @Order(5) // Specifies the order of execution for this test case
    void removePiste() {
        // Ensure that the Piste was added in the previous tests
        assertNotNull(savedPisteId, "Saved Piste ID should not be null");

        // Act
        // Call the service method to remove the specific Piste by its ID.
        pisteService.removePiste(savedPisteId);

        // Assert
        // Validate that attempting to retrieve the removed Piste throws an exception.
        assertThrows(EntityNotFoundException.class, () -> {
            pisteService.retrievePiste(savedPisteId);
        }, "Retrieving a removed Piste should throw EntityNotFoundException");
        System.out.println("Remove Piste: Ok");
    }
}