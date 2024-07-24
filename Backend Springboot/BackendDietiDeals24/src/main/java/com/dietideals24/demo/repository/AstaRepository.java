package com.dietideals24.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.dietideals24.demo.enums.Categoria;
import com.dietideals24.demo.enums.StatoAsta;
import com.dietideals24.demo.models.Asta;
import jakarta.transaction.Transactional;

@Repository
public interface AstaRepository extends CrudRepository<Asta, Integer>{
	
	@Transactional
	@Modifying
	@Query("DELETE from Asta a WHERE a.id = :id")
	void eliminaAsta(@Param("id") int id);
	
	@Query("SELECT a FROM Asta a WHERE a.id = :id")
	Asta getAsta(@Param("id") int id);
	
	@Query("SELECT a FROM Asta a WHERE stato = :statoAsta ORDER BY a.nome")
	List<Asta> cercaTutte(@Param("statoAsta") StatoAsta statoAsta);
	
	@Query("SELECT a FROM Asta a WHERE a.id_creatore = :id_creatore ORDER BY a.nome")
	List<Asta> filtraPerUtente(@Param("id_creatore") int id_creatore);

	@Query("SELECT a FROM Asta a JOIN Offerta o ON a.id = o.id_asta WHERE o.id_utente = :id_utente ORDER BY a.nome")
	List<Asta> filtraPerOfferteUtente(@Param("id_utente") int id_utente);
	
	@Query("SELECT a FROM Asta a WHERE stato = :statoAsta AND (LOWER(a.nome) LIKE LOWER(CONCAT('%', :chiave, '%')) OR LOWER(a.descrizione) LIKE LOWER(CONCAT('%', :chiave, '%'))) ORDER BY a.nome")
	List<Asta> filtraPerParolaChiave(@Param("chiave") String chiave, @Param("statoAsta") StatoAsta statoAsta);
	
	@Query("SELECT a FROM Asta a WHERE a.categoria = :categoria AND stato = :statoAsta")
	List<Asta> filtraPerCategoria(@Param("categoria") Categoria categoria, @Param("statoAsta") StatoAsta statoAsta);
	
	@Query("SELECT a FROM Asta a WHERE stato = :statoAsta AND a.categoria = :categoria AND (LOWER(a.descrizione) LIKE LOWER(CONCAT('%', :chiave, '%')) OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :chiave, '%'))) ORDER BY a.nome")
	List<Asta> filtraPerCategoriaAndParoleChiave(@Param("chiave") String chiave, @Param("categoria") Categoria categoria, @Param("statoAsta") StatoAsta statoAsta);

}
