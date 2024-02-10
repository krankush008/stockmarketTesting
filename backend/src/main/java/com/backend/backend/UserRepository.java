package com.backend.backend;

//import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.repository.CrudRepository;
 import org.springframework.stereotype.Repository;
// import org.springframework.context.annotation.ComponentScan;
// import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


// @EnableJpaRepositories("com.backend.backend.*")
// @ComponentScan(basePackages = { "com.backend.backend.*" })
// @EntityScan("com.backend.backend.*")   
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    // Additional custom queries can be added if needed
}
