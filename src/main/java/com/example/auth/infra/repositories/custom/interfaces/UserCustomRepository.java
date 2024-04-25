package com.example.auth.infra.repositories.custom.interfaces;

import com.example.auth.domain.dtos.user.UserGetListFiltersDTO;
import com.example.auth.infra.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCustomRepository {
    Page<User> findWithFilters(UserGetListFiltersDTO userFilters, Pageable pageable);
}
