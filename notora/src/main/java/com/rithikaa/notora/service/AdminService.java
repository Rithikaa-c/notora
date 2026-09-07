package com.rithikaa.notora.service;

import com.rithikaa.notora.model.Admin;
import com.rithikaa.notora.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository repo;

    public AdminService(AdminRepository repo) {
        this.repo = repo;
    }

    public Admin findByEmail(String email) {
        return repo.findByEmail(email);
    }
}
