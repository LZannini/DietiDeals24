package Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import Models.Asta;

@Repository
public interface Asta_Repository extends JpaRepository<Asta, Integer>{

	Asta creaAsta(Asta asta,String tipo);
	
	@Query("select a from Asta a where a.categoria = :categoria AND a.descrizione LIKE %:key%")
	List<Asta> findPerCategoriaAndParoleChiave(String categoria,String key);
}
