package com.dietideals24.demo.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.dietideals24.demo.models.Asta;
import com.dietideals24.demo.models.Asta_Silenziosa;

import jakarta.transaction.Transactional;

@Repository
public interface Asta_Silenziosa_Repository extends CrudRepository<Asta_Silenziosa, Integer>{

	@Transactional
    @Modifying
    @Query(value = "INSERT INTO asta_silenziosa (id_asta, scadenza) VALUES (:id_asta, :scadenza)", nativeQuery = true)
    void insertAstaSilenziosa(@Param("id_asta") int id_asta, @Param("scadenza") LocalDateTime scadenza);
	
	@Query("DELETE from Asta_Silenziosa a WHERE a.id = :id")
	void eliminaAstaSilenziosa(@Param("id") int id);
	
	@Query("SELECT a FROM Asta_Silenziosa a WHERE a.id = :id")
	Asta_Silenziosa getAstaSilenziosa(@Param("id") int id);
}
