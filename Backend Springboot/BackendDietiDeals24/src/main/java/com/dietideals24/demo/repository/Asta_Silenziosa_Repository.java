package com.dietideals24.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dietideals24.demo.enums.StatoAsta;
import com.dietideals24.demo.models.Asta_Inversa;
import com.dietideals24.demo.models.Asta_Silenziosa;

import jakarta.transaction.Transactional;

@Repository
public interface Asta_Silenziosa_Repository extends CrudRepository<Asta_Silenziosa, Integer>{

	@Transactional
    @Modifying
    @Query(value = "INSERT INTO asta_silenziosa (id, scadenza) VALUES (:id_asta, :scadenza)", nativeQuery = true)
    void insertAstaSilenziosa(@Param("id_asta") int id_asta, @Param("scadenza") String scadenza);
	
	@Transactional
	@Modifying
	@Query("DELETE from Asta_Silenziosa a WHERE a.id = :id")
	void eliminaAstaSilenziosa(@Param("id") int id);
	
	@Query("SELECT a FROM Asta_Silenziosa a WHERE a.id = :id")
	Asta_Silenziosa getAstaSilenziosa(@Param("id") int id);
	
	@Query("SELECT as FROM Asta_Silenziosa as JOIN Asta a ON as.id = a.id WHERE a.stato = :stato")
    List<Asta_Silenziosa> cercaAsteSilenziose(StatoAsta stato);
}
