package com.dietideals24.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dietideals24.demo.enums.Categoria;
import com.dietideals24.demo.models.Asta;
import com.dietideals24.demo.models.dto.AstaDTO;

@Repository
public interface AstaRepository extends CrudRepository<Asta, Integer>{
	
	@Query("DELETE from Asta a WHERE a.id = :id")
	void eliminaAsta(@Param("id") int id);
	
	@Query("SELECT a FROM Asta a WHERE a.id = :id")
	Asta getAsta(@Param("id") int id);
	
	@Query("SELECT a FROM Asta a WHERE a.id_creatore = :id_creatore")
	List<Asta> filtraPerUtente(@Param("id_creatore") int id_creatore);
	
	@Query("SELECT a FROM Asta a WHERE a.nome LIKE %:parola_chiave% OR a.descrizione LIKE %:chiave%")
	List<Asta> filtraPerParolaChiave(@Param("chiave") String chiave);
	
	@Query("SELECT a FROM Asta a WHERE a.categoria = :categoria")
	List<Asta> filtraPerCategoria(@Param("categoria") Categoria categoria);
	
	@Query("SELECT a FROM Asta a WHERE a.categoria = :categoria AND (a.descrizione LIKE %:chiave% OR a.nome LIKE %:chiave%)")
	List<Asta> filtraPerCategoriaAndParoleChiave(@Param("chiave") String chiave, @Param("categoria") Categoria categoria);
}
