package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;
import tn.esprit.spring.services.InstructorServicesImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructorServicesImplTest {

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private InstructorServicesImpl instructorService;

    private Instructor instructor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instructor = new Instructor();
        instructor.setNumInstructor(1L);
        instructor.setFirstName("Alice");
        instructor.setLastName("Johnson");
        instructor.setDateOfHire(LocalDate.of(2020, 5, 10));
        instructor.setCourses(new HashSet<>()); // Initialize courses as an empty set
    }

    @Test
    void testAddInstructor() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor savedInstructor = instructorService.addInstructor(instructor);

        assertNotNull(savedInstructor);
        assertEquals("Alice", savedInstructor.getFirstName());
        assertEquals("Johnson", savedInstructor.getLastName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveAllInstructors() {
        when(instructorRepository.findAll()).thenReturn(Collections.singletonList(instructor));

        List<Instructor> instructors = instructorService.retrieveAllInstructors();

        assertNotNull(instructors);
        assertEquals(1, instructors.size());
        assertEquals(instructor.getFirstName(), instructors.get(0).getFirstName());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testUpdateInstructor() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor updatedInstructor = instructorService.updateInstructor(instructor);

        assertNotNull(updatedInstructor);
        assertEquals("Alice", updatedInstructor.getFirstName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveInstructor() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        Instructor foundInstructor = instructorService.retrieveInstructor(1L);

        assertNotNull(foundInstructor);
        assertEquals("Johnson", foundInstructor.getLastName());
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveInstructor_NotFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        Instructor foundInstructor = instructorService.retrieveInstructor(1L);

        assertNull(foundInstructor);
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void testAddInstructorAndAssignToCourse() {
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(2);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor assignedInstructor = instructorService.addInstructorAndAssignToCourse(instructor, 1L);

        assertNotNull(assignedInstructor);
        assertEquals(1, assignedInstructor.getCourses().size());
        assertTrue(assignedInstructor.getCourses().contains(course));
        verify(courseRepository, times(1)).findById(1L);
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testAddInstructorAndAssignToCourse_CourseNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Instructor assignedInstructor = instructorService.addInstructorAndAssignToCourse(instructor, 1L);

        assertNull(assignedInstructor); // Expect null if course is not found
        verify(courseRepository, times(1)).findById(1L);
        verify(instructorRepository, times(1)).save(instructor);
    }

}
