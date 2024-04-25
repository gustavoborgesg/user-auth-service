//package com.example.auth.infra.repositories.custom.implementations;
//
//import com.example.auth.domain.dtos.user.UserGetListFiltersDTO;
//import com.example.auth.domain.utils.Utils;
//import com.example.auth.infra.entities.Person;
//import com.example.auth.infra.entities.User;
//import com.example.auth.infra.entities.UserRole;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.TypedQuery;
//import jakarta.persistence.criteria.*;
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
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
////@DataJpaTest
////@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class)
//class UserCustomRepositoryImplTest {
//
//    @Mock
//    private EntityManager entityManager;
//    @Mock
//    private CriteriaBuilder criteriaBuilder;
//    @Mock
//    private CriteriaQuery<User> userCriteriaQuery;
//    @Mock
//    private CriteriaQuery<Long> longCriteriaQuery;
//    @Mock
//    private TypedQuery<User> userTypedQuery;
//    @Mock
//    private TypedQuery<Long> longTypedQuery;
//    @Mock
//    private Root<User> userRoot;
//    @Mock
//    private Join<User, UserRole> roleJoin;
//    @Mock
//    private Join<User, Person> personJoin;
//
//    @InjectMocks
//    private UserCustomRepositoryImpl userCustomRepository;
//
//    private Pageable pageable;
//    private Long total;
//    private Long userID;
//    private String userRoleName;
//    private User user;
//    private Person person;
//    private UserRole userRole;
//    private UserGetListFiltersDTO userFilters;
//
//    @BeforeEach
//    void setUp() {
//        userID = 1L;
//        userRoleName = "ROLE_ADMIN";
//        total = 1L;
//        pageable = PageRequest.of(0, 10);
//
//        userRole = createNewUserRole(userRoleName);
//        userRole.setId(userID);
//
//        person = createNewPerson("John Doe", "12345678901", "john.doe@example.com");
//        person.setId(userID);
//
//        user = createNewUser("john", "123", userRole, person);
//        user.setId(userID);
//
//        userFilters = createNewUserGetFiltersDTO("john", true, userRoleName, "John Doe", "12345678901", "john.doe@example.com");
//
//        pageable = PageRequest.of(0, 10);
//
//        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
//        when(criteriaBuilder.createQuery(User.class)).thenReturn(userCriteriaQuery);
//        when(userCriteriaQuery.from(User.class)).thenReturn(userRoot);
//        when(entityManager.createQuery(userCriteriaQuery)).thenReturn(userTypedQuery);
//
//        when(criteriaBuilder.createQuery(Long.class)).thenReturn(longCriteriaQuery);
//        when(entityManager.createQuery(longCriteriaQuery)).thenReturn(longTypedQuery);
//
//        when(userTypedQuery.setFirstResult(anyInt())).thenReturn(userTypedQuery);
//        when(userTypedQuery.setMaxResults(anyInt())).thenReturn(userTypedQuery);
//        when(longTypedQuery.setFirstResult(anyInt())).thenReturn(longTypedQuery);
//        when(longTypedQuery.setMaxResults(anyInt())).thenReturn(longTypedQuery);
//
//        when(userRoot.<User, UserRole>join("role", JoinType.LEFT)).thenReturn(roleJoin);
//        when(userRoot.<User, Person>join("person", JoinType.LEFT)).thenReturn(personJoin);
//    }
//
//    @Test
//    public void testFindWithFilters_Success() {
//        userRoot = userCriteriaQuery.from(User.class);
//        List<User> userList = new ArrayList<>();
//        userList.add(user);
//
//        when(userTypedQuery.getResultList()).thenReturn(userList);
//        when(longTypedQuery.getSingleResult()).thenReturn(1L);
//
//        Page<User> result = userCustomRepository.findWithFilters(userFilters, pageable);
//
//        assertEquals(1, result.getTotalElements());
//        assertEquals(1, result.getContent().size());
//        assertEquals(user, result.getContent().getFirst());
//    }
//
//    @Test
//    void findWithFilters_success_emptyList() {
//        Page<User> expectedPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
//
//        when(entityManager.createQuery(userCriteriaQuery).getResultList()).thenReturn(Collections.emptyList());
//        when(entityManager.createQuery(longCriteriaQuery).getSingleResult()).thenReturn(0L);
//
//        Page<User> resultPage = userCustomRepository.findWithFilters(userFilters, pageable);
//
//        assertNotNull(resultPage);
//        assertTrue(resultPage.getContent().isEmpty());
//        assertEquals(expectedPage.getTotalElements(), resultPage.getTotalElements());
//    }
//
//    @Test
//    void findWithFilters_fail_dueToException() {
//        when(entityManager.createQuery(userCriteriaQuery)).thenThrow(new RuntimeException("Database error"));
//
//        Exception exception = assertThrows(RuntimeException.class, () -> userCustomRepository.findWithFilters(userFilters, pageable));
//
//        assertEquals("Database error", exception.getMessage());
//    }
//
//    private User createNewUser(String username, String password, UserRole role, Person person) {
//        return User.builder()
//                .username(username)
//                .password(password)
//                .role(role)
//                .enabled(true)
//                .dateAlteration(Utils.getDateTimeNowFormatted())
//                .dateCreation(Utils.getDateTimeNowFormatted())
//                .person(person)
//                .build();
//    }
//
//    private UserGetListFiltersDTO createNewUserGetFiltersDTO(String username, Boolean enabled, String roleName, String name, String cpf, String email) {
//        return UserGetListFiltersDTO.builder()
//                .username(username)
//                .enabled(enabled)
//                .roleName(roleName)
//                .cpf(cpf)
//                .name(name)
//                .email(email)
//                .build();
//    }
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
//    private UserRole createNewUserRole(String name) {
//        return UserRole.builder()
//                .roleName(name)
//                .dateAlteration(Utils.getDateTimeNowFormatted())
//                .dateCreation(Utils.getDateTimeNowFormatted())
//                .build();
//    }
//}
//
//
