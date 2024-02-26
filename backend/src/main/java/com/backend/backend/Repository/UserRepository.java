package com.backend.backend.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.backend.backend.Entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    // Additional custom queries can be added if needed
}
