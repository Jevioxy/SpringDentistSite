package com.example.springmodels.controllers;

import com.example.springmodels.models.modelUser;
import com.example.springmodels.repos.userRepository;  // правильное имя репозитория
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private userRepository userRepository;  // правильное имя

    // Получение всех пользователей (API)
    @GetMapping
    public List<modelUser> getAllUsers() {
        return (List<modelUser>) userRepository.findAll();
    }

    // Получение пользователя по ID (API)
    @GetMapping("/{id}")
    public ResponseEntity<modelUser> getUserById(@PathVariable Long id) {
        Optional<modelUser> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Создание нового пользователя (API)
    @PostMapping
    public ResponseEntity<modelUser> createUser(@RequestBody modelUser user) {
        modelUser savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Обновление пользователя (API)
    @PutMapping("/{id}")
    public ResponseEntity<modelUser> updateUserApi(@PathVariable Long id, @RequestBody modelUser userDetails) {
        Optional<modelUser> user = userRepository.findById(id);
        if (user.isPresent()) {
            modelUser updatedUser = user.get();
            updatedUser.setUsername(userDetails.getUsername());
            updatedUser.setRoles(userDetails.getRoles());
            updatedUser.setActive(userDetails.isActive());
            userRepository.save(updatedUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Удаление пользователя (API)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserApi(@PathVariable Long id) {
        Optional<modelUser> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
