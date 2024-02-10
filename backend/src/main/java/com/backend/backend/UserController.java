package com.backend.backend;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/helloUsers")
    public String getUsers() {
        return "hello users";
    }
    
    @GetMapping("/getUsers")
    public List<User> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    @PostMapping("/createUser")
    public User createUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUserData) {
        // Find the existing user by ID
        if (!userRepository.existsById(id)) {
            // If not found, return a 404 Not Found response
             String errorMessage = "User with ID " + id + " not found";
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserErrorResponse(id, errorMessage)
            );
    
        }

        // Save the updated user to the database
        User updatedUser = userRepository.save(updatedUserData);
        return ResponseEntity.ok(updatedUser);
    }

    // Get all users
    
    // @GetMapping
    // public ResponseEntity<List<User>> getAllUsers() {
    //     List<User> users = userRepository.findAll();
    //     return new ResponseEntity<>(users, HttpStatus.OK);
    // }

    // Get a user by ID
    // @GetMapping("/{id}")
    // public ResponseEntity<User> getUserById(@PathVariable Long id) {
    //     User user = userRepository.findById(id).orElse(null);
    //     if (user != null) {
    //         return new ResponseEntity<>(user, HttpStatus.OK);
    //     } else {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    // }

    // Create a new user
    // @PostMapping
    // public ResponseEntity<User> createUser(@RequestBody User newUser) {
    //     User savedUser = userRepository.save(newUser);
    //     return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    // }

    // Other CRUD operations (update and delete) can be added similarly

    
}
