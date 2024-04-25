package com.example.auth.infra.repositories.custom.implementations;

import com.example.auth.domain.dtos.person.PersonGetFiltersDTO;
import com.example.auth.domain.utils.Utils;
import com.example.auth.infra.entities.Person;
import com.example.auth.infra.repositories.custom.interfaces.PersonCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonCustomRepositoryImpl implements PersonCustomRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public PersonCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<Person> findWithFilters(PersonGetFiltersDTO filters, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Constrói a consulta para buscar os registros
        CriteriaQuery<Person> criteriaQuery = buildCriteriaQueryPerson(filters, cb);
        List<Person> resultList = entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // Constrói a consulta para contar o total de registros
        CriteriaQuery<Long> countQuery = buildCriteriaQueryLong(filters, cb);
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }

    private CriteriaQuery<Person> buildCriteriaQueryPerson(PersonGetFiltersDTO personFilters, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
        Root<Person> root = criteriaQuery.from(Person.class);
        Predicate predicate = addPredicates(personFilters, criteriaBuilder, root);

        criteriaQuery.where(predicate);
        return criteriaQuery;
    }

    private CriteriaQuery<Long> buildCriteriaQueryLong(PersonGetFiltersDTO personFilters, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Person> root = criteriaQuery.from(Person.class);
        Predicate predicate = addPredicates(personFilters, criteriaBuilder, root);

        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(predicate);

        return criteriaQuery;
    }

    private Predicate addPredicates(PersonGetFiltersDTO personFilters, CriteriaBuilder cb, Root<Person> personRoot) {
        Predicate predicate = cb.conjunction();
        predicate = addPredicateExactString(predicate, cb, personRoot.get("cpf"), personFilters.getCpf());
        predicate = addPredicateLikeStrig(predicate, cb, personRoot.get("name"), personFilters.getName());
        predicate = addPredicateLikeStrig(predicate, cb, personRoot.get("email"), personFilters.getEmail());

        return predicate;
    }

    private Predicate addPredicateExactString(Predicate predicate, CriteriaBuilder cb, Path<String> path, String value) {
        if (Utils.isStringPresent(value)) {
            predicate = cb.and(predicate, cb.equal(path, value));
        }
        return predicate;
    }

    private Predicate addPredicateLikeStrig(Predicate predicate, CriteriaBuilder cb, Path<String> path, String value) {
        if (Utils.isStringPresent(value)) {
            predicate = cb.and(predicate, cb.like(cb.lower(path), "%" + value.toLowerCase() + "%"));
        }
        return predicate;
    }
}
