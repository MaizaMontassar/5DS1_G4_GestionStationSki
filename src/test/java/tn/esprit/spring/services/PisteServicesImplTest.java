package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.dto.PisteDTO;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.repositories.IPisteRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PisteServicesImplTest {

    @Mock
    private IPisteRepository pisteRepository; // Mocking the Piste repository to avoid real database calls
    @Mock
    private ISkierRepository skierRepository; // Mocking the Skier repository, although not used in this test
    @InjectMocks
    private PisteServicesImpl pisteService; // Injecting mocks into the service class being tested

    private Skier skier; // Skier object used for testing
    private Piste piste; // Piste object used for testing

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes the mocks
        piste = new Piste(); // Creating a new Piste object for testing
        // Setting attributes for the Piste object
        piste.setNumPiste(1L);
        piste.setNamePiste("Piste A");
        piste.setColor(Color.BLUE);
        piste.setLength(300); // Length is set to 300 for verification in tests
        piste.setSlope(10);
        skier = new Skier(); // Creating a new Skier object for testing
        skier.setPistes(new HashSet<>()); // Initialize the pistes collection to avoid NullPointerException
        skier.setNumSkier(1L); // Setting a skier number for consistency
    }

    @Test
    void retrieveAllPistes() {
        // Setting up the expected outcome: a list containing the previously created piste
        List<Piste> expectedPistes = Arrays.asList(piste);
        // Mocking the repository response to return the expected list when findAll() is called
        when(pisteRepository.findAll()).thenReturn(expectedPistes);
        // Calling the method under test, which should interact with the mocked repository
        List<Piste> actualPistes = pisteService.retrieveAllPistes();
        // Asserting the results of the retrieval
        assertNotNull(actualPistes); // Ensure the result is not null
        assertEquals(1, actualPistes.size()); // Verify the list size matches the expected count (1)
        // Size" refers to the number of elements in a collection
        assertEquals(300, actualPistes.get(0).getLength()); // Verify the length of the first piste matches the setup value (300)
        // Verifying that the repository's findAll() method was called exactly once
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void addPiste() {
        // Creating a PisteDTO object to simulate data transfer for a new piste
        PisteDTO pisteDTO = new PisteDTO("Piste A", "RED", 500, 30);

        // Preparing the expected Piste object based on the DTO
        Piste piste = new Piste();
        piste.setNamePiste(pisteDTO.getNamePiste()); // Setting name from DTO
        piste.setColor(Color.valueOf(pisteDTO.getColor().toUpperCase())); // Setting color from DTO (ensured to be uppercase)
        piste.setLength(pisteDTO.getLength()); // Setting length from DTO
        piste.setSlope(pisteDTO.getSlope()); // Setting slope from DTO

        // Mocking the repository's save method to return the prepared Piste when called
        when(pisteRepository.save(any(Piste.class))).thenReturn(piste);

        // Calling the method under test to add a new piste
        Piste savedPiste = pisteService.addPiste(pisteDTO);

        // Asserting the results of the addition
        assertNotNull(savedPiste); // Ensure the saved piste is not null
        assertEquals("Piste A", savedPiste.getNamePiste()); // Verify the name matches the expected value
        assertEquals(Color.RED, savedPiste.getColor()); // Verify the color matches the expected value
        assertEquals(500, savedPiste.getLength()); // Verify the length matches the expected value (500)
        assertEquals(30, savedPiste.getSlope()); // Verify the slope matches the expected value

        // Verifying that the repository's save method was called exactly once
        verify(pisteRepository, times(1)).save(any(Piste.class));
    }

    @Test
    void removePiste() {
        // Act: Calling the remove method to delete a piste with ID 1
        pisteService.removePiste(1L);
        // Assert and Verify: Ensure that the repository's deleteById method was called with the correct ID
        verify(pisteRepository, times(1)).deleteById(1L);
    }

    @Test
    void retrievePiste() {
        // Mocking the repository response to return the previously created piste when searched by ID
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));
        // Calling the method under test to retrieve the piste by ID
        Piste retrievedPiste = pisteService.retrievePiste(1L);
        // Asserting the results of the retrieval
        assertNotNull(retrievedPiste); // Ensure the retrieved piste is not null
        assertEquals("Piste A", retrievedPiste.getNamePiste()); // Verify the name of the retrieved piste matches the expected value
        // Verifying that the repository's findById method was called exactly once with the correct ID
        verify(pisteRepository, times(1)).findById(1L);
    }
}
