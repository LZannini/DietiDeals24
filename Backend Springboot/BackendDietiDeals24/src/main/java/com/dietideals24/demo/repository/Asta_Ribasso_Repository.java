package com.dietideals24.demo.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.dietideals24.demo.models.Asta_Ribasso;
import jakarta.transaction.Transactional;

@Repository
public interface Asta_Ribasso_Repository extends CrudRepository<Asta_Ribasso, Integer>{
	
	@Transactional
    @Modifying
    @Query(value = "INSERT INTO asta_ribasso (id_asta, prezzo, timer, decremento, minimo) VALUES (:id_asta, :prezzo, :timer, :decremento, :minimo)", nativeQuery = true)
    void insertAstaAlRibasso(@Param("id_asta") int id_asta, @Param("prezzo") float prezzo, @Param("timer") LocalDateTime timer, @Param("decremento") float decremento, @Param("minimo") float minimo);

	@Transactional
	@Modifying
	@Query("DELETE from Asta_Ribasso a WHERE a.id = :id")
	void eliminaAstaAlRibasso(@Param("id") int id);
	
	@Query("SELECT a FROM Asta_Ribasso a WHERE a.id = :id")
	Asta_Ribasso getAstaAlRibasso(@Param("id") int id);
}
