package com.rithikaa.notora.repository;

import com.rithikaa.notora.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByEmail(String email);
}
