package com.ftn.sbnz.service.repositories;

import com.ftn.sbnz.model.models.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseJPARepository<User> {
    Optional<User> findByEmail(String email);
}
