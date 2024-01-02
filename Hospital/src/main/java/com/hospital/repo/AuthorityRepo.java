package com.hospital.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.model.Authority;

import java.util.Optional;

public interface AuthorityRepo extends JpaRepository<Authority, Long> {

    Optional<Authority> findByAuthority(String aLong);
}
