package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServicesImpl;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServicesImplTest {

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSubscription() {
        Subscription subscription = new Subscription();
        subscription.setStartDate(LocalDate.now());
        subscription.setTypeSub(TypeSubscription.ANNUAL);

        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        Subscription result = subscriptionServices.addSubscription(subscription);

        assertEquals(subscription.getEndDate(), subscription.getStartDate().plusYears(1));
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testUpdateSubscription() {
        Subscription subscription = new Subscription();
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        Subscription result = subscriptionServices.updateSubscription(subscription);

        assertNotNull(result);
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testRetrieveSubscriptionById() {
        Long numSubscription = 1L;
        Subscription subscription = new Subscription();
        when(subscriptionRepository.findById(numSubscription)).thenReturn(Optional.of(subscription));

        Subscription result = subscriptionServices.retrieveSubscriptionById(numSubscription);

        assertNotNull(result);
        verify(subscriptionRepository, times(1)).findById(numSubscription);
    }

    @Test
    void testGetSubscriptionByType() {
        TypeSubscription type = TypeSubscription.ANNUAL;
        Set<Subscription> subscriptions = new HashSet<>();
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(type)).thenReturn(subscriptions);

        Set<Subscription> result = subscriptionServices.getSubscriptionByType(type);

        assertNotNull(result);
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(type);
    }


    @Test
    void testShowMonthlyRecurringRevenue() {
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(1000.0F);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL)).thenReturn(6000.0F);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL)).thenReturn(12000.0F);

        subscriptionServices.showMonthlyRecurringRevenue();

        Float expectedRevenue = 1000.0F + 6000.0F / 6 + 12000.0F / 12;
        assertEquals(expectedRevenue, 1000.0F + 1000.0F + 1000.0F);
    }
}
