package com.dietideals24.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dietideals24.demo.models.Asta;
import com.dietideals24.demo.models.dto.AstaDTO;

@Repository
public interface AstaRepository extends CrudRepository<Asta, Integer>{

	//Asta creaAsta(Asta asta,String tipo);
	
	@Query("select a from Asta a where a.categoria = :categoria AND a.descrizione LIKE %:key%")
	List<Asta> findPerCategoriaAndParoleChiave(String categoria,String key);
	
	@Query("DELETE from Asta a WHERE a.id = :id")
	void eliminaAsta(@Param("id") int id);
	
	@Query("Select a from Asta a WHERE a.nome LIKE %:nome%")
	List<Asta> findAstaByNome(@Param("nome") String nome);
}
