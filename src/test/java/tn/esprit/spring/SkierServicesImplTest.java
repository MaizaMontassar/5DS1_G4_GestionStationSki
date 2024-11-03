package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;
import tn.esprit.spring.services.SkierServicesImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkierServicesImplTest {

    @InjectMocks
    private SkierServicesImpl skierServices;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllSkiers() {
        Skier skier1 = new Skier(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "City1", null, new HashSet<>(), new HashSet<>());
        Skier skier2 = new Skier(2L, "Jane", "Smith", LocalDate.of(1992, 2, 2), "City2", null, new HashSet<>(), new HashSet<>());

        List<Skier> skiers = Arrays.asList(skier1, skier2);
        when(skierRepository.findAll()).thenReturn(skiers);

        List<Skier> result = skierServices.retrieveAllSkiers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(skierRepository, times(1)).findAll();
    }

    @Test
    void testAddSkier() {
        Skier skier = new Skier();
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.of(2024, 1, 1));
        skier.setSubscription(subscription);

        when(skierRepository.save(skier)).thenReturn(skier);

        Skier result = skierServices.addSkier(skier);

        assertNotNull(result);
        assertEquals(LocalDate.of(2025, 1, 1), skier.getSubscription().getEndDate());
        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    void testRemoveSkier() {
        Long numSkier = 1L;

        doNothing().when(skierRepository).deleteById(numSkier);

        skierServices.removeSkier(numSkier);

        verify(skierRepository, times(1)).deleteById(numSkier);
    }

    @Test
    void testRetrieveSkier() {
        Long numSkier = 1L;
        Skier skier = new Skier(numSkier, "John", "Doe", LocalDate.of(1990, 1, 1), "City1", null, new HashSet<>(), new HashSet<>());

        when(skierRepository.findById(numSkier)).thenReturn(Optional.of(skier));

        Skier result = skierServices.retrieveSkier(numSkier);

        assertNotNull(result);
        assertEquals(numSkier, result.getNumSkier());
        verify(skierRepository, times(1)).findById(numSkier);
    }
}
