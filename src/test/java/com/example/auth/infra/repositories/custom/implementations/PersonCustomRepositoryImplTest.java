//package com.example.auth.infra.repositories.custom.implementations;
//
//import com.example.auth.domain.dtos.person.PersonGetFiltersDTO;
//import com.example.auth.domain.utils.Utils;
//import com.example.auth.infra.entities.Person;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.TypedQuery;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Root;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class PersonCustomRepositoryImplTest {
//
//    @Mock
//    private EntityManager entityManager;
//    @Mock
//    private CriteriaBuilder criteriaBuilder;
//    @Mock
//    private CriteriaQuery<Person> criteriaQuery;
//    @Mock
//    private Root<Person> personRoot;
//    @Mock
//    private TypedQuery<Person> typedQuery;
//    @Mock
//    private TypedQuery<Long> countTypedQuery;
//
//    @InjectMocks
//    private PersonCustomRepositoryImpl personCustomRepository;
//
//    private Long personID;
//    private Person person;
//    private PersonGetFiltersDTO personFilters;
//    private Pageable pageable;
//    @BeforeEach
//    void setUp() {
//        personID = 1L;
//
//        person = createNewPerson("John Doe", "12345678901", "john.doe@example.com");
//        person.setId(personID);
//
//        personFilters = createNewPersonGetFiltersDTO("John", "12345678901", "john.doe@example.com");
//
//        pageable = PageRequest.of(0, 10);
//
//        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
//        when(criteriaBuilder.createQuery(Person.class)).thenReturn(criteriaQuery);
//        when(criteriaQuery.from(Person.class)).thenReturn(personRoot);
//        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
//    }
//
//    @Test
//    void findWithFilters_success_fullFilters() {
//        Page<Person> expectedPage = new PageImpl<>(List.of(person), pageable, 1);
//
//        when(typedQuery.getResultList()).thenReturn(List.of(person));
//        when(countTypedQuery.getSingleResult()).thenReturn(1L);
//
//        Page<Person> resultPage = personCustomRepository.findWithFilters(personFilters, pageable);
//
//        assertNotNull(resultPage);
//        assertEquals(expectedPage.getTotalElements(), resultPage.getTotalElements());
//        assertEquals(expectedPage.getContent().size(), resultPage.getContent().size());
//        assertTrue(resultPage.getContent().containsAll(expectedPage.getContent()));
//    }
//
//    @Test
//    void findWithFilters_success_oneFilter() {
//        personFilters = createNewPersonGetFiltersDTO("John", null, null);
//        Pageable pageable = PageRequest.of(0, 10);
//
//        Page<Person> expectedPage = new PageImpl<>(Collections.singletonList(person), pageable, 1);
//
//        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(person));
//        when(countTypedQuery.getSingleResult()).thenReturn(1L);
//
//        Page<Person> resultPage = personCustomRepository.findWithFilters(personFilters, pageable);
//
//        assertNotNull(resultPage);
//        assertEquals(expectedPage.getTotalElements(), resultPage.getTotalElements());
//        assertTrue(resultPage.getContent().containsAll(expectedPage.getContent()));
//    }
//
//    @Test
//    void findWithFilters_success_emptyList() {
//        Page<Person> expectedPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
//
//        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());
//        when(countTypedQuery.getSingleResult()).thenReturn(0L);
//
//        Page<Person> resultPage = personCustomRepository.findWithFilters(new PersonGetFiltersDTO(), pageable);
//
//        assertNotNull(resultPage);
//        assertEquals(expectedPage.getTotalElements(), resultPage.getTotalElements());
//        assertTrue(resultPage.getContent().isEmpty());
//    }
//
//    @Test
//    void findWithFilters_fail_dueToException() {
//        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
//        when(criteriaBuilder.createQuery(Person.class)).thenReturn(criteriaQuery);
//        when(criteriaQuery.from(Person.class)).thenReturn(personRoot);
//        when(entityManager.createQuery(criteriaQuery)).thenThrow(new RuntimeException("Database error"));
//
//        Exception exception = assertThrows(RuntimeException.class, () -> personCustomRepository.findWithFilters(personFilters, pageable));
//
//        assertEquals("Database error", exception.getMessage());
//
//        verify(typedQuery, never()).getResultList();
//    }
//
//
//    private Person createNewPerson(String name, String cpf, String email) {
//        return Person.builder()
//                .name(name)
//                .cpf(cpf)
//                .email(email)
//                .dateAlteration(Utils.getDateTimeNowFormatted())
//                .dateCreation(Utils.getDateTimeNowFormatted())
//                .build();
//    }
//
//    private PersonGetFiltersDTO createNewPersonGetFiltersDTO(String name, String cpf, String email) {
//        return PersonGetFiltersDTO
//                .builder()
//                .name(name)
//                .cpf(cpf)
//                .email(email)
//                .build();
//    }
//}