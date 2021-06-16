package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
