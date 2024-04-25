package com.example.auth.infra.repositories.custom.implementations;

import com.example.auth.domain.dtos.user.UserGetListFiltersDTO;
import com.example.auth.domain.utils.Utils;
import com.example.auth.infra.entities.Person;
import com.example.auth.infra.entities.User;
import com.example.auth.infra.entities.UserRole;
import com.example.auth.infra.repositories.custom.interfaces.UserCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public UserCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<User> findWithFilters(UserGetListFiltersDTO userFilters, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Constrói a consulta para buscar os registros
        CriteriaQuery<User> query = buildCriteriaQueryUser(userFilters, cb);
        List<User> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // Constrói a consulta para contar o total de registros
        CriteriaQuery<Long> countQuery = buildCriteriaQueryLong(userFilters, cb);
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }

    private CriteriaQuery<User> buildCriteriaQueryUser(UserGetListFiltersDTO userFilters, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        Predicate predicate = addPredicates(userFilters, criteriaBuilder, root);

        criteriaQuery.where(predicate);
        return criteriaQuery;
    }

    private CriteriaQuery<Long> buildCriteriaQueryLong(UserGetListFiltersDTO userFilters, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> root = criteriaQuery.from(User.class);
        Predicate predicate = addPredicates(userFilters, criteriaBuilder, root);

        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(predicate);

        return criteriaQuery;
    }

    private Predicate addPredicates(UserGetListFiltersDTO userFilters, CriteriaBuilder cb, Root<User> userRoot) {
        Predicate predicate = cb.conjunction();
        predicate = addPredicateExactString(predicate, cb, userRoot.get("username"), userFilters.getUsername());
        predicate = addPredicateBoolean(predicate, cb, userRoot.get("enabled"), userFilters.getEnabled());
        predicate = addRoleAndPersonPredicates(predicate, cb, userRoot, userFilters);
        return predicate;
    }

    private Predicate addPredicateExactLong(Predicate predicate, CriteriaBuilder cb, Path<Long> path, Long value) {
        if (value != null) {
            predicate = cb.and(predicate, cb.equal(path, value));
        }
        return predicate;
    }

    private Predicate addPredicateExactString(Predicate predicate, CriteriaBuilder cb, Path<String> path, String value) {
        if (Utils.isStringPresent(value)) {
            predicate = cb.and(predicate, cb.equal(path, value));
        }
        return predicate;
    }

    private Predicate addPredicateBoolean(Predicate predicate, CriteriaBuilder cb, Path<Boolean> path, Boolean value) {
        if (value != null) {
            predicate = cb.and(cb.equal(path, value));
        }
        return predicate;
    }

    private Predicate addRoleAndPersonPredicates(Predicate predicate, CriteriaBuilder cb, Root<User> userRoot, UserGetListFiltersDTO userFilters) {
        if (Utils.isStringPresent(userFilters.getRoleName())) {
            Join<User, UserRole> roleJoin = userRoot.join("role", JoinType.LEFT);
            predicate = cb.and(cb.equal(cb.lower(roleJoin.get("roleName")), userFilters.getRoleName().toLowerCase()));
        }

        Join<User, Person> personJoin = userRoot.join("person", JoinType.LEFT);
        if (Utils.isStringPresent(userFilters.getCpf())) {
            predicate = cb.and(cb.equal(personJoin.get("cpf"), userFilters.getCpf()));
        }
        if (Utils.isStringPresent(userFilters.getName())) {
            predicate = cb.and(cb.like(cb.lower(personJoin.get("name")), "%" + userFilters.getName().toLowerCase() + "%"));
        }
        if (Utils.isStringPresent(userFilters.getEmail())) {
            predicate = cb.and(cb.like(cb.lower(personJoin.get("email")), "%" + userFilters.getEmail().toLowerCase() + "%"));
        }
        return predicate;
    }
}
