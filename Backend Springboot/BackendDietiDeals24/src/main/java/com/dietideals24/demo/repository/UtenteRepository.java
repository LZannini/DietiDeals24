package com.dietideals24.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dietideals24.demo.models.Utente;

@Repository
public interface UtenteRepository extends CrudRepository<Utente, Integer>{
	
	@Query("SELECT u FROM Utente u WHERE u.email = :email AND u.password = :password")
    Optional<Utente> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
	
	@Query("SELECT u FROM Utente u WHERE u.email = :email AND u.id = :id")
    Optional<Utente> findByEmailAndId(@Param("email") String email, @Param("id") int id);
	
	@Query("SELECT u FROM Utente u WHERE u.id = :id")
	Optional<Utente> findById(@Param("id") int id);
	
	@Modifying
	@Transactional
	@Query("UPDATE Utente u SET u.password = :password WHERE u.id = :id")
	void updatePassword(@Param("password") String password, @Param("id") int id);

	@Query("SELECT u FROM Utente u WHERE u.email = :email")
    Optional<Utente> findByEmail(@Param("email") String email);
}
