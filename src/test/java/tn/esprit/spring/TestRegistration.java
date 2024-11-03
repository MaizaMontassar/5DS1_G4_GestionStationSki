package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.services.RegistrationServicesImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestRegistration {

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRegistrationAndAssignToSkier() {
        Registration registration = new Registration();
        Long numSkier = 1L;
        Skier skier = new Skier();
        skier.setNumSkier(numSkier);

        when(skierRepository.findById(numSkier)).thenReturn(Optional.of(skier));
        when(registrationRepository.save(registration)).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, numSkier);

        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        verify(skierRepository, times(1)).findById(numSkier);
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void testNumWeeksCourseOfInstructorBySupport() {
        Long numInstructor = 1L;
        Support support = Support.SNOWBOARD;
        List<Integer> weeks = Collections.singletonList(1);

        when(registrationRepository.numWeeksCourseOfInstructorBySupport(numInstructor, support)).thenReturn(weeks);

        List<Integer> result = registrationServices.numWeeksCourseOfInstructorBySupport(numInstructor, support);

        assertNotNull(result);
        assertEquals(weeks, result);
        verify(registrationRepository, times(1)).numWeeksCourseOfInstructorBySupport(numInstructor, support);
    }
}
