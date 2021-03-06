package com.lifecode.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lifecode.jpa.entity.ConfirmationToken;;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long>{

	Optional<ConfirmationToken> findByToken(String confirmationToken);
}