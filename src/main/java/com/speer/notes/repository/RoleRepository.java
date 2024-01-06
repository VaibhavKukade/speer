package com.speer.notes.repository;

import java.util.Optional;

import com.speer.notes.model.ERole;
import com.speer.notes.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
