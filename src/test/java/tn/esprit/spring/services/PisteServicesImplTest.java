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
    private IPisteRepository pisteRepository;
    @Mock
    private ISkierRepository skierRepository;
    @InjectMocks
    private PisteServicesImpl pisteService;

    private Skier skier;
    private Piste piste;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        piste = new Piste();
        piste.setNumPiste(1L);
        piste.setNamePiste("Piste A");
        piste.setColor(Color.BLUE);
        piste.setLength(300);
        piste.setSlope(10);
        skier = new Skier();
        skier.setPistes(new HashSet<>()); // Initialize the pistes collection
        skier.setNumSkier(1L);
    }

    @Test
    void retrieveAllPistes() {
        List<Piste> expectedPistes = Arrays.asList(piste);
        // Mocking the repository response
        when(pisteRepository.findAll()).thenReturn(expectedPistes);
        // Calling the method under test
        List<Piste> actualPistes = pisteService.retrieveAllPistes();
        // Asserting the results
        assertNotNull(actualPistes);
        assertEquals(1, actualPistes.size());
        assertEquals(300, actualPistes.get(0).getLength());
        // Verifying the interaction with the mocked repository
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void addPiste() {
        // Create a PisteDTO for testing
        PisteDTO pisteDTO = new PisteDTO("Piste A", "RED", 500, 30);

        // Mock the conversion and repository save behavior
        Piste piste = new Piste();
        piste.setNamePiste(pisteDTO.getNamePiste());
        piste.setColor(Color.valueOf(pisteDTO.getColor().toUpperCase()));
        piste.setLength(pisteDTO.getLength());
        piste.setSlope(pisteDTO.getSlope());

        // Mocking the repository save method
        when(pisteRepository.save(any(Piste.class))).thenReturn(piste);

        // Calling the method under test
        Piste savedPiste = pisteService.addPiste(pisteDTO);

        // Asserting the results
        assertNotNull(savedPiste);
        assertEquals("Piste A", savedPiste.getNamePiste());
        assertEquals(Color.RED, savedPiste.getColor());
        assertEquals(500, savedPiste.getLength());
        assertEquals(30, savedPiste.getSlope());

        // Verifying the interaction with the mocked repository
        verify(pisteRepository, times(1)).save(any(Piste.class));
    }

    @Test
    void removePiste() {
        // Act
        pisteService.removePiste(1L);
        // Assert and Verify
        verify(pisteRepository, times(1)).deleteById(1L);
    }

    @Test
    void retrievePiste() {
        // Mocking the repository response
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));
        // Calling the method under test
        Piste retrievedPiste = pisteService.retrievePiste(1L);
        // Asserting the results
        assertNotNull(retrievedPiste);
        assertEquals("Piste A", retrievedPiste.getNamePiste());
        // Verifying the interaction with the mocked repository
        verify(pisteRepository, times(1)).findById(1L);
    }
}