package com.rithikaa.notora.service;

import com.rithikaa.notora.model.User;
import com.rithikaa.notora.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public boolean emailExists(String email) {
        return repo.existsByEmail(email);
    }

    public User findByEmail(String email) {
        return repo.findByEmail(email).orElse(null);
    }
    public void updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        repo.save(user);
    }

    public void saveUser(User user) {
        repo.save(user);
    }
}
